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
import android.widget.*;

public class SettingActivity extends AppCompatActivity {

    int time_index[] = new int[3];
    int color_index[] = new int[3];
    int flash_index[] = new int[3];

    private TextView portText;

    int id[] = new int[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        time_index = intent.getIntArrayExtra("time_index");
        color_index = intent.getIntArrayExtra("color_index");
        flash_index = intent.getIntArrayExtra("flash_index");

        // 時間設定
        Spinner spinner1 = findViewById(R.id.spinner);
        spinner1.setSelection(time_index[0]);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time_index[0] = position;
//                String item = String.valueOf(time_index[0]);
//                Log.w("aaaaaaaaaaa",item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2 = findViewById(R.id.spinner2);
        spinner2.setSelection(time_index[1]);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time_index[1] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner3 = findViewById(R.id.spinner3);
        spinner3.setSelection(time_index[2]);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time_index[2] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //色指定
        RadioGroup group1 = (RadioGroup)findViewById(R.id.radioGroup);
        id[0] = IndexToId(0, color_index[0]);
        group1.check(id[0]);
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                color_index[0] = IdToIndex(0, checkedId);
                RadioButton radio = (RadioButton)findViewById(checkedId);
                Log.w("color1",String.valueOf(color_index[0]));
            }
        });

        RadioGroup group2 = (RadioGroup)findViewById(R.id.radioGroup2);
        id[1] = IndexToId(1, color_index[1]);
        group2.check(id[1]);
        group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                color_index[1] = IdToIndex(1, checkedId);
                RadioButton radio = (RadioButton)findViewById(checkedId);
                Log.w("color2",String.valueOf(color_index[1]));
            }
        });

        RadioGroup group3 = (RadioGroup)findViewById(R.id.radioGroup3);
        id[2] = IndexToId(2, color_index[2]);
        group3.check(id[2]);
        group3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                color_index[2] = IdToIndex(2, checkedId);
                RadioButton radio = (RadioButton)findViewById(checkedId);
                Log.w("color3",String.valueOf(color_index[2]));
            }
        });

        //フラッシュ指定
        final CheckBox chkbox1 = (CheckBox)findViewById(R.id.checkBox);
        if(flash_index[0] == 1)  chkbox1.setChecked(true);
        chkbox1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(chkbox1.isChecked() == true) {
                    // チェックされた状態の時の処理を記述
                    flash_index[0] = 1;
                }
                else {
                    // チェックされていない状態の時の処理を記述
                    flash_index[0] = 0;
                }
            }
        });

        final CheckBox chkbox2 = (CheckBox)findViewById(R.id.checkBox2);
        if(flash_index[1] == 1)  chkbox2.setChecked(true);
        chkbox2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(chkbox2.isChecked() == true) {
                    // チェックされた状態の時の処理を記述
                    flash_index[1] = 1;
                }
                else {
                    // チェックされていない状態の時の処理を記述
                    flash_index[1] = 0;
                }
            }
        });

        final CheckBox chkbox3 = (CheckBox)findViewById(R.id.checkBox3);
        if(flash_index[2] == 1)  chkbox3.setChecked(true);
        chkbox3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(chkbox3.isChecked() == true) {
                    // チェックされた状態の時の処理を記述
                    flash_index[2] = 1;
                }
                else {
                    // チェックされていない状態の時の処理を記述
                    flash_index[2] = 0;
                }
            }
        });

        //wifi ipアドレス
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo w_info = wifiManager.getConnectionInfo();
        int ip_addr_i = w_info.getIpAddress();
        String ip_addr = ((ip_addr_i >> 0) & 0xFF) + "." + ((ip_addr_i >> 8) & 0xFF) + "." + ((ip_addr_i >> 16) & 0xFF) + "." + ((ip_addr_i >> 24) & 0xFF);
        Log.i("Sample", "IP Address:"+ip_addr);
//        String ip_addr = "172.20.11.98";
        portText = findViewById(R.id.portNumber);
        portText.setText(String.format("IP Address:" + ip_addr));
    }

    int IndexToId(int num, int index){
        int id = -1;
        if(num == 0) {
            if (index == 0) id = R.id.radioButton;
            else if (index == 1) id = R.id.radioButton2;
            else if (index == 2) id = R.id.radioButton3;
            else if (index == 3) id = R.id.radioButton4;
            else if (index == 4) id = R.id.radioButton5;
            else if (index == 5) id = R.id.radioButton6;
        }
        else if(num == 1) {
            if (index == 2) id = R.id.radioButton7;
            else if (index == 1) id = R.id.radioButton8;
            else if (index == 2) id = R.id.radioButton9;
            else if (index == 3) id = R.id.radioButton10;
            else if (index == 4) id = R.id.radioButton11;
            else if (index == 5) id = R.id.radioButton12;
        }
        else if(num == 2) {
            if (index == 3) id = R.id.radioButton13;
            else if (index == 1) id = R.id.radioButton14;
            else if (index == 2) id = R.id.radioButton15;
            else if (index == 3) id = R.id.radioButton16;
            else if (index == 4) id = R.id.radioButton17;
            else if (index == 5) id = R.id.radioButton18;
        }
        return id;
    }

    int IdToIndex(int num, int id){
        int index = -1;
        if(num == 0) {
            if (id == R.id.radioButton) index = 0;
            else if (id == R.id.radioButton2) index = 1;
            else if (id == R.id.radioButton3) index = 2;
            else if (id == R.id.radioButton4) index = 3;
            else if (id == R.id.radioButton5) index = 4;
            else if (id == R.id.radioButton6) index = 5;
        }
        else if(num == 1) {
            if (id == R.id.radioButton7) index = 0;
            else if (id == R.id.radioButton8) index = 1;
            else if (id == R.id.radioButton9) index = 2;
            else if (id == R.id.radioButton10) index = 3;
            else if (id == R.id.radioButton11) index = 4;
            else if (id == R.id.radioButton12) index = 5;
        }
        else if(num == 2) {
            if (id == R.id.radioButton13) index = 0;
            else if (id == R.id.radioButton14) index = 1;
            else if (id == R.id.radioButton15) index = 2;
            else if (id == R.id.radioButton16) index = 3;
            else if (id == R.id.radioButton17) index = 4;
            else if (id == R.id.radioButton18) index = 5;
        }
        return index;
    }


    //MainActivityへ値を返す
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("time_index", time_index);
            intent.putExtra("color_index", color_index);
            intent.putExtra("flash_index", flash_index);
            setResult(RESULT_OK, intent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}