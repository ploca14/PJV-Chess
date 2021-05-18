package cz.cvut.fel.pjv.model;

import com.opencsv.bean.CsvBindByPosition;

import java.io.Serializable;

public class GameStatistic implements Serializable {
    @CsvBindByPosition(position = 0)
    private String time;
    @CsvBindByPosition(position = 1)
    private String winner;
    @CsvBindByPosition(position = 2)
    private String loser;

    public GameStatistic() { }

    public GameStatistic(String time, String winner, String loser) {
        this.time = time;
        this.winner = winner;
        this.loser = loser;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
