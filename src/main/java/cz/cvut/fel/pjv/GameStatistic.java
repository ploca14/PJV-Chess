package cz.cvut.fel.pjv;

public class GameStatistic {
    private Integer time;
    private String winner;
    private String loser;

    public Integer getTime() {
        return time;
    }

    public GameStatistic(Integer time, String winner, String loser) {
        this.time = time;
        this.winner = winner;
        this.loser = loser;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLoser() {
        return loser;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }
}
