package com.example.chuazhe.firebase_app_demo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chuazhe.firebase_app_demo.R;

import java.util.List;


public class MyManageUserAdapter extends RecyclerView.Adapter<MyManageUserAdapter.UserViewHolder> {

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView containerName;
        TextView containerAge;
        ImageView containerPhoto;

        UserViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            containerName = (TextView) itemView.findViewById(R.id.container_name);
            containerAge = (TextView) itemView.findViewById(R.id.container_age);
            containerPhoto = (ImageView) itemView.findViewById(R.id.container_photo);
        }
    }

    List<User> UserList;
    Context context;

    MyManageUserAdapter(List<User> UserList, Context context) {
        this.UserList = UserList;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        UserViewHolder pvh = new UserViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder containerViewHolder, final int i) {
        containerViewHolder.containerName.setText(UserList.get(i).getName());
        containerViewHolder.containerAge.setText("Matric No: " + Integer.toString(UserList.get(i).getMatricNo()));

        containerViewHolder.containerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminManageUserActivity.getContext(), AdminViewUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("USER_ID", UserList.get(i).getID());
                AdminManageUserActivity.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

}