package notification.client;

import activity.notification.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/** 
 * This class is to manage the notification service and to load the configuration.
 */
public final class ServiceManager {

    private Context context;

    private SharedPreferences sharedPrefs;

    private String apiKey;

    private String xmppHost;

    private String xmppPort;

    private String callbackActivityPackageName;

    private String callbackActivityClassName;

    public ServiceManager(Context context) {
        this.context = context;

        if (context instanceof Activity) {
            Activity callbackActivity = (Activity) context;
            callbackActivityPackageName = callbackActivity.getPackageName();
            callbackActivityClassName = callbackActivity.getClass().getName();
        }

        apiKey = getMetaDataValue("NOTIFICATION_API_KEY");
        if (apiKey == null) {
                  throw new RuntimeException();
            }

        xmppHost = "10.0.2.2";
        xmppPort = "5222";

        sharedPrefs = context.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(Constants.API_KEY, apiKey);
        editor.putString(Constants.XMPP_HOST, xmppHost);
        editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
        editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME,
                callbackActivityPackageName);
        editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME,
                callbackActivityClassName);
        editor.commit();
        // Log.i(LOGTAG, "sharedPrefs=" + sharedPrefs.toString());
    }

    public void startService() {
        Thread serviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = NotificationService.getIntent();
                context.startService(intent);
            }
        });
        serviceThread.start();
    }

    public void stopService() {
        Intent intent = NotificationService.getIntent();
        context.stopService(intent);
    }

    
        private String getMetaDataValue(String name) {
            Object value = null;
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = packageManager.getApplicationInfo(context
                        .getPackageName(), 128);
                if (applicationInfo != null && applicationInfo.metaData != null) {
                    value = applicationInfo.metaData.get(name);
                }
            } catch (NameNotFoundException e) {
                throw new RuntimeException(
                        "Could not read the name in the manifest file.", e);
            }
            if (value == null) {
                throw new RuntimeException("The name '" + name
                        + "' is not defined in the manifest file's meta data.");
            }
            return value.toString();
        }

    public void setNotificationIcon(int iconId) {
        Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.NOTIFICATION_ICON, iconId);
        editor.commit();
    }

    public static void viewNotificationSettings(Context context) {
        Intent intent = new Intent().setClass(context,
                NotificationSettingsActivity.class);
        context.startActivity(intent);
    }

}
