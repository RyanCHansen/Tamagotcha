package edu.tacoma.uw.css.team5.tamagotcha;

import org.json.JSONException;

/**
 * Interface to handle the task callbacks
 *
 * @author Conor Marsten
 */
public interface UserAccountCallBack {
    /**
     * Callback for doInBackground
     */
    void userAccountCallBackDoInBackground();

    /**
     * Callback for onPostExecute
     *
     * @param response JSON get response
     * @throws JSONException if JSON malformed
     */
    void userAccountCallBackOnPostExecute(String response) throws JSONException;

    /**
     * Callback for onCancelled
     */
    void userAccountCallBackOnCancelled();
}