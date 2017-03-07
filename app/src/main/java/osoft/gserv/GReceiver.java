package osoft.gserv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GReceiver extends BroadcastReceiver {
    public GReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent serviceIntent = new Intent(context,GService.class);
        context.startService(serviceIntent);
        Log.i("GServ", "Intent sent to the service");
    }
}
