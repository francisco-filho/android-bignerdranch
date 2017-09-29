package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
    private static final int REQUEST_CONTACT = 1;
    private static String ARGS_CRIME_ID = "crimeId";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;

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
        mReportButton = (Button) fragmentLayout.findViewById(R.id.crime_report);
        mSuspectButton = (Button) fragmentLayout.findViewById(R.id.crime_suspect);

        if (mCrime.getSuspect() != null)
            mSuspectButton.setText(mCrime.getSuspect());

        final Intent pickContact = new Intent(
                Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        mSuspectButton.setOnClickListener(v -> {
            startActivityForResult(pickContact, REQUEST_CONTACT);
        });

        mReportButton.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
            i = Intent.createChooser(i, getString(R.string.send_report));
            startActivity(i);
        });

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

        PackageManager pkgm = getActivity().getPackageManager();
        if (pkgm.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }
        return fragmentLayout;
    }

    private String getCrimeReport(){
        String solvedString = mCrime.isSolved() ?
                getString(R.string.crime_report_solved) :
                getString(R.string.crime_report_unsolved);

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect() == null ?
                getString(R.string.crime_report_no_suspect) :
                getString(R.string.crime_report_suspect, mCrime.getSuspect());

        return getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) return;

        if (requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        } else if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            try {
                if (c.getCount()==0) return;
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }
}
