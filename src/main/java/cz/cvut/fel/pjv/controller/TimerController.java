package cz.cvut.fel.pjv.controller;

import cz.cvut.fel.pjv.model.Timer;
import cz.cvut.fel.pjv.view.TimerView;

public class TimerController implements Runnable {
    private final AbstractGameController gameController;
    private Timer timer;
    private TimerView timerView;
    private boolean stopThread = false;
    private Long previousMilis = 0L;

    /**
     * @param timer The timer model this controller will be updating
     * @param timerView The timer view this controller will be rerendering
     * @param gameController The game controller that has this time controller
     */
    public TimerController(Timer timer, TimerView timerView, AbstractGameController gameController) {
        this.timer = timer;
        this.timerView = timerView;
        this.gameController = gameController;
    }

    @Override
    public void run() {
        // The timer thread should be running until we want the thread to stop or the game is finished
        while (!(stopThread || gameController.getGameModel().getFinished())) {
            Long currentMilis = System.currentTimeMillis();

            // This checks whether at least a second has passed
            if (currentMilis - previousMilis > 1000) {
                previousMilis = currentMilis;

                // If at least a second has passed we check if the timer isn't paused
                if (timer.getIsTimerRunning()) {
                    // If it isn't we increment the seconds passed and rerender the timer view
                    timer.increaseSecondsPassed();
                    timerView.rerender();
                }
            }

            // if the timer reached 0 we stop the thread and tell the controller the player has run out of time
            if (timer.getTimerFinished()) {
                stopThread = true;
                gameController.playerRunOutOfTime();
                break;
            }
        }
    }

    /**
     * @return The timer model
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * @return The timer view
     */
    public TimerView getTimerView() {
        return timerView;
    }

    /**
     * This method is used to start the timer and make it appear active
     */
    public void start() {
        timer.setRunning(true);
        timerView.showActivated();
    }

    /**
     * This method is used to pause the timer and make it appear deactivated
     */
    public void pause() {
        timer.setRunning(false);
        timerView.showDeactivated();
    }

    public void stopThread() {
        this.stopThread = true;
    }
}
