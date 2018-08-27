package jp.ac.doshisha.mikilab.huetimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    int time_index[] = new int[3];
    int color_index[] = new int[3];
    int flash_index[] = new int[3];
    private static final int REQUESTCODE_TEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        time_index[1] = 2; time_index[2] = 2;
        color_index[0] = 3; color_index[1] = 0; color_index[2] = 0;
        flash_index[2] = 1;
        
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
            Intent intent = new Intent(this, SettingActivity.class);
            intent.putExtra("time_index", time_index);
            intent.putExtra("color_index", color_index);
            intent.putExtra("flash_index", flash_index);
            startActivityForResult(intent,REQUESTCODE_TEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_TEST:
                if (RESULT_OK == resultCode) {
                    time_index = data.getIntArrayExtra("time_index");
                    color_index = data.getIntArrayExtra("color_index");
                    flash_index = data.getIntArrayExtra("flash_index");
                }
                break;
        }
    }
}
