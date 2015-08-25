package net.ynotapps.skrypt.ui.controllers;

import android.view.View;
import android.widget.TextView;

public class ResultController {
    private TextView resultView;
    private SkryptController skryptController;
    private TimeController timeController;

    public ResultController(TextView resultView, SkryptController skryptController, TimeController timeController) {
        this.resultView = resultView;
        this.skryptController = skryptController;
        this.timeController = timeController;
    }

    public void setDisplayResultView(boolean displayResultView) {
        resultView.setVisibility(displayResultView ? View.VISIBLE : View.GONE);
        resultView.setText(getResultString());
    }

    private String getResultString() {
        return String.format("You had a %ds flow\nand spun up %d words", timeController.getDuration(), skryptController.getWordCount());
    }
}
