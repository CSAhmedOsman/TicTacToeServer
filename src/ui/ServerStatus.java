/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author LENOVO
 */
public class ServerStatus extends AnchorPane {
    protected final Rectangle rectangle;
    protected final Label label;
    protected final DropShadow dropShadow;
    protected final Circle circle;
    protected final Rectangle rectangle0;
    protected final Rectangle rectangle1;
    protected final Rectangle rectangle2;
    protected final Rectangle btnLogin;
    protected final Circle circle0;
    protected final Button btnClose;
    protected final DropShadow dropShadow0;
    protected final Button btnMin;
    protected final DropShadow dropShadow1;
    protected final Button btnRefresh;
    protected final DropShadow dropShadow2;
    protected final ToggleButton btnSwitch;
    protected final DropShadow dropShadow3;
    protected final Label label0;
    protected final DropShadow dropShadow4;
    protected final ToggleButton btnStatus;
    protected final DropShadow dropShadow5;
    protected final Label label1;
    protected final DropShadow dropShadow6;
    protected final TextField textOPlayer;
    protected final TextField textTotalPlayers;
    protected final Label label2;
    protected final DropShadow dropShadow7;
    protected final Button btnLogOut;
    protected final DropShadow dropShadow8;
    protected final TextField textAplayer;
    protected final Label label3;
    protected final DropShadow dropShadow9;
    boolean isConnected;
    Connection Newconnection;
    Thread th;

