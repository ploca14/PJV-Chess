package cz.cvut.fel.pjv.controller.network;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import cz.cvut.fel.pjv.model.PlayerStats;
import cz.cvut.fel.pjv.model.GameStatistic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class StatisticsController implements Runnable {
    private final static Logger logger = Logger.getLogger(StatisticsController.class.getName());
    private static final String GAME_STATISTICS_CSV = "./game-statistics.csv";
    private static final String PLAYER_STATISTICS_CSV = "./player-statistics.csv";
    private boolean stopThread = false;

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(6969);

            while(!stopThread) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

                String command = inputStream.readUTF();
                if (command.equals("gameStatistics")) {
                    outputStream.writeObject(getGameStatistics());
                } else if (command.equals("playerStatistics")) {
                    outputStream.writeObject(getPlayerStatistics());
                }

                inputStream.close();
                outputStream.close();
                clientSocket.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.warning("Statistics: Unable to communicate");
        }
    }

    private List<PlayerStats> getPlayerStatistics() {
        List<PlayerStats> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(PLAYER_STATISTICS_CSV))) {
            CsvToBean<PlayerStats> csvToBean = new CsvToBeanBuilder<PlayerStats>(csvReader)
                    .withType(PlayerStats.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            records = csvToBean.parse();
        } catch (FileNotFoundException fileNotFoundException) {
            logger.severe("Statistics: Player statistics file not found!");
        } catch (IOException exception) {
            logger.severe("Statistics: Unable to read player statistics!");
        }

        records.sort(Comparator.comparing(PlayerStats::getWinrate));
        return records;
    }

    private List<GameStatistic> getGameStatistics() {
        List<GameStatistic> records = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(GAME_STATISTICS_CSV))) {
            CsvToBean<GameStatistic> csvToBean = new CsvToBeanBuilder<GameStatistic>(reader)
                    .withType(GameStatistic.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            records = csvToBean.parse();
        } catch (FileNotFoundException fileNotFoundException) {
            logger.severe("Statistics: Game statistics file not found!");
        } catch (IOException exception) {
            logger.severe("Statistics: Unable to read game statistics!");
        }

        records.sort(Comparator.comparing(GameStatistic::getTime));
        return records;
    }

    public void stopThread() {
        stopThread = true;
    }
}
