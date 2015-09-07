package io.ugommirikwe.auctions.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.ugommirikwe.auctions.R;

public class AuctionsActivity extends BaseActivity {
    SharedPreferences prefs;
    String prefName = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_auctions, (ViewGroup) findViewById(R.id.frm_view_layout));

        ButterKnife.bind(this);

        // Check if SharedPrefs has a "user" entry, and if not launch WelcomeActivity, so user can go signin/signup
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        String user = prefs.getString("user", "");
        if (user == "") startActivity(new Intent(this, WelcomeActivity.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auctions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help_auctions) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
