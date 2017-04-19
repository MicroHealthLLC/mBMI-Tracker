package com.microhealthllc.mbmicalc;/*
 * Copyright Txus Ballesteros 2015 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.microhealthllc.About;
import com.microhealthllc.mbmicalc.DB.BmiLogs;
import com.microhealthllc.mbmicalc.DB.DBHandler;
import com.microhealthllc.mbmicalc.chart.ColorArcProgressBar;
import com.microhealthllc.mbmicalc.chart.LineColumnDependencyActivity;
import com.microhealthllc.mbmicalc.chart.SimpleLineChart;
import com.microhealthllc.mbmicalc.floatbutton.FloatingActionButton;
import com.microhealthllc.mbmicalc.floatbutton.FloatingActionMenu;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


public class BmiChartyellow extends AppCompatActivity {

    private FloatingActionMenu menumainred;
    private FloatingActionButton enteredit_fab;
    private FloatingActionButton weight_graphfab;
    private FloatingActionButton bmi_logsfab;
    private FloatingActionButton bmi_info;


    private List<LogModel> logList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LogListAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    Button viewmore;


    public final static String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec",};

    public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

    private LineChartView chartTop;
    private ColumnChartView chartBottom;

    private LineChartData lineData;
    private ColumnChartData columnData;

    TextView bmitext;
    TextView bmi_note;

    ///Title Bar Data
    TextView displayname;
    TextView displayheight;
    TextView displayweight;
    TextView displaybmi;
    TextView lastactivty;

    int metrics = 0;
    private ColorArcProgressBar bar1;
    double  bmi;
    DecimalFormat df = new DecimalFormat("#.##");
    Double gotWeight=0.0;
    Double gotheight=0.0;
    Double gotinches=0.0;
    Double heightfeetsandinches;
    public static final String MetricSettings = "MetricSettings" ;
    SharedPreferences sharedpreferences;
    ListView listView;
    SimpleLineChart mSimpleLineChart;
    DBHandler db;
    ArrayList<String> weightter;
    SharedPreferences.Editor editor;
    SharedPreferences  sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmichartyellow);

        bar1 = (ColorArcProgressBar) findViewById(R.id.bar1);
        menumainred = (FloatingActionMenu) findViewById(R.id.menu_red);
        bmi_note = (TextView) findViewById(R.id.bmi_note);
        bmitext = (TextView)findViewById(R.id.bmi_display);
        displayname =(TextView) findViewById(R.id.displayname);
        displayheight = (TextView) findViewById(R.id.display_height);
        displayweight =(TextView) findViewById(R.id.display_weight);
        displaybmi = (TextView) findViewById(R.id.display_bmi);
         weightter= new ArrayList<>();
        db  = new DBHandler(this);
        enteredit_fab = (FloatingActionButton) findViewById(R.id.enter_edit_data);
        weight_graphfab = (FloatingActionButton) findViewById(R.id.bmi_graph);
        bmi_logsfab =(FloatingActionButton) findViewById(R.id.bmi_logs);
        bmi_info = (FloatingActionButton) findViewById(R.id.bmi_info) ;
        lastactivty = (TextView) findViewById(R.id.last_activity);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
     //   unit_settingsfab = (FloatingActionButton) findViewById(R.id.unitsettings);
        // Enable the Up button
       // ab.setDisplayHomeAsUpEnabled(true);

        viewmore =(Button) findViewById(R.id.view_more);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("BMI Calculator");

        }
     //   ab.setDisplayHomeAsUpEnabled(true);

        setUpNavigationDrawer();

        mAdapter = new LogListAdapter(logList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // *** TOP LINE CHART ***
     //   chartTop = (LineChartView) findViewById(R.id.chart_top);

       // generateInitialLineData();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Log.i("Get curr time",getDateTimeforLastActivity());
        SharedPreferences SP = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        metrics = SP.getInt(getString(R.string.metric_settings),0);

        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BmiChartyellow.this, LogActivity.class);
                startActivity(i);
            }
        });

    if(sharedPref.getString(getString(R.string.last_activity),"").isEmpty()){
        lastactivty.setText(sharedPref.getString(getString(R.string.last_activity),""));
    }


         Bundle bundle = getIntent().getExtras();

        if(bundle!=null) {
            gotheight = bundle.getDouble("height");
            gotWeight = bundle.getDouble("weight");
            bmifunc(gotheight,gotWeight);
        }
        else{
            bar1.setCurrentValues(Double.parseDouble(sharedPref.getString(getString(R.string.display_bmi),"0.0")));

            // Log.i("getweight gotheight",   gotheight+","+gotWeight);

            bmi_note.setText(sharedPref.getString(getString(R.string.display_note),"normal"));
            displayheight.setText(sharedPref.getString(getString(R.string.display_height),"0"));
            displayweight.setText(sharedPref.getString(getString(R.string.display_weight),"0"));
            displaybmi.setText(sharedPref.getString(getString(R.string.display_bmi),"0.0"));

        }

         displayname.setText(SP.getString(getString(R.string.metric_user_name),"User"));
         enteredit_fab.setOnClickListener(clickListener);
         weight_graphfab.setOnClickListener(clickListener);
         bmi_logsfab.setOnClickListener(clickListener);
         bmi_info.setOnClickListener(clickListener);

         //  unit_settingsfab.setOnClickListener(clickListener);




        prepareMovieData();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
                mDrawerLayout.closeDrawer(mNavigationView);
            } else {
                mDrawerLayout.openDrawer(mNavigationView);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public void bmifunc(double height, double weight) {


        try {

        } catch (NumberFormatException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.error_enter_valid))
                    .setPositiveButton(getString(R.string.ok_bro), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
            return;
        }

        try {
            bmi = calcBMI(height, weight, this, metrics);
          //  Log.i("BMI data", ""+bmi);
           // Log.i("BMI data metric", ""+metrics);
        } catch (IllegalArgumentException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(e.getMessage())
                    .setPositiveButton(getString(R.string.ok_bro), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.create();
            builder.show();
            return;
        }
       ;
      //  bmitext.setText( df.format(bmi).toString());
        bar1.setcolors(Color.YELLOW,Color.YELLOW,Color.YELLOW);
        bar1.setcolor3(Color.YELLOW);
        bar1.setcolor1(Color.YELLOW);
        bar1.setcolor2(Color.YELLOW);
        bar1.setCurrentValues(bmi);


       // Log.i("getweight gotheight",   gotheight+","+gotWeight);

        bmi_note.setText(getString(BMIUtils.getJudgement(bmi)));
        displayheight.setText(String.format("%.0f", height));
        displayweight.setText(String.format("%.0f", weight));
        displaybmi.setText(String.format("%.1f", bmi));
        editor = sharedPref.edit();
        editor.putString(getString(R.string.display_height),String.format("%.0f", height));
        editor.putString(getString(R.string.display_weight),String.format("%.0f", weight));
        editor.putString(getString(R.string.display_note),getString(BMIUtils.getJudgement(bmi)));
        editor.putString(getString(R.string.display_bmi),String.format("%.1f", bmi));

        editor.apply();


        try {
           // if(getDateTime().equals())
             if(db.getLast().getDateTime()==null){

                 db.addLog(new BmiLogs(String.format("%.1f", bmi), String.format("%.0f", weight), getDateTime()));
                 lastactivty.setText(""+getDateTimeforLastActivity());
                 editor.putString(getString(R.string.last_activity),getDateTimeforLastActivity());
                 editor.apply();
             }
                else if(db.getLast().getDateTime().equals(getDateTime()))
                {
                    Log.i("Equals",""+db.getLast().getDateTime().equals(getDateTime()));
                    db.updateLastEntry(db.getLast().getId(),String.format("%.1f", bmi), String.format("%.0f", weight),getDateTime());
                    lastactivty.setText(""+getDateTimeforLastActivity());
                    editor.putString(getString(R.string.last_activity),getDateTimeforLastActivity());
                    editor.apply();


                }
                else {
                    db.addLog(new BmiLogs(String.format("%.1f", bmi), String.format("%.0f", weight), getDateTime()));
                    lastactivty.setText(""+getDateTimeforLastActivity());
                    editor.putString(getString(R.string.last_activity),getDateTimeforLastActivity());
                    editor.apply();
                }
         //  Log.i("datetime",) ;

        }
        catch(Exception e){
            Log.d("Error  Exception",e.toString());
        }
    }


    public String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EE MM/dd", Locale.getDefault());
        //Date date = new Date();
        return dateFormat.format(date);
    }


    public String getDateTimeforLastActivity() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, MM.dd h:mm a", Locale.getDefault());
        //Date date = new Date();
        return dateFormat.format(date);
    }
    private void showToast(int message) {


         Toast.makeText(this, message+"", Toast.LENGTH_SHORT);

    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.enter_edit_data:
                {
                    SharedPreferences SP = getSharedPreferences(MetricSettings, Context.MODE_PRIVATE);
                    metrics = SP.getInt(getString(R.string.metric_settings),0);


                  Intent i = new Intent(BmiChartyellow.this, BasicSettings.class);
                  startActivity(i);

              }
                    break;
                case R.id.bmi_graph:

                    Intent i = new Intent(BmiChartyellow.this, LineColumnDependencyActivity.class);
                    startActivity(i);

                    break;
             /*   case R.id.unitsettings:
                //    MaterialDialog.Builder builder = new MaterialDialog.Builder(BmiChart.this);
                   Intent set = new Intent(BmiChart.this, BasicSettings.class);
                   startActivity(set);
                 //  unitoptions();


                    break;*/
                case R.id.bmi_logs :
                    Intent it = new Intent(BmiChartyellow.this, LogActivity.class);
                    startActivity(it);

            }
        }
    };


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
             //   double weightindex = weight * 0.45;
             //   double heightindex = height * 0.025;
                Log.i("metric", metric+"");
                Log.i("weightindex heightIndex", weight + "," + height);
                bmiresults = ((weight) / (height * height))*703;
                Log.i("bmiresults", bmiresults+"");
                return bmiresults ;
            }
        }
       // Log.i("result",bmiresults+"");
       // Log.i("result metric",metric+"");


    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawer(mNavigationView);
        } else {
            super.onBackPressed();
        }
    }

    public void unitoptions(){

        new MaterialDialog.Builder(this)
                .title(R.string.selectunits)
                .items(R.array.metricoptions)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        SharedPreferences.Editor editor;
                        sharedpreferences = getSharedPreferences(MetricSettings, Context.MODE_PRIVATE);
                        editor = sharedpreferences.edit();
                     editor.putInt("metric_unit",which);
                        editor.commit();

                        return true;
                    }
                })
                .positiveText(R.string.ok)
                .show();
    }

    private void setUpNavigationDrawer() {
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setSubtitle(getString(R.string.subtitle));
            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                       // mCurrentSelectedPosition = 0;
                        break;
                    case R.id.navigation_item_2:
                      //  mCurrentSelectedPosition = 1;
                        Intent i = new Intent(BmiChartyellow.this, BasicSettings.class);
                        startActivity(i);

                        break;
                    case R.id.navigation_item_3:
                    //    mCurrentSelectedPosition = 2;
                        Intent j = new Intent(BmiChartyellow.this, LogActivity.class);
                        startActivity(j);
                        break;
                    case R.id.navigation_item_4:
                     //   mCurrentSelectedPosition = 3;
                        new LovelyStandardDialog(BmiChartyellow.this)
                                .setTopColorRes(R.color.accent)
                                .setButtonsColorRes(R.color.accent)

                                .setTitle("Warning!!")
                                .setMessage(" This will delete all stored data,This cannot be undone")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(BmiChartyellow.this, "positive clicked", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(BmiChartyellow.this, BmiChartyellow.class);
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .show();


                        break;
                    case R.id.navigation_item_5:
                        Intent about = new Intent (BmiChartyellow.this, About.class);
                        startActivity(about);

                        break;
                    case R.id.navigation_item_6:
                        Intent js = new Intent(BmiChartyellow.this, LineColumnDependencyActivity.class);
                        startActivity(js);
                        break;
                    case R.id.bmi_info:
                        startActivity(new Intent(BmiChartyellow.this, AdditionalBMIinfo.class));

                }

               // setTabs(mCurrentSelectedPosition + 1);
                mDrawerLayout.closeDrawer(mNavigationView);
                return true;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(getString(R.string.drawer_opened));
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }



    private void prepareMovieData() {
        LogModel loghistory ;


        try {
            // Reading all shops
            Log.d("Reading:", "Reading all Logs.");
            List<BmiLogs> logs = db.getAllShops();

           // for (BmiLogs log : logs) {
           //     loghistory = new LogModel(log.getBmi(), log.getWeight(), log.getDateTime());
//
               for (int i=logs.size()-1;i>logs.size()-3; i--){
                   loghistory = new LogModel(logs.get(i).getBmi(), logs.get(i).getWeight(), logs.get(i).getDateTime());
                   logList.add(loghistory);
               }
// Writing shops to log
          //      Log.d("BMILO: : ", "weight:" + log.getWeight() + "  BMI:" + log.getBmi() + "  DateTime:" + log.getDateTime());

          //  }

        } catch (Exception e) {
            Log.i("Thisexcept", e.toString());
        }


        mAdapter.notifyDataSetChanged();
    }


}
