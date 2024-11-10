import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.SocketTimeoutException;

public class Dstore {
  private int port, cport, timeout;
  private String fileFolder;
  private Socket controllerSocket;
  private ExecutorService executorService;
  private FileWriter logWriter;

  // Main method to start the Dstore

  public static void main(String[] args) {
    Dstore dstore = new Dstore(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
    dstore.start();
  }

  // Constructor to initialize the Dstore and create a log file

  public Dstore(int port, int cport, int timeout, String fileFolder) {
    this.port = port;
    this.cport = cport;
    this.timeout = timeout;
    this.fileFolder = fileFolder;
    this.executorService = Executors.newFixedThreadPool(10);
    try {
      this.logWriter = new FileWriter("dstore_config.txt");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to start the Dstore and connect to the Controller
  // It makes sure that the Dstore is always listening for client connections

  public void start() {
    try {
      controllerSocket = new Socket("localhost", cport);
      log("Dstore connected to Controller on port " + cport);
      PrintWriter out = new PrintWriter(controllerSocket.getOutputStream(), true);
      out.println(Protocol.JOIN_TOKEN + " " + port);
      log("Sent JOIN message to Controller with port " + port);
      ServerSocket serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(timeout);
      while (true) {
        try {
          Socket clientSocket = serverSocket.accept();
          log("Accepted client connection");
          executorService.execute(() -> handleClientRequest(clientSocket));
        } catch (SocketTimeoutException e) {
          log("No client connections received within the timeout period");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // It reads the client request and calls the needed method to handle the request

  private void handleClientRequest(Socket clientSocket) {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      String message = in.readLine();
      log("Received message from client: " + message);
      String[] parts = message.split(" ");
      String command = parts[0];
      switch (command) {
        case Protocol.STORE_TOKEN:
          handleStore(parts[1], Long.parseLong(parts[2]), clientSocket);
          break;
        case Protocol.LOAD_DATA_TOKEN:
          handleLoad(parts[1], clientSocket);
          break;
        case Protocol.REMOVE_TOKEN:
          handleRemove(parts[1]);
          break;
        case Protocol.REBALANCE_STORE_TOKEN:
          handleRebalanceStore(parts[1], Long.parseLong(parts[2]), clientSocket);
          break;
        case Protocol.LIST_TOKEN:
          handleList(out);
          break;
        case Protocol.REBALANCE_TOKEN:
          handleRebalance(out);
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // Method to handle the rebalance request from the Controller
  // It does this by sending the file list to the Controller and then receiving the files to be stored
  // It sends the ACK to the Controller, and then sends the REBALANCE_COMPLETE message to the Controller

  private void handleRebalance(PrintWriter out) throws IOException {
    File folder = new File(fileFolder);
    File[] files = folder.listFiles();
    if (files != null) {
      for (File file : files) {
        String filename = file.getName();
        long filesize = file.length();
        PrintWriter controllerOut = new PrintWriter(controllerSocket.getOutputStream(), true);
        controllerOut.println(Protocol.REBALANCE_STORE_TOKEN + " " + filename + " " + filesize);
        log("Sent REBALANCE_STORE to Controller for " + filename + " with size " + filesize);
      }
    }

    // Send the REBALANCE_COMPLETE message to the Controller
    // And then wait for the next rebalance request from the Controller
    // This is done to make sure that the Dstore is always listening for the rebalance request from the Controller

    BufferedReader in = new BufferedReader(new InputStreamReader(controllerSocket.getInputStream()));
    String message;
    while ((message = in.readLine()) != null) {
      String[] parts = message.split(" ");
      String command = parts[0];
      if (command.equals(Protocol.REMOVE_TOKEN)) {
        String filename = parts[1];
        File file = new File(fileFolder, filename);
        if (file.exists()) {
          file.delete();
          log("Deleted file: " + filename);
          PrintWriter controllerOut = new PrintWriter(controllerSocket.getOutputStream(), true);
          controllerOut.println(Protocol.ACK_TOKEN);
        }
      } else if (command.equals(Protocol.REBALANCE_STORE_TOKEN)) {
        String filename = parts[1];
        long filesize = Long.parseLong(parts[2]);
        handleRebalanceStore(filename, filesize, null);
      }
    }
    out.println(Protocol.REBALANCE_COMPLETE_TOKEN);
    log("Sent REBALANCE_COMPLETE to Controller");
  }

  // Method to handle the STORE request from the client
  // It does by sending the ACK to the client, and then receiving the file content from the client

  private synchronized void handleStore(String filename, long filesize, Socket clientSocket) {
    try {
      PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
      clientOut.println(Protocol.ACK_TOKEN);
      log("Sent ACK to client for STORE " + filename);
      File file = new File(fileFolder, filename);
      FileOutputStream fos = new FileOutputStream(file);
      byte[] buffer = new byte[8192];
      int bytesRead;
      long totalBytesRead = 0;
      InputStream in = clientSocket.getInputStream();
      while (totalBytesRead < filesize) {
        bytesRead = in.read(buffer, 0, (int) Math.min(buffer.length, filesize - totalBytesRead));
        if (bytesRead == -1) {
          break;
        }
        fos.write(buffer, 0, bytesRead);
        totalBytesRead += bytesRead;
      }
      fos.close();
      log("Stored file: " + filename);
      PrintWriter controllerOut = new PrintWriter(controllerSocket.getOutputStream(), true);
      controllerOut.println(Protocol.STORE_ACK_TOKEN + " " + filename + " " + port);
      log("Sent STORE_ACK to Controller for " + filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to handle the LOAD request from the client
  // It does this by sending the file content to the client

  private synchronized void handleLoad(String filename, Socket clientSocket) {
    try {
      File file = new File(fileFolder, filename);
      if (!file.exists()) {
        log("File not found: " + filename);
        return;
      }

      // Send the file content to the client

      byte[] buffer = new byte[8192];
      int bytesRead;
      FileInputStream fis = new FileInputStream(file);
      OutputStream out = clientSocket.getOutputStream();
      while ((bytesRead = fis.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
      }
      out.flush();
      fis.close();
      log("Sent file content to client for LOAD " + filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to handle the REMOVE request from the client
  // It does this by removing the file from the Dstore

  private void handleRemove(String filename) {
    File file = new File(fileFolder, filename);
    if (file.exists()) {
      if (file.delete()) {
        log("Removed file: " + filename);
        sendRemoveAckToController(filename, controllerSocket, port);
      } else {
        log("Failed to remove file: " + filename);
      }
    } else {
      log("File not found for REMOVE: " + filename);
    }
  }

  // Helper method to send the REMOVE_ACK to the Controller

  private void sendRemoveAckToController(String filename, Socket controllerSocket, int port) {
    try {
      PrintWriter controllerOut = new PrintWriter(controllerSocket.getOutputStream(), true);
      controllerOut.println(Protocol.REMOVE_ACK_TOKEN + " " + filename + " " + port);
      log("Sent REMOVE_ACK to Controller for " + filename);
    } catch (IOException e) {
      log("Failed to send REMOVE_ACK to Controller for " + filename + ": " + e.getMessage());
    }
  }

  // Method to handle the REBALANCE_STORE request from the Controller
  // It does this by storing the file content from the Controller
  // It sends the ACK to the Controller, and then sends the REBALANCE_COMPLETE message to the Controller

  private synchronized void handleRebalanceStore(String filename, long filesize, Socket clientSocket) {
    try {
      File file = new File(fileFolder, filename);
      FileOutputStream fos = new FileOutputStream(file);
      byte[] buffer = new byte[8192];
      int bytesRead;
      long totalBytesRead = 0;
      InputStream in = clientSocket.getInputStream();
      while (totalBytesRead < filesize) {
        bytesRead = in.read(buffer, 0, (int) Math.min(buffer.length, filesize - totalBytesRead));
        if (bytesRead == -1) {
          break;
        }
        fos.write(buffer, 0, bytesRead);
        totalBytesRead += bytesRead;
      }
      fos.close();
      log("Stored file from rebalance: " + filename);
      PrintWriter controllerOut = new PrintWriter(controllerSocket.getOutputStream(), true);
      controllerOut.println(Protocol.ACK_TOKEN);
      log("Sent ACK to Controller for REBALANCE_STORE " + filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to handle the LIST request from the Controller
  // It does this by sending the file list to the Controller

  private void handleList(PrintWriter out) {
    File folder = new File(fileFolder);
    File[] files = folder.listFiles();
    StringBuilder response = new StringBuilder(Protocol.LIST_TOKEN);
    if (files != null) {
      for (File file : files) {
        response.append(" ").append(file.getName());
      }
    }
    out.println(response);
    log("Sent file list to Controller: " + response);
  }

  // Method to log the messages to the log file

  private void log(String message) {
    try {
      logWriter.write(message + "\n");
      logWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}