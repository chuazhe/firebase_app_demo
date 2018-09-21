package com.example.chuazhe.firebase_app_demo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chuazhe.firebase_app_demo.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminAnalyticsMyCSDFragment_2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminAnalyticsMyCSDFragment_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAnalyticsMyCSDFragment_2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Override
    @SuppressWarnings("unchecked")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PieChart pieChart = (PieChart) view.findViewById(R.id.piechart);

        Bundle bundle = this.getArguments();
        ArrayList<Double> count = new ArrayList<>();

        count = ((ArrayList<Double>) bundle.getSerializable("result2"));

        pieChart.setUsePercentValues(true);

        pieChart.animateX(1500);


        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.


        List<PieEntry> entries1 = new ArrayList<>();

        entries1.add(new PieEntry(Float.parseFloat(count.get(0) + "f"), "0-50"));
        entries1.add(new PieEntry(Float.parseFloat(count.get(1) + "f"), "51-100"));
        entries1.add(new PieEntry(Float.parseFloat(count.get(2) + "f"), "101-150"));
        entries1.add(new PieEntry(Float.parseFloat(count.get(3) + "f"), "151-200"));
        entries1.add(new PieEntry(Float.parseFloat(count.get(4) + "f"), "201-250"));
        entries1.add(new PieEntry(Float.parseFloat(count.get(5) + "f"), "251-300"));
        entries1.add(new PieEntry(Float.parseFloat(count.get(6) + "f"), "300++"));

        PieDataSet set = new PieDataSet(entries1, "");
        set.setValueTextSize(Float.parseFloat("15f"));
        set.setValueFormatter(new PercentFormatter());


        set.setColors(new int[]{R.color.red, R.color.blue, R.color.orange, R.color.green, R.color.purple, R.color.yellow, R.color.teal}, view.getContext());


        PieData data2 = new PieData(set);

        pieChart.setData(data2);
        pieChart.setEntryLabelColor(R.color.black);
        pieChart.getDescription().setText("Percentage");

    }

    public AdminAnalyticsMyCSDFragment_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAnalyticsMyCSDFragment_2.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAnalyticsMyCSDFragment_2 newInstance(String param1, String param2) {
        AdminAnalyticsMyCSDFragment_2 fragment = new AdminAnalyticsMyCSDFragment_2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_analytics_my_csdfragment_2, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
