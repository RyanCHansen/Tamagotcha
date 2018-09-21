package edu.tacoma.uw.css.team5.tamagotcha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Database controller for the local SQLite database
 *
 * @author Conor Marsten
 */
public class DatabaseController {
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "user_info.db";
    private static final String USER_INFO_TABLE = "user_info";
    private static final String[] DB_COLUMNS = {
        "user_uuid",
        "last_login",
        "last_save",
        "pet_name",
        "happiness",
        "hunger",
        "fitness",
        "status",
        "return_time",
        "next_hunger_tick",
        "next_happiness_tick",
        "next_fitness_tick",
        "last_logout_time"
    };

    private SQLiteDatabase mSQLiteDatabase;
    private UserAccountTask mAuthTask;

    /**
     * Controller constructor
     *
     * @param context Controller's context
     */
    DatabaseController(Context context) {
        DBController mDBConnection = new DBController(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mDBConnection.getWritableDatabase();
    }

    /**
     * Retrieve the user data from the local database
     *
     * @return HashMap of the user's data
     */
    public HashMap<String, String> getUserInfo() {

        Cursor c = mSQLiteDatabase.query(
                USER_INFO_TABLE,  // The table to query
                DB_COLUMNS,        // The columns to return
                null,     // The columns for the WHERE clause
                null,  // The values for the WHERE clause
                null,     // don't group the rows
                null,      // don't filter by row groups
                null      // The sort order
        );
        c.moveToFirst();

        HashMap<String, String> userData = new HashMap<>();

        for(int i = 0; i < DB_COLUMNS.length; i++) {
            userData.put(DB_COLUMNS[i], c.getString(i));
        }

        c.close();
        return userData;
    }

    /**
     * Put the user info into the SQLite database
     *
     * @param params User info as a param bundle
     */
    public void putUserInfo(HashMap<String, String> params) {
        ContentValues content = new ContentValues();
        for (String key : params.keySet()) {
            content.put(key, params.get(key));
        }

        //Cursor c = mSQLiteDatabase.query(USER_INFO_TABLE,);

        mSQLiteDatabase.insert(USER_INFO_TABLE, null, content);
        //mSQLiteDatabase.update();
    }

    /**
     * Helper class for the database controller
     */
    class DBController extends SQLiteOpenHelper {

        private final String CREATE_COURSE_SQL;

        private final String DROP_COURSE_SQL;

        /**
         * Constructor creates and upgrades tables if needed
         *
         * @param context Class context
         * @param name Database name
         * @param factory Cursor
         * @param version Database version
         */
        DBController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_COURSE_SQL = context.getString(R.string.CREATE_COURSE_SQL);
            DROP_COURSE_SQL = context.getString(R.string.DROP_COURSE_SQL);

        }

        /**
         * Create the database if needed
         *
         * @param sqLiteDatabase The database
         */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_COURSE_SQL);
        }

        /**
         * Upgrade the database if needed
         *
         * @param sqLiteDatabase The database
         * @param i Current version
         * @param i1 New version
         */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_COURSE_SQL);
            onCreate(sqLiteDatabase);
        }

    }

}