    public ServerStatus(Connection connection) {

        rectangle = new Rectangle();
        label = new Label();
        dropShadow = new DropShadow();
        circle = new Circle();
        rectangle0 = new Rectangle();
        rectangle1 = new Rectangle();
        rectangle2 = new Rectangle();
        btnLogin = new Rectangle();
        circle0 = new Circle();
        btnClose = new Button();
        dropShadow0 = new DropShadow();
        btnMin = new Button();
        dropShadow1 = new DropShadow();
        btnRefresh = new Button();
        dropShadow2 = new DropShadow();
        btnSwitch = new ToggleButton();
        dropShadow3 = new DropShadow();
        label0 = new Label();
        dropShadow4 = new DropShadow();
        btnStatus = new ToggleButton();
        dropShadow5 = new DropShadow();
        label1 = new Label();
        dropShadow6 = new DropShadow();
        textOPlayer = new TextField();
        textTotalPlayers = new TextField();
        label2 = new Label();
        dropShadow7 = new DropShadow();
        btnLogOut = new Button();
        dropShadow8 = new DropShadow();
        textAplayer = new TextField();
        label3 = new Label();
        dropShadow9 = new DropShadow();

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(600.0);
        setPrefWidth(700.0);

        rectangle.setArcHeight(5.0);
        rectangle.setArcWidth(5.0);
        rectangle.setFill(javafx.scene.paint.Color.valueOf("#ffbdbd"));
        rectangle.setHeight(607.0);
        rectangle.setLayoutX(-8.0);
        rectangle.setLayoutY(-3.0);
        rectangle.setSmooth(false);
        rectangle.setStroke(javafx.scene.paint.Color.BLACK);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle.setWidth(708.0);

        label.setLayoutX(85.0);
        label.setLayoutY(72.0);
        label.setPrefHeight(112.0);
        label.setPrefWidth(615.0);
        label.setText("Tic-Tac-Toe Server Status");
        label.setTextFill(javafx.scene.paint.Color.valueOf("#43115b"));
        label.setFont(new Font("Franklin Gothic Demi Cond", 60.0));

        dropShadow.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow.setSpread(0.98);
        label.setEffect(dropShadow);

        circle.setFill(javafx.scene.paint.Color.valueOf("#00000078"));
        circle.setLayoutX(48.0);
        circle.setLayoutY(72.0);
        circle.setOpacity(0.2);
        circle.setRadius(111.0);
        circle.setStroke(javafx.scene.paint.Color.valueOf("#d0cbcb"));
        circle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        rectangle0.setArcHeight(5.0);
        rectangle0.setArcWidth(5.0);
        rectangle0.setFill(javafx.scene.paint.Color.valueOf("#141414"));
        rectangle0.setHeight(188.0);
        rectangle0.setLayoutX(66.0);
        rectangle0.setLayoutY(492.0);
        rectangle0.setOpacity(0.2);
        rectangle0.setRotate(-35.5);
        rectangle0.setStroke(javafx.scene.paint.Color.BLACK);
        rectangle0.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle0.setWidth(29.0);

        rectangle1.setArcHeight(5.0);
        rectangle1.setArcWidth(5.0);
        rectangle1.setFill(javafx.scene.paint.Color.valueOf("#141414"));
        rectangle1.setHeight(188.0);
        rectangle1.setLayoutX(66.0);
        rectangle1.setLayoutY(492.0);
        rectangle1.setOpacity(0.2);
        rectangle1.setRotate(-126.9);
        rectangle1.setStroke(javafx.scene.paint.Color.BLACK);
        rectangle1.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle1.setWidth(29.0);

        rectangle2.setArcHeight(5.0);
        rectangle2.setArcWidth(5.0);
        rectangle2.setFill(javafx.scene.paint.Color.valueOf("#141414"));
        rectangle2.setHeight(188.0);
        rectangle2.setLayoutX(626.0);
        rectangle2.setLayoutY(169.0);
        rectangle2.setOpacity(0.2);
        rectangle2.setRotate(-50.2);
        rectangle2.setStroke(javafx.scene.paint.Color.BLACK);
        rectangle2.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle2.setWidth(29.0);

        btnLogin.setArcHeight(5.0);
        btnLogin.setArcWidth(5.0);
        btnLogin.setFill(javafx.scene.paint.Color.valueOf("#141414"));
        btnLogin.setHeight(188.0);
        btnLogin.setLayoutX(626.0);
        btnLogin.setLayoutY(169.0);
        btnLogin.setOpacity(0.2);
        btnLogin.setRotate(-138.0);
        btnLogin.setStroke(javafx.scene.paint.Color.BLACK);
        btnLogin.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        btnLogin.setWidth(29.0);

        circle0.setFill(javafx.scene.paint.Color.valueOf("#00000078"));
        circle0.setLayoutX(662.0);
        circle0.setLayoutY(548.0);
        circle0.setOpacity(0.2);
        circle0.setRadius(71.0);
        circle0.setStroke(javafx.scene.paint.Color.valueOf("#d0cbcb"));
        circle0.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        btnClose.setLayoutX(616.0);
        btnClose.setLayoutY(23.0);
        btnClose.setMinHeight(USE_PREF_SIZE);
        btnClose.setMinWidth(50.0);
        btnClose.setMnemonicParsing(false);
        btnClose.setPrefHeight(40.0);
        btnClose.setPrefWidth(20.0);
        btnClose.setStyle("-fx-background-radius: 10; -fx-font-size: 20; -fx-font-weight: bold; -fx-background-color: #FD6D84;");
        btnClose.setText("X");
        btnClose.setTextFill(javafx.scene.paint.Color.WHITE);
        btnClose.setFont(new Font("Franklin Gothic Demi Cond", 43.0));
        btnClose.setCursor(Cursor.HAND);

        dropShadow0.setColor(Color.valueOf("#fff7f7"));
        dropShadow0.setSpread(0.69);
        btnClose.setEffect(dropShadow0);

        btnMin.setLayoutX(547.0);
        btnMin.setLayoutY(23.0);
        btnMin.setMinHeight(USE_PREF_SIZE);
        btnMin.setMinWidth(50.0);
        btnMin.setMnemonicParsing(false);
        btnMin.setPrefHeight(40.0);
        btnMin.setPrefWidth(20.0);
        btnMin.setStyle("-fx-background-radius: 10; -fx-font-size: 20; -fx-font-weight: bold; -fx-background-color: #FD6D84;");
        btnMin.setText("-");
        btnMin.setTextAlignment(javafx.scene.text.TextAlignment.JUSTIFY);
        btnMin.setTextFill(javafx.scene.paint.Color.WHITE);
        btnMin.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP);
        btnMin.setFont(new Font("Franklin Gothic Demi Cond", 43.0));
        btnMin.setCursor(Cursor.HAND);

