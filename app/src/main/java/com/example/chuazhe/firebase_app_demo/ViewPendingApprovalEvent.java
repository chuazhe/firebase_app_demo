package com.example.chuazhe.firebase_app_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.chuazhe.firebase_app_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewPendingApprovalEvent extends AppCompatActivity {

    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    ListView listview;
    ArrayList<String> array_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pending_approval_event);
        listview = (ListView) findViewById(R.id.listview);
        array_list = new ArrayList<String>();


        db1.collection("event").whereEqualTo("approved", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId());
                                array_list.add(document.getId());
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    ViewPendingApprovalEvent.this,
                                    android.R.layout.simple_list_item_1,
                                    array_list);
                            listview.setAdapter(arrayAdapter);

                        } else {
                            System.out.println("fail");
                        }
                    }
                });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                String eventId = array_list.get(position);
                Intent intent = new Intent(getBaseContext(), ViewSpecifiedPendingApprovalEvent.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            }
        });
    }
}

