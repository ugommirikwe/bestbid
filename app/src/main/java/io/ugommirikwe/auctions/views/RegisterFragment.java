package io.ugommirikwe.auctions.views;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.ugommirikwe.auctions.R;
import io.ugommirikwe.auctions.util.AuctionsDbHelper;
import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;

import static io.ugommirikwe.auctions.util.AuctionsDBContract.User;

public class RegisterFragment extends Fragment {

    private AuctionsDbHelper mAuctionsDbHelper;
    private SQLiteDatabase mSQlLiteDatabase;

    @Bind(R.id.txb_fullname_register)
    public EditText txbFullName;

    @Bind(R.id.txv_fullname_validation_message)
    public TextView txvFullNameValidation;

    @Bind(R.id.txb_email_register)
    public EditText txbEmail;

    @Bind(R.id.txv_email_validation_message)
    public TextView txvEmailValidation;

    @Bind(R.id.txb_password_register)
    public EditText txbPassword;

    @Bind(R.id.txv_password_validation_message)
    public TextView txvPasswordValidation;

    @Bind(R.id.txb_password_retype_register)
    public EditText txbPasswordRetype;

    @Bind(R.id.txv_password_retype_validation_message)
    public TextView txvPasswordRetypeValidation;

    @Bind(R.id.btn_signup)
    public Button btnSignUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this, view);

        initDB();

        setUpSignFormValidation();

        return view;
    }

    private void initDB() {
        mAuctionsDbHelper = new AuctionsDbHelper(getActivity());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setUpSignFormValidation() {
        final Pattern emailPattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        // Set validation rule for length of full name
        Observable<Boolean> fullNameValid = WidgetObservable.text(txbFullName)
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() > 4);

        // Run the validation
        fullNameValid
                .distinctUntilChanged()
                .doOnNext(b -> txvFullNameValidation.setText(b ? "" : "Full name is required and should be more than 4 characters in length."))
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(txbFullName::setTextColor);

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

        // Set validation rule to ensure retyped password matches
        Observable<Boolean> passwordRetypeValid = WidgetObservable.text(txbPasswordRetype)
                .map(OnTextChangeEvent::text)
                .map(t -> Objects.equals(t.toString(), txbPassword.getText().toString()));

        // Run the validation
        passwordRetypeValid
                .distinctUntilChanged()
                .doOnNext(b -> txvPasswordRetypeValidation.setText(b ? "" : "Retyped password must be the same as password above."))
                .map(b -> b ? Color.BLACK : Color.RED)
                .subscribe(txbPasswordRetype::setTextColor);

        Observable<Boolean> signUpButtonEnabled =
                Observable.combineLatest(fullNameValid, emailValid, passwordValid, passwordRetypeValid,
                        (a, b, c, d) -> a && b && c && d);

        signUpButtonEnabled
                .distinctUntilChanged()
                .subscribe(btnSignUp::setEnabled);
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

    @OnClick({R.id.btn_signup})
    public void signUpButtonClick(View btn) {
        ContentValues contentValues = new ContentValues(3);
        contentValues.put("fullname", txbFullName.getText().toString());
        contentValues.put("email", txbEmail.getText().toString());
        contentValues.put("password", txbPassword.getText().toString());
        long insert = mSQlLiteDatabase.insert(User.TABLE_NAME, null, contentValues);

        if (insert == 1) {
            Snackbar.make(getView(), "Register successful", Snackbar.LENGTH_LONG)/*.setAction("OK", new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //your action here
                }
            })*/.show();

            // TODO: navigate to AuctionsActivity
        }
        else
            Snackbar.make(getView(), "Register failed", Snackbar.LENGTH_LONG).show();
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
