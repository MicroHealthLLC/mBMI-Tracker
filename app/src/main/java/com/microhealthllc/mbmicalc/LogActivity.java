package com.microhealthllc.mbmicalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.microhealthllc.mbmicalc.DB.BmiLogs;
import com.microhealthllc.mbmicalc.DB.DBHandler;
import com.microhealthllc.mbmicalc.chart.LineColumnDependencyActivity;
import com.microhealthllc.mbmicalc.floatbutton.FloatingActionButton;
import com.microhealthllc.mbmicalc.floatbutton.FloatingActionMenu;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    private List<LogModel> logList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LogListAdapter mAdapter;
    DBHandler db;
    private FloatingActionMenu menumainred;
    private FloatingActionButton home_fab;
    private FloatingActionButton delete_graphfab;
    private FloatingActionButton enteredit_fab;
    private FloatingActionButton bmi_logsfab;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    SharedPreferences.Editor editor;
    SharedPreferences  sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loghistory);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setTitle("BMI Calculator");
        db = new DBHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new LogListAdapter(logList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        menumainred = (FloatingActionMenu) findViewById(R.id.menu_red);
        home_fab =(FloatingActionButton) findViewById(R.id.home) ;
        delete_graphfab = (FloatingActionButton) findViewById(R.id.delete);
        enteredit_fab = (FloatingActionButton) findViewById(R.id.enter_edit_data) ;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        delete_graphfab.setOnClickListener(clickListener);
        home_fab.setOnClickListener(clickListener);
        enteredit_fab.setOnClickListener(clickListener);
        prepareMovieData();
        setUpNavigationDrawer();
    }

    private void prepareMovieData() {
        LogModel loghistory ;

        try {
            // Reading all shops
            Log.d("Reading:", "Reading all Logs.");
            List<BmiLogs> logs = db.getAllShops();

            for (BmiLogs log : logs) {
                loghistory = new LogModel(log.getBmi(), log.getWeight(), log.getDateTime());


                logList.add(loghistory);

// Writing shops to log
                Log.d("BMILO: : ", "weight:" + log.getWeight() + "  BMI:" + log.getBmi() + "  DateTime:" + log.getDateTime());

            }

        } catch (Exception e) {
            Log.i("Thisexcept", e.toString());
        }

        mAdapter.notifyDataSetChanged();
    }



    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.enter_edit_data: {
                    //   SharedPreferences SP = getSharedPreferences(MetricSettings, Context.MODE_PRIVATE);
                    // metrics = SP.getInt(getString(R.string.metric_settings),0);


                    Intent i = new Intent(LogActivity.this, BasicSettings.class);
                    startActivity(i);

                }
                break;
                case R.id.home: {

                    Intent bmiact = new Intent(LogActivity.this, BmiChart.class);
                    Intent bmiactyellow = new Intent(LogActivity.this, BmiChartyellow.class);
                    Intent bmiactorange = new Intent(LogActivity.this, BmiChartRed.class);
                    Intent bmiactred = new Intent(LogActivity.this, BmiChartOrange.class);
                    Intent bmiactbloodred = new Intent(LogActivity.this, BmiChartBloodRed.class);
                    Bundle b = new Bundle();
                    Double tempbmi = Double.parseDouble(sharedPref.getString(getString(R.string.display_bmi), "0.0"));
                    if (tempbmi > 18.5 && tempbmi < 25) {
                        startActivity(bmiact);
                    } else if (tempbmi > 25 && tempbmi < 30 || tempbmi < 18.5) {
                        startActivity(bmiactyellow);

                    } else if (tempbmi > 30 && tempbmi < 35) {
                        startActivity(bmiactorange);
                    } else if (tempbmi > 35 && tempbmi < 40) {
                        startActivity(bmiactred);
                    } else {
                        Log.i("BMI RESULTS", tempbmi + "");
                        startActivity(bmiactbloodred);
                    }
                }
                break;
                case R.id.delete: {

                    new LovelyStandardDialog(LogActivity.this)
                            .setTopColorRes(R.color.accent)
                            .setButtonsColorRes(R.color.accent)

                            .setTitle("Warning!!")
                            .setMessage(" This will delete all stored data,This cannot be undone")
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(LogActivity.this, "positive clicked", Toast.LENGTH_SHORT).show();
                                    db.deleteEntry();
                                    Intent i = new Intent(LogActivity.this, BmiChart.class);
                                    startActivity(i);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();


                }
                break;
                case R.id.bmi_logs:
                    Intent it = new Intent(LogActivity.this, LogActivity.class);
                    startActivity(it);
                    break;

            }
        }
    };



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

                        Intent bmiact = new Intent(LogActivity.this,BmiChart.class );
                        Intent bmiactyellow = new Intent(LogActivity.this,BmiChartyellow.class );
                        Intent bmiactorange = new Intent(LogActivity.this,BmiChartRed.class );
                        Intent bmiactred = new Intent(LogActivity.this,BmiChartOrange.class );
                        Intent bmiactbloodred = new Intent(LogActivity.this,BmiChartBloodRed.class );
                        Bundle b = new Bundle();
                        Double tempbmi = Double.parseDouble(sharedPref.getString(getString(R.string.display_bmi),"0.0"));
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
                        break;
                    case R.id.navigation_item_2:
                        //  mCurrentSelectedPosition = 1;
                        Intent i = new Intent(LogActivity.this, BasicSettings.class);
                        startActivity(i);

                        break;
                    case R.id.navigation_item_3:
                        //    mCurrentSelectedPosition = 2;

                        break;
                    case R.id.navigation_item_4:
                        //   mCurrentSelectedPosition = 3;
                        new LovelyStandardDialog(LogActivity.this)
                                .setTopColorRes(R.color.accent)
                                .setButtonsColorRes(R.color.accent)

                                .setTitle("Warning!!")
                                .setMessage(" This will delete all stored data,This cannot be undone")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(LogActivity.this, "positive clicked", Toast.LENGTH_SHORT).show();
                                        db.deleteEntry();

                                        Intent i = new Intent(LogActivity.this, BmiChart.class);
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                        break;
                    case R.id.navigation_item_5:

                        break;
                    case R.id.navigation_item_6:
                        Intent js = new Intent(LogActivity.this, LineColumnDependencyActivity.class);
                        startActivity(js);

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

}