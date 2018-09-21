package edu.tacoma.uw.css.team5.tamagotcha;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Represents an asynchronous login task used to send/receive user data
 *
 * @author Conor
 */
public class UserAccountTask extends AsyncTask<Void, Void, String> {

    /* Params to write to the JSON bundle */
    private final HashMap<String, String> mParams;
    /* Context to run callbacks */
    private UserAccountCallBack mCallBack;

    /**
     * Constructor for the login async task.
     */
    UserAccountTask(HashMap<String, String> params, UserAccountCallBack fragment) {
        mParams = params;
        mCallBack = fragment;
    }

    /**
     * Required doInBackground method. Put user information into a json and POSTs to the
     * heroku endpoint. Returns a status representing what happened with the user.
     * OK - User information was found and correct.
     * NF - User email was not registered.
     * BAD - User had incorrect password.
     *
     * @param p some params
     * @return Boolean, true if user login was verfied, false if not
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(Void... p) {
        mCallBack.userAccountCallBackDoInBackground();
        // Read response from POST request
        BufferedReader reader;
        String response;

        try {
            // Endpoint
            URL url = new URL(mParams.get("url"));
            // Create a connection and set it to POST
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            // Create json content package
            String content = "";

            // Get the correct post content
            switch (mParams.get("type")) {
                case "login":
                    content = loginJson();
                    break;
                case "register":
                    content = registerJson();
                    break;
                case "get_user":
                    content = userInfoJson();
                    break;
                case "post_user":
                    content = userPostJson();
            }

            if(content.equals("")) {
                return null;
            }
            // Convert to byte stream
            byte[] out = content.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            // Set how many bytes we are sending
            con.setFixedLengthStreamingMode(length);
            // Set the header to json and UTF-8
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.connect();
            // Write out to the endpoint
            try(OutputStream os = con.getOutputStream()) {
                os.write(out);
            }

            // Read the input stream into a String
            InputStream inputStream = con.getInputStream();
            StringBuilder buffer = new StringBuilder();

            // This shouldn't happen but just in case we don't get anything
            if (inputStream == null) {
                return null;
            }

            // Read the input into the string
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Read in the json and make it readable
                buffer.append(line);
                buffer.append("\n");
            }

            // This shouldn't happen but just in case there was no json
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            // Create a json object from the buffer
            response = buffer.toString();
            // debug
            // JSONObject jsonResponse = new JSONObject(response);
            // Log.e("Response: ", jsonResponse.getString("Status"));

        } catch (Exception e) {
            Log.e("API call: ", e.toString());
            return "Error";
        }

        // Status was not ok, or we errored out
        return response;
    }

    /**
     * Required onPostExecute.
     *
     * @param response doInBackground output
     */
    @Override
    protected void onPostExecute(final String response) {
        mCallBack.userAccountCallBackOnCancelled();
        if (response != null) {
            try {
                mCallBack.userAccountCallBackOnPostExecute(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Login not successful
        }
    }

    /**
     * Reset the AsyncTask, reset the loading screen
     */
    @Override
    protected void onCancelled() {
        mCallBack.userAccountCallBackOnCancelled();
    }

    /**
     * JSON data for logging in
     *
     * @return JSON post package
     */
    private String loginJson() {
        return "{\"username\":\"" + mParams.get("username") +
                "\",\"password\":\"" + mParams.get("password") +
                "\"}";
    }

    /**
     * JSON data for registering new user
     *
     * @return JSON post package
     */
    private String registerJson() {
        return "{\"username\":\"" + mParams.get("username") +
                "\",\"password\":\"" + mParams.get("password") +
                "\",\"pet_name\":\"" + mParams.get("pet_name") +
                "\"}";
    }

    /**
     * JSON data for getting user data
     *
     * @return JSON post package
     */
    private String userInfoJson() {
        return "{\"username\":\"" + mParams.get("username") +
                "\"}";
    }

    /**
     * JSON data for sending user data
     *
     * @return JSON post package
     */
    private String userPostJson() {
        return "{\"username\":\"" + mParams.get("username") +
                "\",\"last_login\":\"" + mParams.get("last_login") +
                "\",\"last_save\":\"" + mParams.get("last_save") +
                "\",\"pet_name\":\"" + mParams.get("pet_name") +
                "\",\"happiness\":\"" + mParams.get("happiness") +
                "\",\"hunger\":\"" + mParams.get("hunger") +
                "\",\"fitness\":\"" + mParams.get("fitness") +
                "\",\"status\":\"" + mParams.get("status") +
                "\",\"return_time\":\"" + mParams.get("return_time") +
                "\",\"next_hunger_tick\":\"" + mParams.get("next_hunger_tick") +
                "\",\"next_happiness_tick\":\"" + mParams.get("next_happiness_tick") +
                "\",\"next_fitness_tick\":\"" + mParams.get("next_fitness_tick") +
                "\",\"last_logout_time\":\"" + mParams.get("last_logout_time") +
                "\"}";
    }
}