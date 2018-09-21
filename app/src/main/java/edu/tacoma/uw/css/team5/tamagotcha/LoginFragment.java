package edu.tacoma.uw.css.team5.tamagotcha;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Main fragment for handling the user logins
 *
 * @author Conor Marsten
 */
public class LoginFragment extends Fragment implements UserAccountCallBack {
    /* Fragment interaction listener */
    LoginInteractionListener mListener;
    /* Keep track of the login task */
    private UserAccountTask mAuthTask = null;
    /* Email edit text */
    private android.widget.AutoCompleteTextView mEmailView;
    /* Password edit text */
    private EditText mPasswordView;
    /* The remember chekbox to save user login info */
    private CheckBox mSaveLoginCheck;

    /**
     * Required empty public constructor
     */
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Make the fragment
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    /**
     * onCreate method
     *
     * @param savedInstanceState Saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * onCreateView method
     *
     * @param inflater Inflater
     * @param container Container view
     * @param savedInstanceState Saved instance state
     * @return returns the fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Load in the saved user info if it is there
        android.content.SharedPreferences pref = getContext().getSharedPreferences("tamagotcha", 0);
        SharedPreferences.Editor editor = pref.edit();
        // Load the activity if the user wants to be kept logged in
        if (pref.getString("email", null) != null &&
                pref.getBoolean("loggedIn", false)) {
            Intent intent = new Intent(this.getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            // Reset the info if not
            editor.putString("email", null);
            editor.putBoolean("loggedIn", false);
            editor.apply();
        }

        // Set up checkbox
        mSaveLoginCheck = v.findViewById(R.id.save_login_checkbox);
        // Set up the login form.
        mEmailView = v.findViewById(R.id.email);

        // Edit text for the password
        mPasswordView = v.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // Opens the create account activity
        android.widget.Button createAccountButton = v.findViewById(R.id.register_account_button);
        createAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.sign_in_container, new RegisterFragment() )
                        .commit();
            }
        });

        // Runs the aSyncTask for login verification
        Button signInButton = v.findViewById(R.id.email_sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Don't allow attempt if one is ongoing
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!android.text.TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Create a parameter bundle to send out to the endpoint
            HashMap<String, String> params = new HashMap<>();
            params.put("username", mEmailView.getText().toString());
            params.put("password", mPasswordView.getText().toString());
            params.put("url", "https://tamagotcha.herokuapp.com/user");
            params.put("type", "login");
            params.put("isChecked", "" + mSaveLoginCheck.isChecked());

            mAuthTask = new UserAccountTask(params, this);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Simple email verification checks for an @
     *
     * @param email Email string
     * @return Boolean, true if valid, false if not
     */
    private boolean isEmailValid(String email) {
        // Add simple email regex if need be
        return email.contains("@");
    }

    /**
     * Simple password validator checks if password is 8 chars
     *
     * @param password Password string
     * @return Boolean, true is valid, false if not
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    /**
     * Callback for userAccountTask doInBackground to add and remove the loading screens
     */
    @Override
    public void userAccountCallBackDoInBackground() {
        // Make the loading view appear here
        loadingScreen(true);
    }

    /**
     * Callback for userAccountTask onPostExecute method
     *
     * @param response The JSON response we recieved back from the endpoint
     * @throws JSONException If JSON is malformed
     */
    @Override
    public void userAccountCallBackOnPostExecute(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        loadingScreen(false);
        // Verfied login so start the main activity
        if (jsonResponse.getString("Status").equals("OK")) {
            // Launch the main activity
            Intent intent = new Intent(this.getContext(), MainActivity.class);
            startActivity(intent);

            // Save the user preferences
            SharedPreferences pref = this.getContext().getSharedPreferences("tamagotcha", 0);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("email", mEmailView.getText().toString());
            // If the remember me is checked
            if(mSaveLoginCheck.isChecked()) {
                // Put the email and password
                editor.putBoolean("loggedIn", true);
            }
            // Apply the changes and finish activity
            editor.apply();
            getActivity().finish();
        // Login not successful
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
    }

    /**
     * If the task is cancelled reset it and remove the loading screen
     */
    @Override
    public void userAccountCallBackOnCancelled() {
        mAuthTask = null;
        loadingScreen(false);
    }

    /**
     * Make sure we have the listener implemented
     *
     * @param context Fragment context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginInteractionListener) {
            mListener = (LoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Fragment interaction listener
     */
    public interface LoginInteractionListener {
        void loginInteraction(boolean b);
    }

    /**
     * Fragment interaction method
     *
     * @param b Boolean for loading screen
     */
    public void loadingScreen(boolean b) {
        if (mListener != null) {
            mListener.loginInteraction(b);
        }
    }
}
