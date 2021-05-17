package cz.cvut.fel.pjv.model;

import java.io.Serializable;

public class Timer implements Serializable {
    private final int TIMER_LENGTH_SECONDS = 600;
    private int secondsPassed = 0;
    private boolean isTimerRunning = false;
    private boolean timerFinished = false;

    /**
     * @return How many minutes left there is for the mm:ss format
     */
    public int getMinutesLeft() {
        return (TIMER_LENGTH_SECONDS - secondsPassed) / 60;
    }

    /**
     * @return How many seconds left there is for the mm:ss format
     */
    public int getSecondsLeft() {
        return (TIMER_LENGTH_SECONDS - secondsPassed) % 60;
    }

    /**
     * This methods is used to increment the seconds passed
     */
    public synchronized void increaseSecondsPassed() {
        if (++secondsPassed == TIMER_LENGTH_SECONDS) {
            timerFinished = true;
        }
    }

    /**
     * @return Whether the timer is running or not
     */
    public boolean getIsTimerRunning() {
        return isTimerRunning;
    }

    /**
     * @param isTimerRunning Whether we want to set the timer to be running or not
     */
    public void setRunning(boolean isTimerRunning) {
        this.isTimerRunning = isTimerRunning;
    }

    /**
     * @return whether the timer reached 0
     */
    public boolean getTimerFinished() {
        return timerFinished;
    }

    public int getSecondsPassed() {
        return secondsPassed;
    }
}
