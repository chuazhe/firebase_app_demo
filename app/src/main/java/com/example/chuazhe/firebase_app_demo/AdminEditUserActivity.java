package com.example.chuazhe.firebase_app_demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.Map;

public class AdminEditUserActivity extends AppCompatActivity {

    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    String getId;
    EditText inputMatricNo;
    EditText inputName;
    EditText inputEmail;
    EditText inputMyCSD;
    Button confirmBtn;
    Button cancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_user);
        getId = getIntent().getStringExtra("USER_ID");
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        inputMatricNo = (EditText) findViewById(R.id.inputMatricNo);
        inputName = (EditText) findViewById(R.id.inputName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputMyCSD = (EditText) findViewById(R.id.inputMyCSD);

        DocumentReference docRef = db1.collection("user").document(getId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        inputMatricNo.setText(data.get("matricNo").toString());
                        inputName.setText(data.get("userName").toString());
                        inputEmail.setText(data.get("userEmail").toString());
                        inputMyCSD.setText(data.get("MyCSD").toString());

                    } else {
                        System.out.println("No such document");
                    }
                } else {
                    System.out.println("failed");
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference sfDocRef = db.collection("user").document(getId);

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        transaction.update(sfDocRef, "matricNo", inputMatricNo.getText().toString());
                        transaction.update(sfDocRef, "userName", inputName.getText().toString()); //string
                        transaction.update(sfDocRef, "userEmail", inputEmail.getText().toString()); //string
                        transaction.update(sfDocRef, "MyCSD", Integer.parseInt(inputMyCSD.getText().toString())); //string

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

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }
}
