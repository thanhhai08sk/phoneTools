package org.de_studio.phonetools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class ItemDetailActivity extends AppCompatActivity {
    public  Integer mPosition;
    private static final String LOG_TAG = ItemDetailActivity.class.getSimpleName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
               mPosition =     getIntent().getIntExtra(ItemDetailFragment.ARG_ITEM_ID, 1);
            Toast.makeText(this," mPosition =" + mPosition,Toast.LENGTH_SHORT).show();
            ItemDetailFragment itemDetailFragment = (ItemDetailFragment) getSupportFragmentManager().findFragmentById(R.id.item_detail_container);
            itemDetailFragment.setPosition(mPosition);
        }
//        Bundle bundle = new Bundle();
//        bundle.putInt("position", mPosition);
//        ItemDetailFragment itemDetailFragment = (ItemDetailFragment) getSupportFragmentManager().findFragmentById(R.id.item_detail_container);
//
//        itemDetailFragment.setArguments(bundle);
//        if (itemDetailFragment == null){
//            Log.e(LOG_TAG, " null itemDetailFragment");
//        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
