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

public class AdminAnalyticsMyCSDFragmentMain extends AppCompatActivity implements AdminAnalyticsMyCSDFragment_1.OnFragmentInteractionListener, AdminAnalyticsMyCSDFragment_2.OnFragmentInteractionListener {

    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
    int count_0_to_50 = 0;
    int count_51_to_100 = 0;
    int count_101_to_150 = 0;
    int count_151_to_200 = 0;
    int count_201_to_250 = 0;
    int count_251_to_300 = 0;
    int count_301_and_more = 0;
    double percentage_0_to_50 = 0;
    double percentage_51_to_100 = 0;
    double percentage_101_to_150 = 0;
    double percentage_151_to_200 = 0;
    double percentage_201_to_250 = 0;
    double percentage_251_to_300 = 0;
    double percentage_301_and_more = 0;


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
        Integer[] array = {count_0_to_50, count_51_to_100, count_101_to_150, count_151_to_200, count_201_to_250, count_251_to_300, count_301_and_more};
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(array));
        bundle.putIntegerArrayList("result", list);
        AdminAnalyticsMyCSDFragment_1 myFragment = new AdminAnalyticsMyCSDFragment_1();
        myFragment.setArguments(bundle);
        manager.beginTransaction().replace(R.id.container, myFragment).commit();
    }

    public void switchToFragment2() {
        FragmentManager manager = getSupportFragmentManager();
        Bundle bundle2 = new Bundle();
        Double[] array2 = {percentage_0_to_50, percentage_51_to_100, percentage_101_to_150, percentage_151_to_200, percentage_201_to_250, percentage_251_to_300, percentage_301_and_more};
        ArrayList<Double> list2 = new ArrayList<>(Arrays.asList(array2));
        bundle2.putSerializable("result2", list2);
        AdminAnalyticsMyCSDFragment_2 myFragment2 = new AdminAnalyticsMyCSDFragment_2();
        myFragment2.setArguments(bundle2);
        manager.beginTransaction().replace(R.id.container, myFragment2).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_analytics_my_csdfragment_main);

        final ProgressDialog progress;

        progress = new ProgressDialog(AdminAnalyticsMyCSDFragmentMain.this);
        progress.setTitle("Loading");
        progress.setMessage("Fetching Data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();


        db1.collection("user")
                .whereEqualTo("accessCode", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                calculateForBarGraph(Integer.parseInt(data.get("MyCSD").toString()));
                            }
                            calculateForPie();
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

    @Override
    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }

    public void calculateForBarGraph(int input) {
        if (input > 300) {
            count_301_and_more++;
        } else if (input > 250) {
            count_251_to_300++;
        } else if (input > 200) {
            count_201_to_250++;
        } else if (input > 150) {
            count_151_to_200++;
        } else if (input > 100) {
            count_101_to_150++;
        } else if (input > 50) {
            count_51_to_100++;
        } else {
            count_0_to_50++;
        }
    }

    public void calculateForPie() {
        double total = count_0_to_50 + count_51_to_100 + count_101_to_150 + count_151_to_200 + count_201_to_250 + count_251_to_300 + count_301_and_more;
        percentage_0_to_50 = round(((count_0_to_50 / total) * 100), 1);
        percentage_51_to_100 = round(((count_51_to_100 / total) * 100), 1);
        percentage_101_to_150 = round(((count_101_to_150 / total) * 100), 1);
        percentage_151_to_200 = round(((count_151_to_200 / total) * 100), 1);
        percentage_201_to_250 = round(((count_201_to_250 / total) * 100), 1);
        percentage_251_to_300 = round(((count_251_to_300 / total) * 100), 1);
        percentage_301_and_more = round(((count_301_and_more / total) * 100), 1);

    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
