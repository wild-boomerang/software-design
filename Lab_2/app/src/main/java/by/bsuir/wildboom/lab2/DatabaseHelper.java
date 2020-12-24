package by.bsuir.wildboom.lab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME   = "timer_app.db";
    private static final int SCHEMA             = 1;
    public static final String TABLE_NAME       = "timers";
    public static final String ID_COLUMN        = "id";
    public static final String TITLE_COLUMN     = "title";
    public static final String WARM_UP_COLUMN   = "warm_up";
    public static final String WORKOUT_COLUMN   = "workout";
    public static final String REST_COLUMN      = "rest";
    public static final String COOLDOWN_COLUMN  = "cooldown";
    public static final String CYCLE_COLUMN     = "cycle";
    public static final String SET_COUNT_COLUMN = "set_count";
    public static final String RED_COLUMN       = "red";
    public static final String GREEN_COLUMN     = "green";
    public static final String BLUE_COLUMN      = "blue";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                     + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                     + TITLE_COLUMN     + "TEXT,"
                     + TITLE_COLUMN     + " INTEGER,"
                     + WARM_UP_COLUMN   + " INTEGER,"
                     + WORKOUT_COLUMN   + " INTEGER,"
                     + REST_COLUMN      + " INTEGER,"
                     + COOLDOWN_COLUMN  + " INTEGER,"
                     + CYCLE_COLUMN     + " INTEGER,"
                     + SET_COUNT_COLUMN + " INTEGER,"
                     + RED_COLUMN       + " INTEGER,"
                     + GREEN_COLUMN     + " INTEGER,"
                     + BLUE_COLUMN      + " INTEGER" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }
}
