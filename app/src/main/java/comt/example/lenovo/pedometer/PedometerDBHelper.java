package comt.example.lenovo.pedometer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PedometerDBHelper extends SQLiteOpenHelper {
    private static final String DBName =  "pedometer.db";
    private static final String PEDOMETER = "Pedometer";
    private static final String CREATE_PEDOMETER_TABLE
            = "create table " + PEDOMETER + " (id integer primary key autoincrement, date String)";
    private static final String UPDATE_PEDOMETER_TABLE
            = "alter table " +PEDOMETER + " add steps int";
    public PedometerDBHelper(Context context, int version) { super(context, DBName, null, version); }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PEDOMETER_TABLE);
        sqLiteDatabase.execSQL(UPDATE_PEDOMETER_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        switch (i) {
            case 1:
                //upgrade logic from 1 to 2
                // sqLiteDatabase.execSQL(CREATE_CALORIE_TABLE);
            case 2:
                // upgrade logic from 2 to 3
                break;
            default:
                throw new IllegalStateException("unknown oldVersion " + i);
        }
    }


}
