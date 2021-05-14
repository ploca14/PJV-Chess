package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.controller.ClientController;
import cz.cvut.fel.pjv.view.ClientView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientApp extends Application {

    ClientView clientView = new ClientView();
    ClientController clientController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Chess Game");
        stage.setWidth(615);
        stage.sizeToScene();
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        stage.setResizable(false);
        stage.setScene(clientView.createScene());
        stage.show();

        clientController = new ClientController(clientView, stage);
    }
}
