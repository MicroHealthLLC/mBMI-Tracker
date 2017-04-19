package com.microhealthllc.mbmicalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class EnterBMIINfo_kgm extends AppCompatActivity {
    EditText editweight;
    EditText editheight;
    int metrics=0;
    public static final String MetricSettings = "MetricSettings" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmilog_kgm);
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
                if(TextUtils.isEmpty(editheight.getText().toString()) ||TextUtils.isEmpty(editweight.getText().toString()) ){

                }
                else {
                    b.putDouble("height", Double.parseDouble(editheight.getText().toString()));
                    b.putDouble("weight", Double.parseDouble(editweight.getText().toString()));
                    Log.i("Dataheight",""+Double.parseDouble(editheight.getText().toString()));
                    Log.i("Dataweight",""+Double.parseDouble(editweight.getText().toString()));
                    bmiact.putExtras(b);
                    startActivity(bmiact);
                }

                return true;

            case R.id.action_settings:

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }


    private void initEditText() {

        editweight  = (EditText) findViewById(R.id.weight);
        editweight.setSelection(editweight.getText().length());

        editheight = (EditText) findViewById(R.id.height);
        editheight.setSelection(editheight.getText().length());

    }
}