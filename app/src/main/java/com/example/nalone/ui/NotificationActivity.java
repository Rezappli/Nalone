package com.example.nalone.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nalone.HomeActivity;
import com.example.nalone.R;
import com.example.nalone.objects.Notification;
import com.example.nalone.objects.User;
import com.example.nalone.util.Horloge;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.setUserImage;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerNotif;
    private FirestoreRecyclerAdapter adapter;
    private ImageView buttonBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerNotif = findViewById(R.id.recyclerNotif);
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        HomeActivity.buttonNotif.setImageDrawable(getResources().getDrawable(R.drawable.notification_none));
        adapterNotif();
    }

    private void adapterNotif() {

        Query query = mStoreBase.collection("users").document(USER_ID).collection("notifications").orderBy("mDate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Notification> options = new FirestoreRecyclerOptions.Builder<Notification>().setQuery(query, Notification.class).build();

        adapter = new FirestoreRecyclerAdapter<Notification, NotifViewHolder>(options) {
            @NonNull
            @Override
            public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notif, parent, false);
                return new NotifViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final NotifViewHolder userViewHolder, int i, @NonNull final Notification n) {
               // Notification notification;

                final User[] u = new User[1];
                userViewHolder.nomOwner.setText(n.getmOwner());
                userViewHolder.descNotif.setText(n.getmDesc());
                mStoreBase.collection("users").whereEqualTo("uid", n.getmOwnerRef().getId()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot doc : task.getResult()){
                                        u[0] = doc.toObject(User.class);
                                    }
                                }
                                setUserImage(u[0], getApplicationContext(), userViewHolder.imagePerson);
                            }
                        });

                userViewHolder.dateNotif.setText(Horloge.verifDay(n.getmDate()));
                //loading.setVisibility(View.GONE);


            }
        };
        recyclerNotif.setHasFixedSize(true);
        recyclerNotif.setLayoutManager(new LinearLayoutManager(this));
        recyclerNotif.setAdapter(adapter);
        adapter.startListening();
    }




    private class NotifViewHolder extends RecyclerView.ViewHolder {

        private TextView nomOwner;
        private TextView descNotif, dateNotif;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);

            nomOwner = itemView.findViewById(R.id.nomOwner);
            descNotif = itemView.findViewById(R.id.descNotif);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            dateNotif = itemView.findViewById(R.id.dateNotif);

        }


    }

}