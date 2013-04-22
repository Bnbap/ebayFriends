package notification.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/** 
 * Broadcast receiver that handles push notification messages from the server.
 * This should be registered as receiver in AndroidManifest.xml. 
 * 
 */
public final class NotificationReceiver extends BroadcastReceiver {

    //    private NotificationService notificationService;

    public NotificationReceiver() {
    }

    //    public NotificationReceiver(NotificationService notificationService) {
    //        this.notificationService = notificationService;
    //    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
            String notificationId = intent
                    .getStringExtra(Constants.NOTIFICATION_ID);
            String notificationApiKey = intent
                    .getStringExtra(Constants.NOTIFICATION_API_KEY);
            String notificationTitle = intent
                    .getStringExtra(Constants.NOTIFICATION_TITLE);
            String notificationMessage = intent
                    .getStringExtra(Constants.NOTIFICATION_MESSAGE);
            String notificationUri = intent
                    .getStringExtra(Constants.NOTIFICATION_URI);

            Notifier notifier = new Notifier(context);
            notifier.notify(notificationId, notificationApiKey,
                    notificationTitle, notificationMessage, notificationUri);
        }

        //        } else if (Constants.ACTION_NOTIFICATION_CLICKED.equals(action)) {
        //            String notificationId = intent
        //                    .getStringExtra(Constants.NOTIFICATION_ID);
        //            String notificationApiKey = intent
        //                    .getStringExtra(Constants.NOTIFICATION_API_KEY);
        //            String notificationTitle = intent
        //                    .getStringExtra(Constants.NOTIFICATION_TITLE);
        //            String notificationMessage = intent
        //                    .getStringExtra(Constants.NOTIFICATION_MESSAGE);
        //            String notificationUri = intent
        //                    .getStringExtra(Constants.NOTIFICATION_URI);
        //
        //            Log.e(LOGTAG, "notificationId=" + notificationId);
        //            Log.e(LOGTAG, "notificationApiKey=" + notificationApiKey);
        //            Log.e(LOGTAG, "notificationTitle=" + notificationTitle);
        //            Log.e(LOGTAG, "notificationMessage=" + notificationMessage);
        //            Log.e(LOGTAG, "notificationUri=" + notificationUri);
        //
        //            Intent detailsIntent = new Intent();
        //            detailsIntent.setClass(context, NotificationDetailsActivity.class);
        //            detailsIntent.putExtras(intent.getExtras());
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_ID, notificationId);
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_API_KEY, notificationApiKey);
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_TITLE, notificationTitle);
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_MESSAGE, notificationMessage);
        //            //            detailsIntent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
        //            detailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //            detailsIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        //
        //            try {
        //                context.startActivity(detailsIntent);
        //            } catch (ActivityNotFoundException e) {
        //                Toast toast = Toast.makeText(context,
        //                        "No app found to handle this request",
        //                        Toast.LENGTH_LONG);
        //                toast.show();
        //            }
        //
        //        } else if (Constants.ACTION_NOTIFICATION_CLEARED.equals(action)) {
        //            //
        //        }

    }

}
