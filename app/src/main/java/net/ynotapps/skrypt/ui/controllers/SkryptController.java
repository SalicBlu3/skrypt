package net.ynotapps.skrypt.ui.controllers;

import android.widget.TextView;

import net.ynotapps.skrypt.model.dto.Skrypt;

import java.util.Calendar;

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

    public boolean contains(String keyword) {
        return skrypt.contains(keyword.toLowerCase());
    }

    public void saveSkrypt() {
        new Skrypt(Calendar.getInstance().getTime(), skrypt).save();
    }
}
