package io.ugommirikwe.auctions.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ugommirikwe.auctions.R;

public class BaseActivity extends AppCompatActivity {
    private static final String STATE_SELECTED_POSITION = "state_selected_position";

    private static final String PREFERENCES_FILE = "auctions_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "pref_user_learned_drawer";

    private int mCurrentSelectedPosition;
    private boolean mFromSavedInstanceState = false;
    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    protected Toolbar mToolbar;
    protected ActionBar mActionBar;

    SharedPreferences prefs;
    String prefName = "User";

    @Bind(R.id.txv_nav_drawer_header_title)
    public TextView txvNavDrawerHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition =
                    savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        ButterKnife.bind(this);

        setUpToolbar();
        setUpNavDrawer();
        setUpNavigationViewItemsClickHandler();

        // TODO: Display user's name in NavDrawer menu// Check if SharedPrefs has a "user" entry, and if not launch WelcomeActivity, so user can go signin/signup
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        String user = prefs.getString("user", "");
        if (user != "") txvNavDrawerHeaderTitle.setText(user);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mToolbar != null) {
            mActionBar = getSupportActionBar();
            mActionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });

            Boolean mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));
            if (!mUserLearnedDrawer) {
                mDrawerLayout.openDrawer(GravityCompat.START);
                mUserLearnedDrawer = true;
                saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, mUserLearnedDrawer.toString());
            }
        }
    }

    private void setUpNavigationViewItemsClickHandler() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view_main);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                View mainLayout = findViewById(R.id.frm_view_layout);
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.mnu_my_profile:
                        // TODO: Launch the My Profile Activity/Screen
                        Snackbar.make(mainLayout, "My Profile",
                                Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 0;
                        return true;

                    case R.id.mnu_my_auctions:
                        Snackbar.make(mainLayout, "My Auctions",
                                Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 1;
                        return true;

                    case R.id.mnu_my_bids:
                        Snackbar.make(mainLayout, "My Bids",
                                Snackbar.LENGTH_SHORT).show();
                        mCurrentSelectedPosition = 2;
                        return true;

                    case R.id.mnu_signout:
                        mCurrentSelectedPosition = 3;

                        // Remove user from SharedPrefs
                        SharedPreferences sharedPref = getSharedPreferences(prefName, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("user", "");
                        editor.commit();

                        // Navigate to WelcomeActivity
                        Intent i = new Intent(BaseActivity.this, WelcomeActivity.class);

                        startActivity(i);
                        finish();
                        return true;

                    default:
                        return true;
                }
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }
}
