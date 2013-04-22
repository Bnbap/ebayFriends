package notification.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/** 
 * A broadcast receiver to handle the changes in network connection states.
 *
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    private NotificationService notificationService;

    public ConnectivityReceiver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    	String action = intent.getAction();

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                notificationService.connect();
            }
        } else {
            notificationService.disconnect();
        }
    }

}
