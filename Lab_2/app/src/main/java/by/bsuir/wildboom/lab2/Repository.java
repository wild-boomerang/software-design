package by.bsuir.wildboom.lab2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Repository {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public Repository(Context context) {
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addTimer(String title, String warmUp, String workout, String rest, String cooldown,
                         String cycle, String setCount, String red, String green, String blue)
    {
        String query = "INSERT INTO " + DatabaseHelper.TABLE_NAME + " ("
                + DatabaseHelper.TITLE_COLUMN     + ", " + DatabaseHelper.WARM_UP_COLUMN + ", "
                + DatabaseHelper.WORKOUT_COLUMN   + ", " + DatabaseHelper.REST_COLUMN    + ", "
                + DatabaseHelper.COOLDOWN_COLUMN  + ", " + DatabaseHelper.CYCLE_COLUMN   + ", "
                + DatabaseHelper.SET_COUNT_COLUMN + ", " + DatabaseHelper.RED_COLUMN     + ", "
                + DatabaseHelper.GREEN_COLUMN     + ", " + DatabaseHelper.BLUE_COLUMN    + ") "
                + "VALUES ('"
                + title    + "', " + warmUp + ", " + workout  + ", " + rest + ", "
                + cooldown + ", "  + cycle  + ", " + setCount + ", " + red  + ", "
                + green    + ", "  + blue   + " );";

        this.open();
        db.execSQL(query);
        this.close();
    }

    public void editTimer(String Id, String title, String warmUp, String workout, String rest,
                          String cooldown, String cycle, String setCount)
    {
        String query = "UPDATE " + DatabaseHelper.TABLE_NAME
                     + " SET "
                     + DatabaseHelper.TITLE_COLUMN     + " = '" + title    + "', "
                     + DatabaseHelper.WARM_UP_COLUMN   + " = "  + warmUp   + ", "
                     + DatabaseHelper.WORKOUT_COLUMN   + " = "  + workout  + ", "
                     + DatabaseHelper.REST_COLUMN      + " = "  + rest     + ", "
                     + DatabaseHelper.COOLDOWN_COLUMN  + " = "  + cooldown + ", "
                     + DatabaseHelper.CYCLE_COLUMN     + " = "  + cycle    + ", "
                     + DatabaseHelper.SET_COUNT_COLUMN + " = "  + setCount + " "
                     + "WHERE id = " + Id + ";";

        this.open();
        db.execSQL(query);
        this.close();
    }

    public ArrayList<TimerInfo> setTimers() {
        ArrayList<TimerInfo> timers = new ArrayList<>();
        String query = "SELECT * FROM timers;";

        this.open();
        Cursor rawQuery = db.rawQuery(query, null);
        if (rawQuery.moveToFirst()) {
            do {
                int id = rawQuery.getInt(0);
                String name = rawQuery.getString(1);
                int warmUp = rawQuery.getInt(2);
                int workout = rawQuery.getInt(3);
                int rest = rawQuery.getInt(4);
                int cooldown = rawQuery.getInt(5);
                int cycle = rawQuery.getInt(6);
                int setCount = rawQuery.getInt(7);
                int red = rawQuery.getInt(8);
                int green = rawQuery.getInt(9);
                int blue = rawQuery.getInt(10);

                TimerInfo timerInfo = new TimerInfo(id, warmUp, workout, rest, cooldown, cycle, setCount);
                timerInfo.setColor(new int[] { red, green, blue });
                timerInfo.setTitle(name);
                timers.add(timerInfo);
            } while(rawQuery.moveToNext());
        }
        rawQuery.close();
        this.close();

        return timers;
    }

    public void deleteTimer(String id) {
        String query = "DELETE FROM timers WHERE id = " + id + ";";

        this.open();
        db.execSQL(query);
        this.close();
    }

    public void deleteAllTimers() {
        String query = "DELETE FROM timers;";

        this.open();
        db.execSQL(query);
        this.close();
    }
}
