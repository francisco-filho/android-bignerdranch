package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by francisco on 22/09/17.
 */

public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.geoquiz.date";
    private DatePicker mDatePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null, false);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date);
        mDatePicker.init(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                null);

        Dialog d = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    int year = mDatePicker.getYear();
                    int month = mDatePicker.getMonth();
                    int day = mDatePicker.getDayOfMonth();
                    Date date1 = new GregorianCalendar(year, month, day).getTime();
                    sendResult(Activity.RESULT_OK, date1);
                })
                .create();
        return d;
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
