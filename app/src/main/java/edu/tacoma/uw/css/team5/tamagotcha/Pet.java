package edu.tacoma.uw.css.team5.tamagotcha;

import java.util.HashMap;

/**
 *
 * mStatus is used to see if the pet is at home.
 * 0 = at home.
 * 1 = out at the movies.
 * 2 = out on a run.
 * 3 = out at the gym.
 * 4 = out at the disco.
 *
 * mReturnTime is used to know when pet will be
 * returning from the current activity it is out on.
 */
public class Pet {

    private static final int MIN_STAT_VALUE = 0;
    private static final int MAX_STAT_VALUE = 100;

    private int mHunger, mHappiness, mFitness, mStatus;
    private long mNextHungerTick, mNextHappinessTick, mNextFitnessTick;
    private long mReturnTime, mLoginTime, mLastLogoutTime;
    private String mPetName = "Pet Name";


    /**
     * constructor for Pet
     *
     * @param params params
     */
    public Pet(HashMap<String, String> params) {
        this.mHunger = Integer.parseInt(params.get("hunger"));
        this.mHappiness = Integer.parseInt(params.get("happiness"));
        this.mFitness = Integer.parseInt(params.get("fitness"));
        this.mStatus = Integer.parseInt(params.get("status"));
        if (!params.get("return_time").equals("null"))
            this.mReturnTime = Long.parseLong(params.get("return_time"));
        else
            this.mReturnTime = 0;

        this.mNextHungerTick = Long.parseLong(params.get("next_hunger_tick"));
        this.mNextHappinessTick = Long.parseLong(params.get("next_happiness_tick"));
        this.mNextFitnessTick = Long.parseLong(params.get("next_fitness_tick"));

        this.mLoginTime = System.currentTimeMillis();
        this.mLastLogoutTime = Long.parseLong(params.get("last_login"));
        this.mPetName = params.get("pet_name");
        //Log.e("PET CREATED: ", this.toString());
    }


    /**
     * Setter for Name
     *
     * @param name
     */
    public void setPetName(String name) {
        this.mPetName = name;
    }

    /**
     * get pet name
     *
     * @return pet name
     */
    public String getPetName() {
        return this.mPetName;
    }

    /**
     * get pet hunger
     *
     * @return hunger
     */
    public int getHunger() {
        return mHunger;
    }

    /**
     *
     * @param hunger pet hunger
     */
    public void setHunger(int hunger) {
        this.mHunger += hunger;

        if(this.mHunger < MIN_STAT_VALUE) {
            this.mHunger = MIN_STAT_VALUE;
        }
        else if (this.mHunger > MAX_STAT_VALUE) {
            this.mHunger = MAX_STAT_VALUE;
        }
    }

    /**
     * get pet happiness
     *
     * @return pet happiness
     */
    public int getHappiness() {
        return mHappiness;
    }

    /**
     * set pet happiness
     *
     * @param happiness pet happiness
     */
    public void setHappiness(int happiness) {
        this.mHappiness += happiness;

        if(this.mHappiness < MIN_STAT_VALUE) {
            this.mHappiness = MIN_STAT_VALUE;
        }
        else if (this.mHappiness > MAX_STAT_VALUE) {
            this.mHappiness = MAX_STAT_VALUE;
        }
    }

    /**
     * get pet fitness
     *
     * @return pet fitness
     */
    public int getFitness() {
        return mFitness;
    }

    /**
     * set pet fitness
     *
     * @param fitness pet fitnes
     */
    public void setFitness(int fitness) {
        this.mFitness += fitness;

        if(this.mFitness < MIN_STAT_VALUE) {
            this.mFitness = MIN_STAT_VALUE;
        }
        else if (this.mFitness > MAX_STAT_VALUE) {
            this.mFitness = MAX_STAT_VALUE;
        }
    }

    /**
     * get pet status
     *
     * @return pet status
     */
    public int getStatus() {
        return mStatus;
    }

    /**
     * set pet status
     *
     * @param status pet status
     */
    public void setStatus(int status) {
        this.mStatus = status;
    }

    /**
     * get return time
     *
     * @return return time
     */
    public long getReturnTime() {
        return mReturnTime;
    }

    /**
     * set pet return time
     *
     * @param mReturnTime return time
     */
    public void setReturnTime(long mReturnTime) {
        this.mReturnTime = mReturnTime;
    }

    /**
     * get login time
     *
     * @return login time
     */
    public long getmLoginTime() {
        return mLoginTime;
    }

    /**
     * set login time
     *
     * @param loginTime login time
     */
    public void setmLoginTime(long loginTime) {
        this.mLoginTime = loginTime;
    }

    /**
     * pet logout time
     *
     * @return logout time
     */
    public long getmLastLogoutTime() {
        return mLastLogoutTime;
    }

    /**
     * set pet logout time
     *
     * @param logoutTime logout time
     */
    public void setmLogoutTime(long logoutTime) {
        this.mLastLogoutTime = logoutTime;
    }

    /**
     * get hunger tick
     *
     * @return hunger tick
     */
    public long getmNextHungerTick() {
        return mNextHungerTick;
    }

    /**
     * set hunger tick
     *
     * @param mNextHungerTick hunger tick
     */
    public void setmNextHungerTick(long mNextHungerTick) {
        this.mNextHungerTick = mNextHungerTick;
    }

    /**
     * get happiness tick
     *
     * @return happiness tick
     */
    public long getmNextHappinessTick() {
        return mNextHappinessTick;
    }

    /**
     * set happiness tick
     *
     * @param mNextHappinessTick happiness tick
     */
    public void setmNextHappinessTick(long mNextHappinessTick) {
        this.mNextHappinessTick = mNextHappinessTick;
    }

    /**
     * get fitness tick
     *
     * @return fitness tick
     */
    public long getmNextFitnessTick() {
        return mNextFitnessTick;
    }

    /**
     * set fitness tick
     *
     * @param mNextFitnessTick fitness tick
     */
    public void setmNextFitnessTick(long mNextFitnessTick) {
        this.mNextFitnessTick = mNextFitnessTick;
    }

    /**
     * to string method
     *
     * @return string
     */
    @Override
    public String toString() {
        return "PET: hunger " + mHunger + ", happiness " + mHappiness + ", fitness " + mFitness;
    }
}
