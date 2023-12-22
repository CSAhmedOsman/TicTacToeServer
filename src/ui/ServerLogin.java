/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.IOException;
import server.Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.derby.jdbc.ClientDriver;
import util.Database;

public class ServerLogin extends AnchorPane {

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
    protected final GridPane gridPane;
    protected final ColumnConstraints columnConstraints;
    protected final ColumnConstraints columnConstraints0;
    protected final RowConstraints rowConstraints;
    protected final RowConstraints rowConstraints0;
    protected final RowConstraints rowConstraints1;
    protected final RowConstraints rowConstraints2;
    protected final TextField textServer;
    protected final Label label0;
    protected final DropShadow dropShadow2;
    protected final TextField textDBname;
    protected final Label label1;
    protected final DropShadow dropShadow3;
    protected final Label label2;
    protected final DropShadow dropShadow4;
    protected final TextField textUserName;
    protected final Label label3;
    protected final DropShadow dropShadow5;
    protected final Button btnConnect;
    protected final DropShadow dropShadow6;
    protected final PasswordField passwordField;
    private Label statusLabel;
    private double xOffset = 0;
    private double yOffset = 0;
    Server server;

    public ServerLogin(Stage stage) {

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
        gridPane = new GridPane();
        columnConstraints = new ColumnConstraints();
        columnConstraints0 = new ColumnConstraints();
        rowConstraints = new RowConstraints();
        rowConstraints0 = new RowConstraints();
        rowConstraints1 = new RowConstraints();
        rowConstraints2 = new RowConstraints();
        textServer = new TextField();
        label0 = new Label();
        dropShadow2 = new DropShadow();
        textDBname = new TextField();
        label1 = new Label();
        dropShadow3 = new DropShadow();
        label2 = new Label();
        dropShadow4 = new DropShadow();
        textUserName = new TextField();
        label3 = new Label();
        dropShadow5 = new DropShadow();
        btnConnect = new Button();
        dropShadow6 = new DropShadow();
        statusLabel = new Label();
        passwordField = new PasswordField();
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

        label.setLayoutX(132.0);
        label.setLayoutY(72.0);
        label.setPrefHeight(112.0);
        label.setPrefWidth(437.0);
        label.setText("Tic-Tac-Toe Server");
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

        dropShadow.setColor(Color.web("#fff7f7"));
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
        dropShadow.setColor(Color.web("#fff7f7"));
        dropShadow1.setSpread(0.69);
        btnMin.setEffect(dropShadow1);

        gridPane.setLayoutX(159.0);
        gridPane.setLayoutY(204.0);
        gridPane.setPrefHeight(193.0);
        gridPane.setPrefWidth(392.0);

        columnConstraints.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints.setMaxWidth(167.0);
        columnConstraints.setMinWidth(10.0);
        columnConstraints.setPrefWidth(141.0);

        columnConstraints0.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints0.setMaxWidth(235.0);
        columnConstraints0.setMinWidth(10.0);
        columnConstraints0.setPrefWidth(221.0);

        rowConstraints.setMaxHeight(47.0);
        rowConstraints.setMinHeight(10.0);
        rowConstraints.setPrefHeight(47.0);
        rowConstraints.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        rowConstraints0.setMaxHeight(152.0);
        rowConstraints0.setMinHeight(7.0);
        rowConstraints0.setPrefHeight(62.0);
        rowConstraints0.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        rowConstraints1.setMaxHeight(152.0);
        rowConstraints1.setMinHeight(10.0);
        rowConstraints1.setPrefHeight(49.0);
        rowConstraints1.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        rowConstraints2.setMaxHeight(152.0);
        rowConstraints2.setMinHeight(10.0);
        rowConstraints2.setPrefHeight(53.0);
        rowConstraints2.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        GridPane.setColumnIndex(textServer, 1);
        textServer.setPrefHeight(35.0);
        textServer.setPrefWidth(228.0);
        textServer.setStyle("-fx-background-radius: 50;");

        label0.setText("Server:");
        label0.setTextOverrun(javafx.scene.control.OverrunStyle.LEADING_WORD_ELLIPSIS);
        label0.setFont(new Font("Franklin Gothic Medium", 21.0));

        dropShadow2.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow2.setHeight(11.87);
        dropShadow2.setRadius(4.6925);
        dropShadow2.setSpread(0.83);
        dropShadow2.setWidth(8.9);
        label0.setEffect(dropShadow2);

        GridPane.setColumnIndex(textDBname, 1);
        GridPane.setRowIndex(textDBname, 1);
        textDBname.setPrefHeight(37.0);
        textDBname.setPrefWidth(222.0);
        textDBname.setStyle("-fx-background-radius: 50;");

        GridPane.setRowIndex(label1, 1);
        label1.setText("DataBase Name");
        label1.setFont(new Font("Franklin Gothic Medium", 18.0));

        dropShadow3.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow3.setHeight(11.87);
        dropShadow3.setRadius(4.6925);
        dropShadow3.setSpread(0.83);
        dropShadow3.setWidth(8.9);
        label1.setEffect(dropShadow3);

        GridPane.setRowIndex(label2, 2);
        label2.setText("User Name");
        label2.setFont(new Font("Franklin Gothic Medium", 18.0));

        dropShadow4.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow4.setHeight(11.87);
        dropShadow4.setRadius(4.6925);
        dropShadow4.setSpread(0.83);
        dropShadow4.setWidth(8.9);
        label2.setEffect(dropShadow4);

        GridPane.setColumnIndex(textUserName, 1);
        GridPane.setRowIndex(textUserName, 2);
        textUserName.setPrefHeight(37.0);
        textUserName.setPrefWidth(216.0);
        textUserName.setStyle("-fx-background-radius: 50;");

        GridPane.setRowIndex(label3, 3);
        label3.setPrefHeight(22.0);
        label3.setPrefWidth(108.0);
        label3.setText("Password");
        label3.setFont(new Font("Franklin Gothic Medium", 18.0));

        dropShadow5.setColor(javafx.scene.paint.Color.WHITE);
        dropShadow5.setHeight(11.87);
        dropShadow5.setRadius(4.6925);
        dropShadow5.setSpread(0.83);
        dropShadow5.setWidth(8.9);
        label3.setEffect(dropShadow5);

        GridPane.setColumnIndex(passwordField, 1);
        GridPane.setRowIndex(passwordField, 3);
        passwordField.setPrefHeight(37.0);
        passwordField.setPrefWidth(216.0);
        passwordField.setStyle("-fx-background-radius: 50;");

        btnConnect.setLayoutX(258.0);
        btnConnect.setLayoutY(417.0);
        btnConnect.setMnemonicParsing(false);
        btnConnect.setPrefHeight(37.0);
        btnConnect.setPrefWidth(177.0);
        btnConnect.setStyle("-fx-background-radius: 100; -fx-background-color: #EAD3D7;");
        btnConnect.setText("Connect");
        btnConnect.setTextFill(javafx.scene.paint.Color.valueOf("#43115b"));
        btnConnect.setFont(new Font("Gill Sans Ultra Bold Condensed", 22.0));
        btnConnect.setCursor(Cursor.HAND);

        dropShadow6.setHeight(35.83);
        dropShadow6.setRadius(17.415);
        dropShadow6.setWidth(35.83);
        btnConnect.setEffect(dropShadow6);

        statusLabel.setLayoutX(50.0);
        statusLabel.setLayoutY(480.0);
        statusLabel.setFont(new Font("Arial", 15));
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
        gridPane.getColumnConstraints().add(columnConstraints);
        gridPane.getColumnConstraints().add(columnConstraints0);
        gridPane.getRowConstraints().add(rowConstraints);
        gridPane.getRowConstraints().add(rowConstraints0);
        gridPane.getRowConstraints().add(rowConstraints1);
        gridPane.getRowConstraints().add(rowConstraints2);
        gridPane.getChildren().add(textServer);
        gridPane.getChildren().add(label0);
        gridPane.getChildren().add(textDBname);
        gridPane.getChildren().add(label1);
        gridPane.getChildren().add(label2);
        gridPane.getChildren().add(textUserName);
        gridPane.getChildren().add(label3);
        gridPane.getChildren().add(passwordField);
        getChildren().add(gridPane);
        getChildren().add(btnConnect);
        getChildren().add(statusLabel);

        textDBname.setText("TicTacToeDB");
        textDBname.setDisable(true);
        textServer.setText("5005");
        textServer.setDisable(true);
        
        btnConnect.setOnAction(e -> {
            connectToServer();
            connectToDatabase();
            navigateToNextScene(stage, Database.getConnection());
        });
        btnMin.setOnAction(e -> {
            stage.setIconified(true); // This will minimize the window
        });
        btnClose.setOnAction(e -> {
            Platform.exit();
            stage.close();
        });
    }

    private boolean connectToDatabase() {
        String username = textUserName.getText();
        String password = passwordField.getText();
        try {
            DriverManager.registerDriver(new ClientDriver());
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToeDB", username, password);
            statusLabel.setText("Connected to database!");
            return true;
        } catch (SQLException e) {
            // Connection failed
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("DB Connection failed: \n" + e.getMessage());
            return false;
        }
    }

    private boolean connectToServer() {
        try {
            server = new Server();
            return true;
        } catch (IOException e) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Server Connection failed: \n" + e.getMessage());
            return false;
        }
    }

    private void navigateToNextScene(Stage stage, Connection connection) {

        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.TRANSPARENT);
        Parent root = new ServerStatus(connection, server);
        Scene scene = new Scene(root);
        
        newStage.setScene(scene);
        newStage.show();
        stage.close();

    }
}
