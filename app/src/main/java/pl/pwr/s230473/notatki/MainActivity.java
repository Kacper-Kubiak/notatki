package pl.pwr.s230473.notatki;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG = "DEBUG"; //Stała dla Logowania
    ListView listView; //listView na notatki

    private NoteSQL noteSQL; // zmienna z Bazą danych
    private List<Note> notesList = new ArrayList<>(); // Lista z notatkami
    public CustomAdapter customAdapter; // Właśny adapter dla listView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView); // Przypisanie wartości z 'ekranu' do zmiennej

        noteSQL = new NoteSQL(this); // Utworzenie połączenia z bazą danych
        notesList.addAll(noteSQL.getAllNotes()); // Pobranie wszystkich notatek i przypisanie ich do listy z notatkami

        // Włączenie notyfikacji dla wszystkich notatek. Sprawdzenie czy powiadomienie aktywne jest w funkcji setAlertClock
        for(Note tmpNote : notesList){
            setAlertClock(tmpNote);
        }

        customAdapter = new CustomAdapter(this, notesList); // Własny adapter z listą 'notesList'
        listView.setAdapter(customAdapter); // i ustawienie własnego adaptera dla listView

        //Nasłuchiwanie na 'długie' naciśnięcie dla listView (notatki)
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                showActionsDialog(pos);// Wyświetlamy okienko z opcjami 'Edycji' oraz 'Usuwania'
                return true;
            }
        });
    }

    // Własne menu z "+" w celu dodania nowej notatki
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Jeżeli nacisneliśmy w nagłówku "dodaj" to otwieramy ekranik z dodawaniem
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                showNoteDialog(false, null, -1);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    // Wyświetlamy ekran z wyborem opcji "Edycji" lub "Usunięcia" notatki
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edytuj", "Usuń"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wybierz opcje");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position); // edycja notatki
                } else {
                    deleteNote(position); // usunięcie notatki
                }
            }
        });
        builder.show();
    }

    // Dialog/Ekran do dodawania lub edycji notatki
    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        //Dodajemy nasz ekran
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        // Zmienne z elementami graficznymi
        final EditText inputNote = view.findViewById(R.id.note);
        final Button btnDatePicker = view.findViewById(R.id.btn_date);
        final Button btnTimePicker = view.findViewById(R.id.btn_time);
        final EditText txtDate = view.findViewById(R.id.in_date);
        final EditText txtTime = view.findViewById(R.id.in_time);
        final CheckBox checkAlert = view.findViewById(R.id.powiadomienie);

        //Tytuł notatki zależny od tego czy edytujemy czy tworzymy notatke
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? "Nowa notatka" : "Edytuj notatke");

        //Wybór daty na naszym ekranie - pokazujemy po nacisnieciu guzika
        //nowe okno z wyborem daty
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                txtDate.setText(String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        //Wybór godziny na naszym ekranie - podobny ekran do tego od daty
        //po nacisnieciu godziny
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                txtTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        //Jeżeli to aktualizacja to uzupełnij pola tekstowe
        // na takie jakie są w celu ich edycji
        if (shouldUpdate) {
            if(note != null) inputNote.setText(note.getNote());
            checkAlert.setChecked(note.getAlert());
            txtDate.setText(note.getDate());
            txtTime.setText(note.getTime());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "Aktualizuj" : "Zapisz", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        //Gdy 'potwierdziliśmy' okno to wykonujemy to poniżej:
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    //Notatka nie może być pusta
                    Toast.makeText(MainActivity.this, "Wpisz notatke!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && note != null) {
                    //Aktualizujemy notatke o nowe rzeczy
                    updateNote(inputNote.getText().toString(), position, checkAlert.isChecked(), txtDate.getText().toString() + " " + txtTime.getText().toString());
                } else {
                    //Dodajemy nową notatkę
                    Log.d(DEBUG, "Data: " + txtDate.getText().toString() + " Czas: " + txtTime.getText().toString() );
                    createNote(inputNote.getText().toString(), checkAlert.isChecked(), txtDate.getText().toString() + " " + txtTime.getText().toString());
                }
            }
        });
    }

    //Aktualizacja notatki
    private void updateNote(String note, int position, boolean alert, String alertTime) {
        Note nn = notesList.get(position);
        nn.setNote(note);
        nn.setAlert(alert);
        nn.setTimeAlert(alertTime);
        noteSQL.updateNote(nn);
    }

    //Usunięcie notatki
    private void deleteNote(int position) {
        noteSQL.deleteNote(notesList.get(position));
        notesList.remove(position);
        customAdapter.notifyDataSetChanged();
    }

    //Swtorzenie nowej notatki
    private void createNote(String note, boolean alert, String alertTime) {
        //long id = noteSQL.insertNote(note);
        long id = noteSQL.insertNote(note, alert, alertTime);
        Note nn = noteSQL.getNote(id);

        if (nn != null) {
            notesList.add(0, nn);
        }
        customAdapter.notifyDataSetChanged();
        setAlertClock(nn); // po dodaniu włączamy odrazu zegar z notyfikacjami
    }

    //Funkcja do włączenia powiadomienia o danej godzinie
    public void setAlertClock(Note note) {
        if(!note.getAlert()) return;
        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(note.getTimeAlert());Intent intent = new Intent(this, MyBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), 234324243, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(this, "Alarm set on " + date1.toString(),Toast.LENGTH_LONG).show();
            Log.d(DEBUG,"DATA alarmu: " + date1.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
