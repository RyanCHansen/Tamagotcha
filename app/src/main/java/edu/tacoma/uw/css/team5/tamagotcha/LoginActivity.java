package edu.tacoma.uw.css.team5.tamagotcha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * The app's login screen. Users login information is verified by flask deployed on heroku.
 *
 * @author Conor Marsten
 */
public class LoginActivity extends AppCompatActivity
        implements RegisterFragment.RegisterInteractionListener,
        LoginFragment.LoginInteractionListener {

    /* Keep track of the login task */
    private UserAccountTask mAuthTask = null;
    /* Email edit text */
    private AutoCompleteTextView mEmailView;
    /* Password edit text */
    private EditText mPasswordView;
    /* The loading screen view animation */
    private View mProgressView;
    /* The login screen view */
    private View mLoginFormView;
    /* The remember chekbox to save user login info */
    private CheckBox saveLoginCheck;
    /* Loading screen fragment so we don't lose it */
    private LoadingScreenFragment mLoadingFragment;


    /**
     * onCreate method. Initializes the fragment container for sign in fragments.
     *
     * @param savedInstanceState Saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.sign_in_container, new LoginFragment() )
                .commit();
    }

    /**
     * Adds the progress fragment into the container. Also removes it.
     *
     * @param b Show progress fragment if true
     */
    public void showProgress(boolean b) {
        if (b) {
            // Add progress view fragment
            mLoadingFragment = new LoadingScreenFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sign_in_container,  mLoadingFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            // Remove progress view fragment
            getSupportFragmentManager().beginTransaction()
                    .remove(mLoadingFragment)
                    .commit();
        }
    }

    /**
     * Login fragment interface to add/remove loading screen
     *
     * @param b Boolean for progress fragment
     */
    @Override
    public void loginInteraction(boolean b) {
        showProgress(b);
    }

    /**
     * Register fragment interface to add/remove loading screen
     *
     * @param b Boolean for progress fragment
     */
    @Override
    public void registerInteraction(boolean b) {
        showProgress(b);
    }
}

