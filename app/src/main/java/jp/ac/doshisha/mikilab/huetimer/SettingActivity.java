package jp.ac.doshisha.mikilab.huetimer;

import android.content.Intent;
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

        RadioGroup group1 = (RadioGroup)findViewById(R.id.radioGroup);
        group1.check(R.id.radioButton + color_index[0]);
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                color_index[0] = checkedId - R.id.radioButton;
                RadioButton radio = (RadioButton)findViewById(checkedId);
            }
        });

        RadioGroup group2 = (RadioGroup)findViewById(R.id.radioGroup2);
        group2.check(R.id.radioButton7 + color_index[1]);
        group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                color_index[1] = checkedId - R.id.radioButton7;
                RadioButton radio = (RadioButton)findViewById(checkedId);
            }
        });

        RadioGroup group3 = (RadioGroup)findViewById(R.id.radioGroup3);
        group3.check(R.id.radioButton13 + color_index[2]);
        group3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                color_index[2] = checkedId - R.id.radioButton13;
                RadioButton radio = (RadioButton)findViewById(checkedId);
            }
        });

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
    }

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
