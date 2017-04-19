package com.microhealthllc.mbmicalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.microhealthllc.mbmicalc.chart.SlimChart;


import static com.microhealthllc.mbmicalc.R.id.slimChart;
import static com.microhealthllc.mbmicalc.R.id.weight;

public class EnterBMIINfo extends AppCompatActivity {
    EditText editweight;
    EditText editheightin;
    EditText editheightft;
    int metrics=0;
    public static final String MetricSettings = "MetricSettings" ;
    SharedPreferences sharedpreferences;
    Double heigtwithinch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmilog);
        //Optional - create colors array
        SharedPreferences SP = getSharedPreferences(MetricSettings, Context.MODE_PRIVATE);
        metrics = SP.getInt("metric_unit",0);


        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        initEditText();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                Intent bmiact = new Intent(this,BmiChart.class );
                Bundle b = new Bundle();
                if(TextUtils.isEmpty(editheightft.getText().toString()) ||TextUtils.isEmpty(editweight.getText().toString()) ){

                }
                else {
                    if(!TextUtils.isEmpty(editheightin.getText().toString()) ){


                            Double inchesval = Double.parseDouble(editheightin.getText().toString());
                            Log.i("inchesmonitor",String.format("%.1f", inchesval));
                            //  b.putDouble("inches", Double.parseDouble(editheightin.getText().toString()));
                            heigtwithinch = convertfeettoInches(Double.parseDouble(editheightft.getText().toString()), Double.parseDouble(editheightin.getText().toString()));

                        }
                        else {
                            heigtwithinch =  convertfeettoInches(Double.parseDouble(editheightft.getText().toString()),0.0);
                        }
                        b.putDouble("height",heigtwithinch);
                    }




                    b.putDouble("weight", Double.parseDouble(editweight.getText().toString()));
                    Log.i("Dataheight",""+Double.parseDouble(editheightft.getText().toString()));
                    Log.i("Dataweight",""+Double.parseDouble(editweight.getText().toString()));
                    Log.i("realiheight",heigtwithinch+"");
                    bmiact.putExtras(b);
                    startActivity(bmiact);
                return true;



            case R.id.action_settings:

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }
    public Double convertfeettoInches(Double feets, Double inches){
        //1foot ==12 inches
      //  Log.i("inches",  (inches!=0? (feets *12) +inches:(feets*12))+"");
        if(inches.equals(null)){
            inches = 0.0;
        }

        return (feets *12) +inches ;

    }


    private void initEditText() {

        editweight  = (EditText) findViewById(R.id.weight);


        editweight.setSelection(editweight.getText().length());
        editheightft = (EditText) findViewById(R.id.height);
        editheightft.setSelection(editheightft.getText().length());
        editheightin = (EditText) findViewById(R.id.inches);




    }
}