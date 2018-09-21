package com.example.chuazhe.firebase_app_demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chuazhe.firebase_app_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {
    private EditText inputDescrip;
    private TextView image_indicator;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
    FirebaseFirestore db3 = FirebaseFirestore.getInstance();
    ProgressDialog progress;
    String currentDate;
    String currentTime;
    String problemType;
    int newcountID;
    int countID;
    int countPhoto;
    String uniqueID;
    String matricNo;
    String nameStudent;
    String reportDescrip;
    String emailStudent;

    //a Uri object to store file path
    private Uri filePath = null;
    private Uri filePath2 = null;
    private Uri filePath3 = null;


    private void generateUniqueReportID() {
        DocumentReference docRef = db.collection("unique").document("reports");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> data = document.getData();
                    if (document.exists()) {
                        String str = String.valueOf(data.get("countID"));
                        countID = Integer.parseInt(str);
                        newcountID = countID + 1;
                        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
                        DateFormat timeFormat2 = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date();
                        String currentDate = dateFormat2.format(date);
                        currentDate = currentDate.replace("/", "");
                        String currentTime = timeFormat2.format(date);
                        str = String.format("%06d", countID);
                        uniqueID = currentDate + "_" + currentTime + "_R" + str;
                    } else {
                        System.out.println("No such document");
                    }
                }
            }
        });

    }

    private void updateUniqueReportID() {

        final DocumentReference sfDocRef2 = db1.collection("unique").document("reports");
        db3.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(sfDocRef2, "countID", newcountID); //string
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Report a Problem");

        generateUniqueReportID();

        // Set up the login form.
        image_indicator = (TextView) findViewById(R.id.image_indicator);
        inputDescrip = (EditText) findViewById(R.id.idDescrip);
        progress = new ProgressDialog(ReportActivity.this);
        Button sendBtn = (Button) findViewById(R.id.sendBtn);
        Button buttonUpload = (Button) findViewById(R.id.uploadBtn);
        Button clearBtn = (Button) findViewById(R.id.clearBtn);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_of_problem, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                problemType = String.valueOf(item);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                System.out.println("qwert" + user.getUid());
                DocumentReference docRef2 = db2.collection("user").document(user.getUid());
                docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                matricNo = (data.get("matricNo").toString());
                                nameStudent = (data.get("userName").toString());
                                emailStudent = (data.get("userEmail").toString());
                            } else {
                                System.out.println("No such document");
                            }
                        }

                    }
                });
                //Do something after 3000ms
            }
        }, 3000);

        //For uploading image
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 234);
            }
        });

        //For uploading image
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countPhoto = 0;
                filePath = null;
                filePath2 = null;
                filePath3 = null;
                image_indicator.setText("No photo will be uploaded");
            }
        });

        /*Action for sent button*/
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                progress.setTitle("Loading");
                progress.setMessage("Sending Report...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();

                reportDescrip = String.valueOf(inputDescrip.getText());
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                currentDate = dateFormat.format(date);
                currentTime = timeFormat.format(date);

                int result = Integer.parseInt(matricNo);

                //Create an object
                Map<String, Object> obj = new HashMap<>();

                obj.put("nameStudent", nameStudent);
                obj.put("matricNo", result);
                obj.put("details", reportDescrip);
                obj.put("submitDate", currentDate);
                obj.put("submitTime", currentTime);
                obj.put("problemType", problemType);
                obj.put("emailStud", emailStudent);
                obj.put("read", false);


                //Put into the table reports
                db1.collection("reports").document(uniqueID).set(obj)
                        //If success
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateUniqueReportID();
                                Toast.makeText(ReportActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                System.out.println("Success");
                                finish();
                            }
                        })
                        //If failure
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ReportActivity.this, "Failed, no Internet!", Toast.LENGTH_SHORT).show();
                                System.out.println("Fail");
                                progress.dismiss();
                            }
                        });


                System.out.println(filePath);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                ;
                StorageReference storageReference = storage.getReference();
                System.out.println("FCM registration token:" + FirebaseInstanceId.getInstance().getToken());

                if (filePath != null) {
                    StorageReference ref = storageReference.child("images/" + uniqueID + "_1");
                    ref.putFile(filePath);
                }
                if (filePath2 != null) {
                    StorageReference ref = storageReference.child("images/" + uniqueID + "_2");
                    ref.putFile(filePath2);
                }
                if (filePath3 != null) {
                    StorageReference ref = storageReference.child("images/" + uniqueID + "_3");
                    ref.putFile(filePath3);
                }
            }
        });
    }

    ;


    //To watch the activity of the intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 234 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            countPhoto++;
            switch (countPhoto) {
                case 0:
                    image_indicator.setText("No photo will be uploaded");
                    break;
                case 1:
                    filePath = data.getData();
                    image_indicator.setText("1 photo will be uploaded");
                    break;
                case 2:
                    filePath2 = data.getData();
                    image_indicator.setText("2 photos will be uploaded");
                    break;
                case 3:
                    filePath3 = data.getData();
                    image_indicator.setText("3 photos will be uploaded");
                    break;

            }
        }

    }

}


