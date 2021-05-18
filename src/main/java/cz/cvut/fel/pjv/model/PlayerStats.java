package cz.cvut.fel.pjv.model;

import com.opencsv.bean.CsvBindByPosition;

import java.io.Serializable;

public class PlayerStats implements Serializable {
    @CsvBindByPosition(position = 0)
    private String playerName;
    @CsvBindByPosition(position = 1)
    private Integer gamesPlayed;
    @CsvBindByPosition(position = 2)
    private Integer gamesWon;
    @CsvBindByPosition(position = 3)
    private Integer gamesLost;
    @CsvBindByPosition(position = 4)
    private String winrate;

    /**
     * This constructor is used for the CSVReader
     */
    public PlayerStats() { }

    /**
     * @param playerName The players name
     * @param gamesPlayed Total games played
     * @param gamesWon Total games won
     * @param gamesLost Total games lost
     */
    public PlayerStats(String playerName, Integer gamesPlayed, Integer gamesWon, Integer gamesLost) {
        this.playerName = playerName;
        this.gamesPlayed = gamesPlayed;
        this.gamesLost = gamesLost;
        this.gamesWon = gamesWon;
        double winrateDoubleValue = (double) gamesWon / gamesPlayed;
        winrateDoubleValue = winrateDoubleValue*100;
        this.winrate = winrateDoubleValue+"%";
    }

    public Integer getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(Integer gamesLost) {
        this.gamesLost = gamesLost;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    public String getWinrate() {
        return winrate;
    }

    /**
     * This method is used to increase the games played, games won and update the win rate
     */
    public void increaseGamesWon() {
        gamesPlayed++;
        gamesWon++;
        double winrateDoubleValue = (double) gamesWon / gamesPlayed;
        winrateDoubleValue = winrateDoubleValue*100;
        this.winrate = winrateDoubleValue+"%";
    }

    /**
     * This method is used to increase the games played, games lost and update the win rate
     */
    public void increaseGamesLost() {
        gamesPlayed++;
        gamesLost++;
        double winrateDoubleValue = (double) gamesWon / gamesPlayed;
        winrateDoubleValue = winrateDoubleValue*100;
        this.winrate = winrateDoubleValue+"%";
    }
}
