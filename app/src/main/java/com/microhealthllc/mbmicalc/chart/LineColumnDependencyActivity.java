package com.microhealthllc.mbmicalc.chart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fenchtose.tooltip.TooltipAnimation;
import com.microhealthllc.About;
import com.microhealthllc.mbmicalc.BasicSettings;
import com.microhealthllc.mbmicalc.BmiChart;
import com.microhealthllc.mbmicalc.BmiChartBloodRed;
import com.microhealthllc.mbmicalc.BmiChartOrange;
import com.microhealthllc.mbmicalc.BmiChartRed;
import com.microhealthllc.mbmicalc.BmiChartyellow;
import com.microhealthllc.mbmicalc.DB.BmiLogs;
import com.microhealthllc.mbmicalc.DB.DBHandler;
import com.microhealthllc.mbmicalc.LogActivity;
import com.microhealthllc.mbmicalc.R;
import com.microhealthllc.mbmicalc.floatbutton.FloatingActionButton;
import com.microhealthllc.mbmicalc.floatbutton.FloatingActionMenu;

import com.tooltip.Tooltip;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class LineColumnDependencyActivity extends ActionBarActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    SharedPreferences.Editor editor;
    SharedPreferences  sharedPref;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_column_dependency);
        ActionBar ab = getSupportActionBar();
        db = new DBHandler(this);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setTitle("BMI Calculator");
        setUpNavigationDrawer();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        setUpNavigationDrawer();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public final static String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec",};

        public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

        private LineChartView chartTop;
        private LineChartView weightbottomgraph;
        private LineChartData lineDataforweight;
        private ColumnChartView chartBottom;
        DBHandler db;
        ArrayList<String> weightlist;
        ArrayList<String> bmilist;
        ArrayList<String> datetimelist;
        private LineChartData lineData;
        private ColumnChartData columnData;
        private FloatingActionMenu menumainred;
        private FloatingActionButton home_fab;
        private FloatingActionButton delete_graphfab;
        private FloatingActionButton enteredit_fab;
        SharedPreferences.Editor editor;
        SharedPreferences  sharedPref;
        private FloatingActionButton bmi_logsfab;

        TextView displayname;
        TextView displayheight;
        TextView displayweight;
        TextView displaybmi;
        private ViewGroup root;

        private int tooltipColor;
        private int tooltipSize;
        private int tooltipPadding;

        private int tipSizeSmall;
        private int tipSizeRegular;
        private int tipRadius;


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_line_column_dependency, container, false);
            weightlist = new ArrayList<>();
            bmilist = new ArrayList<>();
            datetimelist = new ArrayList<>();
            db = new DBHandler(getActivity());
            // *** TOP LINE CHART ***

            menumainred = (FloatingActionMenu) rootView.findViewById(R.id.menu_red);
            home_fab =(FloatingActionButton) rootView.findViewById(R.id.home) ;
            delete_graphfab = (FloatingActionButton) rootView.findViewById(R.id.delete);

          enteredit_fab = (FloatingActionButton) rootView.findViewById(R.id.enter_edit_data) ;
            chartTop = (LineChartView) rootView.findViewById(R.id.chart_top);
            weightbottomgraph = (LineChartView) rootView.findViewById(R.id.chart_weight);
            sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            displayname =(TextView) rootView.findViewById(R.id.displayname);
            displayheight = (TextView) rootView.findViewById(R.id.display_height);
            displayweight =(TextView) rootView.findViewById(R.id.display_weight);
            displaybmi = (TextView) rootView.findViewById(R.id.display_bmi);
            bmi_logsfab =(FloatingActionButton) rootView.findViewById(R.id.bmi_logs);


            // Generate and set data for line chart
            //   generateInitialLineData();


            displayname.setText(sharedPref.getString(getString(R.string.metric_user_name),"User"));
           // bmi_note.setText(sharedPref.getString(getString(R.string.display_note),"normal"));
            displayheight.setText(sharedPref.getString(getString(R.string.display_height),"0"));
            displayweight.setText(sharedPref.getString(getString(R.string.display_weight),"0"));
            displaybmi.setText(sharedPref.getString(getString(R.string.display_bmi),"0.0"));

            try {
                // Reading all shops
                Log.d("Reading:", "Reading all Logs.");
                List<BmiLogs> logs = db.getAllShops();

                for (BmiLogs log : logs) {

                    weightlist.add(log.getWeight());
                    bmilist.add(log.getBmi());
                    datetimelist.add(log.getDateTime());

// Writing shops to log
                    Log.d("BMILO: : ", "weight:" + log.getWeight() + "  BMI:" + log.getBmi() + "  DateTime:" + log.getDateTime());

                }

            } catch (Exception e) {
                Log.i("Thisexcept", e.toString());
            }

            Log.i("LastEntry", "date" + db.getLast().getDateTime() + "weight" + db.getLast().getWeight() + "bmi" + db.getLast().getBmi());

            generateBMIGraph(datetimelist, bmilist);
            generateWeightGraph(datetimelist, weightlist);
            chartTop.setOnValueTouchListener(new ValueTouchListener_bmi());
            weightbottomgraph.setOnValueTouchListener(new ValueTouchListener_w());

            // *** BOTTOM COLUMN CHART ***
            //    generateLineData(ChartUtils.COLOR_GREEN, 80);
            // chartBottom = (ColumnChartView) rootView.findViewById(R.id.chart_bottom);

            //  generateColumnData();
            delete_graphfab.setOnClickListener(clickListener);
            home_fab.setOnClickListener(clickListener);
            enteredit_fab.setOnClickListener(clickListener);
            bmi_logsfab.setOnClickListener(clickListener);

            return rootView;
        }


        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.enter_edit_data:
                    {
                     //   SharedPreferences SP = getSharedPreferences(MetricSettings, Context.MODE_PRIVATE);
                       // metrics = SP.getInt(getString(R.string.metric_settings),0);


                        Intent i = new Intent(getActivity(), BasicSettings.class);
                        startActivity(i);

                    }
                    break;
                    case R.id.home: {

                        Intent bmiact = new Intent(getActivity(),BmiChart.class );
                        Intent bmiactyellow = new Intent(getActivity(),BmiChartyellow.class );
                        Intent bmiactorange = new Intent(getActivity(),BmiChartRed.class );
                        Intent bmiactred = new Intent(getActivity(),BmiChartOrange.class );
                        Intent bmiactbloodred = new Intent(getActivity(),BmiChartBloodRed.class );
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
                    }
                        break;
                    case R.id.delete:
                        {

                            new LovelyStandardDialog(getActivity())
                                    .setTopColorRes(R.color.accent)
                                    .setButtonsColorRes(R.color.accent)

                                    .setTitle("Warning!!")
                                    .setMessage(" This will delete all stored data,This cannot be undone")
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getActivity(), "positive clicked", Toast.LENGTH_SHORT).show();
                                            db.deleteEntry();
                                            Intent i = new Intent(getActivity(), BmiChart.class);
                                            startActivity(i);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .show();


                    }
                    break;
                    case R.id.bmi_logs :
                        Intent it = new Intent(getActivity(), LogActivity.class);
                        startActivity(it);
                        break;

                }
            }
        };
        private void generateColumnData() {

            int numSubcolumns = 1;
            int numColumns = months.length;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
                }

                axisValues.add(new AxisValue(i).setLabel(months[i]));

                columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }

            columnData = new ColumnChartData(columns);

            columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

            chartBottom.setColumnChartData(columnData);

            // Set value touch listener that will trigger changes for chartTop.
            // chartBottom.setOnValueTouchListener(new ValueTouchListener());

            // Set selection mode to keep selected month column highlighted.
            chartBottom.setValueSelectionEnabled(true);

            chartBottom.setZoomType(ZoomType.HORIZONTAL);

            // chartBottom.setOnClickListener(new View.OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // SelectedValue sv = chartBottom.getSelectedValue();
            // if (!sv.isSet()) {
            // generateInitialLineData();
            // }
            //
            // }
            // });

        }

        /**
         * Generates initial data for line chart. At the begining all Y values are equals 0. That will change when user
         * will select value on column chart.
         */

        private void generateBMIGraph(ArrayList<String> datetimelist, ArrayList<String> bmilist) {

            String[] datetimetoarray = new String[datetimelist.size()];
            datetimetoarray = datetimelist.toArray(datetimetoarray);

            String[] bmitoArray = new String[bmilist.size()];
            bmitoArray = bmilist.toArray(bmitoArray);


            int numValues = datetimetoarray.length;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {


                if ((Float.parseFloat(bmitoArray[i]) >=80)){


                    values.add(new PointValue(i, Float.parseFloat("80")).setLabel(datetimetoarray[i]));

                }
                else {
                    values.add(new PointValue(i, Float.parseFloat(bmitoArray[i])).setLabel(datetimetoarray[i]));

                }
                axisValues.add(new AxisValue(i).setLabel(datetimetoarray[i]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_ORANGE).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

            chartTop.setLineChartData(lineData);

            // For build-up animation you have to disable viewport recalculation.
            chartTop.setViewportCalculationEnabled(false);

            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(0, 80, 6, 0);
            chartTop.setMaximumViewport(v);
            chartTop.setCurrentViewport(v);

            chartTop.setZoomType(ZoomType.HORIZONTAL);

        }

        private void generateWeightGraph(ArrayList<String> datetimelist, ArrayList<String> weightlist) {

            String[] datetimetoarray = new String[datetimelist.size()];
            datetimetoarray = datetimelist.toArray(datetimetoarray);

            String[] weightlisttoarray = new String[weightlist.size()];
            weightlisttoarray = weightlist.toArray(weightlisttoarray);


            int numValues = datetimetoarray.length;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {

                if(Float.parseFloat(weightlisttoarray[i])>=500){

                    values.add(new PointValue(i, Float.parseFloat("500")).setLabel(datetimetoarray[i]));
                }
                else{
                    values.add(new PointValue(i, Float.parseFloat(weightlisttoarray[i])).setLabel(datetimetoarray[i]));
                }


                axisValues.add(new AxisValue(i).setLabel(datetimetoarray[i]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_ORANGE).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            lineDataforweight = new LineChartData(lines);
            lineDataforweight.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            lineDataforweight.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

            weightbottomgraph.setLineChartData(lineDataforweight);

            // For build-up animation you have to disable viewport recalculation.
            weightbottomgraph.setViewportCalculationEnabled(false);

            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(0, 500, 6, 0);
            weightbottomgraph.setMaximumViewport(v);
            weightbottomgraph.setCurrentViewport(v);

            weightbottomgraph.setZoomType(ZoomType.HORIZONTAL);

        }

        private void generateInitialLineData() {
            int numValues = 7;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {
                values.add(new PointValue(i, 0));
                axisValues.add(new AxisValue(i).setLabel(days[i]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

            chartTop.setLineChartData(lineData);

            // For build-up animation you have to disable viewport recalculation.
            chartTop.setViewportCalculationEnabled(false);

            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(0, 110, 6, 0);
            chartTop.setMaximumViewport(v);
            chartTop.setCurrentViewport(v);

            chartTop.setZoomType(ZoomType.HORIZONTAL);

        }

        private void generateLineData(int color, float range) {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            line.setColor(color);
            for (PointValue value : line.getValues()) {
                // Change target only for Y value.
                value.setTarget(value.getX(), (float) Math.random() * range);
            }

            // Start new data animation with 300ms duration;
            chartTop.startDataAnimation(300);
        }
/*
        private class ValueTouchListener implements ColumnChartOnValueSelectListener {

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                generateLineData(value.getColor(), 100);
            }

            @Override
            public void onValueDeselected() {

                generateLineData(ChartUtils.COLOR_GREEN, 0);

            }
        }*/


             private class ValueTouchListener_w  implements LineChartOnValueSelectListener {

            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                //Toast.makeText(getActivity(), "" + new String(value.getLabel())+"bmi :"+value.getY(), Toast.LENGTH_SHORT).show();



                new SimpleTooltip.Builder(getActivity())
                        .anchorView(weightbottomgraph)
                        .text(new String(value.getLabel())+"  weight :"+value.getY())
                        .gravity(Gravity.END)
                        .animated(true)
                        .modal(true)
                        .backgroundColor(getResources().getColor(R.color.primary))
                        .arrowColor(getResources().getColor(R.color.primary))
                         .textColor(getResources().getColor(R.color.cpb_white))



                        .build()
                        .show();
            }

                 @Override
                 public void onValueDeselected() {
                     // TODO Auto-generated method stub

                 }

        }
            private class ValueTouchListener_bmi implements LineChartOnValueSelectListener {

                @Override
                public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                    //Toast.makeText(getActivity(), "" + new String(value.getLabel())+"bmi :"+value.getY(), Toast.LENGTH_SHORT).show();

                    new SimpleTooltip.Builder(getActivity())
                            .anchorView(chartTop)
                            .text(new String(value.getLabel())+" bmi :"+value.getY())
                            .gravity(Gravity.END)
                            .textColor(getResources().getColor(R.color.cpb_white))
                            .animated(true)
                            .modal(true)
                            .backgroundColor(getResources().getColor(R.color.primary))
                            .arrowColor(getResources().getColor(R.color.primary))
                            .build()
                            .show();


                }


                @Override
                public void onValueDeselected() {
                    // TODO Auto-generated method stub

                }

            }
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
                sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                Intent bmiact = new Intent(LineColumnDependencyActivity.this,BmiChart.class );
                Intent bmiactyellow = new Intent(LineColumnDependencyActivity.this,BmiChartyellow.class );
                Intent bmiactorange = new Intent(LineColumnDependencyActivity.this,BmiChartRed.class );
                Intent bmiactred = new Intent(LineColumnDependencyActivity.this,BmiChartOrange.class );
                Intent bmiactbloodred = new Intent(LineColumnDependencyActivity.this,BmiChartBloodRed.class );
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        // mCurrentSelectedPosition = 0;
                    String temp =    sharedPref.getString(getString(R.string.display_bmi),"0.0");
                        Double tempbmi = Double.parseDouble(temp);

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

                       // Intent iv = new Intent(LineColumnDependencyActivity.this, BmiChart.class);
                       // startActivity(iv);
                        break;
                    case R.id.navigation_item_2:
                        //  mCurrentSelectedPosition = 1;
                        Intent i = new Intent(LineColumnDependencyActivity.this, BasicSettings.class);
                        startActivity(i);

                        break;
                    case R.id.navigation_item_3:
                        //    mCurrentSelectedPosition = 2;
                        Intent j = new Intent(LineColumnDependencyActivity.this, LogActivity.class);
                        startActivity(j);
                        break;
                    case R.id.navigation_item_4:
                        //   mCurrentSelectedPosition = 3;
                        new LovelyStandardDialog(LineColumnDependencyActivity.this)
                                .setTopColorRes(R.color.accent)
                                .setButtonsColorRes(R.color.accent)

                                .setTitle("Warning!!")
                                .setMessage(" This will delete all stored data,This cannot be undone")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(LineColumnDependencyActivity.this, "positive clicked", Toast.LENGTH_SHORT).show();
                                        db.deleteEntry();
                                        Intent i = new Intent(LineColumnDependencyActivity.this, BmiChart.class);
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .show();


                        break;
                    case R.id.navigation_item_5:
                        Intent about = new Intent (LineColumnDependencyActivity.this, About.class);
                        startActivity(about);

                        break;
                    case R.id.navigation_item_6:
                        Intent js = new Intent(LineColumnDependencyActivity.this, LineColumnDependencyActivity.class);
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

