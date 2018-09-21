package com.example.chuazhe.firebase_app_demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class ViewSpecifiedReportActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    ArrayList<Report> reportArrayList = new ArrayList<Report>();
    MySpecifiedReportAdapter adapter;
    Report reportObj;
    ListView listview;
    FloatingActionButton deleteBtn;
    ImageView image_view_1;
    ImageView image_view_2;
    ImageView image_view_3;
    int count;
    String getId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_specified_report);
        getId = getIntent().getStringExtra("REPORT_ID");
        setTitle(getId);
        listview = (ListView) findViewById(R.id.report_information);
        listview.setDivider(null);
        image_view_1 = (ImageView) findViewById(R.id.image_view_1);
        image_view_2 = (ImageView) findViewById(R.id.image_view_2);
        image_view_3 = (ImageView) findViewById(R.id.image_view_3);
        deleteBtn = (FloatingActionButton) findViewById(R.id.deleteBtn);


        DocumentReference docRef = db1.collection("reports").document(getId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        reportObj = new Report();
                        Map<String, Object> data = document.getData();
                        reportObj.setID(document.getId());
                        reportObj.setDetails(data.get("details").toString());
                        reportObj.setMatricNo(data.get("matricNo").toString());
                        reportObj.setName(data.get("nameStudent").toString());
                        reportObj.setSubmitDate(data.get("submitDate").toString());
                        reportObj.setSubmitTime(data.get("submitTime").toString());
                        reportObj.setProblemType(data.get("problemType").toString());
                        reportObj.setEmail(data.get("emailStud").toString());
                        reportObj.setRead(Boolean.parseBoolean(data.get("read").toString()));
                        reportArrayList.add(reportObj);

                        adapter = new MySpecifiedReportAdapter(listview.getContext(), R.layout.list_specified_report, reportArrayList);
                        listview.setAdapter(adapter);


                        for (count = 1; count <= 3; count++) {
                            String str = Integer.toString(count);
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            // Create a reference with an initial file path and name
                            StorageReference gsReference = storage.getReferenceFromUrl("gs://eventsusm.appspot.com/images/" + document.getId() + "_" + str);
                            gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uriStr = uri.toString();
                                    if (image_view_1.getDrawable() != null) {
                                        image_view_1.setVisibility(View.VISIBLE);
                                        Picasso.get().load(uriStr).into(image_view_1);
                                    } else if (image_view_2.getDrawable() != null) {
                                        image_view_2.setVisibility(View.VISIBLE);
                                        Picasso.get().load(uriStr).into(image_view_2);
                                    } else if (image_view_3.getDrawable() != null) {
                                        image_view_3.setVisibility(View.VISIBLE);
                                        Picasso.get().load(uriStr).into(image_view_3);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    System.out.println("no image..");
                                }
                            });
                        }

                        if (reportObj.getRead() == false) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            final DocumentReference sfDocRef = db.collection("reports").document(getId);

                            db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    transaction.update(sfDocRef, "read", true);

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
                    } else {
                        System.out.println("No such document");
                    }
                } else {
                    System.out.println("failed");
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSpecifiedReportActivity.this);

                builder
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                db.collection("reports").document(getId)
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
    }}


