package cz.cvut.fel.pjv.view;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import cz.cvut.fel.pjv.GameStatistic;
import cz.cvut.fel.pjv.PlayerStats;
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerStatsView {
    private final VBox root = new VBox();
    private final VBox overView = new VBox();
    private final HBox insertIp = new HBox();
    TableView<PlayerStats> tableView;
    Label title = new Label("STATISTICS");
    TabPane tabPane;

    public PlayerStatsView() {

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

        insertIp.getChildren().addAll(labell, ipTextField, submitBtn);
        insertIp.setSpacing(10);

        TableColumn<PlayerStats, String> nameColumn = new TableColumn<>("PLAYER NAME");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));

        TableColumn<PlayerStats, Integer> gamesPlayedColumn = new TableColumn<>("GAMES PLAYED");
        gamesPlayedColumn.setMinWidth(150);
        gamesPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("gamesPlayed"));

        TableColumn<PlayerStats, String> winrateColumn = new TableColumn<>("WIN RATE");
        winrateColumn.setMinWidth(150);
        winrateColumn.setCellValueFactory(new PropertyValueFactory<>("winrate"));

        tableView = new TableView<>();;
        tableView.setItems(getPlayersStats());
        tableView.editingCellProperty();
        tableView.getColumns().addAll(nameColumn, gamesPlayedColumn, winrateColumn);

        overView.setAlignment(Pos.CENTER);
        overView.getChildren().addAll(title, insertIp, tableView);

        overView.setPadding(new Insets(30,30,30,30));
        root.getChildren().addAll(overView);
    }
    public ObservableList<PlayerStats> getPlayersStats() {
        ArrayList<PlayerStats> playersList = new ArrayList<>();
        List<String[]> allData = null;
        try (CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\kristov\\Documents\\chess\\src\\playerStatistics.txt"));) {
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
            String name = row[0];
            String gamesPlayed = row[1];
            String gamesWon = row[2];
            String gamesLost = row[3];

            playersList.add(new PlayerStats(name, Integer.parseInt(gamesPlayed), Integer.parseInt(gamesWon), Integer.parseInt(gamesLost)));
        }

        return FXCollections.observableArrayList(playersList);
    }
}
