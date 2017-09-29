package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by francisco on 27/09/17.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format(
                "CREATE TABLE %s (" +
                        "_id integer primary key autoincrement, " +
                        "%s," +
                        "%s," +
                        "%s," +
                        "%s)",
                CrimeTable.NAME,
                CrimeTable.Cols.UUID,
                CrimeTable.Cols.TITLE,
                CrimeTable.Cols.DATE,
                CrimeTable.Cols.SOLVED)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
