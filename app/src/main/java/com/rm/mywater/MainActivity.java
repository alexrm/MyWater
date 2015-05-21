package com.rm.mywater;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rm.mywater.ui.main.MainFragment;
import com.rm.mywater.util.base.BaseActivity;


public class MainActivity extends BaseActivity implements OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

//        for (int i = 0; i < 20; i++) {
//
//            DrinkHistoryDatabase.addDrink(this, Drink.getDummy());
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        ArrayList<Day> days = DrinkHistoryDatabase.retrieveDays(this);

//        Prefs.clear();
//        DrinkHistoryDatabase.clearTables(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MainFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentAction(Object data, int key) {

        Intent newActivityIntent = new Intent(this, ContentActivity.class);
        newActivityIntent.putExtra(ContentActivity.KEY_FRAGMENT_EXTRA, key);
        newActivityIntent.putExtra(ContentActivity.KEY_TITLE_EXTRA, key);

        startActivity(newActivityIntent);
    }
}
