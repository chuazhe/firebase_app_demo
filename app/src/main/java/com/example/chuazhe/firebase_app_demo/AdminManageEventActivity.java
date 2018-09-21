package com.example.chuazhe.firebase_app_demo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;

import com.example.chuazhe.firebase_app_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AdminManageEventActivity extends Activity {

    List<EventObject> EventList = new ArrayList<>();
    private RecyclerView rv;
    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    EventObject obj;
    public static Context mContext;
    FloatingActionButton searchCalendar;
    String sv;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    MyManageEventAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getBaseContext();
        setContentView(R.layout.activity_admin_manage_event);
        searchCalendar = (FloatingActionButton) findViewById(R.id.searchCalendar);
        myCalendar = Calendar.getInstance();

        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();

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
                new DatePickerDialog(AdminManageEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public static Context getContext() {
        return mContext;
    }

    private void initializeData() {
        db1.collection("event")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                obj = new EventObject();
                                Map<String, Object> data = document.getData();
                                obj.setType(document.getId());
                                obj.setEventName(data.get("eventName").toString());
                                EventList.add(obj);
                            }
                            initializeAdapter();

                        } else {
                            System.out.println("fail");
                        }
                    }
                });
    }

    private void initializeAdapter() {
        adapter = new MyManageEventAdapter(EventList, AdminManageEventActivity.this);
        rv.setAdapter(adapter);
    }

    private void searchWithCalendar() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        sv = sdf.format(myCalendar.getTime());

        EventList.clear();
        adapter.notifyDataSetChanged();

        /*

        db1.collection("event")
                .whereEqualTo("", sv)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();

                                reportArrayList.add(reportObj);
                            }
                            initializeAdapter();

                            });
                        } else {
                            System.out.println("fail");
                        }
                    }
                });

*/
    }
}
