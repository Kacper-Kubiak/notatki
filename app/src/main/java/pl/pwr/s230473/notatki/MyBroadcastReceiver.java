package pl.pwr.s230473.notatki;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Klasa odpowiedzialna za wyświetlanie elementu (Toast/Notyfikacji)
 * o odpowiedniej godzinie.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm!!!", Toast.LENGTH_LONG).show();
    }
}
