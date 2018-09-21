/**
 * Logout Fragment used to verify that the user actually
 * wants to log out of the app.
 *
 * @author Ryan Hansen
 */

package edu.tacoma.uw.css.team5.tamagotcha;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Logout Fragment used to verify that the user actually
 * wants to log out of the app.
 */
public class LogoutFragment extends Fragment {

    private AppCompatImageButton confirmLogoutButton;
    private AppCompatImageButton cancelLogoutButton;
    private AppCompatImageButton exitButton;
    private Fragment LogoutFragment;

    public LogoutFragment() {
        
    }


    /**
     * Populates the fragment with the buttons associated with it
     * and creates their OnClick listeners.
     *
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        LogoutFragment = this;

        confirmLogoutButton = view.findViewById(R.id.confirm_logout_button);
        confirmLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = view.getContext().getSharedPreferences("tamagotcha", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("email", null);
                editor.putBoolean("loggedIn", false);
                editor.commit();

                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        cancelLogoutButton = view.findViewById(R.id.cancel_logout_button);
        cancelLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .remove(LogoutFragment)
                        .commit();
            }
        });

        exitButton = view.findViewById(R.id.exit_button2);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .remove(LogoutFragment)
                        .commit();
            }
        });

        return view;

    }

}
