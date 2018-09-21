package com.example.chuazhe.firebase_app_demo;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.chuazhe.firebase_app_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class AdminAnalyticsProblemFragmentMain extends AppCompatActivity implements AdminAnalyticsProblemFragment_1.OnFragmentInteractionListener, AdminAnalyticsProblemFragment_2.OnFragmentInteractionListener {

    int count_1 = 0;
    int count_2 = 0;
    int count_3 = 0;
    int count_4 = 0;
    double percentage_1 = 0;
    double percentage_2 = 0;
    double percentage_3 = 0;
    double percentage_4 = 0;

    FirebaseFirestore db1 = FirebaseFirestore.getInstance();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bar_chart:
                    switchToFragment1();
                    return true;
                case R.id.pie_chart:
                    switchToFragment2();
                    return true;
            }
            return false;
        }
    };


    public void switchToFragment1() {
        FragmentManager manager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        Integer[] array = {count_1, count_2, count_3, count_4};
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(array));
        bundle.putIntegerArrayList("result", list);
        AdminAnalyticsProblemFragment_1 myFragment = new AdminAnalyticsProblemFragment_1();
        myFragment.setArguments(bundle);
        manager.beginTransaction().replace(R.id.container, myFragment).commit();
    }

    public void switchToFragment2() {
        FragmentManager manager = getSupportFragmentManager();
        Bundle bundle2 = new Bundle();
        Double[] array2 = {percentage_1, percentage_2, percentage_3, percentage_4};
        ArrayList<Double> list2 = new ArrayList<>(Arrays.asList(array2));
        bundle2.putSerializable("result2", list2);
        AdminAnalyticsProblemFragment_2 myFragment2 = new AdminAnalyticsProblemFragment_2();
        myFragment2.setArguments(bundle2);
        manager.beginTransaction().replace(R.id.container, myFragment2).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_analytics_problem_fragment_main);

        final ProgressDialog progress;

        progress = new ProgressDialog(AdminAnalyticsProblemFragmentMain.this);
        progress.setTitle("Loading");
        progress.setMessage("Fetching Data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();


        db1.collection("reports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                calculateForBarGraph(Integer.parseInt(data.get("problemCode").toString()));
                            }
                            calculateForPieChart();
                        } else {
                            System.out.println("fail");
                        }
                    }
                });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switchToFragment1();
                progress.dismiss();
            }
        }, 2000);
    }

    public void calculateForBarGraph(int input) {
        switch (input) {
            case 1:
                count_1++;
                break;
            case 2:
                count_2++;
                break;
            case 3:
                count_3++;
                break;
            case 4:
                count_4++;
                break;
        }
    }

    public void calculateForPieChart() {
        double total = count_1 + count_2 + count_3 + count_4;
        percentage_1 = round(((count_1 / total) * 100), 1);
        percentage_2 = round(((count_2 / total) * 100), 1);
        percentage_3 = round(((count_3 / total) * 100), 1);
        percentage_4 = round(((count_4 / total) * 100), 1);
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
