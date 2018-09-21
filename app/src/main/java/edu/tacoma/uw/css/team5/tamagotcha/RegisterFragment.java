package edu.tacoma.uw.css.team5.tamagotcha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Main fragment for handling the user registration
 *
 * @author Conor Marsten
 */
public class RegisterFragment extends Fragment implements UserAccountCallBack{
    /* Register fragment interaction listener */
    private RegisterInteractionListener mListener;
    /* Email edit text */
    private EditText mEmailView;
    /* Password edit text */
    private EditText mPasswordView;
    /* Password confirm edit text */
    private EditText mConfirmPasswordView;
    /* Pet name text */
    private EditText mPetNameView;
    /* Keep track of the create account task */
    private UserAccountTask mAuthTask = null;

    public RegisterFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        // Run register attempt
        Button registerButton = v.findViewById(R.id.create_account_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (createAccount()) {
                    attemptRegister();
                }
            }
        });

        // Initialize the text fields
        mEmailView = v.findViewById(R.id.create_account_email);
        mPasswordView = v.findViewById(R.id.create_account_password);
        mConfirmPasswordView = v.findViewById(R.id.create_account_password_confirm);
        mPetNameView = v.findViewById(R.id.create_account_pet_name);

        Button cancelButton = v.findViewById(R.id.cancel_account_registration_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        return v;
    }

    /**
     * Go back to the login screen if the user cancels
     */
    private void cancel() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.sign_in_container, new LoginFragment() )
                .commit();
    }

    /**
     * Start the register UserAccountTask
     */
    private void attemptRegister() {
        // Create the params bundle
        HashMap<String, String> params = new HashMap<>();
        params.put("username", mEmailView.getText().toString());
        params.put("password", mPasswordView.getText().toString());
        params.put("pet_name", mPetNameView.getText().toString());
        params.put("url", "https://tamagotcha.herokuapp.com/user/add");
        params.put("type", "register");

        mAuthTask = new UserAccountTask(params, this);
        mAuthTask.execute((Void) null);
    }

    /**
     * Verifies the edit text fields strings so that they are valid for creating an account
     *
     * @return Returns true if the fields are correct and returns false otherwise.
     */
    private boolean createAccount() {
        // Check length
        if (isPasswordValid()) {
            // Check that fields are the same
            if (verifyPassword()) {
                return true;
            } else {
                // Passwords were different
                mPasswordView.setError(getString(R.string.error_create_password));
                mPasswordView.requestFocus();
            }
        } else {
            // Password too short
            mPasswordView.setError(getString(R.string.error_create_password_length));
            mPasswordView.requestFocus();
        }
        return false;
    }

    /**
     * Check that the password and confirm password strings are the same
     *
     * @return Returns true if the passwords match
     */
    private boolean verifyPassword() {
        return mPasswordView.getText().toString().equals(mConfirmPasswordView.getText().toString());
    }

    /**
     * Check that the password is over 8 characters
     *
     * @return Returns true if the password is > 8
     */
    private boolean isPasswordValid() {
        return mPasswordView.length() > 7;
    }

    public void loadingScreen(boolean b) {
        if (mListener != null) {
            mListener.registerInteraction(b);
        }
    }

    /**
     * Make sure the listener is implemented
     *
     * @param context Fragment context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterInteractionListener) {
            mListener = (RegisterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * onDetach method
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * UserAccountTask callback method for doInBackground
     */
    @Override
    public void userAccountCallBackDoInBackground() {
        // Make loading view appear here
        loadingScreen(true);
    }

    /**
     * UserAccountTask callback method for onPostExecute
     *
     * @param response JSON response
     * @throws JSONException If JSON malformed
     */
    @Override
    public void userAccountCallBackOnPostExecute(String response) throws JSONException {
        loadingScreen(false);
        JSONObject jsonResponse = new JSONObject(response);

        // If the user is added successfuly start save info and load main activity
        if (jsonResponse.getString("Status").equals("User added")) {
            Intent intent = new Intent(this.getContext(), MainActivity.class);
            startActivity(intent);

            // Save the user preferences
            SharedPreferences pref = this.getContext().getSharedPreferences("tamagotcha", 0);
            SharedPreferences.Editor editor = pref.edit();

            // Put the email and password
            editor.putString("email", mEmailView.getText().toString());
            editor.putBoolean("loggedIn", true);

            editor.apply();

            getActivity().finish();
        }
    }

    /**
     * UserAccountTask callback method for onCancelled
     */
    @Override
    public void userAccountCallBackOnCancelled() {
        // Null the task and remove the loading screen
        mAuthTask = null;
        loadingScreen(false);
    }

    /**
     * Register fragment interface
     */
    public interface RegisterInteractionListener {
        void registerInteraction(boolean b);
    }
}
