package edu.lakeland.mealrater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MealDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mealRatings.db";
    private static final int DATABASE_VERSION = 4;

    // Database creation sql statement
    private static final String CREATE_TABLE_MEALRATINGS =
            "create table mealRatings (_id integer primary key autoincrement, "
                    + "meal text, "
                    + "restaurant text, "
                    + "rating text, "
                    + "restaurant_latitude double, "
                    + "restaurant_longitude double, "
                    + "mealPhoto blob);";


    public MealDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEALRATINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MealDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS mealRatings");
        onCreate(db);
    }
}
