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
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.Set;

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
        String[] dateStrings = new String[10];
        String[] detailStrings = new String[10];
        try {
            Log.e(LOG_TAG, "get information from website");
            String url = "https://id.vtc.vn/tin-tuc/chuyen-muc-49/tin-khuyen-mai.html";
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("div.tt_dong1");
            for (int i = resultStrings.length - 1; i >= 0; i--) {
                Element mainElement = elements.get(i).select("a").get(1);
                String title = mainElement.text();
                resultStrings[i] = title;
                Element dateElement = elements.get(i).select("span[style]").first();
                String date = dateElement.text();
                dateStrings[i] = date;

                Element detailLink = elements.get(i).select("a[href]").first();
                String detail = detailLink.attr("href");
                String detailText ="";
                String editedDetailText  ="";
                try {
                    String detailUrl = detail;
                    Document detailDocument = Jsoup.connect(detailUrl).get();
                    Elements detailElements = detailDocument.select("div.tt_dong3");
                    detailText = detailElements.first().text();
                    String detailTextLowercase = detailText.toLowerCase();
                    int startIndex = detailTextLowercase.indexOf("như sau:") +9;
                    if (startIndex <=10) startIndex = detailTextLowercase.indexOf("như sau :") +10;
                    if (startIndex <=10) startIndex = detailTextLowercase.indexOf("như sau") +8;
                    if (startIndex <=10) startIndex = detailTextLowercase.indexOf("như sau :") +10;
                    if (startIndex <=10) startIndex = detailTextLowercase.indexOf("nhu sau:") +9;
                    if (startIndex <=10) startIndex = detailTextLowercase.indexOf("nhu sâu:") +9;
                    if (startIndex <=10) startIndex = detailTextLowercase.indexOf("nhưsau:") +8;
                    if (startIndex <=10) startIndex = detailTextLowercase.indexOf("nhu sau") +8;
                    int endIndex = detailTextLowercase.indexOf("ngày vàng khuyến mãi");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngày vàng khuyến");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngay vàng khuyến");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngày vang khuyến");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngày vàng khuyen");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngáy vàng khuyến");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngày váng khuyến");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngay vang khuyen");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngày vàng khuyên");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngày vàng khuến");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngày vàn khuyến");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("ngàu vàng khuyến");
                    if (endIndex ==-1) endIndex = detailTextLowercase.indexOf("này vàng khuyến");
                    editedDetailText= detailText.substring(startIndex,endIndex);
                }catch (IOException e){
                    Log.e(LOG_TAG, "err get detail from detailLink");
                }
                Cursor cursor = getApplicationContext().getContentResolver().query(PhoneToolsContract.DealEntry.CONTENT_URI,
                        DealFragment.DEAL_COLUMNS,
                        PhoneToolsContract.DealEntry.COLUMN_TITLE + " = ? ",
                        new String[]{title},
                        null);
                SharedPreferences sharedPreferences = getSharedPreferences("notiShare", 0);
                Set<String> defaultNoti = new HashSet<String>();
                defaultNoti.add("mobifone");
                defaultNoti.add("vinaphone");
                defaultNoti.add("viettel");
                Set<String> stringSet = sharedPreferences.getStringSet("noti",defaultNoti);
                if (cursor.getCount() == 0) {
                    if (i<=2){
                        String tit = title.toLowerCase();
                        if ((tit.contains("viettel")& stringSet.contains("viettel"))
                                | tit.contains("vinaphone")& stringSet.contains("vinaphone")
                                | tit.contains("mobifone")& stringSet.contains("mobifone")) {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                            builder.setContentTitle(getString(R.string.notification_new_deal_title)).setContentText(title).setSmallIcon(R.drawable.ic_action_sms);
                            Notification notification = builder.build();
                            NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                            notificationManager.notify(i, notification);
                        }
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PhoneToolsContract.DealEntry.COLUMN_DATE,date);
                    contentValues.put(PhoneToolsContract.DealEntry.COLUMN_TITLE, title);
                    contentValues.put(PhoneToolsContract.DealEntry.COLUMN_DETAIL, editedDetailText);
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
