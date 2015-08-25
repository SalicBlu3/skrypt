package net.ynotapps.skrypt.ui.controllers;

import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import net.ynotapps.skrypt.model.dto.Skrypt;

import java.util.Calendar;

public class SkryptController {
    private TextView skryptView;
    private ScrollView scrollView;
    private String skrypt = "";

    public SkryptController(TextView skryptView, ScrollView scrollView) {
        this.skryptView = skryptView;
        this.scrollView = scrollView;
    }

    public void reset() {
        skrypt = "";
        displaySkrypt();
    }

    public void displaySkrypt() {
        skryptView.setText(skrypt);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    public void updateSkrypt(String skrypt) {
        this.skrypt = skrypt;
        displaySkrypt();
    }

    public boolean contains(String keyword) {
        return skrypt.contains(keyword.toLowerCase());
    }

    public int getWordCount() {
        return skrypt.split(" ").length;
    }

    public void saveSkrypt() {
        new Skrypt(Calendar.getInstance().getTime(), skrypt).save();
    }
}
