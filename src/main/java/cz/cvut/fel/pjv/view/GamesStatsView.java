package cz.cvut.fel.pjv.view;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import cz.cvut.fel.pjv.ClientApp;
import cz.cvut.fel.pjv.GameStatistic;
import cz.cvut.fel.pjv.PlayerStats;
import cz.cvut.fel.pjv.controller.ClientController;
import cz.cvut.fel.pjv.controller.GameController;
import cz.cvut.fel.pjv.model.Game;
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
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GamesStatsView {
    private final VBox root = new VBox();
    private final VBox overView = new VBox();
    private final HBox insertIp = new HBox();
    TableView<GameStatistic> tableView;
    Label title = new Label("STATISTICS");
    TabPane tabPane;

    public GamesStatsView() {

    }

    public Scene createScene(){
        Scene scene = new Scene(root);
        createStatsOverview();
        return scene;
    }

    public void createStatsOverview() {
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        title.setPadding(new Insets(10, 0, 50, 0));

        Label labell = new Label("ip adress: ");
        TextField ipTextField = new TextField();
        Button submitBtn = new Button("submit");
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

        tableView = new TableView<>();;
        tableView.setItems(getGameStats());
        tableView.editingCellProperty();
        tableView.getColumns().addAll(timeColumn, winnerNameColumn, loserNameColumn);

        overView.setAlignment(Pos.CENTER);
        overView.getChildren().addAll(title, insertIp, tableView);

        overView.setPadding(new Insets(30,30,30,30));
        root.getChildren().addAll(overView);
    }

    public ObservableList<GameStatistic> getGameStats() {
        ArrayList<GameStatistic> gameStatsList = new ArrayList<>();
        List<String[]> allData = null;
        try (CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\kristov\\Documents\\chess\\src\\gameStatistics.txt"));) {
            allData = csvReader.readAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        for (String[] row: allData
        ) {
            String time = row[0];
            String winner = row[1];
            String loser = row[2];

            gameStatsList.add(new GameStatistic(Integer.parseInt(time), winner, loser));
        }

        Collections.sort(gameStatsList, Comparator.comparing(GameStatistic::getTime));
        return FXCollections.observableArrayList(gameStatsList);
    }
}
