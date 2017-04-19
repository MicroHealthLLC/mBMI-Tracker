package com.microhealthllc.mbmicalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


import com.microhealthllc.SimpleEula;
import com.microhealthllc.mbmicalc.BmiChart;
import com.microhealthllc.mbmicalc.EnterBMIINfo;
import com.microhealthllc.mbmicalc.EnterBMIINfo_kgm;
import com.microhealthllc.mbmicalc.R;

import java.io.IOException;

public class BasicSettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText username;
    EditText editweight;
    EditText editheightin;
    EditText editheightft;
    int metrics=0;
    public static final String MetricSettings = "MetricSettings" ;
    SharedPreferences sharedpreferences;
  //  FloatingActionButton okpinnedup;
    FloatingActionButton okpineddown;
    Double heigtwithinch;
    SharedPreferences.Editor editor;
    Spinner spinner;
    SharedPreferences  sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bsettings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Context context = getApplicationContext();
        initEditText();
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //= getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        try {
            new SimpleEula(this).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, R.layout.spinnerlayout);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinnerlayout);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        //spinner.setItems("Imperial Units: height/ft. in  weight/lbs", "Metric Units: height/m weight/kg");

        metrics = sharedPref.getInt(getString(R.string.metric_settings), 0);

        if (metrics == 1) {
            editheightin.setVisibility(View.GONE);
            editheightft.setHint("height (m)");
            editheightft.setText(sharedPref.getString(getString(R.string.metric_height), ""));
            editweight.setText(sharedPref.getString(getString(R.string.metric_weight), ""));
            username.setText(sharedPref.getString(getString(R.string.metric_user_name), ""));
            spinner.setSelection(1);
            editweight.setHint("weight (kg)");
        } else {
            spinner.setSelection(0);
            editheightin.setVisibility(View.VISIBLE);
            editheightft.setHint("feets");
            editweight.setHint("weight (lb)");
            editheightft.setText(sharedPref.getString(getString(R.string.metric_height), ""));
            editweight.setText(sharedPref.getString(getString(R.string.metric_weight), ""));
            editheightin.setText(sharedPref.getString(getString(R.string.metric_inches), ""));
            username.setText(sharedPref.getString(getString(R.string.metric_user_name), ""));
        }

        okpineddown = (FloatingActionButton) findViewById(R.id.fabdown);
        okpineddown.setOnClickListener(clickListener);
        //okpinnedup = (FloatingActionButton) findViewById(R.id.fabup);
      //  okpinnedup.setOnClickListener(clickListener);

    }
    public Double convertfeettoInches(Double feets, Double inches){
        //1foot ==12 inches
        //  Log.i("inches",  (inches!=0? (feets *12) +inches:(feets*12))+"");
        return (feets *12) +inches ;

    }

    private void initEditText() {

        editweight = (EditText) findViewById(R.id.weight);

        username = (EditText) findViewById(R.id.username);
        editweight.setSelection(editweight.getText().length());
        editheightft = (EditText) findViewById(R.id.height);
        editheightft.setSelection(editheightft.getText().length());
        editheightin = (EditText) findViewById(R.id.inches);


    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fabdown:
                {
                    Intent bmiact = new Intent(BasicSettings.this,BmiChart.class );
                    Intent bmiactyellow = new Intent(BasicSettings.this,BmiChartyellow.class );
                    Intent bmiactorange = new Intent(BasicSettings.this,BmiChartRed.class );
                    Intent bmiactred = new Intent(BasicSettings.this,BmiChartOrange.class );
                    Intent bmiactbloodred = new Intent(BasicSettings.this,BmiChartBloodRed.class );
                    Bundle b = new Bundle();
                    if(TextUtils.isEmpty(editheightft.getText().toString()) ||TextUtils.isEmpty(editweight.getText().toString()) || TextUtils.isEmpty(username.getText().toString()) )
                    {
                        Snackbar.make(v, "One or more of the following fields is blank: username, height and weight",Snackbar.LENGTH_SHORT).show();
                    }

                    else {

                        editor = sharedPref.edit();
                        if (metrics ==1){
                            b.putDouble("height", Double.parseDouble(editheightft.getText().toString()));
                            b.putDouble("weight", Double.parseDouble(editweight.getText().toString()));
                            b.putString("username",username.getText().toString());

                            try {
                                editor.putString(getString(R.string.metric_height),editheightft.getText().toString());
                                editor.apply();
                                editor = sharedPref.edit();
                                editor.putString(getString(R.string.metric_weight),editweight.getText().toString());
                                editor.apply();
                                editor = sharedPref.edit();
                                editor.putString(getString(R.string.metric_user_name),username.getText().toString());
                                editor.apply();
                            }
                            catch (Exception e){
                                Log.i("Exceptionhanler",e.toString());
                            }

                            bmiact.putExtras(b);
                            bmiactorange.putExtras(b);
                            bmiactyellow.putExtras(b);
                            bmiactred.putExtras(b);
                            bmiactbloodred.putExtras(b);

                            Double tempbmi =   calcBMI(Double.parseDouble(editheightft.getText().toString()),Double.parseDouble(editweight.getText().toString()),BasicSettings.this,metrics);
                            if (tempbmi >18.5 &&tempbmi <25){
                                startActivity(bmiact);
                            }
                            else if(tempbmi>25 && tempbmi <30||tempbmi <18.5){
                                startActivity(bmiactyellow);

                            }
                            else if(tempbmi>30 && tempbmi <35){
                                startActivity(bmiactorange);
                            }

                            else if(tempbmi>35 && tempbmi <40){
                                startActivity(bmiactred);
                            }
                            else {
                                Log.i("BMI RESULTS",tempbmi+"");
                                startActivity(bmiactbloodred);
                            }
                        }
                        else {
                            editor = sharedPref.edit();
                            if (!TextUtils.isEmpty(editheightin.getText().toString())) {

                                Double inchesval = Double.parseDouble(editheightin.getText().toString());
                                Log.i("inchesmonitor", String.format("%.1f", inchesval));
                                //  b.putDouble("inches", Double.parseDouble(editheightin.getText().toString()));
                                heigtwithinch = convertfeettoInches(Double.parseDouble(editheightft.getText().toString()), Double.parseDouble(editheightin.getText().toString()));

                            } else {
                                heigtwithinch = convertfeettoInches(Double.parseDouble(editheightft.getText().toString()), 0.0);
                            }
                            b.putString("username",username.getText().toString());
                            b.putDouble("height", heigtwithinch);
                            b.putDouble("weight", Double.parseDouble(editweight.getText().toString()));
                            try {
                                editor.putString(getString(R.string.metric_user_name),username.getText().toString());
                                editor.apply();
                                editor.putString(getString(R.string.metric_height),editheightft.getText().toString());
                                editor.apply();
                                editor.putString(getString(R.string.metric_weight),editweight.getText().toString());
                                editor.apply();
                                editor.putString(getString(R.string.metric_inches),editheightin.getText().toString());
                                editor.apply();

                            }
                            catch (Exception e){
                                Log.i("ErroException",e.toString());
                            }

                            bmiact.putExtras(b);
                            bmiactorange.putExtras(b);
                            bmiactyellow.putExtras(b);
                            bmiactred.putExtras(b);
                            bmiactbloodred.putExtras(b);

                            Double tempbmi =   calcBMI(heigtwithinch,Double.parseDouble(editweight.getText().toString()),BasicSettings.this,metrics);
                            if (tempbmi >18.5 &&tempbmi <25){
                                startActivity(bmiact);
                            }
                            else if(tempbmi>25 && tempbmi <30||tempbmi <18.5){
                                startActivity(bmiactyellow);

                            }
                            else if(tempbmi>30 && tempbmi <35){
                                startActivity(bmiactorange);
                            }
                            else if(tempbmi>35 && tempbmi <40){
                                startActivity(bmiactred);
                            }
                            else {
                                Log.i("BMI RESULTS",tempbmi+"");
                                startActivity(bmiactbloodred);
                            }

                        }

                        /////////////////////////////////////////////////////////////////////////////////////////////////////
                      /*  Log.i("Dataheight",""+Double.parseDouble(editheightft.getText().toString()));
                        Log.i("Dataweight",""+Double.parseDouble(editweight.getText().toString()));
                        Log.i("realiheight",heigtwithinch+"");
                        bmiact.putExtras(b);
                        bmiactorange.putExtras(b);
                        bmiactyellow.putExtras(b);
                        bmiactred.putExtras(b);

                     Double tempbmi =   calcBMI(Double.parseDouble(editheightft.getText().toString()),Double.parseDouble(editweight.getText().toString()),BasicSettings.this,metrics);
                        if (tempbmi >18.5 &&tempbmi <25){
                            startActivity(bmiact);
                        }
                        else if(tempbmi>25 && tempbmi <30||tempbmi <18.5){
                            startActivity(bmiactyellow);

                        }
                        else if(tempbmi>30 && tempbmi <35){
                            startActivity(bmiactorange);
                        }
                        else {
                            Log.i("BMI RESULTS",tempbmi+"");
                            startActivity(bmiactred);
                        }*/
                       // startActivity(bmiact);
                    }


                }
                case R.id.fabup:
                {

                }


            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        editor = sharedPref.edit();
        if (position==1){
            metrics = 1;

            editor.putInt(getString(R.string.metric_settings),metrics);
            editor.apply();
            editheightin.setVisibility(View.GONE);
            editheightft.setHint("height (m)");
            editweight.setHint("weight (kg)");
        }
        else {

            metrics=0;
            editor = sharedPref.edit();
            editor.putInt(getString(R.string.metric_settings),0);
            editor.apply();
            editheightin.setVisibility(View.VISIBLE);
            editheightft.setHint("feets");
            editweight.setHint("weight (lb)");
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public  double   calcBMI(double height, double weight, Context ctx, int metric) {
        double bmiresults;
        if (height <= 0 || weight <= 0) {
            throw new IllegalArgumentException(ctx.getString(R.string.error_less_than_zero));
        }
        else{
            if(metric ==1){

                double weightindex = weight;
                double heightindex = height;
                Log.i("weightindex heightIndex",   weightindex+","+heightindex);
                Log.i("metric", metric+"");
                bmiresults = (weightindex)/(heightindex*heightindex);
                Log.i("bmiresults", bmiresults+"");
                return bmiresults ;
            }
            else {
                double weightindex = weight * 0.45;
                double heightindex = height * 0.025;
                Log.i("metric", metric+"");
                Log.i("weightindex heightIndex", weightindex + "," + heightindex);
                bmiresults = (weightindex) / (heightindex * heightindex);
                return bmiresults ;
            }
        }
        // Log.i("result",bmiresults+"");
        // Log.i("result metric",metric+"");


    }
}
