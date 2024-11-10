package uk.ac.soton.comp1206.scene;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class LobbyScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(LobbyScene.class);

  ArrayList<String> channels = new ArrayList<>();

  GridPane gridPane;

  TextArea chatArea;

  VBox chatBoxContainer;

  TextField chatInput;

  Label sendChat;
  Label quitChat;

  Label startGame;

  Label startChannel;

  Label quitChannel;

  Label showUsers;

  BorderPane mainPane;

  Boolean host = false;

  /**
   * Create a new scene, passing in the GameWindow the scene will be displayed in
   *
   * @param gameWindow the game window
   */

  public LobbyScene(GameWindow gameWindow) {
    super(gameWindow);
  }

  public void initialise() {

  }

  /**
   * Build the scene
   */

  public void build() {
    logger.info("Building " + this.getClass().getName());

    Multimedia.playMusic("game_start.mp3");

    root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

    var menuPane = new StackPane();
    menuPane.setMaxWidth(gameWindow.getWidth());
    menuPane.setMaxHeight(gameWindow.getHeight());
    menuPane.getStyleClass().add("menu-background");
    root.getChildren().add(menuPane);

    mainPane = new BorderPane();
    menuPane.getChildren().add(mainPane);

    fetchChannels();
    incomingMessages();

    gridPane = new GridPane();
    gridPane.setVgap(10);
    mainPane.setCenter(gridPane);


    startChannel = new Label("Start Channel");
    startChannel.getStyleClass().add("buttons");
    mainPane.setBottom(startChannel);

    startChannel.setOnMouseClicked(event -> {
      startChannel();
    });


    quitChannel = new Label("Disconnect");
    quitChannel.getStyleClass().add("buttons");
    mainPane.setRight(quitChannel);

    quitChannel.setOnMouseClicked(event -> {
      gameWindow.getCommunicator().send("QUIT");
      shutdown();
    });

    chatBoxContainer = new VBox();
  }

  /**
   * Fetch the channels from the server
   */

  public void fetchChannels() {
    // Request current channels using the communicator
    TimerTask requestChannelsTask = new TimerTask() {
      @Override
      public void run() {
        gameWindow.getCommunicator().send("LIST");
      }
    };

    // Repeat the request every 5 seconds
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(requestChannelsTask, 0, 5000);
  }

  /**
   * Listen for incoming messages
   */

  public void incomingMessages() {
    gameWindow.getCommunicator().addListener(message -> {
      if (!message.startsWith("CHANNELS"))
        return;
      {
        Platform.runLater(() -> this.receiveChannels(message));
      }
    });

    gameWindow.getCommunicator().addListener(message -> {
      if (!message.startsWith("ERROR"))
        return;
      {
        Platform.runLater(() -> this.receiveError(message));
      }
    });
  }

  /**
   * Receive the channels from the server
   *
   * @param message the message from the server
   */

  public void receiveChannels(String message) {
    // Clear existing channels before adding new ones
    if (channels != null) {
      gridPane.getChildren().clear();
    }

    Text nameHeaders = new Text("Channels:");
    nameHeaders.getStyleClass().add("score");
    gridPane.add(nameHeaders, 0, 0);

    message = message.substring(9);
    String[] channelArray = message.split("\n");

    int row = 1;

    for (String item : channelArray) {
      Text channelText = new Text(item);
      channelText.getStyleClass().add("score");
      gridPane.add(channelText, 0, row);
      row++;

      channelText.setOnMouseClicked(event -> {
        gameWindow.getCommunicator().send("JOIN " + item);
        chatWindow();
      });
    }

  }

  /**
   * Open a dialog to create a new channel
   */

  public void startChannel() {
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("Enter channel name");
    dialog.setHeaderText(null);
    dialog.setContentText("Channel name:");

    Optional<String> userInput = dialog.showAndWait();

    if (userInput.isPresent() && !userInput.get().isEmpty()) {
      gameWindow.getCommunicator().send("CREATE " + userInput.get());

      gameWindow.getCommunicator().addListener(message -> {
        if (!message.startsWith("HOST"))
          return;
        {
          host = true;
        }
      });
    }
  }

  /**
   * Creating the chat window
   */

  public void chatWindow() {
    TextInputDialog dialog = new TextInputDialog("");
    dialog.setTitle("Enter nickname");
    dialog.setHeaderText(null);
    dialog.setContentText("Nickname:");

    Optional<String> userInput = dialog.showAndWait();

    if (userInput.isPresent() && !userInput.get().isEmpty()) {
      gameWindow.getCommunicator().send("NICK " + userInput.get());
    }

    chatArea = new TextArea();
    chatArea.setPrefHeight(100);
    chatArea.setEditable(false);

    chatInput = new TextField();
    chatInput.setPromptText("Enter message");


    sendChat = new Label("Send");
    sendChat.getStyleClass().add("buttons");


    quitChat = new Label("Quit");
    quitChat.getStyleClass().add("buttons");


    showUsers = new Label("Show Users");
    showUsers.getStyleClass().add("buttons");

    if (host) {

      startGame = new Label("Start Game");
      startGame.getStyleClass().add("buttons");
      chatBoxContainer.getChildren().addAll(chatArea, chatInput, sendChat, quitChat, startGame, showUsers);

      startGame.setOnMouseClicked(event -> {
        gameWindow.getCommunicator().send("START");
        gameWindow.startMultiplayerChallenge();
        chatInput.clear();
      });
    }
    else {
      chatBoxContainer.getChildren().addAll(chatArea, chatInput, sendChat, quitChat, showUsers);
    }

    root.getChildren().add(chatBoxContainer);
    chatBoxContainer.setAlignment(Pos.BOTTOM_CENTER);

    sendChat.setOnMouseClicked(event -> {
      gameWindow.getCommunicator().send("MSG " + chatInput.getText());
      chatInput.clear();
    });

    quitChat.setOnMouseClicked(event -> {
      gameWindow.getCommunicator().send("PART");

      System.out.println("Host is" + host);
      if (host) {
        chatBoxContainer.getChildren().removeAll(chatArea, chatInput, sendChat, quitChat, startGame, showUsers);
      }
      else {
        chatBoxContainer.getChildren().removeAll(chatArea, chatInput, sendChat, quitChat, showUsers);
      }
      incomingMessages();
    });

    gameWindow.getCommunicator().addListener(message -> {
      if (!message.startsWith("MSG"))
        return;
      {
        Platform.runLater(() -> this.receiveChat(message));
      }
    });

    showUsers.setOnMouseClicked(event -> {
      gameWindow.getCommunicator().send("USERS");
    });

    gameWindow.getCommunicator().addListener(message -> {
      if (!message.startsWith("USERS"))
        return;
      {
        Platform.runLater(() -> this.showUsers(message));
      }
    });

    gameWindow.getCommunicator().addListener(message -> {
      if (!message.startsWith("START"))
        return;
      {
        Platform.runLater(gameWindow::startMultiplayerChallenge);
      }
    });
  }

  /**
   * Receive chat messages from the server
   *
   * @param message the message from the server
   */

  public void receiveChat(String message) {
    message = message.substring(4);
    String[] filter = message.split(":");
    String sender = filter[0];
    String content = filter[1];

    chatArea.appendText(sender + ": " + content + "\n");
  }

  /**
   * Receive error messages from the server
   *
   * @param message the message from the server
   */

  public void receiveError(String message) {
    message = message.substring(6);
    System.out.println("Received error message: " + message);
  }

  /**
   * Show the users in the chat
   *
   * @param message the message from the server
   */

  public void showUsers(String message) {
    message = message.substring(5);
    String[] users = message.split("\n");

    for (String user : users) {
      chatArea.appendText("[CONSOLE: On this chat exists] " + ": " + user + "\n");
    }
  }

  /**
   * Shuts down the application
   */

  public void shutdown() {
    cleanup();
    Stage stage = (Stage) getScene().getWindow();
    stage.close();
  }

  /**
   * Cleans up the application
   */

  public void cleanup() {
    Multimedia.stopPlay();
  }

}
