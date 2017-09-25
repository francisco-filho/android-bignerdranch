package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by francisco on 19/09/17.
 */

public class CrimeFragment extends Fragment {
    private static final int REQUEST_DATE = 0;
    private static String ARGS_CRIME_ID = "crimeId";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_ID, uuid);
        CrimeFragment cf = new CrimeFragment();
        cf.setArguments(args);
        return cf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID uuid = (UUID)getArguments()
                .getSerializable(ARGS_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(uuid);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) fragmentLayout.findViewById(R.id.crime_title);
        mDateButton = (Button) fragmentLayout.findViewById(R.id.crime_date);
        mSolvedCheckBox = (CheckBox) fragmentLayout.findViewById(R.id.crime_solved);

        mTitleField.setText(mCrime.getTitle());
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mDateButton.setText(mCrime.getDate().toString());

        mDateButton.setOnClickListener((view) -> {
            FragmentManager fm = getFragmentManager();
            DatePickerFragment datePickerFragment =
                    DatePickerFragment.newInstance(mCrime.getDate());
            datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
            datePickerFragment.show(fm, "DATE_PICKER_TAG");
        });


        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        mSolvedCheckBox.setOnCheckedChangeListener((view, checked) -> {
            mCrime.setSolved(checked);
        });

        return fragmentLayout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DATE && resultCode == Activity.RESULT_OK) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }
    }
}
