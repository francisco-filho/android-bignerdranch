package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by francisco on 19/09/17.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes = new ArrayList<>();

    private CrimeLab(Context context){}

    public static CrimeLab get(Context context){
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for (Crime c : mCrimes) {
            if (id.equals(c.getId())) {
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }
}
