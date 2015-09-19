package se.aleer.smarthome;

import android.app.ActionBar;
import android.app.TabActivity;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TabHost;

public class RemoteControl extends AppCompatActivity implements SwitchListFragment.OnEditSwitchListener, SwitchManagerFragment.OnManagedSwitchListener {

    private static final String TAG = "RemoteControl";

    private Fragment contentFragment;
    SwitchListFragment switchListFragment;
    SettingFragment settingFragment;
    SwitchManagerPopup mAddSwitchPopup;

    // If edit-switch mode selected in SwitchListFragment this is runs
    public void onEditSwitch(Switch swtch)
    {
        // #### Switch fragment from SwitchListFragment to SwitchManagerFragment ####
        SwitchManagerFragment managerFragment = (SwitchManagerFragment)
                getFragmentManager().findFragmentByTag(SwitchManagerFragment.ARG_ITEM_ID);

        if (managerFragment == null) {
            // Create fragment
            managerFragment = new SwitchManagerFragment();
        }
        // Set switch so that it can be edited
        managerFragment.setSwitch(swtch);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever it is in the fragment container with this one...
        transaction.replace(R.id.content_frame, managerFragment, SwitchManagerFragment.ARG_ITEM_ID);
        // Add transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // If SwitchManagerFragment needs update switch in SwitchListFragment this is executed.
    public void onManagedSwitch(Switch swtch, boolean remove){
        // Remove SwitchManagerFragment from stack so its clean to the next time..
        getFragmentManager().popBackStack();
        // #### Switch fragment from SwitchListFragment to SwitchManagerFragment ####
        SwitchListFragment listFragment = (SwitchListFragment)
                getFragmentManager().findFragmentByTag(SwitchListFragment.ARG_ITEM_ID);

        if (listFragment == null) {
            // Create fragment
            listFragment = new SwitchListFragment();
        }
        // Set switch so that it can be edited
        listFragment.manageManagedSwitch(swtch, remove);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever it is in the fragment container with this one...
        transaction.replace(R.id.content_frame, listFragment, SwitchListFragment.ARG_ITEM_ID);
        // Add transaction to the back stack so the user can navigate back
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SwitchListFragment switchListFragment = new SwitchListFragment();
        fragmentTransaction.add(R.id.content_frame, switchListFragment, SwitchListFragment.ARG_ITEM_ID);
        fragmentTransaction.commit();

        /** Adding tabs src: */
        //mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        //mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        //mTabHost.addTab(mTabHost.newTabSpec("Switches").setIndicator("Switches"), SwitchListFragment.class, null);
        //mTabHost.addTab(mTabHost.newTabSpec("Settings").setIndicator("Tab 2"), Settings.class, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*if (contentFragment instanceof SettingFragment) {
            outState.putString("content", SettingFragment.ARG_ITEM_ID);
        } else {*/
        outState.putString("content", SwitchListFragment.ARG_ITEM_ID);
        //}
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remote_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "Stack count: " + getFragmentManager().getBackStackEntryCount());
                getFragmentManager().popBackStack();
                Log.d(TAG, "Stack count: " + getFragmentManager().getBackStackEntryCount());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
    public void hideUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(false); }



    /*public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate());

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.replace(R.id.content_frame, fragment, tag);
            //Only FavoriteListFragment is added to the back stack.
            //if (!(fragment instanceof SettingFragment)) {
              //  transaction.addToBackStack(tag);
            //}
            transaction.commit();
            contentFragment = fragment;
        }
    }*/

    /*
 * We call super.onBackPressed(); when the stack entry count is > 0. if it
 * is instanceof ProductListFragment or if the stack entry count is == 0, then
 * we finish the activity.
 * In other words, from ProductListFragment on back press it quits the app.
 */
    @Override
    public void onBackPressed() {
        SwitchManagerFragment smf = (SwitchManagerFragment)getFragmentManager().findFragmentByTag(SwitchManagerFragment.ARG_ITEM_ID);
        if(smf != null && smf.isVisible()) {
            SwitchListFragment fragment = new SwitchListFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content_frame, fragment, SwitchListFragment.ARG_ITEM_ID);
            transaction.commit();
        }
        else {
            super.onBackPressed();
        }
        /*FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else if (contentFragment instanceof SwitchListFragment
                || fm.getBackStackEntryCount() == 0) {
            finish();
        }*/
    }

}