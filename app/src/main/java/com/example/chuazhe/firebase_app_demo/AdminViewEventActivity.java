package com.example.chuazhe.firebase_app_demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chuazhe.firebase_app_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class AdminViewEventActivity extends AppCompatActivity {

    String getId;
    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    ListView listview;
    FloatingActionButton deleteBtn;
    FloatingActionButton editBtn;
    ArrayList<String> array_list = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_event);

        getId = getIntent().getStringExtra("EVENT_ID");
        listview = (ListView) findViewById(R.id.listview);
        deleteBtn = (FloatingActionButton) findViewById(R.id.deleteBtn);
        editBtn = (FloatingActionButton) findViewById(R.id.editBtn);
        listview.setDivider(null);
        arrayAdapter = new ArrayAdapter<String>(
                AdminViewEventActivity.this,
                android.R.layout.simple_list_item_1,
                array_list);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewEventActivity.this);

                builder
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                db1.collection("event").document(getId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                System.out.println("DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                System.out.println("Error deleting document");
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminViewEventActivity.this, AdminEditEventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EVENT_ID", getId);
                AdminManageEventActivity.getContext().startActivity(intent);
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
                        onStart();
                        Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                    }
                }, 1500); // Delay in millis
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        array_list.clear();
        arrayAdapter.notifyDataSetChanged();

        DocumentReference docRef = db1.collection("event").document(getId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        array_list.add("Event Name: " + (data.get("eventName").toString()));
                        listview.setAdapter(arrayAdapter);

                    } else {
                        System.out.println("No such document");
                    }
                } else {
                    System.out.println("failed");
                }
            }
        });
    }
}
