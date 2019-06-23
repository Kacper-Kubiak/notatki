package pl.pwr.s230473.notatki;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Note {
    private static final String DEBUG = "DEBUG";
    private long id;
    private String note;
    private String timestamp;
    private boolean alert;
    private String timeAlert;

    public Note() { }
    public Note(long id, String note, String timestamp, boolean alert, String timeAlert) {
        this.id = id;
        this.note = note;
        this.timestamp = timestamp;
        this.alert = alert;
        this.timeAlert = timeAlert;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimestamp() { return timestamp; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean getAlert() { return alert; }

    public void setAlert(boolean alert) { this.alert = alert; }

    public String getTimeAlert() { return timeAlert; }

    public void setTimeAlert(String timeAlert) { this.timeAlert = timeAlert; }


    public String getDate() {
        Log.d(DEBUG,timeAlert.substring(0,10));
        return timeAlert.substring(0,10);
        //return "01/01/2001";
    }

    public String getTime() {
        Log.d(DEBUG,timeAlert.substring(11,16));
        return timeAlert.substring(11,16);
        //return "11:30";HH:mm
    }
}
