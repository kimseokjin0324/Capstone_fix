package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static String TAG = "phptest";

    private TextView mTextViewResult;
    private ArrayList<PersonalData> mArrayList;
    private UsersAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;

    private String TAG_JSON="webnautes";
    private static String TAG_Lectnum = "Lectnum";
    private static String TAG_Lectname = "Lectname";
    private static String TAG_StartTime = "StartTime";
    private static String TAG_EndTime = "EndTime";
    private static String TAG_Location = "Location";

    SwipeRefreshLayout mSwipeRefreshLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());




        mArrayList = new ArrayList<>();

        mAdapter = new UsersAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout= findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        Button button_all = (Button) findViewById(R.id.button_main_all);
        button_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mArrayList.clear();
                mAdapter.notifyDataSetChanged();

                getData9029 task9029= new getData9029();
                task9029.execute( "http://gongdoli.aws-exercise.net/함박관/B1층/testing.php", "");
                System.out.println("안녕하세요");



            }
        });


    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mArrayList.clear();
                mAdapter.notifyDataSetChanged();


                getData9029 task9029= new getData9029();
                task9029.execute( "http://gongdoli.aws-exercise.net/함박관/B1층/testing.php", "");
                System.out.println("안녕하세요");

                mSwipeRefreshLayout.setRefreshing(false);
            }
        },100);
    }





    //Y9029 정보 파싱
    private class getData9029 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult9029();

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }
    private void showResult9029(){


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            Calendar cal = Calendar.getInstance();
            int nWeek = cal.get(Calendar.DAY_OF_WEEK);
            System.out.println("nWeek = "+ nWeek);

            //현재 시간 구하는 함수
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
            String formatDate = sdfNow.format(date);





            String locat[]= new String[jsonArray.length()];
            long Sttime[] = new long[jsonArray.length()];
            long Edtime[] = new long[jsonArray.length()];
            String lect[] = new String[jsonArray.length()];



            for(int i=0; i<jsonArray.length(); i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String Lectnum = item.optString(TAG_Lectnum);
                String Lectname = item.optString(TAG_Lectname);
                String StartTime = item.optString(TAG_StartTime);
                String EndTime= item.optString(TAG_EndTime);
                String Location = item.optString(TAG_Location);
                PersonalData personalData = new PersonalData();

                lect[i]= Lectname;
                locat[i]=Location;

/*
                if(locat[i].equals("Y9001")){
                    TextView resultY9001= (TextView) findViewById(R.id.resultY9001);
                    if(formatDate.compareTo(StartTime)>0 && formatDate.compareTo(EndTime)<0){
                        resultY9001.setText(Location+"는수업중입니다 \n"+" 강의명: \t"+ Lectname+"\n 시작 시간 : \t"+StartTime+"\t종료 시간 :\t"+ EndTime);
                        resultY9001.setBackgroundColor(Color.RED);
                    }

                    else if(formatDate.compareTo(StartTime)<0){
                        //long hour= (Sttime[i]-Ftgettime)/60000/60;
                        //long minute = (Sttime[i]-Ftgettime)/60000%60;
                        resultY9001.setText(Location+"는 비어있습니다 \n"+"다음 수업 시간 :  "+ StartTime);
                        resultY9001.setBackgroundColor(Color.GREEN);

                    }

                    else{
                        resultY9001.setText("오늘의 수업은 더 이상 없습니다.");
                        resultY9001.setBackgroundColor(Color.GREEN);
                    }
                }
/*

 */

                personalData.setLectname(Lectname);
                personalData.setStartTime(StartTime);
                personalData.setEndTime(EndTime);
                personalData.setLocation(Location);


                mArrayList.add(personalData);
                mAdapter.notifyDataSetChanged();


                }






        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }



}