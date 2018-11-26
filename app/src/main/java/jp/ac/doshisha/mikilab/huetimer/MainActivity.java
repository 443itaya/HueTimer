package jp.ac.doshisha.mikilab.huetimer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import okhttp3.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    int time_index[] = new int[3];
    int color_index[] = new int[3];
    int flash_index[] = new int[3];
    int hue_index;
    String url;
    private static final int REQUESTCODE_TEST1 = 1;
    private static final int REQUESTCODE_TEST2 = 2;

    int minute = 0, second = 0;
    int start_flag = 0;
    int count;
    int countup_flag = 0;
    int demo_flag = 0;
    long countNumber;
    long interval = 100;
    int buttonNum;
    int time1, time2;

    private TextView timerText;
    private Timer timer;
    private Handler handler = new Handler();
    private CountDown countDown;
    private Socket socket;
    private ServerSocket serverSocket;

    int flashTime = 15;

    //KC103

    //KC104
    //String url = "http://172.20.11.99/api/fKod5kAVYn2n0kXjKZaQ-XiP-CD5RvJQlsPdHD9a/groups/1/action";

    String json;
    private String res = "";
    Button sb;


    //起動後
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting 初期設定
        time_index[1] = 2; time_index[2] = 2;
        color_index[0] = 5; color_index[1] = 1; color_index[2] = 0;
        flash_index[2] = 1;
        hue_index = 0;
        url = "http://172.20.11.100/api/z2YrJsBIMyPZlHWprsFmIjlfI2WaR9kxTHA6XVaI/groups/1/action"; //KC103

        //smartphoneとの通信
        thread();

        //初期表示
        minute = 0;
        timerText = findViewById(R.id.timer);
        timerText.setText(String.format("%1$02d:%2$02d", minute, second));

    }

    //ボタン押した時
    public void onButtonClick(View v){

        thread();

        switch (v.getId()){
            case R.id.button_01:
                if(start_flag == 0 && demo_flag == 0) {
                    minute += 1;
                    timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                }
                break;

            case R.id.button_05:
                if(start_flag == 0 && demo_flag == 0) {
                    minute += 5;
                    timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                }
                break;

            case R.id.button_10:
                if(start_flag == 0 && demo_flag == 0) {
                    minute += 10;
                    timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                }
                break;

            case R.id.button_15:
                if(start_flag == 0 && demo_flag == 0) {
                    minute += 15;
                    timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                }
                break;

            case R.id.demo_button:
                if(start_flag == 0) {
                    sb = findViewById(R.id.demo_button);
                    if(demo_flag == 0) {
                        demo_flag = 1;
                        minute = 0;
                        second = 10;
                        timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                        sb.setText("デモ中");
                    }
                }
                break;

            case R.id.clear_button:
                if(start_flag == 0) {
                    countup_flag = 0;
                    demo_flag = 0 ;
                    minute = 0; second = 0;
                    timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                    timerText.setTextColor(Color.parseColor("#40C4FF"));
                    if(demo_flag == 0){
                        sb = findViewById(R.id.demo_button);
                        sb.setText("デモ用");
                        demo_flag = 0;
                    }

                    try {
                        stopLights();
                    }catch (IOException e){

                    }
                }
                break;

            case R.id.start_button:

                sb = findViewById(R.id.start_button);

                if(start_flag == 0){
                    if(countup_flag == 0) {
                        countNumber = (minute*60+second)*1000;
                        countDown = new CountDown(countNumber, interval);
                        countDown.start();
                    }else {
                        countDown.countup();
                    }

                    sb.setText("Stop");
                    start_flag = 1;
                }
                else{
                    countDown.cancel();
                    if(countup_flag == 1){
                        timer.cancel();
                    }
                    sb.setText("Start");
                    start_flag = 0;
                }
                break;
        }
    }

    //カウントダウン
    public class CountDown extends CountDownTimer {

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // カウントダウン完了後に呼ばれる
            count = 0;
            countup_flag = 1;
            try {
                lights(color_index[0]);
            }catch (IOException e){

            }

            countup();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // インターバル(countDownInterval)毎に呼ばれる
            minute = (int)millisUntilFinished/1000/60;  second = (int)millisUntilFinished/1000%60;
            timerText.setText(String.format("%1$02d:%2$02d", minute, second));
        }

        //カウントアップ
        public void countup() {
            timer = new Timer();
            timerText.setTextColor(Color.RED);

            if(time_index[1] == 0)  time1 = 600;
            else if (time_index[1] == 1) time1 = 1800;
            else time1 = 3000;
            if(time_index[2] == 0) time2 = 1200;
            else if (time_index[2] == 1) time2 = 3600;
            else time2 = 6000;

            timer.scheduleAtFixedRate (new TimerTask() {
                @Override
                public void run() {
                    // handlerdを使って処理をキューイングする
                    handler.post(new Runnable() {
                        public void run() {
                            count++;
                            minute = count * 100 / 1000 / 60;   second = count * 100 / 1000 % 60;
                            timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                            if(demo_flag == 1){     //デモ用
                                if(count < 100){
                                    if(flash_index[0] == 1){
                                        if(count % (flashTime * 2) == 0){
                                            try{
                                                noLights(color_index[0]);
                                            }catch (IOException e){

                                            }
                                        }else if(count % flashTime == 0){
                                            try {
                                                lights(color_index[0]);
                                            }catch (IOException e){

                                            }
                                        }

                                    }
                                }else if(count == 100){
                                    try{
                                        lights(color_index[1]);
                                    }catch (IOException e){

                                    }
                                }else if(count < 200){
                                    if(flash_index[1] == 1){
                                        if(count % (flashTime * 2) == 0){
                                            try{
                                                noLights(color_index[1]);
                                            }catch (IOException e){

                                            }
                                        }else if(count % flashTime == 0){
                                            try {
                                                lights(color_index[1]);
                                            }catch (IOException e){

                                            }
                                        }
                                    }
                                }else if(count == 200){
                                    try{
                                        lights(color_index[2]);
                                    }catch (IOException e){

                                    }
                                }else{
                                    if(flash_index[2] == 1){
                                        if(count % (flashTime * 2) == 0){
                                            try{
                                                noLights(color_index[2]);
                                            }catch (IOException e){

                                            }
                                        }else if(count % flashTime == 0){
                                            try {
                                                lights(color_index[2]);
                                            }catch (IOException e){

                                            }
                                        }
                                    }
                                }
                            }else{      //通常使用
                                if(time1 < time2){
                                    if(count < time1){
                                        if(flash_index[0] == 1){
                                            if(count % (flashTime * 2) == 0){
                                                try{
                                                    noLights(color_index[0]);
                                                }catch (IOException e){

                                                }
                                            }else if(count % flashTime == 0){
                                                try {
                                                    lights(color_index[0]);
                                                }catch (IOException e){

                                                }
                                            }

                                        }
                                    }else if(count == time1){
                                        try{
                                            lights(color_index[1]);
                                        }catch (IOException e){

                                        }
                                    }else if(count < time2){
                                        if(flash_index[1] == 1){
                                            if(count % (flashTime * 2) == 0){
                                                try{
                                                    noLights(color_index[1]);
                                                }catch (IOException e){

                                                }
                                            }else if(count % flashTime == 0){
                                                try {
                                                    lights(color_index[1]);
                                                }catch (IOException e){

                                                }
                                            }
                                        }
                                    }else if(count == time2){
                                        try{
                                            lights(color_index[2]);
                                        }catch (IOException e){

                                        }
                                    }else{
                                        if(flash_index[2] == 1){
                                            if(count % (flashTime * 2) == 0){
                                                try{
                                                    noLights(color_index[2]);
                                                }catch (IOException e){

                                                }
                                            }else if(count % flashTime == 0){
                                                try {
                                                    lights(color_index[2]);
                                                }catch (IOException e){

                                                }
                                            }
                                        }
                                    }
                                }else{
                                    if(count < time2){
                                        if(flash_index[0] == 1){
                                            if(count % (flashTime * 2) == 0){
                                                try{
                                                    noLights(color_index[0]);
                                                }catch (IOException e){

                                                }
                                            }else if(count % flashTime == 0){
                                                try {
                                                    lights(color_index[0]);
                                                }catch (IOException e){

                                                }
                                            }

                                        }
                                    }else if(count == time2){
                                        try{
                                            lights(color_index[1]);
                                        }catch (IOException e){

                                        }
                                    }else if(count < time1){
                                        if(flash_index[1] == 1){
                                            if(count % (flashTime * 2) == 0){
                                                try{
                                                    noLights(color_index[1]);
                                                }catch (IOException e){

                                                }
                                            }else if(count % flashTime == 0){
                                                try {
                                                    lights(color_index[1]);
                                                }catch (IOException e){

                                                }
                                            }
                                        }
                                    }else if(count == time1){
                                        try{
                                            lights(color_index[2]);
                                        }catch (IOException e){

                                        }
                                    }else{
                                        if(flash_index[2] == 1){
                                            if(count % (flashTime * 2) == 0){
                                                try{
                                                    noLights(color_index[2]);
                                                }catch (IOException e){

                                                }
                                            }else if(count % flashTime == 0){
                                                try {
                                                    lights(color_index[2]);
                                                }catch (IOException e){

                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    });
                }
            }, 0, interval);
        }

    }

    public void postTest() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failMessage();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.w("onResponse", res);
                    }
                });
            }
        });
    }
    private void failMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.w("failMessage", "fail");
            }
        });
    }

    void lights(int color) throws IOException{
        if(color == 0)  json = "{\"on\":true,\"hue\":0,\"bri\":254,\"sat\":254}";
        else if(color == 1) json = "{\"on\":true,\"hue\":60000,\"bri\":254,\"sat\":254}";
        else if(color == 2)  json = "{\"on\":true,\"hue\":46014,\"bri\":254,\"sat\":254}";
        else if(color == 3) json = "{\"on\":true,\"hue\":24000,\"bri\":254,\"sat\":254}";
        else if(color == 4) json = "{\"on\":true,\"hue\":7774,\"bri\":254,\"sat\":254}";
        else if(color == 5) json = "{\"on\":true,\"hue\":50000,\"bri\":254,\"sat\":254}";
        else json = "{\"on\":true,\"hue\":15324,\"bri\":254,\"sat\":121}";
        postTest();
    }

    void stopLights() throws IOException{
        json = "{\"on\":true,\"hue\":15324,\"bri\":254,\"sat\":121}";
        postTest();
    }

    void noLights(int color) throws  IOException{
        if(color == 0)  json = "{\"on\":false,\"hue\":0,\"bri\":254,\"sat\":254}";
        else if(color == 1) json = "{\"on\":false,\"hue\":60000,\"bri\":254,\"sat\":254}";
        else if(color == 2)  json = "{\"on\":false,\"hue\":46014,\"bri\":240,\"sat\":254}";
        else if(color == 3) json = "{\"on\":false,\"hue\":24000,\"bri\":240,\"sat\":254}";
        else if(color == 4) json = "{\"on\":false,\"hue\":7774,\"bri\":254,\"sat\":254}";
        else if(color == 5) json = "{\"on\":false,\"hue\":50000,\"bri\":254,\"sat\":254}";
        else json = "{\"on\":false,\"hue\":15324,\"bri\":254,\"sat\":121}";
        postTest();
    }

    void thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(8080);
                    for(;;) {
                        socket = serverSocket.accept();
                        InputStreamReader in = new InputStreamReader(socket.getInputStream());
                        BufferedReader br = new BufferedReader(in);
                        String message = br.readLine();

                        Log.w("aaaaaaaaaaaaaaaaa", message);
                        String[] m = message.split(",", 0);
                        minute = Integer.parseInt(m[0]);
                        second = Integer.parseInt(m[1]);
                        start_flag = Integer.parseInt(m[2]);
                        buttonNum = Integer.parseInt(m[3]);
                        countup_flag = Integer.parseInt(m[4]);
                        count = Integer.parseInt(m[5]);
                        demo_flag = Integer.parseInt(m[6]);

                        handler.post(new Runnable() {
                            public void run() {
                                timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                                second++;
                                if(buttonNum == 50) {
                                    Button sb = findViewById(R.id.start_button);
                                    if (start_flag == 1) {
                                        if (countup_flag == 0) {
                                            countNumber = (minute * 60 + second) * 1000;
                                            countDown = new CountDown(countNumber, interval);
                                            countDown.start();
                                        } else {
                                            countDown.countup();
                                        }
                                        sb.setText("Stop");
                                    } else {
                                        countDown.cancel();
                                        if (countup_flag == 1) {
                                            timer.cancel();
                                        }
                                        sb.setText("Start");
                                    }
                                }else if(buttonNum == 0){
                                    countup_flag = 0;
                                    demo_flag = 0 ;
                                    minute = 0; second = 0;
                                    timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                                    timerText.setTextColor(Color.parseColor("#40C4FF"));
                                    if(demo_flag == 0){
                                        sb = findViewById(R.id.demo_button);
                                        sb.setText("デモ用");
                                    }

                                    try {
                                        stopLights();
                                    }catch (IOException e){

                                    }
                                }else if(buttonNum == 30){
                                    if(start_flag == 0) {
                                        if(demo_flag == 1) {
                                            minute = 0;
                                            second = 10;
                                            timerText.setText(String.format("%1$02d:%2$02d", minute, second));
                                            sb = findViewById(R.id.demo_button);
                                            sb.setText("デモ中");
                                        }
                                    }
                                }

                            }
                        });
                    }

                }catch( IOException e ) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //SettingActivity
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
            startActivityForResult(intent,REQUESTCODE_TEST1);
            return true;
        }
        if (id == R.id.action_settings2) {
            Intent intent = new Intent(this, Setting2Activity.class);
            hue_index = intent.getIntExtra("hue_index",0);
            startActivityForResult(intent,REQUESTCODE_TEST2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //SettingActivityから値を持ってくる
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_TEST1:
                if (RESULT_OK == resultCode) {
                    time_index = data.getIntArrayExtra("time_index");
                    color_index = data.getIntArrayExtra("color_index");
                    flash_index = data.getIntArrayExtra("flash_index");
                }
                break;
            case REQUESTCODE_TEST2:
                if (RESULT_OK == resultCode) {
                    hue_index = data.getIntExtra("time_index" ,0);
                    url = data.getStringExtra("url");
                    Log.w("url",url);
                }
                break;
        }
    }
}