package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.Timer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TimerView extends Text {
    private final Timer timerModel;

    public TimerView(Timer timerModel) {
        this.timerModel = timerModel;

        this.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        render();
    }

    private void render() {
        this.setText(getTimerAsString());
    }

    /**
     * This methods is used to rerender the Timer view
     */
    public void rerender() {
        this.render();
    }

    private String getTimerAsString() {
        return String.format("%02d:%02d", timerModel.getMinutesLeft(), timerModel.getSecondsLeft());
    }

    /**
     * This method is used to make the timer view appear active
     */
    public void showActivated() {
        this.setFill(Color.BLACK);
    }

    /**
     * This method is used to make the timer view appear deactivated
     */
    public void showDeactivated() {
        this.setFill(Color.GRAY);
    }
}
