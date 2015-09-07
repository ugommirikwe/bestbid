package io.ugommirikwe.auctions.views;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.ugommirikwe.auctions.R;

public class WelcomeActivity extends AppCompatActivity {

    @Bind(R.id.pgr_signin_signup)
    protected ViewPager viewPager;

    ViewPagerAdapter adapter;
    CharSequence Titles[] = {"Sign In", "Sign Up"};
    int numbOfTabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, numbOfTabs);

        // Preserves state of content in each screen/page as user moves among them
        viewPager.setOffscreenPageLimit(numbOfTabs);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_signin_signup);
        tabLayout.setupWithViewPager(viewPager);
        /*tabLayout.getTabAt(0).setIcon(R.drawable.abc_ic_menu_selectall_mtrl_alpha);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_add_person);*/
    }


    /**
     * Custom FragmentStatePagerAdapter for dynamically loading the SignIn and SignUp screens dynamically
     */
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        CharSequence Titles[];

        // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
        int NumbOfTabs;

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
        }

        /**
         * Returns the fragment for the every position in the View Pager
         * @param position Numeric position of fragment as represented by the Tabs for accessing them.
         */
        @Override
        public Fragment getItem(int position) {

            Fragment tab = null;

            switch (position) {
                case 0:
                    tab = new LoginFragment();
                    break;
                case 1:
                    tab = new RegisterFragment();
                    break;
            }

            return tab;
        }

        // This method return the titles for the Tabs in the Tab Strip
        @Override
        public CharSequence getPageTitle(int position) {
            /*Drawable icon = getResources().getDrawable(iconResId[position]);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(icon, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;*/
            return Titles[position];
            //return "";
        }

        // This method return the Number of tabs for the tabs Strip
        @Override
        public int getCount() {
            return NumbOfTabs;
        }

    }
}
