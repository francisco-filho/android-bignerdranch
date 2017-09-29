package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by francisco on 19/09/17.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context){
        mDatabase = new CrimeBaseHelper(context).getWritableDatabase();
    }

    public static CrimeLab get(Context context){
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()});

        if (cursor.getCount() == 0) {
            return null;
        }

        try {
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public ContentValues getContentValues(Crime c){
        ContentValues cv = new ContentValues();
        cv.put(CrimeTable.Cols.UUID, c.getId().toString());
        cv.put(CrimeTable.Cols.TITLE, c.getTitle());
        cv.put(CrimeTable.Cols.DATE, c.getDate().getTime());
        cv.put(CrimeTable.Cols.SOLVED, c.isSolved());
        cv.put(CrimeTable.Cols.SUSPECT, c.getSuspect());
        return cv;
    }

    public void addCrime(Crime c) {
        mDatabase.insert(CrimeTable.NAME, null, getContentValues(c));
    }

    public void updateCrime(Crime c){
        UUID uuid = c.getId();

        mDatabase.update(CrimeTable.NAME, getContentValues(c),
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()});
    }

    public CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor c = mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CrimeCursorWrapper(c);
    }

    public static class CrimeCursorWrapper extends CursorWrapper {

        public CrimeCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Crime getCrime(){
            String uuid = getString(getColumnIndex(CrimeTable.Cols.UUID));
            String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
            long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
            int solved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
            String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

            Crime c = new Crime(UUID.fromString(uuid));
            c.setTitle(title);
            c.setDate(new Date(date));
            c.setSolved(solved != 0);
            c.setSuspect(suspect);
            return c;
        }
    }
}
