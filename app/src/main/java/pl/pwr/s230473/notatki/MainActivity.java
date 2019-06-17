package pl.pwr.s230473.notatki;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] TIMESTAMP_TAB = {"1","2","3","4","5","6","7","8","9","10"};
    String[] NOTES_TAB = {"A","B","C","D","E","F","G","H","I","J"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return NOTES_TAB.length;
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

            textView_timestamp.setText(TIMESTAMP_TAB[position]);
            textView_note.setText(NOTES_TAB[position]);
            return convertView;
        }
    }
}
