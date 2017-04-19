package com.microhealthllc.mbmicalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.microhealthllc.mbmicalc.DB.BmiLogs;
import com.microhealthllc.mbmicalc.DB.DBHandler;
import com.microhealthllc.mbmicalc.chart.SimpleLineChart;

import java.util.HashMap;
import java.util.List;

public class Chart extends AppCompatActivity {
    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        db  = new DBHandler(this);
     SimpleLineChart   mSimpleLineChart = (SimpleLineChart) findViewById(R.id.simpleLineChart);
        String[] xItem = {"1","2","3","4","5","6","7"};
        String[] yItem = {"50","40","30","20","10"};
        if(mSimpleLineChart == null)
            Log.e("wing","null!!!!");
        mSimpleLineChart.setXItem(xItem);
        mSimpleLineChart.setYItem(yItem);
        HashMap<Integer,Integer> pointMap = new HashMap();
        for(int i = 0;i<xItem.length;i++){
            pointMap.put(i, (int) (Math.random()*5));
        }
        mSimpleLineChart.setData(pointMap);


        SimpleLineChart   mSimpleLineChartWeight = (SimpleLineChart) findViewById(R.id.simpleLineChartweight);


        String[] xItemweight = {"1","2","3","4","5","6","7"};
        String[] yItemweight = {"0","100","200","300","400","500"};
        if(mSimpleLineChartWeight == null)
            Log.e("wing","null!!!!");
        mSimpleLineChartWeight.setXItem(xItemweight);
        mSimpleLineChartWeight.setYItem(yItemweight);
        HashMap<Integer,Integer> pointMapweight = new HashMap();
        for(int i = 0;i<xItemweight.length;i++){
            pointMapweight.put(i, (int) (Math.random()*5));
        }
        mSimpleLineChartWeight.setData(pointMapweight);

        try {
            // Reading all shops
            Log.d("Reading:", "Reading all Logs.");
            List<BmiLogs> logs = db.getAllShops();

            for (BmiLogs log : logs) {
                String bmilog = "Id:" + log.getId() + " ,BMI:" + log.getBmi() + ",Weight:" + log.getWeight();
// Writing shops to log
                Log.d("BMILOG: : ", bmilog);
            }

        }
        catch(Exception e){
            Log.i("Thisexcept",e.toString());
        }
    }
}
