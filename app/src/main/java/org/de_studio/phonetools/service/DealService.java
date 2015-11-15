package org.de_studio.phonetools.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.de_studio.phonetools.DealFragment;
import org.de_studio.phonetools.PhoneToolsContract;
import org.de_studio.phonetools.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by hai on 11/14/2015.
 */
public class DealService extends IntentService {
    private static final String LOG_TAG = DealService.class.getSimpleName();

    public DealService() {
        super("deal");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String[] resultStrings = new String[10];
        try {
            Log.e(LOG_TAG, "get information from website");
            String url = "https://vienthong.com.vn/tin-tuc/tin-khuyen-mai/";
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("li.clearfix a[href] [title]");
            for (int i = resultStrings.length - 1; i >= 0; i--) {
                Element element = elements.get(i);
                String title = element.attr("title");
                resultStrings[i] = title;

                Cursor cursor = getApplicationContext().getContentResolver().query(PhoneToolsContract.DealEntry.CONTENT_URI,
                        DealFragment.DEAL_COLUMNS,
                        PhoneToolsContract.DealEntry.COLUMN_TITLE + " = ? ",
                        new String[]{title},
                        null);


                if (cursor.getCount() == 0) {
                    if (i<=2){
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                        builder.setContentTitle(getString(R.string.notification_new_deal_title)).setContentText(title).setSmallIcon(R.drawable.ic_action_sms);
                        Notification notification = builder.build();
                        NotificationManager notificationManager = (NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                        notificationManager.notify(i,notification);
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PhoneToolsContract.DealEntry.COLUMN_TITLE, title);
                    contentValues.put(PhoneToolsContract.DealEntry.COLUMN_IS_NEW, 1);
                    getApplicationContext().getContentResolver().insert(PhoneToolsContract.DealEntry.CONTENT_URI, contentValues);

                }
            }
            Cursor cursor1 = getApplicationContext().getContentResolver().query(PhoneToolsContract.DealEntry.CONTENT_URI,
                    DealFragment.DEAL_COLUMNS,
                    null,
                    null,
                    null);
            if (cursor1.getCount() >= 11) {
                int rowsDelete = getApplicationContext().getContentResolver().delete(PhoneToolsContract.DealEntry.CONTENT_URI,
                        PhoneToolsContract.DealEntry._ID + " = ? ",
                        new String[]{"SELECT MIN(_ID) FROM deal"});
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "err get information from website");

        }
    }
    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"onReceive of AlarmReceiver ne",Toast.LENGTH_LONG).show();
            Intent alarmIntent = new Intent(context, DealService.AlarmReceiver2.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0,alarmIntent,PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),2*60*60*1000,pi);
        }
    }
    public static class AlarmReceiver2 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"onReceive of AlarmReceiver2222 ne",Toast.LENGTH_LONG).show();

            Intent NotiIntent = new Intent(context,DealService.class);
            context.startService(NotiIntent);
        }
    }

}
