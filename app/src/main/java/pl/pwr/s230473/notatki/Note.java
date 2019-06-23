package pl.pwr.s230473.notatki;

public class Note {
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
        return "01/01/2001";
    }

    public String getTime() {
        return "11:30";
    }
}
