package jp.ac.doshisha.mikilab.huetimer;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Setting2Activity extends AppCompatActivity {

    private TextView portText;

    int hue_index;
    String url[] = new String[4];

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        url[0] = "http://172.20.11.100/api/z2YrJsBIMyPZlHWprsFmIjlfI2WaR9kxTHA6XVaI/groups/1/action"; //KC103
        url[1] = "http://172.20.11.99/api/fKod5kAVYn2n0kXjKZaQ-XiP-CD5RvJQlsPdHD9a/groups/1/action"; //KC104
        url[2] = "http://172.20.11.99/api/fKod5kAVYn2n0kXjKZaQ-XiP-CD5RvJQlsPdHD9a/groups/1/action"; //KC111

        Intent intent = getIntent();
        hue_index = intent.getIntExtra("hue_index",0);

        //wifi ipアドレス
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo w_info = wifiManager.getConnectionInfo();
        int ip_addr_i = w_info.getIpAddress();
        String ip_addr = ((ip_addr_i >> 0) & 0xFF) + "." + ((ip_addr_i >> 8) & 0xFF) + "." + ((ip_addr_i >> 16) & 0xFF) + "." + ((ip_addr_i >> 24) & 0xFF);
        Log.i("Sample", "IP Address (Wi-Fi) : "+ip_addr);
        portText = findViewById(R.id.portNumber);
        portText.setText(String.format("IP Address (Wi-Fi) : " + ip_addr));

        editText = findViewById(R.id.editText);
        editText.setText(url[hue_index]);

        Spinner spinner = findViewById(R.id.spinner4);
        spinner.setSelection(hue_index);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hue_index = position;
                editText.setText(url[hue_index]);
//                String item = String.valueOf(time_index[0]);
//                Log.w("aaaaaaaaaaa",item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //MainActivityへ値を返す
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            String sendurl = editText.getText().toString();
            Log.w("aaaaaaaaaaa",sendurl);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("hue_index", hue_index);
            intent.putExtra("url", sendurl);
            setResult(RESULT_OK, intent);
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

}
