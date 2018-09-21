package edu.tacoma.uw.css.team5.tamagotcha;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


/**
 * Main activity which is used after login screen
 *
 * @author Ryan Hansen
 * @author Conor M.
 */
public class MainActivity extends AppCompatActivity
        implements ActivitiesFragment.OnActivitiesFragmentInteractionListener ,
                    UserAccountCallBack{
    // DEBUGGING VALUES
    private static final long PET_HUNGER_TICK_VALUE = 3000; //3 SECONDS
    private static final long PET_HAPPINESS_TICK_VALUE = 4500; //4.5 SECONDS
    private static final long PET_FITNESS_TICK_VALUE = 6000; //6 SECONDS

//    private static final long PET_HUNGER_TICK_VALUE = 1800000; //30 minutes
//    private static final long PET_HAPPINESS_TICK_VALUE = 2700000; //45 minutes
//    private static final long PET_FITNESS_TICK_VALUE = 3600000; //60 minutes
    private static final String notifchannel = "notifchannel";
    private FragmentManager fragmentManager;
    private TextView countdownTimer;
    private Handler mHandler;
    private long mHungerDelay, mHappinessDelay, mFitnessDelay;
    public Pet mPet;
    private UserAccountTask mAuthTask;
    private int mAuthState;
    private Fragment mLoadingScreenView;
    private ImageView monster;

    /**
     * creates the main activity screen.
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        //sets the countdown timer attributes
        countdownTimer = findViewById(R.id.countdown_timer);
        countdownTimer.setTextSize(22f);
        countdownTimer.setTypeface(Typeface.DEFAULT_BOLD);
        countdownTimer.setTextColor(Color.parseColor("#000000"));
        countdownTimer.setVisibility(View.INVISIBLE);

        monster = findViewById(R.id.monster_pic);

        loadData();

        ImageButton shareButton = (ImageButton) findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap screenshot = Screenshot.takeScreenshotOfView(view);
                launchShareFragment(view, screenshot);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    private void startPet() {
        mPet.setmLoginTime(System.currentTimeMillis());

        //create the Handler for pet stat deterioration
        //and start the deterioration.
        mHandler = new Handler();
        startStatDeterioration();

        //show pet return timer if the pet is away on a
        //activity.
        showReturnTimer();
    }

    @Override
    public void onBackPressed() {

        if (fragmentManager.getBackStackEntryCount() == 0) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LogoutFragment logoutFrag = new LogoutFragment();
            fragmentTransaction.add(R.id.main_activity, logoutFrag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                launchNotification();
            }
        }, 10000);

        super.onDestroy();
        //shutdown the pet stat deterioration
        mHandler.removeCallbacks(petHungerUpdate);
        mHandler.removeCallbacks(petHappinessUpdate);
        mHandler.removeCallbacks(petFitnessUpdate);
    }

    private void saveData() {
        // Build a large param bundle to send to the backend
        // I didn't have time to make this not garbage
        android.content.SharedPreferences pref = this.getSharedPreferences("tamagotcha", 0);
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "post_user");
        params.put("url", "https://tamagotcha.herokuapp.com/db/update");
        params.put("username", pref.getString("email", null));
        params.put("last_login", mPet.getmLoginTime() + "");
        params.put("last_save", System.currentTimeMillis() + "");
        params.put("happiness", mPet.getHappiness() + "");
        params.put("hunger", mPet.getHunger() + "");
        params.put("fitness", mPet.getFitness() + "");
        params.put("status", mPet.getStatus() + "");
        params.put("return_time", mPet.getReturnTime() + "");
        params.put("next_hunger_tick", mPet.getmNextHungerTick() + "");
        params.put("next_happiness_tick", mPet.getmNextHappinessTick() + "");
        params.put("next_fitness_tick", mPet.getmNextFitnessTick() + "");
        params.put("last_logout_time", System.currentTimeMillis() + "");
        mAuthTask = new UserAccountTask(params, this);
        mAuthTask.execute((Void) null);
    }

    private void loadData() {
        // Load in the user data when they start main
        mAuthState = 1;
        // Param bundle
        android.content.SharedPreferences pref = this.getSharedPreferences("tamagotcha", 0);
        HashMap<String, String> params = new HashMap<>();
        params.put("username", pref.getString("email", null));
        params.put("url", "https://tamagotcha.herokuapp.com/db/get");
        params.put("type", "get_user");
        mAuthTask = new UserAccountTask(params, this);
        mAuthTask.execute((Void) null);
    }

    /**
     * Launches the Fridge fragment.
     * @param view view
     */
    public void launchFridgeFragment(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FridgeFragment fridgeFrag = new FridgeFragment();
        fragmentTransaction.add(R.id.main_activity, fridgeFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Launches the Logout Fragment
     * @param view view
     */
    public void launchLogoutFragment(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LogoutFragment logoutFrag = new LogoutFragment();
        fragmentTransaction.add(R.id.main_activity, logoutFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Launches the Graph fragment.
     * @param view view
     */
    public void launchGraphFragment(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GraphFragment graphFrag = new GraphFragment();
        fragmentTransaction.add(R.id.main_activity, graphFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Launches the Activities fragment
     * @param view view
     */
    public void launchActivitiesFragment(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ActivitiesFragment activitiesFrag = new ActivitiesFragment();
        fragmentTransaction.add(R.id.main_activity, activitiesFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Launches the Share fragment
     * @param view view
     */
    public void launchShareFragment(View view, Bitmap screenshot) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ShareFragment shareFrag = new ShareFragment();

        //Intent intent = new Intent(this,
        fragmentTransaction.add(R.id.main_activity, shareFrag);
        fragmentTransaction.addToBackStack(null);

        Bundle args = new Bundle();
        args.putParcelable("image", screenshot);
        shareFrag.setArguments(args);
        fragmentTransaction.commit();

    }


    /**
     * implemented interface method from ActivitiesFragment which
     * passes the information of which activity was chosen by the user
     * back to this main activity.
     *
     * @param activityChosen activityChosen
     * @param duration duration
     */
    @Override
    public void activitySelected(int activityChosen, long duration) {

        mPet.setStatus(activityChosen);
        long ts = (System.currentTimeMillis());
        mPet.setReturnTime(ts + duration);

        //a call to the method which will display the countdown timer
        if (mPet.getStatus() != 0) {
            showReturnTimer();
        }
    }

    /**
     * Call this method to display the return timer for the pet
     * if it is out on an activity.
     *
     * Also should be called when restarting the app AFTER the data has been
     * loaded from the database. Will (should) display the countdown timer for the
     * return time.
     */
    private void showReturnTimer() {

        long theTime = mPet.getReturnTime() - System.currentTimeMillis();

        if (mPet.getStatus() != 0) {
            monster.setVisibility(View.INVISIBLE);
        }

        if (theTime > 0) {

            new CountDownTimer(theTime, 1000) {

                public void onTick(long millisUntilFinished) {
                    countdownTimer.setVisibility(View.VISIBLE);

                    String text = String.format(Locale.getDefault(), "Your Pet Will Return in:\n" +
                                    "%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);

                    countdownTimer.setText(text);
                }

                public void onFinish() {
                    countdownTimer.setText("");
                    countdownTimer.setVisibility(View.INVISIBLE);
                    mPet.setStatus(0);
                    monster.setVisibility(View.VISIBLE);
                }

            }.start();
        }
    }


    /**
     * starts the 3 handlers which will run infinitely
     * while the app is running which reduce the pets stats
     * at given intervals.
     */
    private void startStatDeterioration() {

        mHungerDelay = statDeteriorationHelper(PET_HUNGER_TICK_VALUE);
        mHappinessDelay = statDeteriorationHelper(PET_HAPPINESS_TICK_VALUE);
        mFitnessDelay = statDeteriorationHelper(PET_FITNESS_TICK_VALUE);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                petHungerUpdate.run();
            }
        }, mHungerDelay);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                petHappinessUpdate.run();
            }
        }, mHappinessDelay);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                petFitnessUpdate.run();
            }
        }, mFitnessDelay);
    }

    /**
     * Helper method for stats which calculates how many ticks have passed since
     * the user last logged out. calculates the ticks and applies them to stats
     * as well as setting correct delay before starting the next ticks.
     *
     * @param tickSize time (in milliseconds) of delay between pet stat deterioration ticks.
     * @return delay time before starting next stat deterioration tick.
     */
    private long statDeteriorationHelper(long tickSize) {
        long delay, totalTimeLoggedOut, timeLeft, firstTickFromLogout;
        int ticksSinceLastLogin = 0;

        //total time since last login
        if (mPet.getmLastLogoutTime() == 0) {
            //if the pet is brand new it will fall into this branch
            totalTimeLoggedOut = 0;
        }
        else {
            totalTimeLoggedOut = mPet.getmLoginTime() - mPet.getmLastLogoutTime();
        }


        //depending on which tick size (stat determined) gets the time
        //the next tick would have taken place after the last logout time
        if (tickSize == PET_HUNGER_TICK_VALUE) {
            firstTickFromLogout = mPet.getmNextHungerTick() - mPet.getmLastLogoutTime();
        }
        else if (tickSize == PET_HAPPINESS_TICK_VALUE) {
            firstTickFromLogout = mPet.getmNextHappinessTick() - mPet.getmLastLogoutTime();
        }
        else {
            firstTickFromLogout = mPet.getmNextFitnessTick() - mPet.getmLastLogoutTime();
        }

        if (firstTickFromLogout < totalTimeLoggedOut ) {
            ticksSinceLastLogin--;
        }

        // (1) gets time left between the last logout and current login
        //after subtracting the first tick that would have occurred
        //
        // (2) adds a value depending on how many  full tick intervals
        //occurred from remaining time
        //
        // (3) takes the remainder of the time left and adds it to the return
        //value which will be used to set a delay for next tick

        timeLeft = totalTimeLoggedOut - firstTickFromLogout; // (1)
        ticksSinceLastLogin -= (int) (timeLeft / tickSize); // (2)
        delay = timeLeft % tickSize; // (3)

        //applies the ticks occurred since last logout to the
        //appropriate stat of the pet
        if (tickSize == PET_HUNGER_TICK_VALUE) {
            mPet.setHunger( (ticksSinceLastLogin * (-1)) );
        }
        else if (tickSize == PET_HAPPINESS_TICK_VALUE) {
            mPet.setHappiness(ticksSinceLastLogin);
        }
        else {
            mPet.setFitness(ticksSinceLastLogin);
        }

        mPet.setmNextHungerTick(System.currentTimeMillis() + PET_HUNGER_TICK_VALUE);
        mPet.setmNextHappinessTick(System.currentTimeMillis() + PET_HAPPINESS_TICK_VALUE);
        mPet.setmNextFitnessTick(System.currentTimeMillis() + PET_FITNESS_TICK_VALUE);

//        mPet.setHunger(50);
//        mPet.setHappiness(50);
//        mPet.setFitness(50);

        if(delay < 0) {
            delay = 0;
        }
        return delay;
    }

    private void dbInsertUserData(HashMap<String, String> params) {
        DatabaseController db = new DatabaseController(this);
        db.putUserInfo(params);
        Log.e("PET INFO: ", "dbInsertUserData: " +  params.toString());
        mPet = new Pet(params);
    }


    //
    // Auth callbacks
    //

    @Override
    public void userAccountCallBackDoInBackground() {
        // Show loading screen
        if (mAuthState == 1) {
            mLoadingScreenView = new LoadingScreenFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.main_activity, mLoadingScreenView)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void userAccountCallBackOnPostExecute(String response) throws JSONException {
        mAuthTask = null;
        if (mAuthState == 1) {
            fragmentManager.beginTransaction()
                    .remove(mLoadingScreenView)
                    .commit();
            JSONObject content = new JSONObject(response);
            HashMap<String, String> params = new HashMap<>();
            Iterator<String> i = content.keys();
            while (i.hasNext()) {
                String key = i.next();
                params.put(key, content.getString(key));
            }
            dbInsertUserData(params);
            startPet();
            mAuthState = 0;
        }
    }

    @Override
    public void userAccountCallBackOnCancelled() {
        mAuthTask = null;
        if (mAuthState == 1) {
            fragmentManager.beginTransaction()
                    .remove(mLoadingScreenView)
                    .commit();

        }
    }

    //
    // Stats
    //

    //puts the pets hunger deterioration on a infinite loop
    //while the app is running.
    final Runnable petHungerUpdate = new Runnable() {
        @Override
        public void run() {
            mPet.setHunger(1);
            mPet.setmNextHungerTick(System.currentTimeMillis() + PET_HUNGER_TICK_VALUE);
            mHandler.postDelayed(petHungerUpdate, PET_HUNGER_TICK_VALUE);
        }
    };

    //puts the pets happiness deterioration on a infinite loop
    //while the app is running.
    final Runnable petHappinessUpdate = new Runnable() {
        @Override
        public void run() {
            mPet.setHappiness(-1);
            mPet.setmNextHappinessTick(System.currentTimeMillis() + PET_HAPPINESS_TICK_VALUE);
            mHandler.postDelayed(petHappinessUpdate, PET_HAPPINESS_TICK_VALUE);
        }
    };

    //puts the pets fitness deterioration on a infinite loop
    //while the app is running.
    final Runnable petFitnessUpdate = new Runnable() {
        @Override
        public void run() {
            mPet.setFitness(-1);
            mPet.setmNextFitnessTick(System.currentTimeMillis() + PET_FITNESS_TICK_VALUE);
            mHandler.postDelayed(petFitnessUpdate, PET_FITNESS_TICK_VALUE);
        }
    };


    /**
     * Notification service setup for reminding the user to
     * check on their pet!
     */
    public void launchNotification() {

        NotificationChannel NC;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NC = new NotificationChannel(notifchannel, "NC", NotificationManager.IMPORTANCE_DEFAULT);
            NC.enableLights(true);
            NC.enableVibration(true);
            NC.setLightColor(R.color.colorPrimary);
            NC.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            notificationManager.createNotificationChannel(NC);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, notifchannel)
                .setSmallIcon(R.drawable.notification_pet)
                .setContentTitle("Hey You!")
                .setContentText("Don't Forget About Your Pet!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(6565, mBuilder.build());
    }

}
