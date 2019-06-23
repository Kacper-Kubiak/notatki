package pl.pwr.s230473.notatki;

import android.util.Log;

/**
 * Klasa Notatki
 *
 * @param id    ID notatki
 * @param note  Tekst notatki
 * @param timestamp Czas utworzenia notatki
 * @param alert Czy włączone przypomnienie
 * @param timeAlert Czas o którym ma się pojawić alert
 */
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

    // Zwracanie tylko dnia notifikacji
    public String getDate() {
        Log.d(DEBUG,timeAlert.substring(0,10));
        return timeAlert.substring(0,10);
        //return "01/01/2001";
    }

    // Zwracanie tylko godziny notifikacji
    public String getTime() {
        Log.d(DEBUG,timeAlert.substring(11,16));
        return timeAlert.substring(11,16);
        //return "11:30";HH:mm
    }
}
