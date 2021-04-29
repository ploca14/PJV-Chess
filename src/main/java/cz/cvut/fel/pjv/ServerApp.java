package cz.cvut.fel.pjv;

import cz.cvut.fel.pjv.controller.network.ServerController;
import cz.cvut.fel.pjv.model.network.Server;
import cz.cvut.fel.pjv.view.ServerView;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerApp extends Application {
    ServerView serverView = new ServerView();
    Server serverModel = new Server();
    ServerController serverController;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Chess Server");
        stage.setWidth(600);
        stage.setHeight(600);
        stage.setScene(serverView.getScene());
        stage.show();

        serverController = new ServerController(serverView, serverModel);
    }
}
