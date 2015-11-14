package org.de_studio.phonetools.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import org.de_studio.phonetools.DealFragment;
import org.de_studio.phonetools.PhoneToolsContract;
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
//                    Log.e(LOG_TAG, "number of the same is: " + cursor.getCount());
                if (cursor.getCount() == 0) {
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
}