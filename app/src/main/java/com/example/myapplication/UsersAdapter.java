package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList= null;
    private Activity context = null;


    public UsersAdapter(Activity context, ArrayList<PersonalData> list){
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView Lectnum;
        protected TextView Lectname;
        protected TextView StartTime;
        protected TextView EndTime;
        protected TextView Location;
        protected  TextView result;

        public CustomViewHolder(View view) {
            super(view);

            this.Lectname = (TextView) view.findViewById(R.id.Lectname);
            this.StartTime = (TextView) view.findViewById(R.id.StartTime);
            this.EndTime = (TextView) view.findViewById(R.id.EndTime);
            this.Location =(TextView) view.findViewById(R.id.Location);
            this.result = (TextView) view.findViewById(R.id.result);

        }

    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position){

        Calendar cal = Calendar.getInstance();
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println("nWeek = "+ nWeek);

        //현재 시간 구하는 함수
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
        String formatDate = sdfNow.format(date);



        viewholder.Lectname.setText(mList.get(position).getLectname());
        viewholder.StartTime.setText(mList.get(position).getStartTime());
        viewholder.EndTime.setText(mList.get(position).getEndTime());
        viewholder.Location.setText(mList.get(position).getLocation());

        if(formatDate.compareTo(mList.get(position).getStartTime())>0 && formatDate.compareTo(mList.get(position).getEndTime())<0){
            viewholder.result.setText("현 강의실은 수업중입니다");
            viewholder.result.setBackgroundColor(Color.RED);

        }

        else if(formatDate.compareTo(mList.get(position).getStartTime())<0){
            viewholder.result.setText("현 강의실은 비어있습니다.\n" + "다음 강의 시간 : \t"+mList.get(position).getStartTime()) ;
            viewholder.result.setBackgroundColor(Color.GREEN);

        }
        else{
            viewholder.result.setText("현 강의실은 비어있습니다.\n" + "오늘의 강의는 더이상 없어요") ;
            viewholder.result.setBackgroundColor(Color.GREEN);
        }


    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }



}

