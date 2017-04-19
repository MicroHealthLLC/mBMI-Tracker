package com.microhealthllc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microhealthllc.mbmicalc.BasicSettings;
import com.microhealthllc.mbmicalc.BmiChart;
import com.microhealthllc.mbmicalc.BmiChartRed;
import com.microhealthllc.mbmicalc.DB.DBHandler;
import com.microhealthllc.mbmicalc.LogActivity;
import com.microhealthllc.mbmicalc.LogListAdapter;
import com.microhealthllc.mbmicalc.R;
import com.microhealthllc.mbmicalc.chart.LineColumnDependencyActivity;
import com.microhealthllc.mbmicalc.floatbutton.FloatingActionButton;
import com.microhealthllc.mbmicalc.floatbutton.FloatingActionMenu;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import butterknife.ButterKnife;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by ubuntuadmin on 3/24/17.
 */

public class About  extends AppCompatActivity {

    private FloatingActionMenu menumainred;
    private FloatingActionButton enteredit_fab;
    private FloatingActionButton weight_graphfab;
    private FloatingActionButton bmi_logsfab;
    DBHandler db;
    private RecyclerView recyclerView;
    private LogListAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    Button twitterview;
    Button websitetxt;
    Button privacypolicy;
    Button termsofUse;
    TextView Lincenceagreement;
            TextView rateview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        db = new DBHandler(this);
        menumainred = (FloatingActionMenu) findViewById(R.id.menu_red);
        enteredit_fab = (FloatingActionButton) findViewById(R.id.enter_edit_data);
        weight_graphfab = (FloatingActionButton) findViewById(R.id.bmi_graph);
        bmi_logsfab =(FloatingActionButton) findViewById(R.id.bmi_logs);
         twitterview = (Button) findViewById(R.id.followus);
        websitetxt =(Button) findViewById(R.id.webaddress);
        privacypolicy =(Button) findViewById(R.id.privacyp);
        termsofUse = (Button) findViewById(R.id.termsofuse);
       // twitterview.setMovementMethod(LinkMovementMethod.getInstance());

        twitterview.setText("@microhealthtalk");
        twitterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        gotoURl("https://twitter.com/MicroHealthTalk");
            }
        });
        websitetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoURl("https://www.microhealthllc.com/");
            }
        });
                termsofUse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoURl("https://www.microhealthllc.com/about/terms-of-use/");

                    }
                });
                  privacypolicy.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                          gotoURl("https://www.microhealthllc.com/about/privacy-policy/");

                      }
                  });


     //   twitterview.setClickable(true);
    //    websitetxt =(TextView) findViewById(R.id.websiteurl);
     //   websitetxt.setClickable(true);
     //   websitetxt.setText(Html.fromHtml(website));

     //   twitterview.setText(Html.fromHtml(twiteracc));
       // Linkify.addLinks(twitterview,Linkify.ALL);
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Bmi Calculator");

        }
        setUpNavigationDrawer();

        enteredit_fab.setOnClickListener(clickListener);
        weight_graphfab.setOnClickListener(clickListener);
        bmi_logsfab.setOnClickListener(clickListener);


    }

    private void gotoURl(String url){

        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);

    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.enter_edit_data:
                {
                  //  SharedPreferences SP = getSharedPreferences(MetricSettings, Context.MODE_PRIVATE);
                 //   metrics = SP.getInt(getString(R.string.metric_settings),0);


                    Intent i = new Intent(About.this, BasicSettings.class);
                    startActivity(i);

                }
                break;
                case R.id.bmi_graph:

                    Intent i = new Intent(About.this, LineColumnDependencyActivity.class);
                    startActivity(i);

                    break;
             /*   case R.id.unitsettings:
                //    MaterialDialog.Builder builder = new MaterialDialog.Builder(BmiChart.this);
                   Intent set = new Intent(BmiChart.this, BasicSettings.class);
                   startActivity(set);
                 //  unitoptions();


                    break;*/
                case R.id.bmi_logs :
                    Intent it = new Intent(About.this, LogActivity.class);
                    startActivity(it);

            }
        }
    };

    private void setupToolbar() {
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawer(mNavigationView);
        } else {
            super.onBackPressed();
        }
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
                        Intent gh = new Intent(About.this, BasicSettings.class);
                        startActivity(gh);
                        break;
                    case R.id.navigation_item_2:
                        //  mCurrentSelectedPosition = 1;
                        Intent i = new Intent(About.this, BasicSettings.class);
                        startActivity(i);

                        break;
                    case R.id.navigation_item_3:
                        //    mCurrentSelectedPosition = 2;
                        Intent j = new Intent(About.this, LogActivity.class);
                        startActivity(j);
                        break;
                    case R.id.navigation_item_4:
                        //   mCurrentSelectedPosition = 3;
                        new LovelyStandardDialog(About.this)
                                .setTopColorRes(R.color.accent)
                                .setButtonsColorRes(R.color.accent)

                                .setTitle("Warning!!")
                                .setMessage(" This will delete all stored data,This cannot be undone")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(About.this, "positive clicked", Toast.LENGTH_SHORT).show();
                                          db.deleteEntry();
                                        Intent i = new Intent(About.this, BmiChartRed.class);
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .show();


                        break;
                    case R.id.navigation_item_5:
                            Intent home = new Intent(About.this, BmiChart.class);
                        startActivity(home);
                        break;
                    case R.id.navigation_item_6:
                        Intent js = new Intent(About.this, LineColumnDependencyActivity.class);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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



}
