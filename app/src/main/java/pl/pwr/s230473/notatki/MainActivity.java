package pl.pwr.s230473.notatki;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] TIMESTAMP_TAB = {"1","2","3","4","5","6","7","8","9","10"};
    String[] NOTES_TAB = {"A","B","C","D","E","F","G","H","I","J"};

    private NoteSQL noteSQL;
    private Cursor noteCursor;
    private List<Note> notesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        noteSQL = new NoteSQL(this);
        notesList.add(new Note(1, "Tak", "2019-02-01 03:04:05"));
        notesList.add(new Note(2, "Nie", "2019-02-01 03:04:05"));
        notesList.add(new Note(3, "Nie wiem", "2019-02-01 03:04:05"));
        //notesList.addAll(noteSQL.getAllNotes());

        CustomAdapter customAdapter = new CustomAdapter(this, notesList);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Log.d("DEBUG", "Działa");
            }
        });
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
