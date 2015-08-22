package net.ynotapps.skrypt.ui.controllers;

import android.widget.TextView;

public class SkryptController {
    private TextView skryptView;
    private String skrypt = "";

    public SkryptController(TextView skryptView) {
        this.skryptView = skryptView;
    }

    public void reset() {
        skrypt = "";
        displaySkrypt();
    }

    public void displaySkrypt() {
        skryptView.setText(skrypt);
    }

    public void updateSkrypt(String skrypt) {
        this.skrypt = skrypt;
        displaySkrypt();
    }
}
