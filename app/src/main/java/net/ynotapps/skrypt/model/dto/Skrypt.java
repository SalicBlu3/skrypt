package net.ynotapps.skrypt.model.dto;

import com.orm.SugarRecord;

import java.util.Date;

public class Skrypt extends SugarRecord {

    private Date timestamp;
    private String text;
    private String title = "[No title]";

    public Skrypt() {
    }

    public Skrypt(Date timestamp, String text) {
        this.timestamp = timestamp;
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean hasTimestamp() {
        return timestamp != null;
    }

    @Override
    public String toString() {
        return "Skrypt{" +
                "timestamp=" + timestamp +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
