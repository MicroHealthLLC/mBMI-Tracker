package com.microhealthllc.mbmicalc.ui;

import android.app.Activity;
import android.os.Bundle;

import com.microhealthllc.mbmicalc.DummyContent;
import com.microhealthllc.mbmicalc.R;

import static com.microhealthllc.mbmicalc.log.LogUtil.makeLogTag;


/**
 * The base class for all Activity classes.
 * This class creates and provides the navigation drawer and toolbar.

 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public abstract class BaseActivity extends Activity {
    private static final String TAG = makeLogTag(BaseActivity.class);





    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dum);
        setupDetailFragment();
    }
    /**
     * Sets up the navigation drawer.
     */

    private void setupDetailFragment() {
       // BMI_fragment fragment =  BMI_fragment.newInstance(DummyContent.ITEMS.get(0).id);
        //getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }



    /**
     * Creates the item click listener.
     * @param navigationView the navigation view
     */


    /**
     * Handles the navigation item click.
     * @param itemId the clicked item
     */


    /**
     * Handles the navigation item click and starts the corresponding activity.
     * @param item the selected navigation item
     */


    /**
     * Provides the action bar instance.
     * @return the action bar.
     */



    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * have to override this method.
     */



    public abstract boolean providesActivityToolbar();


}
