/**
 * Fragment class for choosing a activity to send you pet out on
 *
 * @author Ryan Hansen
 */

package edu.tacoma.uw.css.team5.tamagotcha;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


/**
 * Fragment class for choosing a activity to send you pet out on
 */
public class ActivitiesFragment extends Fragment {

    private int buttonSelector = 0;
    private OnActivitiesFragmentInteractionListener mListener;
    private MainActivity mainActivityHolder;
    private AppCompatImageButton activitiesButton;
    private Fragment activitiesFragment;

    /**
     * basic constructor
     */
    public ActivitiesFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        //used to access the activities assets
        mainActivityHolder = (MainActivity) getActivity();

        activitiesFragment = this;
        activitiesButton = view.findViewById(R.id.activities_button_two);
        activitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .remove(activitiesFragment)
                        .commit();
            }
        });

        final ImageButton popcornButton = view.findViewById(R.id.popcornButton);
        final ImageButton runButton = view.findViewById(R.id.runButton);
        final ImageButton dumbbellButton = view.findViewById(R.id.dumbbellButton);
        final ImageButton discoButton = view.findViewById(R.id.discoButton);
        final TextView textDisplay = view.findViewById(R.id.text_display);
        textDisplay.setTextSize(18f);
        textDisplay.setTextColor(Color.parseColor("#85754d"));
        textDisplay.setTypeface(Typeface.DEFAULT_BOLD);

        //setup for the popcorn button OnClickListener
        popcornButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelector = 1;
                popcornButton.setImageResource(R.drawable.popcorn_border);
                runButton.setImageResource(R.drawable.run);
                dumbbellButton.setImageResource(R.drawable.dumbbell);
                discoButton.setImageResource(R.drawable.disco);

                String text = String.format(Locale.getDefault(), "Go to the Movies!\n" +
                        "Duration: 3 Hours\n" +
                        "-10 Hunger\n" +
                        "+30 Happiness\n" +
                        "-10 Fitness");
                textDisplay.setText(text);

            }
        });

        //setup for the run button OnClickListener
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelector = 2;
                popcornButton.setImageResource(R.drawable.popcorn);
                runButton.setImageResource(R.drawable.run_border);
                dumbbellButton.setImageResource(R.drawable.dumbbell);
                discoButton.setImageResource(R.drawable.disco);

                String text = String.format(Locale.getDefault(),"Go for a Run!\n" +
                        "Duration: 1 Hours\n" +
                        "+20 Hunger\n" +
                        "+5 Happiness\n" +
                        "+10 Fitness");
                textDisplay.setText(text);
            }
        });

        //setup for the dumbbell button OnClickListener
        dumbbellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelector = 3;
                popcornButton.setImageResource(R.drawable.popcorn);
                runButton.setImageResource(R.drawable.run);
                dumbbellButton.setImageResource(R.drawable.dumbbell_border);
                discoButton.setImageResource(R.drawable.disco);

                String text = String.format(Locale.getDefault(),"Go to the the Gym!\n" +
                        "Duration: 2 Hours\n" +
                        "+30 Hunger\n" +
                        "+10 Happiness\n" +
                        "+20 Fitness");
                textDisplay.setText(text);
            }
        });

        //setup for the disco button OnClickListener
        discoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelector = 4;
                popcornButton.setImageResource(R.drawable.popcorn);
                runButton.setImageResource(R.drawable.run);
                dumbbellButton.setImageResource(R.drawable.dumbbell);
                discoButton.setImageResource(R.drawable.disco_border);

                String text = String.format(Locale.getDefault(),"Go to the Disco!\n" +
                        "Duration: 3 Hours\n" +
                        "+20 Hunger\n" +
                        "+20 Happiness\n" +
                        "+5 Fitness");
                textDisplay.setText(text);
            }
        });

        //setup for the confirmation button OnClickListener
        Button confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Please Select an Activity";

                if (mainActivityHolder.mPet.getStatus() == 0) {
                    if (buttonSelector == 1) {
                        message = "Your pet is going out for a movie!";
                        mainActivityHolder.mPet.setHunger(-10);
                        mainActivityHolder.mPet.setHappiness(30);
                        mainActivityHolder.mPet.setFitness(-10);
                        mListener.activitySelected(1, 10800000);
                    }
                    else if (buttonSelector == 2) {
                        message = "Your pet is going out for a run!";
                        mainActivityHolder.mPet.setHunger(20);
                        mainActivityHolder.mPet.setHappiness(5);
                        mainActivityHolder.mPet.setFitness(10);
                        mListener.activitySelected(2, 3600000);
                    }
                    else if (buttonSelector == 3) {
                        message = "Your pet is going out to the gym!";
                        mainActivityHolder.mPet.setHunger(30);
                        mainActivityHolder.mPet.setHappiness(10);
                        mainActivityHolder.mPet.setFitness(20);
                        mListener.activitySelected(3, 7200000);
                    }
                    else if (buttonSelector == 4) {
                        message = "Your pet is going out to the disco!";
                        mainActivityHolder.mPet.setHunger(20);
                        mainActivityHolder.mPet.setHappiness(20);
                        mainActivityHolder.mPet.setFitness(5);
                        mListener.activitySelected(4, 10800000);
                    }
                }
                else {
                    message = "Your pet is already out on a activity!";
                }

                Toast.makeText(getActivity(), message,
                        Toast.LENGTH_LONG).show();

                if (buttonSelector != 0) {
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }


    //setup for the listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnActivitiesFragmentInteractionListener) context;
        }
        catch (Exception e) {
            Log.d("Activities Fragment", "" + e);
        }
    }

    //setup for the listener
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //setup for the listener
    public interface OnActivitiesFragmentInteractionListener {
        void activitySelected(int activityChosen, long duration);
    }


}
