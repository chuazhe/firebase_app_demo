package com.example.chuazhe.firebase_app_demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.chuazhe.firebase_app_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView reminder;
    TextView reminder3;
    int countReport;
    int countEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        setTitle("Home Screen");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        reminder = (TextView) findViewById(R.id.reminder);
        reminder3 = (TextView) findViewById(R.id.reminder3);

        reminder.setText("");
        reminder3.setText("");


        countReport = 0;
        countEvent = 0;

        db.collection("reports").whereEqualTo("read", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                countReport++;
                            }
                        } else {
                            System.out.println("fail");
                        }
                    }
                });

        db1.collection("event").whereEqualTo("approved", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                countEvent++;
                            }
                        } else {
                            System.out.println("fail");
                        }
                    }
                });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reminder.setText(Integer.toString(countReport) + " unread message!");
                reminder3.setText(Integer.toString(countEvent) + " pending approval event!");
                //Do something after 3000ms
            }
        }, 3000);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inbox) {
            startActivity(new Intent(AdminMainActivity.this, ViewReportActivity.class));

        } else if (id == R.id.nav_home) {
            AdminMainActivity.super.onStart();

        } else if (id == R.id.nav_pending_approval_event) {
            startActivity(new Intent(AdminMainActivity.this, ViewPendingApprovalEvent.class));
        } else if (id == R.id.nav_announcement) {
            startActivity(new Intent(AdminMainActivity.this, AdminAnnouncementHelper.class));

        } else if (id == R.id.nav_manage_user) {
            startActivity(new Intent(AdminMainActivity.this, AdminManageUserActivity.class));

        } else if (id == R.id.nav_manage_event) {
            startActivity(new Intent(AdminMainActivity.this, AdminManageEventActivity.class));
        } else if (id == R.id.nav_analyse_mycsd) {
            startActivity(new Intent(AdminMainActivity.this, AdminAnalyticsMyCSDFragmentMain.class));

        } else if (id == R.id.nav_analyse_problem) {
            startActivity(new Intent(AdminMainActivity.this, AdminAnalyticsProblemFragmentMain.class));

        } else if (id == R.id.nav_report) {
            startActivity(new Intent(AdminMainActivity.this, ReportActivity.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
