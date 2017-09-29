package com.bignerdranch.android.criminalintent.database;

/**
 * Created by francisco on 27/09/17.
 */

public class CrimeDbSchema {

    public static class CrimeTable {
        public static final String NAME = "crimes";

        public static class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}
