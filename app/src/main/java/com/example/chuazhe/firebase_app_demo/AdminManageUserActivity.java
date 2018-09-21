package com.example.chuazhe.firebase_app_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.chuazhe.firebase_app_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminManageUserActivity extends Activity {

    List<User> UserList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    private RecyclerView rv;
    String inputMatricNo;
    FloatingActionButton searchUser;
    User obj;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();

        setContentView(R.layout.activity_admin_manage_user);

        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        searchUser = (FloatingActionButton) findViewById(R.id.searchUser);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();

        searchUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminManageUserActivity.this);
                final EditText input = new EditText(AdminManageUserActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setHint("Enter Matric No");
                builder.setView(input);

                builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputMatricNo = input.getText().toString();
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
    }

    public static Context getContext() {
        return mContext;
    }

    private void initializeData() {
        db1.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                obj = new User();
                                Map<String, Object> data = document.getData();
                                obj.setId(document.getId());
                                obj.setName(data.get("userName").toString());
                                obj.setMatricNo(Integer.parseInt(data.get("matricNo").toString()));
                                UserList.add(obj);
                            }
                            initializeAdapter();

                        } else {
                            System.out.println("fail");
                        }
                    }
                });
    }


    private void searchWithUser() {

        UserList.clear();
        db.collection("user")
                .whereEqualTo("matricNo", inputMatricNo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                obj = new User();
                                Map<String, Object> data = document.getData();
                                obj.setId(document.getId());
                                obj.setName(data.get("userName").toString());
                                obj.setMatricNo(Integer.parseInt(data.get("matricNo").toString()));
                                UserList.add(obj);
                            }
                            initializeAdapter();
                        } else {
                            System.out.println("fail");
                        }
                    }
                });

    }

    private void initializeAdapter() {
        MyManageUserAdapter adapter = new MyManageUserAdapter(UserList, this);
        rv.setAdapter(adapter);
    }

}
