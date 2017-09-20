package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by francisco on 19/09/17.
 */

public class CrimeListFragment extends Fragment {
    RecyclerView mCrimeRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);

        updateIU();
        return v;
    }

    private void updateIU(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        CrimeAdapter adapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCrimeRecyclerView.setAdapter(adapter);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleText;
        private TextView mDateText;
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            mTitleText = (TextView)itemView.findViewById(R.id.crime_title);
            mDateText = (TextView)itemView.findViewById(R.id.crime_date);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime){
            mCrime = crime;
            mTitleText.setText(mCrime.getTitle());
            mDateText.setText(mCrime.getDate().toString());
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mCrime.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimeList;

        public CrimeAdapter(List<Crime> mCrimeList) {
            this.mCrimeList = mCrimeList;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime c = mCrimeList.get(position);
            holder.bind(c);
        }

        @Override
        public int getItemCount() {
            return mCrimeList.size();
        }
    }

}
