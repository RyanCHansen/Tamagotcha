package edu.tacoma.uw.css.team5.tamagotcha;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

/**
 * Movable image button class
 */
public class MovableImageButton extends AppCompatImageButton {

    /**
     * Constructor, just calls super
     *
     * @param context View context
     * @param attrs View attributes
     */
    public MovableImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Override the performClick so we don't get weird interactions with old API levels
     *
     * @return the superclass performClick
     */
    @Override
    public boolean performClick() {
        return super.performClick();
    }
}