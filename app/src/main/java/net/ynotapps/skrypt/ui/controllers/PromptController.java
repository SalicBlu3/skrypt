package net.ynotapps.skrypt.ui.controllers;

import android.widget.TextView;

import net.ynotapps.skrypt.utils.PromptUtils;

public class PromptController {
    private PromptUtils promptUtils = new PromptUtils();
    private TextView promptView;
    private String currentPrompt = "";

    public PromptController(TextView promptView) {
        this.promptView = promptView;
        displayCurrentPrompt();
    }

    public void displayCurrentPrompt() {
        promptView.setText(currentPrompt);
    }

    public String  showNext() {
        currentPrompt = promptUtils.getNextPrompt(promptView.getText().toString());
        displayCurrentPrompt();
        return currentPrompt;
    }

    public void reset() {
        currentPrompt = "";
        displayCurrentPrompt();
    }
}
