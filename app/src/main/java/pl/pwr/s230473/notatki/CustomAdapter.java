package pl.pwr.s230473.notatki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//Własny adapter do wyświetlania listy
public class CustomAdapter extends BaseAdapter
{
    private Context context;
    private List<Note> notesList;
    public CustomAdapter(Context context, List<Note> notesList)
    {
        this.context = context;
        this.notesList = notesList;
    }

    /**
     * Zwraca ilość elementów w liście z notatkami
     *
     * @return  ilość elementów w liście
     */
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

    /**

     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = inflater.inflate(R.layout.customlayout, null);
        TextView textView_timestamp = convertView.findViewById(R.id.textView_timestamp);
        TextView textView_note = convertView.findViewById(R.id.textView_note);

        Note note = notesList.get(position);
        textView_timestamp.setText(formatDate(note.getTimestamp()));
        textView_note.setText(note.getNote());
        return convertView;
    }

    /**
     * Formuje datę do wyświetlania na notatce
     *
     * @param String    dostaje date wejściową
     * @return          date w odpowiednim formacie
     */
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
