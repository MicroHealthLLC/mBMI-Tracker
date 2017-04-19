package com.microhealthllc.mbmicalc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dan on 3/20/17.
 */


public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.MyViewHolder> {

    private List<LogModel> logList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bmi, weight, datetime;

        public MyViewHolder(View view) {
            super(view);
            bmi = (TextView) view.findViewById(R.id.bmi_text);
            weight = (TextView) view.findViewById(R.id.weight_txt);
            datetime = (TextView) view.findViewById(R.id.date_text);
        }
    }


    public LogListAdapter(List<LogModel> moviesList) {
        this.logList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listone, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       LogModel movie = logList.get(position);
        holder.bmi.setText(movie.getBmi());
        holder.weight.setText(movie.getWeight());
        holder.datetime.setText(movie.getDatetime());
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }
}