        dropShadow1.setColor(Color.valueOf("#fff7f7"));
        dropShadow1.setSpread(0.69);
        btnMin.setEffect(dropShadow1);

        btnRefresh.setLayoutX(307.0);
        btnRefresh.setLayoutY(486.0);
        btnRefresh.setMnemonicParsing(false);
        btnRefresh.setPrefHeight(63.0);
        btnRefresh.setPrefWidth(121.0);
        btnRefresh.setStyle("-fx-background-radius: 100; -fx-background-color: #EAD3D7;");
        btnRefresh.setText("Refresh");
        btnRefresh.setTextFill(javafx.scene.paint.Color.valueOf("#43115b"));
        btnRefresh.setFont(new Font("Gill Sans Ultra Bold Condensed", 22.0));
        btnRefresh.setCursor(Cursor.HAND);

        dropShadow2.setHeight(35.83);
        dropShadow2.setRadius(17.415);
        dropShadow2.setWidth(35.83);
        btnRefresh.setEffect(dropShadow2);

        btnSwitch.setLayoutX(473.0);
        btnSwitch.setLayoutY(219.0);
        btnSwitch.setMnemonicParsing(false);
        btnSwitch.setPrefHeight(45.0);
        btnSwitch.setPrefWidth(99.0);
        btnSwitch.setStyle("-fx-background-radius: 50;");
        btnSwitch.setText("OFF");
        btnSwitch.setTextFill(javafx.scene.paint.Color.valueOf("#43115b"));
        btnSwitch.setCursor(Cursor.HAND);
        btnSwitch.setFont(new Font("Gill Sans Ultra Bold Condensed", 23.0));

        dropShadow3.setSpread(0.33);
        btnSwitch.setEffect(dropShadow3);

        label0.setLayoutX(43.0);
        label0.setLayoutY(231.0);
        label0.setText("Server Stauts");
        label0.setFont(new Font("Franklin Gothic Medium", 21.0));

        dropShadow4.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow4.setHeight(10.385);
        dropShadow4.setRadius(4.32125);
        dropShadow4.setSpread(0.83);
        dropShadow4.setWidth(8.9);
        label0.setEffect(dropShadow4);

        btnStatus.setLayoutX(286.0);
        btnStatus.setLayoutY(220.0);
        btnStatus.setMnemonicParsing(false);
        btnStatus.setPrefHeight(45.0);
        btnStatus.setPrefWidth(163.0);
        btnStatus.setStyle("-fx-background-color: #3bd035; -fx-background-radius: 30;");
        btnStatus.setText("Connected");
        btnStatus.setFont(new Font("Franklin Gothic Heavy", 20.0));

        btnStatus.setEffect(dropShadow5);

