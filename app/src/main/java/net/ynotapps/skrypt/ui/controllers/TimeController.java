package net.ynotapps.skrypt.ui.controllers;

import java.util.Calendar;
import java.util.Date;

public class TimeController {

    public static final int MILLISECONDS = 1000;
    private Date timeStart;
    private Date timeStop;

    public void start() {
        timeStart = getTime();
    }

    private Date getTime() {
        return Calendar.getInstance().getTime();
    }

    public void stop() {
        timeStop = getTime();
    }

    /**
     * Gets the time difference between start and stop
     * @return
     */
    public int getDuration() {
        if (timeStart == null || timeStop == null) {
            return 0;
        }

        return (int) ((timeStop.getTime() - timeStart.getTime()) / MILLISECONDS);
    }

    public void reset() {
        timeStart = null;
        timeStop = null;
    }

}
