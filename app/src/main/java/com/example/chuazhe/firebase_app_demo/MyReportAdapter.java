package com.example.chuazhe.firebase_app_demo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chuazhe.firebase_app_demo.R;

import java.util.List;

public class MyReportAdapter extends ArrayAdapter<Report> {

    int resource;
    String response;
    Context context;

    public MyReportAdapter(Context context, int resource, List<Report> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LinearLayout reportsView;
        Report obj = getItem(position);
        if (convertView == null) {
            reportsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, reportsView, true);
        } else {
            reportsView = (LinearLayout) convertView;
        }
        TextView reportID = (TextView) reportsView.findViewById(R.id.reportID);
        TextView reportTime = (TextView) reportsView.findViewById(R.id.reportTime);
        reportID.setText(obj.getID());
        if (obj.getRead() == false) {
            reportID.setTypeface(null, Typeface.BOLD);
        }

        reportTime.setText("Matric Number: " + obj.getMatricNo());
        return reportsView;
    }
}