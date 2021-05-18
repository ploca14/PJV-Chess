package cz.cvut.fel.pjv;

public class PlayerStats {
    private String playerName;
    private Integer gamesPlayed;
    private Integer gamesWon;
    private Integer gamesLost;
    private String winrate;

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
}
