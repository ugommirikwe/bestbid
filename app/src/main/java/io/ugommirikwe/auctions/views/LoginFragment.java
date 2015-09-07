package io.ugommirikwe.auctions.views;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.ugommirikwe.auctions.R;
import io.ugommirikwe.auctions.util.AuctionsDBContract;
import io.ugommirikwe.auctions.util.AuctionsDbHelper;
import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;

import static io.ugommirikwe.auctions.util.AuctionsDBContract.*;

public class LoginFragment extends Fragment {

    private AuctionsDbHelper mAuctionsDbHelper;
    private SQLiteDatabase mSQlLiteDatabase;

    SharedPreferences prefs;
    String prefName = "User";

    @Bind(R.id.txb_email_login)
    public EditText txbEmail;

    @Bind(R.id.txv_email_validation_login)
    public TextView txvEmailValidation;

    @Bind(R.id.txb_password_login)
    public EditText txbPassword;

    @Bind(R.id.txv_password_validation_login)
    public TextView txvPasswordValidation;

    @Bind(R.id.btn_login)
    public Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        initDB();

        setUpSignInFormValidation();

        return view;
    }

    private void initDB() {
        mAuctionsDbHelper = new AuctionsDbHelper(getActivity());
    }

    private void setUpSignInFormValidation() {
        final Pattern emailPattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        // Validate email
        Observable<Boolean> emailValid = WidgetObservable.text(txbEmail)
                .map(OnTextChangeEvent::text)
                .map(t -> emailPattern.matcher(t).matches());

        emailValid
                .distinctUntilChanged()
                .doOnNext(b -> txvEmailValidation.setText(b ? "" : "Invalid email address."))
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(txbEmail::setTextColor);

        // Set validation rule for length of password
        Observable<Boolean> passwordValid = WidgetObservable.text(txbPassword)
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() > 6);

        // Run the validation
        passwordValid
                .distinctUntilChanged()
                .doOnNext(b -> txvPasswordValidation.setText(b ? "" : "Password is required and should be more than 6 characters in length."))
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(txbPassword::setTextColor);

        Observable<Boolean> signUpButtonEnabled =
                Observable.combineLatest(emailValid, passwordValid, (a, b) -> a && b);

        signUpButtonEnabled
                .distinctUntilChanged()
                .subscribe(btnLogin::setEnabled);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSQlLiteDatabase = mAuctionsDbHelper.getWritableDatabase();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSQlLiteDatabase.close();
    }

    @OnClick({R.id.btn_login})
    public void signInButtonClick(View btn) {
        ContentValues contentValues = new ContentValues(3);
        contentValues.put("email", txbEmail.getText().toString());
        contentValues.put("password", txbPassword.getText().toString());

        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
            User._ID,
            User.COLUMN_NAME_FULLNAME
        };


        String selectQuery = "SELECT  * FROM " + User.TABLE_NAME + " WHERE "
                + User.COLUMN_NAME_EMAIL + " = '" + txbEmail.getText().toString() + "' AND "
                + User.COLUMN_NAME_PASSWORD + " = '" + txbPassword.getText().toString() + "'";

        //Cursor c = mSQlLiteDatabase.query(true, User.TABLE_NAME, projection, User.COLUMN_NAME_EMAIL + " = '" + txbEmail.getText().toString() + "'", null, null, null, null, null);

        Cursor c = mSQlLiteDatabase.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
            int matches = c.getCount();
            if (matches > 0) {
                String fullname = c.getString(c.getColumnIndex(User.COLUMN_NAME_FULLNAME));
                // Save user email in SharedPrefs
                SharedPreferences sharedPref = getActivity().getSharedPreferences(prefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("user", fullname);
                //editor.apply();
                editor.commit();
                c.close();
                Snackbar.make(getView(), "Login Successful", Snackbar.LENGTH_LONG).show();

                // Navigate to AuctionsActivity
                Intent i = new Intent(getActivity(), AuctionsActivity.class);

                // set the new task and clear flags
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            else {
                Snackbar.make(getView(), "Login Failed", Snackbar.LENGTH_LONG).setAction("OK", v -> {
                    // Clear Snackbar
                }).show();
            }
        }
        else {
            Snackbar.make(getView(), "Database Error", Snackbar.LENGTH_LONG).setAction("OK", v -> {
                // Clear Snackbar
            }).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
