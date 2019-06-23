package pl.pwr.s230473.notatki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NoteSQL extends SQLiteOpenHelper {

    //Aktualna wersja bazy danych.
    private static final int DATABASE_VERSION = 1;

    //informacje o nazwach kolumn dla SQL
    private static final String DATABASE_NAME = "notes_db";
    private static final String TABLE_NAME = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_ALERT = "alert";
    private static final String COLUMN_TIMEALERT = "timealert";

    //Konstruktor
    public NoteSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Tworzenie tablicy dla SQLite
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE + " TEXT,"
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_ALERT + " BIT DEFAULT '0',"
                + COLUMN_TIMEALERT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")");
    }

    /*
     * Aktualizacja bazy danych
     * Wykonuje się tylko gdy zmieniła się wersja bazy danych
     * i należy dokonać aktualizacji struktury
     */
    // w
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        clearDataBase(db);
        onCreate(db);
    }

    // Usunięcie aktualnej bazy danych (o ile istnieje)
    public void clearDataBase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    //Funkcja do 'wyzerowania' numeru wersji.
    //Wykonuje się tylko w chwili gdy mamy noszą wersje bazy niż jest na SQLite
    /*@Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }*/

    // Dodanie wpisu do bazy danych
    public long insertNote(String note, boolean alert, String timeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note);
        if(alert)
        {
            values.put(COLUMN_ALERT, alert?1:0);
            values.put(COLUMN_TIMEALERT, timeStamp);
        }
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    //Pobranie jednej notatki z bazy danych
    public Note getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_NOTE, COLUMN_TIMESTAMP, COLUMN_ALERT, COLUMN_TIMEALERT},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_ALERT))>0?true:false,
                cursor.getString(cursor.getColumnIndex(COLUMN_TIMEALERT)));
        cursor.close();
        return note;
    }

    //Pobranie wszystkich notatek z bazy danych
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
                note.setAlert(cursor.getInt(cursor.getColumnIndex(COLUMN_ALERT))>0?true:false);
                note.setTimeAlert(cursor.getString(cursor.getColumnIndex(COLUMN_TIMEALERT)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }

    //Policzenie ile mamy notatek w bazie danych
    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //Aktualizacja jednej konkretnej notatki
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note.getNote());
        if(note.getAlert())
        {
            values.put(COLUMN_ALERT, note.getAlert()?1:0);
            values.put(COLUMN_TIMEALERT, note.getTimeAlert());
        }
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    //Usunięcie notatki z bazy danych
    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}