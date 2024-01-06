/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author w
 */
public class ServerApp extends Application {

    public static Stage stage;

    @Override
    public void start(Stage s) throws Exception {
        stage = s;
        stage.initStyle(StageStyle.TRANSPARENT);
        Parent root = new ServerLogin();
        util.Util.displayScreen(root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