        label1.setLayoutX(43.0);
        label1.setLayoutY(297.0);
        label1.setPrefHeight(26.0);
        label1.setPrefWidth(319.0);
        label1.setText("Total number of players");
        label1.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP);
        label1.setFont(new Font("Franklin Gothic Medium", 21.0));

        dropShadow6.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow6.setHeight(11.87);
        dropShadow6.setRadius(4.6925);
        dropShadow6.setSpread(0.83);
        dropShadow6.setWidth(8.9);
        label1.setEffect(dropShadow6);

        textOPlayer.setEditable(false);
        textOPlayer.setLayoutX(323.0);
        textOPlayer.setLayoutY(347.0);
        textOPlayer.setPrefHeight(39.0);
        textOPlayer.setPrefWidth(88.0);
        textOPlayer.setStyle("-fx-background-radius: 50; -fx-alignment: center;");
        textOPlayer.setFont(new Font("System Bold", 18.0));

        textTotalPlayers.setEditable(false);
        textTotalPlayers.setLayoutX(322.0);
        textTotalPlayers.setLayoutY(290.0);
        textTotalPlayers.setPrefHeight(39.0);
        textTotalPlayers.setPrefWidth(88.0);
        textTotalPlayers.setStyle("-fx-background-radius: 50; -fx-alignment: center;");
        textTotalPlayers.setFont(new Font("System Bold", 18.0));

        label2.setLayoutX(46.0);
        label2.setLayoutY(354.0);
        label2.setText("Number of online players");
        label2.setFont(new Font("Franklin Gothic Medium", 21.0));

        dropShadow7.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow7.setHeight(11.87);
        dropShadow7.setRadius(4.6925);
        dropShadow7.setSpread(0.83);
        dropShadow7.setWidth(8.9);
        label2.setEffect(dropShadow7);

        btnLogOut.setLayoutX(56.0);
        btnLogOut.setLayoutY(486.0);
        btnLogOut.setMnemonicParsing(false);
        btnLogOut.setPrefHeight(63.0);
        btnLogOut.setPrefWidth(133.0);
        btnLogOut.setStyle("-fx-background-radius: 100; -fx-background-color: #EAD3D7;");
        btnLogOut.setText("Log Out");
        btnLogOut.setTextFill(javafx.scene.paint.Color.valueOf("#43115b"));
        btnLogOut.setFont(new Font("Gill Sans Ultra Bold Condensed", 22.0));
        btnLogOut.setCursor(Cursor.HAND);

        dropShadow8.setHeight(35.83);
        dropShadow8.setRadius(17.415);
        dropShadow8.setWidth(35.83);
        btnLogOut.setEffect(dropShadow8);

        textAplayer.setEditable(false);
        textAplayer.setLayoutX(322.0);
        textAplayer.setLayoutY(405.0);
        textAplayer.setPrefHeight(39.0);
        textAplayer.setPrefWidth(88.0);
        textAplayer.setStyle("-fx-background-radius: 50; -fx-alignment: center;");
        textAplayer.setFont(new Font("System Bold", 18.0));

        label3.setLayoutX(43.0);
        label3.setLayoutY(412.0);
        label3.setText("Number of available players");
        label3.setFont(new Font("Franklin Gothic Medium", 21.0));

        dropShadow9.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow9.setHeight(11.87);
        dropShadow9.setRadius(4.6925);
        dropShadow9.setSpread(0.83);
        dropShadow9.setWidth(8.9);
        label3.setEffect(dropShadow9);

        getChildren().add(rectangle);
        getChildren().add(label);
        getChildren().add(circle);
        getChildren().add(rectangle0);
        getChildren().add(rectangle1);
        getChildren().add(rectangle2);
        getChildren().add(btnLogin);
        getChildren().add(circle0);
        getChildren().add(btnClose);
        getChildren().add(btnMin);
        getChildren().add(btnRefresh);
        getChildren().add(btnSwitch);
        getChildren().add(label0);
        getChildren().add(btnStatus);
        getChildren().add(label1);
        getChildren().add(textOPlayer);
        getChildren().add(textTotalPlayers);
        getChildren().add(label2);
        getChildren().add(btnLogOut);
        getChildren().add(textAplayer);
        getChildren().add(label3);
        
        btnSwitch.setText("Off");
        btnMin.setOnAction(e -> {
            Stage stage = (Stage) btnMin.getScene().getWindow();
            stage.setIconified(true); // This will minimize the window
        });
        btnClose.setOnAction(e -> {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServerStatus.class.getName()).log(Level.SEVERE, null, ex);
            }
            Platform.exit();
            th.stop();
            stage.close();
        });
        isConnected = true;
        Newconnection = connection;
        btnSwitch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Newconnection = switchConnection(connection);
            }
        });
        th = new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(() -> {
                        displayAvailableplayers(Newconnection);
                        displayOnlineplayers(Newconnection);
                        displayTotalplayers(Newconnection);
                    });
                    Thread.sleep(6000); // Sleep for 6 seconds (for demonstration)
                } catch (InterruptedException ex) {
                    Platform.runLater(() -> showAlert("Server is Disconnected!", "The connection to SQL Server has been closed."));
                    Logger.getLogger(ServerStatus.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        th.start(); // Start the thread

        btnRefresh.setOnAction((event) -> {
                if (Newconnection!=null) {
                    displayAvailableplayers(Newconnection);
                    displayOnlineplayers(Newconnection);
                    displayTotalplayers(Newconnection);
                } else {
                    showAlert("Server is Disconected!", "The connection to SQL Server has been closed.");
                }
         
            btnRefresh.setDisable(true); // Disable the button when clicked
            new Thread(() -> {
                try {
                    Thread.sleep(10000); // Sleep for 10 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                // After 30 seconds, enable the button on the JavaFX Application Thread
                Platform.runLater(() -> btnRefresh.setDisable(false));
            }).start();
        });
        btnLogOut.setOnAction((event) -> {
                    disconnectFromSQL(connection);
                    th.stop();
                    Stage stage = (Stage) btnLogOut.getScene().getWindow();
                    navigateToNextScene(stage);
                
                });
        }
    
                

    

    private Connection switchConnection(Connection connection) {
        if (isConnected) {
            th.suspend();
            disconnectFromSQL(connection);
        } else {
            try {
                connection = connectToSQL();
                th.resume();
                return connection;
            } catch (ClassNotFoundException e) {
                showAlert("Server is Disconected!", "The connection to SQL Server has been closed.");
                e.printStackTrace(); // Proper error handling needed here
            }
        }
        return null;
    }

    private void disconnectFromSQL(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                isConnected = false;
                btnSwitch.setText("On");
                btnStatus.setStyle("-fx-background-color: #ff0000");
                btnStatus.setText("Disconnected");
                System.out.println("Disconnected from DataBase Server.");
            
            } catch (SQLException e) {
                System.out.println("Error while disconnecting: " + e.getMessage());
            }
        }
       
    }

    private Connection connectToSQL() throws ClassNotFoundException {
        String server = "DESKTOP-C2J9487:1433";
        String database = "gameDB";
        String username = "abdelrhman";
        String password = "root";
        Connection connection = null;
        try {
            DriverManager.registerDriver(new ClientDriver());
             connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToeDB", "root", "root");
          
            isConnected = true;
            btnSwitch.setText("Off");
            btnStatus.setStyle("-fx-background-color: #3bd035");
            btnStatus.setText("Connected");
            System.out.println("Connected to DataBase.");
        } catch (SQLException e) {
            isConnected = false;
            btnSwitch.setText("On");
            System.out.println("Connection failed. Error: " + e.getMessage());
        }
        return connection;
    }

    private void displayTotalplayers(Connection connection) {
        try {
            String countPlayersQuery = "SELECT COUNT(*) AS total_players FROM player";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countPlayersQuery);

            int playerCount = 0;
            if (resultSet.next()) {
                playerCount = resultSet.getInt("total_players");
            }
            textTotalPlayers.setText(String.valueOf(playerCount));

            resultSet.close();
            statement.close();
            System.out.println("Connected to TicTacToeDB.");
        } catch (SQLException ex) {
            showAlert("Server is Disconected!", "The connection to SQL Server has been closed.");
            Logger.getLogger(ServerStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayOnlineplayers(Connection connection) {
        try {
            String countPlayersQuery = "SELECT COUNT(*) AS total_players FROM player WHERE isAvailable = false";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countPlayersQuery);

            int playerCount = 0;
            if (resultSet.next()) {
                playerCount = resultSet.getInt("total_players");
            }
            textOPlayer.setText(String.valueOf(playerCount));

            resultSet.close();
            statement.close();
            System.out.println("Connected to TicTacToeDB.");
        } catch (SQLException ex) {
            showAlert("Server is Disconected!", "The connection to SQL Server has been closed.");
            Logger.getLogger(ServerStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayAvailableplayers(Connection connection) {
        try {
            String countPlayersQuery = "SELECT COUNT(*) AS total_players FROM player WHERE isAvailable = true";
            // Create statement and execute query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countPlayersQuery);

            // Get the player count from the result set
            int playerCount = 0;
            if (resultSet.next()) {
                playerCount = resultSet.getInt("total_players");
            }
            // Display the player count in the status label
            textAplayer.setText(String.valueOf(playerCount));

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();

        } catch (SQLException ex) {
            showAlert("Server is Disconected!", "The connection to SQL Server has been closed.");
            Logger.getLogger(ServerStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void navigateToNextScene(Stage stage) {
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.TRANSPARENT);
        Parent root = new ServerLogin();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
        stage.close();
    }
}
