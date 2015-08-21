package net.ynotapps.skrypt.ui.controllers;

import android.widget.TextView;

public class PointController {

    private TextView textView;
    private int tally = 0;

    public PointController(TextView textView) {
        this.textView = textView;
        displayTally();
    }

    public void displayTally() {
        textView.setText(String.valueOf(tally));
    }

    public void tallyUp() {
        tally++;
        displayTally();
    }

    public void reset() {
        tally = 0;
        displayTally();
    }
}
