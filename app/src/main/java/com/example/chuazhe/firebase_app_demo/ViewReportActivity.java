package com.example.chuazhe.firebase_app_demo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chuazhe.firebase_app_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;


public class ViewReportActivity extends AppCompatActivity {

    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    ListView listview;
    ArrayList<Report> reportArrayList = new ArrayList<Report>();
    Report reportObj;
    Calendar myCalendar;
    MyReportAdapter adapter;
    FloatingActionButton searchCalendar;
    FloatingActionButton searchUser;
    FloatingActionButton searchUnread;
    DatePickerDialog.OnDateSetListener date;
    String sv;
    SwipeRefreshLayout swipeLayout;
    int action_code;
    int inputMatricNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        setTitle("View Report");
        action_code = 1;
        myCalendar = Calendar.getInstance();
        searchCalendar = (FloatingActionButton) findViewById(R.id.calendar);
        searchUser = (FloatingActionButton) findViewById(R.id.searchUser);
        searchUnread = (FloatingActionButton) findViewById(R.id.searchUnread);


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfDate) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfDate);
                searchWithCalendar();
            }

        };

        searchCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ViewReportActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        searchUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewReportActivity.this);
                final EditText input = new EditText(ViewReportActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setHint("Enter Matric No");
                builder.setView(input);

                builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputMatricNo = Integer.parseInt(input.getText().toString());
                        searchWithUser();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        searchUnread.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchWithUnread();
            }
        });


        // Getting SwipeContainerLayout
        swipeLayout = findViewById(R.id.swipe_container);
        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                        switch (action_code) {
                            case 1:
                                onStart();
                                break;
                            case 2:
                                searchWithCalendar();
                                break;
                            case 3:
                                searchWithUser();
                                break;
                            case 4:
                                searchWithUnread();
                                break;
                        }
                    }
                }, 1500); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright));

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (action_code == 1) {
            reportArrayList.clear();

            listview = (ListView) findViewById(R.id.listview);
            //get all item from the collection named reports
            db1.collection("reports")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    reportObj = new Report();
                                    Map<String, Object> data = document.getData();
                                    reportObj.setID(document.getId());
                                    //reportObj.setDetails(data.get("details").toString());
                                    reportObj.setMatricNo(data.get("matricNo").toString());
                                    //reportObj.setName(data.get("nameStudent").toString());
                                    reportObj.setSubmitTime(data.get("submitTime").toString());
                                    reportObj.setRead(Boolean.parseBoolean(data.get("read").toString()));
                                    //reportObj.setProblemType(data.get("problemType").toString());
                                    reportArrayList.add(reportObj);
                                }
                                adapter = new MyReportAdapter(listview.getContext(), R.layout.list_report, reportArrayList);
                                listview.setAdapter(adapter);

                                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> a, View v, int position,
                                                            long id) {
                                        String reportID = reportArrayList.get(position).getID();
                                        Intent intent = new Intent(getBaseContext(), ViewSpecifiedReportActivity.class);
                                        intent.putExtra("REPORT_ID", reportID);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                System.out.println("fail");
                            }
                        }
                    });
        } else if (action_code == 2) {
            searchWithCalendar();
        } else if (action_code == 3) {
            searchWithUser();
        } else if (action_code == 4) {
            searchWithUnread();
        }

    }

    private void searchWithCalendar() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        action_code = 2;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        sv = sdf.format(myCalendar.getTime());

        reportArrayList.clear();
        adapter.notifyDataSetChanged();

        db1.collection("reports")
                .whereEqualTo("submitDate", sv)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                reportObj = new Report();
                                Map<String, Object> data = document.getData();
                                reportObj.setID(document.getId());
                                reportObj.setDetails(data.get("details").toString());
                                reportObj.setMatricNo(data.get("matricNo").toString());
                                reportObj.setName(data.get("nameStudent").toString());
                                reportObj.setSubmitDate(data.get("submitDate").toString());
                                reportObj.setSubmitTime(data.get("submitTime").toString());
                                reportObj.setProblemType(data.get("problemType").toString());
                                reportObj.setRead(Boolean.parseBoolean(data.get("read").toString()));
                                reportArrayList.add(reportObj);
                            }
                            MyReportAdapter adapter = new MyReportAdapter(listview.getContext(), R.layout.list_report, reportArrayList);
                            listview.setAdapter(adapter);

                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> a, View v, int position,
                                                        long id) {
                                    String reportID = reportArrayList.get(position).getID();
                                    Intent intent = new Intent(getBaseContext(), ViewSpecifiedReportActivity.class);
                                    intent.putExtra("REPORT_ID", reportID);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            System.out.println("fail");
                        }
                    }
                });


    }

    private void searchWithUser() {
        action_code = 3;

        reportArrayList.clear();
        adapter.notifyDataSetChanged();

        db1.collection("reports")
                .whereEqualTo("matricNo", inputMatricNo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                reportObj = new Report();
                                Map<String, Object> data = document.getData();
                                reportObj.setID(document.getId());
                                reportObj.setDetails(data.get("details").toString());
                                reportObj.setMatricNo(data.get("matricNo").toString());
                                reportObj.setName(data.get("nameStudent").toString());
                                reportObj.setSubmitDate(data.get("submitDate").toString());
                                reportObj.setSubmitTime(data.get("submitTime").toString());
                                reportObj.setProblemType(data.get("problemType").toString());
                                reportObj.setRead(Boolean.parseBoolean(data.get("read").toString()));
                                reportArrayList.add(reportObj);
                            }
                            MyReportAdapter adapter = new MyReportAdapter(listview.getContext(), R.layout.list_report, reportArrayList);
                            listview.setAdapter(adapter);

                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> a, View v, int position,
                                                        long id) {
                                    String reportID = reportArrayList.get(position).getID();
                                    Intent intent = new Intent(getBaseContext(), ViewSpecifiedReportActivity.class);
                                    intent.putExtra("REPORT_ID", reportID);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            System.out.println("fail");
                        }
                    }
                });

    }

    private void searchWithUnread() {
        action_code = 4;

        reportArrayList.clear();
        adapter.notifyDataSetChanged();

        db1.collection("reports")
                .whereEqualTo("read", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                reportObj = new Report();
                                Map<String, Object> data = document.getData();
                                reportObj.setID(document.getId());
                                reportObj.setDetails(data.get("details").toString());
                                reportObj.setMatricNo(data.get("matricNo").toString());
                                reportObj.setName(data.get("nameStudent").toString());
                                reportObj.setSubmitDate(data.get("submitDate").toString());
                                reportObj.setSubmitTime(data.get("submitTime").toString());
                                reportObj.setProblemType(data.get("problemType").toString());
                                reportObj.setRead(Boolean.parseBoolean(data.get("read").toString()));
                                reportArrayList.add(reportObj);
                            }
                            MyReportAdapter adapter = new MyReportAdapter(listview.getContext(), R.layout.list_report, reportArrayList);
                            listview.setAdapter(adapter);

                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> a, View v, int position,
                                                        long id) {
                                    String reportID = reportArrayList.get(position).getID();
                                    Intent intent = new Intent(getBaseContext(), ViewSpecifiedReportActivity.class);
                                    intent.putExtra("REPORT_ID", reportID);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            System.out.println("fail");
                        }
                    }
                });
    }

}