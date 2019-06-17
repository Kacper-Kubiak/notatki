package pl.pwr.s230473.notatki;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

    ListView listView;
    //String[] TIMESTAMP_TAB = {"1","2","3","4","5","6","7","8","9","10"};
    //String[] NOTES_TAB = {"A","B","C","D","E","F","G","H","I","J"};

    private NoteSQL noteSQL;
    private Cursor noteCursor;
    private List<Note> notesList = new ArrayList<>();
    public CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        noteSQL = new NoteSQL(this);
        /*notesList.add(new Note(1, "Tak", "2019-02-01 03:04:05"));
        notesList.add(new Note(2, "Nie", "2019-02-01 03:04:05"));
        notesList.add(new Note(3, "Nie wiem", "2019-02-01 03:04:05"));*/
        notesList.addAll(noteSQL.getAllNotes());

        customAdapter = new CustomAdapter(this, notesList);
        listView.setAdapter(customAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                //Log.v("long clicked","pos: " + pos);
                showActionsDialog(pos);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                showNoteDialog(false, null, -1);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edytuj", "Usuń"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wybierz opcje");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }

    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        final Button btnDatePicker= view.findViewById(R.id.btn_date);
        final Button btnTimePicker= view.findViewById(R.id.btn_time);
        final EditText txtDate= view.findViewById(R.id.in_date);
        final EditText txtTime= view.findViewById(R.id.in_time);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? "Nowa notatka" : "Edytuj notatke");

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
                                txtDate.setText(String.format("%02d", (day+1)) + "/" + String.format("%02d", month) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
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

        if (shouldUpdate && note != null) {
            inputNote.setText(note.getNote());
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

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Wpisz notatke!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    updateNote(inputNote.getText().toString(), position);
                } else {
                    // create new note
                    createNote(inputNote.getText().toString());
                }
            }
        });
    }

    private void updateNote(String note, int position) {
        Note nn = notesList.get(position);
        nn.setNote(note);
        noteSQL.updateNote(nn);
    }

    private void deleteNote(int position) {
        noteSQL.deleteNote(notesList.get(position));
        notesList.remove(position);
        customAdapter.notifyDataSetChanged();

    }

    private void createNote(String note) {
        long id = noteSQL.insertNote(note);
        Note nn = noteSQL.getNote(id);

        if (nn != null) {
            notesList.add(0, nn);
        }
        customAdapter.notifyDataSetChanged();
    }

    class CustomAdapter extends BaseAdapter
    {
        private Context context;
        private List<Note> notesList;
        public CustomAdapter(Context context, List<Note> notesList)
        {
            this.context = context;
            this.notesList = notesList;
        }

        @Override
        public int getCount() {
            return notesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout, null);
            TextView textView_timestamp = convertView.findViewById(R.id.textView_timestamp);
            TextView textView_note = convertView.findViewById(R.id.textView_note);

            Note note = notesList.get(position);
            textView_timestamp.setText(formatDate(note.getTimestamp()));
            textView_note.setText(note.getNote());
            return convertView;
        }

        private String formatDate(String dateStr) {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = fmt.parse(dateStr);
                SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
                return fmtOut.format(date);
            } catch (ParseException e) {

            }

            return "";
        }
    }
}
