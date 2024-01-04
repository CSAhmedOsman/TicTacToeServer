/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.IOException;
import javafx.scene.Scene;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
import org.apache.derby.jdbc.ClientDriver;
import server.Server;

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
    protected final Rectangle rectangle3;
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
    protected final Label labelError;
    protected final TextField textOPlayer;
    protected final TextField textTotalPlayers;
    protected final Button btnLogOut;
    protected final DropShadow dropShadow6;
    protected final TextField textAPlayer;
    protected final CategoryAxis categoryAxis;
    protected final NumberAxis numberAxis;
    protected final BarChart<String, Number> barChart;
    protected boolean isConnected;
    protected Thread th;
    protected Connection myConnection;
    protected Connection newConnection;
    protected Server myServer;
    protected boolean isRunning;

    public ServerStatus(Connection connection, Server server) {

        rectangle = new Rectangle();
        label = new Label();
        dropShadow = new DropShadow();
        circle = new Circle();
        rectangle0 = new Rectangle();
        rectangle1 = new Rectangle();
        rectangle2 = new Rectangle();
        rectangle3 = new Rectangle();
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
        labelError = new Label();
        dropShadow6 = new DropShadow();
        textTotalPlayers = new TextField();
        textOPlayer = new TextField();
        textAPlayer = new TextField();
        btnLogOut = new Button();
        categoryAxis = new CategoryAxis();
        numberAxis = new NumberAxis();
        barChart = new BarChart<>(categoryAxis, numberAxis);

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(600.0);
        setPrefWidth(700.0);

        rectangle.setArcHeight(5.0);
        rectangle.setArcWidth(5.0);
        rectangle.setFill(javafx.scene.paint.Color.valueOf("#ffbdbd"));
        rectangle.setHeight(600.0);
        rectangle.setWidth(700.0);
        rectangle.setSmooth(false);
        rectangle.setStroke(javafx.scene.paint.Color.BLACK);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

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

        circle0.setFill(javafx.scene.paint.Color.valueOf("#00000078"));
        circle0.setLayoutX(662.0);
        circle0.setLayoutY(548.0);
        circle0.setOpacity(0.2);
        circle0.setRadius(71.0);
        circle0.setStroke(javafx.scene.paint.Color.valueOf("#d0cbcb"));
        circle0.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

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

        rectangle3.setArcHeight(5.0);
        rectangle3.setArcWidth(5.0);
        rectangle3.setFill(javafx.scene.paint.Color.valueOf("#141414"));
        rectangle3.setHeight(188.0);
        rectangle3.setLayoutX(626.0);
        rectangle3.setLayoutY(169.0);
        rectangle3.setOpacity(0.2);
        rectangle3.setRotate(-138.0);
        rectangle3.setStroke(javafx.scene.paint.Color.BLACK);
        rectangle3.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle3.setWidth(29.0);

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

        btnRefresh.setLayoutX(80.0);
        btnRefresh.setLayoutY(430.0);
        btnRefresh.setMnemonicParsing(false);
        btnRefresh.setPrefHeight(50.0);
        btnRefresh.setPrefWidth(120.0);
        btnRefresh.setStyle("-fx-background-radius: 100; -fx-background-color: #EAD3D7;");
        btnRefresh.setText("Refresh");
        btnRefresh.setTextFill(javafx.scene.paint.Color.valueOf("#43115b"));
        btnRefresh.setFont(new Font("Gill Sans Ultra Bold Condensed", 22.0));
        btnRefresh.setCursor(Cursor.HAND);

        dropShadow2.setHeight(35.83);
        dropShadow2.setRadius(17.415);
        dropShadow2.setWidth(35.83);
        btnRefresh.setEffect(dropShadow2);

        btnSwitch.setLayoutX(475.0);
        btnSwitch.setLayoutY(180.0);
        btnSwitch.setMnemonicParsing(false);
        btnSwitch.setPrefHeight(50.0);
        btnSwitch.setPrefWidth(100.0);
        btnSwitch.setText("On");
        btnSwitch.setStyle("-fx-background-radius: 50;");
        btnSwitch.setTextFill(javafx.scene.paint.Color.valueOf("#43115b"));
        btnSwitch.setCursor(Cursor.HAND);
        btnSwitch.setFont(new Font("Gill Sans Ultra Bold Condensed", 23.0));

        dropShadow3.setSpread(0.33);
        btnSwitch.setEffect(dropShadow3);

        btnStatus.setLayoutX(285.0);
        btnStatus.setLayoutY(180.0);
        btnStatus.setMnemonicParsing(false);
        btnStatus.setPrefHeight(50.0);
        btnStatus.setPrefWidth(170.0);
        btnStatus.setText("Disconnected");
        btnStatus.setStyle("-fx-background-color: #ff0000; -fx-background-radius: 30;");
        btnStatus.setFont(new Font("Franklin Gothic Heavy", 20.0));

        btnStatus.setEffect(dropShadow3);

        label0.setLayoutX(80.0);
        label0.setLayoutY(195.0);
        label0.setText("Server Stauts");
        label0.setFont(new Font("Franklin Gothic Medium", 21.0));

        dropShadow4.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow4.setSpread(0.83);
        label0.setEffect(dropShadow4);

        labelError.setLayoutX(285.0);
        labelError.setLayoutY(245.0);
        labelError.setPrefHeight(17.0);
        labelError.setPrefWidth(305.0);
        labelError.setText("");

        dropShadow5.setColor(javafx.scene.paint.Color.RED);
        dropShadow5.setSpread(0.98);
        labelError.setEffect(dropShadow5);

        textTotalPlayers.setDisable(true);
        textTotalPlayers.setEditable(false);
        textTotalPlayers.setLayoutX(285.0);
        textTotalPlayers.setLayoutY(280.0);
        textTotalPlayers.setPrefHeight(40.0);
        textTotalPlayers.setPrefWidth(90.0);
        textTotalPlayers.setStyle("-fx-background-radius: 50; -fx-alignment: center;");
        textTotalPlayers.setFont(new Font("System Bold", 18.0));

        textOPlayer.setDisable(true);
        textOPlayer.setEditable(false);
        textOPlayer.setLayoutX(405.0);
        textOPlayer.setLayoutY(280.0);
        textOPlayer.setPrefHeight(40.0);
        textOPlayer.setPrefWidth(90.0);
        textOPlayer.setStyle("-fx-background-radius: 50; -fx-alignment: center;");
        textOPlayer.setFont(new Font("System Bold", 18.0));

        textAPlayer.setDisable(true);
        textAPlayer.setEditable(false);
        textAPlayer.setLayoutX(525.0);
        textAPlayer.setLayoutY(280.0);
        textAPlayer.setPrefHeight(40.0);
        textAPlayer.setPrefWidth(90.0);
        textAPlayer.setStyle("-fx-background-radius: 50; -fx-alignment: center;");
        textAPlayer.setFont(new Font("System Bold", 18.0));

        btnLogOut.setLayoutX(80.0);
        btnLogOut.setLayoutY(510.0);
        btnLogOut.setMnemonicParsing(false);
        btnLogOut.setPrefHeight(50.0);
        btnLogOut.setPrefWidth(120.0);
        btnLogOut.setStyle("-fx-background-radius: 100; -fx-background-color: #EAD3D7;");
        btnLogOut.setText("Log Out");
        btnLogOut.setTextFill(javafx.scene.paint.Color.valueOf("#43115b"));
        btnLogOut.setFont(new Font("Gill Sans Ultra Bold Condensed", 22.0));
        btnLogOut.setCursor(Cursor.HAND);

        dropShadow6.setHeight(35.83);
        dropShadow6.setRadius(17.415);
        dropShadow6.setWidth(35.83);
        btnLogOut.setEffect(dropShadow6);

        categoryAxis.setLabel("User's Type");
        numberAxis.setLabel("Number of Users");

        XYChart.Series<String, Number> regesteredUsersSeries = new XYChart.Series<>();
        regesteredUsersSeries.setName("Regestered Users");
        XYChart.Series<String, Number> onlineUsersSeries = new XYChart.Series<>();
        onlineUsersSeries.setName("Online Users");
        XYChart.Series<String, Number> playingUsersSeries = new XYChart.Series<>();
        playingUsersSeries.setName("Playing Users");

        regesteredUsersSeries.getData().add(new XYChart.Data<>("Regestered", 0/*put num of users*/));
        onlineUsersSeries.getData().add(new XYChart.Data<>("Online", 0/*put num of users*/));
        playingUsersSeries.getData().add(new XYChart.Data<>("Playing", 0/*put num of users*/));
        barChart.getData().addAll(regesteredUsersSeries, onlineUsersSeries, playingUsersSeries);

        barChart.setLayoutX(225.0);
        barChart.setLayoutY(322.0);
        barChart.setPrefHeight(266.0);
        barChart.setPrefWidth(448.0);

        getChildren().add(rectangle);
        getChildren().add(label);
        getChildren().add(circle);
        getChildren().add(rectangle0);
        getChildren().add(rectangle1);
        getChildren().add(rectangle2);
        getChildren().add(rectangle3);
        getChildren().add(circle0);
        getChildren().add(btnClose);
        getChildren().add(btnMin);
        getChildren().add(btnRefresh);
        getChildren().add(btnSwitch);
        getChildren().add(label0);
        getChildren().add(btnStatus);
        getChildren().add(labelError);
        getChildren().add(textOPlayer);
        getChildren().add(textTotalPlayers);
        getChildren().add(btnLogOut);
        getChildren().add(textAPlayer);
        getChildren().add(barChart);

        //-----------------ahmed + abdelrahman Works--------------------
        isRunning = true;
        isConnected = false;
        myServer = server;
        myConnection = connection;
        newConnection = myConnection;

        try {
            if ((connection != null) && (server != null)) {
                if ((server.isRunning()) && (!connection.isClosed())) {
                    isConnected = true;
                    btnSwitch.setText("Off");
                    btnStatus.setStyle("-fx-background-color: #3bd035; -fx-background-radius: 30;");
                    btnStatus.setText("Connected");
                }
            }
        } catch (SQLException ex) {
            Platform.runLater(() -> util.Util.showDialog(Alert.AlertType.ERROR, "Error!", "Error while Getting Server Status.\n" + ex.getMessage()));
        }

        addListener();

        th = new Thread(() -> {
            while (isRunning) {
                try {
                    Platform.runLater(() -> {
                        if (isConnected) {
                            updateChartData(getTotalPlayers(newConnection),
                                    getOnlinePlayers(newConnection),
                                    getNotAvailableplayers(newConnection));
                        }
                    });
                    Thread.sleep(6000); // Sleep for 6 seconds (for demonstration)
                } catch (InterruptedException ex) {
                    Platform.runLater(() -> util.Util.showDialog(Alert.AlertType.ERROR, "Server is Disconnected!", "The connection to SQL Server has been closed.\n" + ex.getMessage()));
                }
            }
        });
        th.start(); // Start the thread
    }

    private void addListener() {

        btnMin.setOnAction(e -> {
            Stage stage = (Stage) btnMin.getScene().getWindow();
            stage.setIconified(true); // This will minimize the window
        });

        btnClose.setOnAction(e -> {
            try {
                isRunning = false;
                if (newConnection != null) {
                    if (!newConnection.isClosed()) {
                        newConnection.close();
                    }
                }
                if (myServer != null) {
                    if (myServer.isRunning()) {
                        myServer.close();
                    }
                }
            } catch (SQLException ex) {
                Platform.runLater(() -> util.Util.showDialog(Alert.AlertType.ERROR, "Close Error!", "Error while Closing.\n" + ex.getMessage()));
            }
            th.stop();
            Platform.exit();
        });

        btnSwitch.setOnAction((ActionEvent e) -> {
            newConnection = switchConnection(myConnection);
            updateChartData(0, 0, 0);
        });

        btnLogOut.setOnAction((event) -> {
            try {
                isRunning = false;
                disconnectFromSQL(myConnection);
                myServer.close();
                th.stop();
                navigateToNextScene();
            } catch (SQLException ex) {
                Platform.runLater(() -> util.Util.showDialog(Alert.AlertType.ERROR, "Logout Error!", "Error while Logout.\n" + ex.getMessage()));
            }
        });

        btnRefresh.setOnAction((event) -> {
            btnRefresh.setDisable(true); // Disable the button when clicked
            if (newConnection != null && isConnected) {
                updateChartData(getTotalPlayers(newConnection),
                        getOnlinePlayers(newConnection),
                        getNotAvailableplayers(newConnection));
            } else {
                util.Util.showDialog(Alert.AlertType.ERROR, "Server is Disconected!", "The connection to SQL Server has been closed.");
            }
            new Thread(() -> {
                try {
                    Thread.sleep(10000); // Sleep for 10 seconds
                } catch (InterruptedException e) {
                    util.Util.showDialog(Alert.AlertType.ERROR, "Server is Disconected!", "The connection to SQL Server has been closed.");
                    Thread.currentThread().stop();
                }
                // After 30 seconds, enable the button on the JavaFX Application Thread
                Platform.runLater(() -> btnRefresh.setDisable(false));
            }).start();
        });
    }

    private Connection switchConnection(Connection connection) {
        if (isConnected) {
            try {
                th.suspend();
                myServer.close();
                disconnectFromSQL(connection);
                labelError.setText("");
            } catch (SQLException ex) {
                labelError.setText("Error while disconnecting DataBase: " + ex.getMessage());
            }
        } else {
            try {
                connection = connectToSQL();
                myServer.connect();
                th.resume();
                labelError.setText("");
                return connection;
            } catch (IOException ex) {
                labelError.setText("Error while Connecting Server: " + ex.getMessage());
            } catch (SQLException ex) {
                labelError.setText("Error while Connecting Database: " + ex.getMessage());
            }
        }
        return null;
    }

    private void disconnectFromSQL(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
            isConnected = false;
            btnSwitch.setText("On");
            btnStatus.setStyle("-fx-background-color: #ff0000; -fx-background-radius: 30;");
            btnStatus.setText("Disconnected");
            labelError.setText("");
        }
    }

    private Connection connectToSQL() throws SQLException {
        Connection connection = null;
        DriverManager.registerDriver(new ClientDriver());
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToeDB", "root", "root");
        isConnected = true;
        btnSwitch.setText("Off");
        btnStatus.setStyle("-fx-background-color: #3bd035; -fx-background-radius: 30;");
        btnStatus.setText("Connected");
        labelError.setText("");
        return connection;
    }

    private int getTotalPlayers(Connection connection) {
        int playerCount = 0;
        try {
            String countPlayersQuery = "SELECT COUNT(*) AS total_players FROM player";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countPlayersQuery);

            if (resultSet.next()) {
                playerCount = resultSet.getInt("total_players");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            util.Util.showDialog(Alert.AlertType.ERROR, "Server is Disconected!", "The connection to SQL Server has been closed.\n" + ex.getMessage());
        }
        return playerCount;
    }

    private int getOnlinePlayers(Connection connection) {
        int playerCount = 0;
        try {
            String countPlayersQuery = "SELECT COUNT(*) AS total_players FROM player WHERE isOnline = true";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countPlayersQuery);

            if (resultSet.next()) {
                playerCount = resultSet.getInt("total_players");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            util.Util.showDialog(Alert.AlertType.ERROR, "Server is Disconected!", "The connection to SQL Server has been closed.\n" + ex.getMessage());
        }
        return playerCount;
    }

    private int getNotAvailableplayers(Connection connection) {
        int playerCount = 0;
        try {
            String countPlayersQuery = "SELECT COUNT(*) AS total_players FROM player WHERE isOnline = true and isAvailable = false";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countPlayersQuery);

            if (resultSet.next()) {
                playerCount = resultSet.getInt("total_players");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            util.Util.showDialog(Alert.AlertType.ERROR, "Server is Disconected!", "The connection to SQL Server has been closed.\n" + ex.getMessage());
        }
        return playerCount;
    }

    private void navigateToNextScene() {
        Stage stage = (Stage) this.getScene().getWindow();
        Parent root = new ServerLogin();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void updateChartData(int regesteredUsers, int onlineUsers, int playingUsers) {
        XYChart.Series<String, Number> regesteredUsersSeries = barChart.getData().get(0);
        XYChart.Series<String, Number> onlineUsersSeries = barChart.getData().get(1);
        XYChart.Series<String, Number> playingUsersSeries = barChart.getData().get(2);

        regesteredUsersSeries.getData().get(0).setYValue(regesteredUsers);
        textTotalPlayers.setText(String.valueOf(regesteredUsers));
        onlineUsersSeries.getData().get(0).setYValue(onlineUsers);
        textOPlayer.setText(String.valueOf(onlineUsers));
        playingUsersSeries.getData().get(0).setYValue(playingUsers);
        textAPlayer.setText(String.valueOf(playingUsers));
    }
}
