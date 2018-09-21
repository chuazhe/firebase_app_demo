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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminAnalyticsProblemFragment_1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminAnalyticsProblemFragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAnalyticsProblemFragment_1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AdminAnalyticsProblemFragment_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAnalyticsProblemFragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAnalyticsProblemFragment_1 newInstance(String param1, String param2) {
        AdminAnalyticsProblemFragment_1 fragment = new AdminAnalyticsProblemFragment_1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BarChart barChart = (BarChart) view.findViewById(R.id.barchart);

        Bundle bundle = this.getArguments();
        ArrayList<Integer> count = new ArrayList<>();
        count = bundle.getIntegerArrayList("result");


        barChart.animateY(1500);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, count.get(0))); //Problem code 1
        entries.add(new BarEntry(1, count.get(1))); //2
        entries.add(new BarEntry(2, count.get(2))); //3
        entries.add(new BarEntry(3, count.get(3))); //4


        BarDataSet dataSet = new BarDataSet(entries, "Type of Problem");

        dataSet.setValueFormatter(new AdminAnalyticsProblemFragment_1.MyValueFormatter());
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(Float.parseFloat("15f"));
        dataSet.setColors(new int[]{R.color.red, R.color.blue, R.color.orange, R.color.green, R.color.purple, R.color.yellow, R.color.teal}, view.getContext());


        final HashMap<Integer, String> numMap = new HashMap<>();
        numMap.put(0, "Illegal Event");
        numMap.put(1, "Wrong allocation of MyCSD ");
        numMap.put(2, "Update Info");
        numMap.put(3, "Feedback");
        numMap.put(4, "Others");

        XAxis xAxis = barChart.getXAxis();

        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(numMap.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return numMap.get((int) value);
            }
        });

        YAxis yAxisLeft = barChart.getAxisLeft();
        barChart.getAxisRight().setEnabled(false);
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setGranularityEnabled(true); // Required to enable granularity
        barChart.getDescription().setEnabled(false);


        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.setDrawValueAboveBar(true);
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
        return inflater.inflate(R.layout.fragment_admin_analytics_problem_fragment_1, container, false);

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

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value); // e.g. append a dollar-sign
        }
    }
}
