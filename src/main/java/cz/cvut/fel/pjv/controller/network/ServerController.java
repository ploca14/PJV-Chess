package cz.cvut.fel.pjv.controller.network;

import cz.cvut.fel.pjv.model.network.Lobby;
import cz.cvut.fel.pjv.model.network.Server;
import cz.cvut.fel.pjv.view.ServerView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class ServerController {
    private ServerView view;
    private Server model;
    private List<LobbyController> lobbies = new ArrayList<>();

    /**
     * @param serverView The server view this controller will be rerendering
     * @param model The server model this controller will be updating
     */
    public ServerController(ServerView serverView, Server model) {
        this.view = serverView;
        this.model = model;
        initController();
    }

    /**
     * This method is used to initialize the controller
     */
    private void initController() {
        // First we create the neccesary event listeners
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // When the user click on create lobby
                try {
                    // We get the values from the UI
                    String nameValue = view.getNameField().getText();
                    int portValue = Integer.parseInt(view.getPortField().getText());

                    // We validate those values
                    if (validateName(nameValue) && validatePort(portValue)) {
                        // If all the values are valid we create a new lobby
                        Lobby lobby = new Lobby(nameValue, portValue);
                        LobbyController lobbyController = new LobbyController(lobby, model);
                        Thread thread = new Thread(lobbyController); // Each lobby runs on its own thread
                        thread.start();

                        // We add the lobby the the model so it gets represented in the UI
                        model.addLobby(lobby);
                        lobbies.add(lobbyController);
                    }

                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "The port must be a number").show();
                }
            }
        };

        view.getCreateButton().setOnAction(buttonHandler);

        // And we set the data source for our active lobbies table
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

    public List<LobbyController> getLobbies() {
        return lobbies;
    }
}
