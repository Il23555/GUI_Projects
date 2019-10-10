package MVC;

import javafx.application.Platform;
import javafx.scene.control.Label;
import java.util.*;

public class TimerLabel extends Label {

    public TimerLabel (Timer timer){
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private TimerTask timerTask = new TimerTask() {

        private volatile int time = -1;

        private Runnable refresher = new Runnable() {
            @Override
            public void run ()
            {
                int t = time;
                TimerLabel.this.setText(String.format("%02d:%02d:%02d", t/3600, t / 60, t % 60));
            }
        };

        @Override
        public void run ()
        {
            time++;
            Platform.runLater(refresher);
        }
    };
}
