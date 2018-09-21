package com.example.chuazhe.firebase_app_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chuazhe.firebase_app_demo.R;

import java.util.List;

public class MySpecifiedReportAdapter extends ArrayAdapter<Report> {

    int resource;
    String response;
    Context context;

    public MySpecifiedReportAdapter(Context context, int resource, List<Report> items) {
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
        TextView matricNo = (TextView) reportsView.findViewById(R.id.matricNo);
        TextView nameStudent = (TextView) reportsView.findViewById(R.id.nameStudent);
        TextView submitMnT = (TextView) reportsView.findViewById(R.id.submitMnT);
        TextView problemType = (TextView) reportsView.findViewById(R.id.problemType);
        TextView details = (TextView) reportsView.findViewById(R.id.details);
        TextView studEmail = (TextView) reportsView.findViewById(R.id.submitEmail);
        matricNo.setText("Matric No: " + obj.getMatricNo());
        nameStudent.setText("Name: " + obj.getName());
        studEmail.setText("Email: " + obj.getEmail());
        submitMnT.setText("Submit Date & Time: " + obj.getSubmitDate() + " " + obj.getSubmitTime());
        problemType.setText("Type of Problem: " + obj.getProblemType());
        details.setText("Comments: " + obj.getDetails());
        return reportsView;
    }
}