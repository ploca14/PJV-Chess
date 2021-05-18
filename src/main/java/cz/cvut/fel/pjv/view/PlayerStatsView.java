package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.PlayerStats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class PlayerStatsView {
    private final VBox root = new VBox();
    private final VBox overView = new VBox();
    private final HBox insertIp = new HBox(10);
    TableView<PlayerStats> tableView;
    Label title = new Label("STATISTICS");
    private final Button submitBtn = new Button("Load statistics");
    private final Button mainMenu = new Button("Main menu");
    private final Label ipLabel = new Label("IP Address: ");
    private final TextField ipTextField = new TextField();

    /**
     * This method is used to create the player stats view scene
     * @return The player stats view scene
     */
    public Scene createScene(){
        Scene scene = new Scene(root);
        createStatsOverview();
        return scene;
    }

    private void createStatsOverview() {
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        title.setPadding(new Insets(10, 0, 50, 0));

        insertIp.getChildren().addAll(ipLabel, ipTextField, submitBtn, mainMenu);
        insertIp.setPadding(new Insets(10));
        insertIp.setAlignment(Pos.CENTER);

        TableColumn<PlayerStats, String> nameColumn = new TableColumn<>("PLAYER NAME");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));

        TableColumn<PlayerStats, Integer> gamesPlayedColumn = new TableColumn<>("GAMES PLAYED");
        gamesPlayedColumn.setMinWidth(150);
        gamesPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("gamesPlayed"));

        TableColumn<PlayerStats, String> winrateColumn = new TableColumn<>("WIN RATE");
        winrateColumn.setMinWidth(150);
        winrateColumn.setCellValueFactory(new PropertyValueFactory<>("winrate"));

        tableView = new TableView<>();
        tableView.getColumns().addAll(nameColumn, gamesPlayedColumn, winrateColumn);

        overView.setAlignment(Pos.CENTER);
        overView.getChildren().addAll(title, insertIp, tableView);

        overView.setPadding(new Insets(30,30,30,30));
        root.getChildren().addAll(overView);
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }

    public TextField getIpField() {
        return ipTextField;
    }

    public Button getMainMenu() {
        return mainMenu;
    }

    public void setTableViewItems(List<PlayerStats> gameStatsList) {
        ObservableList<PlayerStats> observableList = FXCollections.observableArrayList(gameStatsList);
        tableView.setItems(observableList);
    }
}
