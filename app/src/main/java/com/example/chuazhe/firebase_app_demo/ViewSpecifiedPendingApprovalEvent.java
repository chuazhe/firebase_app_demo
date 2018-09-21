package com.example.chuazhe.firebase_app_demo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.chuazhe.firebase_app_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Map;

public class ViewSpecifiedPendingApprovalEvent extends AppCompatActivity {

    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    ArrayList<String> array_list= new ArrayList<String>();
    ListView listview;
    String getId;
    Button approveBtn;
    Button declineBtn;
    EditText commentBox;
    String comment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_specified_pending_approval_event);
        getId = getIntent().getStringExtra("EVENT_ID");
        setTitle(getId);
        listview = (ListView) findViewById(R.id.event_information);
        approveBtn=(Button) findViewById(R.id.approveBtn);
        declineBtn=(Button) findViewById(R.id.declineBtn);
        commentBox=(EditText) findViewById(R.id.commentBox);
        listview.setDivider(null);

        /*
        image_view_1 = (ImageView) findViewById(R.id.image_view_1);
        image_view_2 = (ImageView) findViewById(R.id.image_view_2);
        image_view_3 = (ImageView) findViewById(R.id.image_view_3);*/


        DocumentReference docRef = db1.collection("event").document(getId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        array_list.add("Event name: "+data.get("eventName").toString());

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                ViewSpecifiedPendingApprovalEvent.this,
                                android.R.layout.simple_list_item_1,
                                array_list );
                        listview.setAdapter(arrayAdapter);

                    } else {
                        System.out.println("No such document");
                    }
                } else {
                    System.out.println("failed");
                }
            }
        });


        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference sfDocRef = db.collection("event").document(getId);

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        transaction.update(sfDocRef, "approved", true); //string
                        System.out.println(commentBox.getText().toString());

                        finish();

                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Transaction success!");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Transaction failure.");
                            }
                        });

            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(commentBox.getText().toString());
                finish();


            }
        });
    }
}
