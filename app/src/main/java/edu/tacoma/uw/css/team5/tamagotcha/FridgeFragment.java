/**
 * Fragment used for choosing a food element to feed to the pet.
 *
 * @author Ryan Hansen
 */

package edu.tacoma.uw.css.team5.tamagotcha;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment used for choosing a food element to feed to the pet.
 */
public class FridgeFragment extends Fragment {

    /* Keep track of fridge fragment */
    private Fragment fridgeFragment;
    /* Keep track of main activity */
    private MainActivity mainActivityHolder;

    public FridgeFragment() {
        // Required empty public constructor
    }

    /**
     *
     * on create view method called when starting the fragment.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fridge, container, false);
        fridgeFragment = this;
        mainActivityHolder = (MainActivity) getActivity();

        // Initialize all the buttons with listeners so we can move them
        MovableImageButton pizza = v.findViewById(R.id.pizza);
        pizza.setOnTouchListener(new ImageMover());
        MovableImageButton cake = v.findViewById(R.id.cake);
        cake.setOnTouchListener(new ImageMover());
        MovableImageButton cookies = v.findViewById(R.id.cookies);
        cookies.setOnTouchListener(new ImageMover());
        MovableImageButton donut = v.findViewById(R.id.donut);
        donut.setOnTouchListener(new ImageMover());

        // Button to close fridge
        AppCompatImageButton fridgeButton = v.findViewById(R.id.fridge_fragment_button);
        fridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .remove(fridgeFragment)
                        .commit();
            }
        });
        return v;
    }

    /**
     * onDestroy method
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * onTouchListener to add to all the buttons so they can be moved
     */
    class ImageMover implements View.OnTouchListener {

        float originalX;
        float originalY;
        float x;
        float y;
        float dx;
        float dy;

        /**
         * Moves the images around when they are clicked and then dragged on the screen
         *
         * @param view Imagebutton
         * @param motionEvent Motion events on the image button
         * @return true if an event happened
         */
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // Grab the view when touched
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                view.performClick();
                originalX = view.getX();
                originalY = view.getY();
                x = motionEvent.getRawX() - view.getX(); //- view.getWidth();
                y = motionEvent.getRawY() - view.getY(); //- view.getHeight();
                Log.e("(X, Y)", x + " " + y);
            // Move the with the drag motions
            } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE){
                dx = motionEvent.getRawX();
                dy = motionEvent.getRawY();
                view.setX(dx - x);
                view.setY(dy - y);
            // Reset the position of the food and reduce the pet hunger
            } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                view.setX(originalX);
                view.setY(originalY);

                //only works if pet is available
                if (mainActivityHolder.mPet.getStatus() == 0) {
                    mainActivityHolder.mPet.setHunger(-10);
                }

            }

            return true;
        }
    }
}
