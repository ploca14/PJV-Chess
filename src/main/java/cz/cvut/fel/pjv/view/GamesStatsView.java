package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.GameStatistic;
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


public class GamesStatsView {
    private final VBox root = new VBox();
    private final VBox overView = new VBox();
    private final HBox insertIp = new HBox(10);
    private TableView<GameStatistic> tableView;
    private final Label title = new Label("STATISTICS");
    private final Button submitBtn = new Button("Load statistics");
    private final Button mainMenu = new Button("Main menu");
    private final Label ipLabel = new Label("IP Address: ");
    private final TextField ipTextField = new TextField();

    /**
     * This method is used to create the game stats view scene
     * @return The game stats view scene
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

        TableColumn<GameStatistic, String> timeColumn = new TableColumn<>("TIME");
        timeColumn.setMinWidth(150);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<GameStatistic, String> winnerNameColumn = new TableColumn<>("WINNER");
        winnerNameColumn.setMinWidth(150);
        winnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("loser"));

        TableColumn<GameStatistic, String> loserNameColumn = new TableColumn<>("LOSER");
        loserNameColumn.setMinWidth(150);
        loserNameColumn.setCellValueFactory(new PropertyValueFactory<>("winner"));

        tableView = new TableView<>();
        tableView.getColumns().addAll(timeColumn, winnerNameColumn, loserNameColumn);

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

    /**
     * This method is used to set the data source of the game statistics table
     * @param gameStatsList The data source for the game statistics table
     */
    public void setTableViewItems(List<GameStatistic> gameStatsList) {
        ObservableList<GameStatistic> observableList = FXCollections.observableArrayList(gameStatsList);
        tableView.setItems(observableList);
    }
}
