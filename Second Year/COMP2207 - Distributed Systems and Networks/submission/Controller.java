import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Controller {
  private int cport, replicationFactor, timeout, rebalancePeriod;
  private ServerSocket serverSocket;
  private Map<String, FileDetails> index;
  private Map<Integer, Socket> dstoreSockets;
  private ExecutorService executorService;
  private ScheduledExecutorService scheduledExecutor;
  private Map<String, Socket> clientSockets = new ConcurrentHashMap<>();
  private CountDownLatch removeLatch;
  private static final Map<PrintWriter, List<Integer>> dstoreLoadForClients = new ConcurrentHashMap<>();
  private static final Map<PrintWriter, List<Integer>> dstoreReloadForClients = new ConcurrentHashMap<>();
  private FileWriter logWriter;

  // Main method to start the controller

  public static void main(String[] args) {
    Controller controller = new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
    controller.start();
  }

  // Constructor to initialize the controller and create a log file

  public Controller(int cport, int replicationFactor, int timeout, int rebalancePeriod) {
    this.cport = cport;
    this.replicationFactor = replicationFactor;
    this.timeout = timeout;
    this.rebalancePeriod = rebalancePeriod;
    this.index = new ConcurrentHashMap<>();
    this.dstoreSockets = new ConcurrentHashMap<>();
    this.executorService = Executors.newFixedThreadPool(10);
    this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    try {
      this.logWriter = new FileWriter("controller_config.txt");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to start the controller and handle the incoming connections
  // It makes sure that the controller is started on the given port and then it starts the rebalance task

  public void start() {
    try {
      serverSocket = new ServerSocket(cport);
      log("Controller started on port " + cport);
      executorService.execute(this::rebalanceTask);
      while (true) {
        Socket socket = serverSocket.accept();
        executorService.execute(() -> handleConnection(socket));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to handle the incoming connections and the messages from the Dstores and the clients

  private void handleConnection(Socket socket) {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String message;
      while ((message = in.readLine()) != null) {
        String[] parts = message.split(" ");
        String command = parts[0];
        log("Received command: " + command);
        switch (command) {
          case Protocol.JOIN_TOKEN:
            handleJoin(Integer.parseInt(parts[1]), socket);
            break;
          case Protocol.STORE_TOKEN:
            handleStore(parts[1], Long.parseLong(parts[2]), socket, out);
            break;
          case Protocol.STORE_ACK_TOKEN:
            handleStoreAck(parts[1], Integer.parseInt(parts[2]));
            break;
          case Protocol.LOAD_TOKEN:
            handleLoad(parts[1], out);
            break;
          case Protocol.RELOAD_TOKEN:
            handleReload(parts[1], out);
            break;
          case Protocol.REMOVE_TOKEN:
            handleRemove(parts[1], out);
            break;
          case Protocol.REMOVE_ACK_TOKEN:
            handleRemoveAck(parts[1], Integer.parseInt(parts[2]));
            break;
          case Protocol.LIST_TOKEN:
            handleList(out);
            break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      int dstorePort = getDstorePort(socket);
      if (dstorePort != -1) {
        dstoreSockets.remove(dstorePort);
        log("Dstore disconnected: " + dstorePort);
        rebalance();
      }
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // Helper method to get the port of the Dstore

  private int getDstorePort(Socket socket) {
    for (Map.Entry<Integer, Socket> entry : dstoreSockets.entrySet()) {
      if (entry.getValue().equals(socket)) {
        return entry.getKey();
      }
    }
    return -1;
  }

  // Method to handle the JOIN message from the Dstore
  // It does this by adding the Dstore to the list of Dstores

  private void handleJoin(int port, Socket socket) {
    dstoreSockets.put(port, socket);
    log("Dstore joined: " + port);
  }

  // Method to handle the STORE_ACK message from the Dstore
  // It does this by removing the Dstore from the list of Dstores

  private void handleStoreAck(String filename, int dstorePort) {
    FileDetails fileInfo = index.get(filename);
    if (fileInfo != null) {
      fileInfo.dstores.remove(Integer.valueOf(dstorePort));
      log("Received STORE_ACK for " + filename + " from Dstore " + dstorePort);
      if (fileInfo.dstores.isEmpty()) {
        fileInfo.status = FileStatus.STORE_COMPLETE;
        log("Store complete: " + filename);
        Socket clientSocket = clientSockets.get(filename);
        if (clientSocket != null) {
          try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(Protocol.STORE_COMPLETE_TOKEN);
            log("Sent STORE_COMPLETE to client for " + filename);
            clientSockets.remove(filename);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  // Method to handle the LOAD message from the client
  // It does this by sending the LOAD_FROM message to the Dstore
  // If the file is not found, it sends an error message

  private synchronized void handleLoad(String filename, PrintWriter out) {
    log("Handling LOAD request for " + filename);
    log("The dstore size is " + dstoreSockets.size() + " and the replication factor is " + replicationFactor);

    if (dstoreSockets.size() < replicationFactor) {
      out.println(Protocol.ERROR_NOT_ENOUGH_DSTORES_TOKEN);
      log("Sent ERROR_NOT_ENOUGH_DSTORES for LOAD " + filename);
      return;
    }

    FileDetails fileInfo = index.get(filename);

    if (fileInfo == null || fileInfo.status != FileStatus.STORE_COMPLETE) {
      out.println(Protocol.ERROR_FILE_DOES_NOT_EXIST_TOKEN);
      log("Sent ERROR_FILE_DOES_NOT_EXIST for LOAD " + filename);
      return;
    }

    // Select the Dstores for the file
    // If the Dstores are not available, select a random Dstore
    // Send the LOAD_FROM message to the Dstore

    synchronized (fileInfo) {
      List<Integer> availableDstores = new ArrayList<>(fileInfo.dstores);
      if (availableDstores.isEmpty()) {
        List<Integer> allDstores = new ArrayList<>(dstoreSockets.keySet());
        int dstorePort = allDstores.get(new Random().nextInt(allDstores.size()));
        out.println(Protocol.LOAD_FROM_TOKEN + " " + dstorePort + " " + fileInfo.filesize);
        out.flush();
        log("Sent LOAD_FROM response: " + dstorePort);
      } else {
        int dstorePort = availableDstores.remove(0);
        out.println(Protocol.LOAD_FROM_TOKEN + " " + dstorePort + " " + fileInfo.filesize);
        out.flush();
        log("Sent LOAD_FROM response: " + dstorePort);
      }
    }
  }

  // Method to handle the RELOAD message from the client
  // It does this by sending the LOAD_FROM message to the Dstore
  // If the file is not found, it sends an error message

  private synchronized void handleReload(String filename, PrintWriter out) {

    FileDetails fileInfo = retrieveNameFile(filename);

    if (fileInfo == null || fileInfo.status != FileStatus.STORE_COMPLETE) {
      out.println(Protocol.ERROR_FILE_DOES_NOT_EXIST_TOKEN);
      log("Sent ERROR_FILE_DOES_NOT_EXIST for RELOAD " + filename);
      return;
    }

    // Select the Dstores for the file
    // If the Dstores are not available, select a random Dstore
    // Send the LOAD_FROM message to the Dstore

    List<Integer> availableDstores = new ArrayList<>(fileInfo.dstores);
    List<Integer> lastDstoreLoad = dstoreLoadForClients.getOrDefault(out, new ArrayList<>());

    if (!lastDstoreLoad.isEmpty()) {
      availableDstores.remove(lastDstoreLoad.get(lastDstoreLoad.size() - 1));
    }

    List<Integer> dstoresAttempted = dstoreReloadForClients.getOrDefault(out, new ArrayList<>());
    availableDstores.removeAll(dstoresAttempted);

    if (!availableDstores.isEmpty()) {
      Random random = new Random();
      int randomIndex = random.nextInt(availableDstores.size());
      int selectedDstorePort = availableDstores.get(randomIndex);
      dstoresAttempted.add(selectedDstorePort);
      dstoreReloadForClients.put(out, dstoresAttempted);
      out.println(Protocol.LOAD_FROM_TOKEN + " " + selectedDstorePort + " " + fileInfo.filesize);
      out.flush();
      log("Sent LOAD_FROM response for RELOAD: " + selectedDstorePort);
    } else {
      out.println(Protocol.ERROR_LOAD_TOKEN);
      log("Error reloading file " + filename + ": All available Dstores have been exhausted");
      dstoreReloadForClients.remove(out);
    }
  }

  // Helper method to retrieve the file details from the index

  private FileDetails retrieveNameFile(String filename) {
    return index.get(filename);
  }

  // Method to handle the REMOVE_ACK message from the Dstore
  // It does this by removing the Dstore from the list of Dstores

  private void handleRemoveAck(String filename, int dstorePort) {

    FileDetails fileInfo = index.get(filename);

    if (fileInfo != null) {
      synchronized (fileInfo) {
        fileInfo.dstores.remove(Integer.valueOf(dstorePort));
        log("Received REMOVE_ACK for " + filename + " from Dstore " + dstorePort);
        if (fileInfo.dstores.isEmpty()) {
          removeLatch.countDown();
        }
      }
    }
  }

  // Method to handle the STORE message from the client
  // It does this by selecting the Dstores and sending the STORE_TO message to the client

  private void handleStore(String filename, long filesize, Socket socket, PrintWriter out) {

    if (dstoreSockets.size() < replicationFactor) {
      out.println(Protocol.ERROR_NOT_ENOUGH_DSTORES_TOKEN);
      log("Sent ERROR_NOT_ENOUGH_DSTORES for STORE " + filename);
      return;
    }

    if (index.containsKey(filename)) {
      out.println(Protocol.ERROR_FILE_ALREADY_EXISTS_TOKEN);
      log("Sent ERROR_FILE_ALREADY_EXISTS for STORE " + filename);
      return;
    }

    // Select the Dstores for the file
    // Send the STORE_TO message to the client
    // Start the timeout task
    // If the timeout task is triggered, remove the file from the Dstores

    List<Integer> selectedDstores = selectDstores();
    FileDetails fileInfo = new FileDetails(filesize, selectedDstores, FileStatus.STORE_IN_PROGRESS);
    index.put(filename, fileInfo);
    clientSockets.put(filename, socket);
    log("Started STORE operation for " + filename);
    String storeMessage = buildStoreMessage(selectedDstores);
    out.println(storeMessage);
    log("Sent STORE_TO message to client: " + storeMessage);
    scheduledExecutor.schedule(() -> {
      if (index.containsKey(filename)) {
        FileDetails storingFileInfo = index.get(filename);
        if (storingFileInfo.status == FileStatus.STORE_IN_PROGRESS) {
          storingFileInfo.dstores.forEach(dstorePort -> sendMessageToDstore(dstorePort, Protocol.REMOVE_TOKEN + " " + filename));
          index.remove(filename);
          clientSockets.remove(filename);
          log("STORE operation timed out for " + filename);
        }
      }
    }, timeout, TimeUnit.MILLISECONDS);
  }

  // Method to handle the REMOVE message from the client
  // It does this by sending the REMOVE message to the Dstore

  private synchronized void handleRemove(String filename, PrintWriter out) {

    if (dstoreSockets.size() < replicationFactor) {
      out.println(Protocol.ERROR_NOT_ENOUGH_DSTORES_TOKEN);
      log("Sent ERROR_NOT_ENOUGH_DSTORES for REMOVE " + filename);
      return;
    }

    FileDetails fileInfo = index.get(filename);
    if (fileInfo == null || fileInfo.status != FileStatus.STORE_COMPLETE) {
      out.println(Protocol.ERROR_FILE_DOES_NOT_EXIST_TOKEN);
      log("Sent ERROR_FILE_DOES_NOT_EXIST for REMOVE " + filename);
      return;
    }

    removeLatch = new CountDownLatch(fileInfo.dstores.size());

    for (int dstorePort : fileInfo.dstores) {
      sendMessageToDstore(dstorePort, Protocol.REMOVE_TOKEN + " " + filename);
    }

    log("Started REMOVE operation for " + filename);

    try {
      if (removeLatch.await(timeout, TimeUnit.MILLISECONDS)) {
        index.remove(filename);
        out.println(Protocol.REMOVE_COMPLETE_TOKEN);
        log("Remove complete: " + filename);
      } else {
        out.println(Protocol.ERROR_FILE_DOES_NOT_EXIST_TOKEN);
        log("REMOVE operation timed out for " + filename);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  // Method to handle the LIST message from the client
  // It does this by sending the list of files to the client

  private void handleList(PrintWriter out) {

    if (dstoreSockets.size() < replicationFactor) {
      out.println(Protocol.ERROR_NOT_ENOUGH_DSTORES_TOKEN);
      log("Sent ERROR_NOT_ENOUGH_DSTORES for LIST");
      return;
    }

    StringBuilder response = new StringBuilder(Protocol.LIST_TOKEN);

    for (String filename : index.keySet()) {
      FileDetails fileInfo = index.get(filename);
      if (fileInfo.status == FileStatus.STORE_COMPLETE) {
        response.append(" ").append(filename);
      }
    }

    out.println(response);

    log("Sent file list: " + response);
  }

  // Method to start the rebalance task
  // It does this by checking the number of Dstores and the replication factor

  private void rebalanceTask() {

    while (true) {
      try {
        Thread.sleep(rebalancePeriod * 1000L);
        if (dstoreSockets.size() >= replicationFactor) {
          log("Starting rebalance operation");
          rebalance();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  // Method to rebalance the files across the Dstores
  // It does this by checking the files in each Dstore and then rebalancing them

  private void rebalance() {

    Map<Integer, List<String>> dstoreFiles = new HashMap<>();

    for (int dstorePort : dstoreSockets.keySet()) {
      List<String> files = getDstoreFiles(dstorePort);
      dstoreFiles.put(dstorePort, files);
    }

    // Remove the files that are not in the index

    for (int dstorePort : dstoreFiles.keySet()) {
      List<String> files = dstoreFiles.get(dstorePort);
      for (String filename : files) {
        if (!index.containsKey(filename)) {
          sendMessageToDstore(dstorePort, Protocol.REMOVE_TOKEN + " " + filename);
          log("Sent REMOVE to Dstore " + dstorePort + " for " + filename);
        }
      }
    }

    // Rebalance the files across the Dstores

    for (String filename : index.keySet()) {
      FileDetails fileInfo = index.get(filename);
      if (fileInfo.status == FileStatus.STORE_COMPLETE) {
        List<Integer> dstoresWithFile = new ArrayList<>(fileInfo.dstores);
        boolean fileFoundInDstores = false;

        // Check if the file is found in any Dstore
        // If it is found, remove the Dstore from the list
        // If the Dstore is less than the replication factor, add the Dstore to the list
        // Send the REBALANCE_STORE message to the Dstore

        for (int dstorePort : dstoreFiles.keySet()) {
          if (dstoresWithFile.contains(dstorePort)) {
            dstoresWithFile.remove(Integer.valueOf(dstorePort));
            fileFoundInDstores = true;
          } else if (dstoresWithFile.size() < replicationFactor) {
            dstoresWithFile.add(dstorePort);
            sendMessageToDstore(dstorePort, Protocol.REBALANCE_STORE_TOKEN + " " + filename + " " + fileInfo.filesize);
            log("Sent REBALANCE_STORE to Dstore " + dstorePort + " for " + filename);
          }
        }

        // Remove the file from the index if it is not found in any Dstore

        if (!fileFoundInDstores) {
          index.remove(filename);
          log("Removed " + filename + " from index as it was not found in any Dstore");
        }

        // Remove the file from the Dstores if the Dstore is more than the replication factor

        for (int dstorePort : dstoresWithFile) {
          sendMessageToDstore(dstorePort, Protocol.REMOVE_TOKEN + " " + filename);
          log("Sent REMOVE to Dstore " + dstorePort + " for " + filename);
        }

        // Send the REBALANCE message to the Dstores

        for (int dstorePort : dstoreSockets.keySet()) {
          sendMessageToDstore(dstorePort, Protocol.REBALANCE_TOKEN);
          log("Sent REBALANCE to Dstore " + dstorePort);
        }

        fileInfo.dstores = dstoresWithFile;
      }
    }

    // Rebalance the files across the Dstores

    int totalFiles = index.size();
    int minFilesPerDstore = (int) Math.floor((double) totalFiles * replicationFactor / dstoreSockets.size());
    int maxFilesPerDstore = (int) Math.ceil((double) totalFiles * replicationFactor / dstoreSockets.size());

    List<Integer> dstorePortsList = new ArrayList<>(dstoreSockets.keySet());
    Map<Integer, Integer> dstoreFileCount = new HashMap<>();

    // Check the number of files in each Dstore

    for (String filename : index.keySet()) {
      FileDetails fileInfo = index.get(filename);
      if (fileInfo.status == FileStatus.STORE_COMPLETE) {
        for (int dstorePort : fileInfo.dstores) {
          dstoreFileCount.put(dstorePort, dstoreFileCount.getOrDefault(dstorePort, 0) + 1);
        }
      }
    }

    // Rebalance the files across the Dstores
    // It does this by removing the files from the Dstore if the Dstore has more files than the max files per Dstore
    // Then it adds the files to the Dstore if the Dstore has less files than the min files per Dstore
    // It sends the REMOVE and REBALANCE_STORE messages to the Dstore

    for (int dstorePort : dstorePortsList) {
      int fileCount = dstoreFileCount.getOrDefault(dstorePort, 0);
      if (fileCount > maxFilesPerDstore) {
        int filesToRemove = fileCount - maxFilesPerDstore;
        List<String> filesToRemoveList = new ArrayList<>();
        for (String filename : index.keySet()) {
          FileDetails fileInfo = index.get(filename);
          if (fileInfo.status == FileStatus.STORE_COMPLETE && fileInfo.dstores.contains(dstorePort)) {
            filesToRemoveList.add(filename);
            if (filesToRemoveList.size() == filesToRemove) {
              break;
            }
          }
        }
        for (String filename : filesToRemoveList) {
          sendMessageToDstore(dstorePort, Protocol.REMOVE_TOKEN + " " + filename);
          log("Sent REMOVE to Dstore " + dstorePort + " for " + filename + " (rebalancing)");
        }
      } else if (fileCount < minFilesPerDstore) {
        int filesToAdd = minFilesPerDstore - fileCount;
        List<String> filesToAddList = new ArrayList<>();
        for (String filename : index.keySet()) {
          FileDetails fileInfo = index.get(filename);
          if (fileInfo.status == FileStatus.STORE_COMPLETE && !fileInfo.dstores.contains(dstorePort)) {
            filesToAddList.add(filename);
            if (filesToAddList.size() == filesToAdd) {
              break;
            }
          }
        }
        for (String filename : filesToAddList) {
          FileDetails fileInfo = index.get(filename);
          sendMessageToDstore(dstorePort, Protocol.REBALANCE_STORE_TOKEN + " " + filename + " " + fileInfo.filesize);
          log("Sent REBALANCE_STORE to Dstore " + dstorePort + " for " + filename + " (rebalancing)");
        }
      }
    }

    log("Rebalance operation completed");
  }

  // Method to get the files from the Dstore
  // It does this by sending the LIST message to the Dstore
  // It then reads the response from the Dstore and returns the list of files

  private List<String> getDstoreFiles(int dstorePort) {
    List<String> files = new ArrayList<>();
    sendMessageToDstore(dstorePort, Protocol.LIST_TOKEN);
    log("Sent LIST to Dstore " + dstorePort);

    // Read the response from the Dstore and add the files to the list

    try {
      Socket dstoreSocket = dstoreSockets.get(dstorePort);
      BufferedReader in = new BufferedReader(new InputStreamReader(dstoreSocket.getInputStream()));
      String response = in.readLine();
      log("Received file list from Dstore " + dstorePort + ": " + response);
      String[] parts = response.split(" ");
      files.addAll(Arrays.asList(parts).subList(1, parts.length));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return files;
  }

  // Method to send a message to the Dstore

  private void sendMessageToDstore(int dstorePort, String message) {
    try {
      Socket dstoreSocket = dstoreSockets.get(dstorePort);
      PrintWriter out = new PrintWriter(dstoreSocket.getOutputStream(), true);
      out.println(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Helper method to select the Dstores for replication

  private List<Integer> selectDstores() {
    List<Integer> selectedDstores = new ArrayList<>(dstoreSockets.keySet());
    Collections.shuffle(selectedDstores);
    return selectedDstores.subList(0, replicationFactor);
  }

  // Helper method to build the STORE_TO message for the client

  private String buildStoreMessage(List<Integer> dstores) {
    StringBuilder sb = new StringBuilder(Protocol.STORE_TO_TOKEN);
    for (int dstorePort : dstores) {
      sb.append(" ").append(dstorePort);
    }
    return sb.toString();
  }

  private void log(String message) {
    try {
      logWriter.write(message + "\n");
      logWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Enum to define the status of the file

  private enum FileStatus {
    STORE_IN_PROGRESS,
    STORE_COMPLETE,
    REMOVE_IN_PROGRESS,
    REMOVE_COMPLETE
  }

  // Class to store the details of the file
  // This is so that the controller can keep track of the file details

  private static class FileDetails {
    private long filesize;
    private List<Integer> dstores;
    private FileStatus status;

    public FileDetails(long filesize, List<Integer> dstores, FileStatus status) {
      this.filesize = filesize;
      this.dstores = dstores;
      this.status = status;
    }
  }
}