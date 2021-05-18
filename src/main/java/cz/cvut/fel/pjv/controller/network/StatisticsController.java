package cz.cvut.fel.pjv.controller.network;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import cz.cvut.fel.pjv.model.PlayerStats;
import cz.cvut.fel.pjv.model.GameStatistic;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class StatisticsController implements Runnable {
    private final static Logger logger = Logger.getLogger(StatisticsController.class.getName());
    private static final String GAME_STATISTICS_CSV = "./game-statistics.csv";
    private static final String PLAYER_STATISTICS_CSV = "./player-statistics.csv";
    private boolean stopThread = false;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private DataInputStream inputStream;

    @Override
    public void run() {
        try {
             serverSocket = new ServerSocket(6969);

            while(!stopThread) {
                clientSocket = serverSocket.accept();
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputStream = new DataInputStream(clientSocket.getInputStream());

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
        } catch (SocketException exception) {
            logger.warning("Statistics: Server socket closed, aborting accept");
        } catch (IOException exception) {
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

        records.sort((o1, o2) -> {
            BigDecimal winRate1 = new BigDecimal(o1.getWinrate().trim().replace("%", ""));
            BigDecimal winRate2 = new BigDecimal(o2.getWinrate().trim().replace("%", ""));
            return winRate2.compareTo(winRate1);
        });
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

    /**
     * This method is used to close all the socket communication and end the thread
     */
    public void stopThread() {
        try {
            if (inputStream != null) { inputStream.close(); }
            if (outputStream != null) { outputStream.close(); }
            if (clientSocket != null) { clientSocket.close(); }

            serverSocket.close();
        } catch (IOException exception) {
            logger.info("Statistics: Unable to close socket");
        }

        stopThread = true;
    }
}
