package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.model.network.Lobby;
import cz.cvut.fel.pjv.model.network.Server;
import cz.cvut.fel.pjv.view.ServerView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

public class ServerController {
    private ServerView view;
    private Server model;

    public ServerController(ServerView serverView, Server model) {
        this.view = serverView;
        this.model = model;
        initController();
    }

    private void initController() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String nameValue = view.getNameField().getText();
                    int portValue = Integer.parseInt(view.getPortField().getText());

                    if (validateName(nameValue) && validatePort(portValue)) {
                        Lobby lobby = new Lobby(nameValue, portValue);
                        LobbyController lobbyController = new LobbyController(lobby, model);
                        Thread thread = new Thread(lobbyController);
                        thread.start();

                        model.addLobby(lobby);
                    }

                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "The port must be a number").show();
                }
            }
        };

        view.getCreateButton().setOnAction(buttonHandler);
        view.getTable().setItems(model.getLobbies());
    }

    private boolean validatePort(Integer port) {
        if (port < 1  || port > 65535) {
            new Alert(Alert.AlertType.ERROR, "The port must be between 1 and 65535").show();
            return false;
        }

        return true;
    }

    private boolean validateName(String name) {
        if (name.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "You must specify a name").show();
            return false;
        }

        return true;
    }

}
