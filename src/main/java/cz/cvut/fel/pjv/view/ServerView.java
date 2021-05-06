package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.controller.network.LobbyController;
import cz.cvut.fel.pjv.model.chestpieces.Color;
import cz.cvut.fel.pjv.model.network.Lobby;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ServerView {
    private final VBox root = new VBox();
    private TableView<Lobby> table = new TableView<>();
    private HBox hBox;
    private Label nameLabel;
    private Label portLabel;
    private TextField portField;
    private TextField nameField;
    private Button createButton;

    public Scene getScene() {
        Scene scene = new Scene(root);

        createForm();
        createTable();

        root.setSpacing(10);
        root.setPadding(new Insets(20));
        return scene;
    }

    private void createForm() {
        Label title = new Label("Create new lobby");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        hBox = new HBox();
        nameLabel = new Label("Lobby name:");
        nameField = new TextField();
        portLabel = new Label("Port number:");
        portField = new TextField();
        createButton = new Button("Create");

        hBox.getChildren().addAll(nameLabel, nameField, portLabel, portField, createButton);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(title, hBox);
    }

    private void createTable() {
        Label title = new Label("Active lobbies");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableColumn<Lobby, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.65));
        table.getColumns().add(nameCol);

        TableColumn<Lobby, Integer> portCol = new TableColumn<>("Port");
        portCol.setCellValueFactory(new PropertyValueFactory<>("port"));
        portCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        portCol.setMinWidth(50);
        table.getColumns().add(portCol);

        TableColumn<Lobby, Integer> connectedPlayersCol = new TableColumn<>("Connected players");
        connectedPlayersCol.setCellValueFactory(cellData -> cellData.getValue().getConnectedPlayersCountProperty().asObject());
        connectedPlayersCol.prefWidthProperty().bind(table.widthProperty().multiply(0.24));
        connectedPlayersCol.setMinWidth(120);
        table.getColumns().add(connectedPlayersCol);

        root.getChildren().addAll(title ,table);
    }

    public TextField getPortField() {
        return portField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public TableView<Lobby> getTable() {
        return table;
    }
}
