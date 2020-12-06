package com.example.nalone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nalone.items.ItemImagePerson;
import com.example.nalone.ui.amis.display.AmisFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.nalone.util.Constants.dateFormat;
import static com.example.nalone.util.Constants.formatD;
import static com.example.nalone.util.Constants.mStore;
import static com.example.nalone.util.Constants.mStoreBase;

public class InfosEvenementsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTitle;
    private TextView mDate;
    private TextView mTimer;
    private TextView mOwner;
    private TextView mDescription;
    private FirestoreRecyclerAdapter adapter;
    private List<String> members = new ArrayList<>();

    public static Evenement EVENT_LOAD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_evenements);

        mTitle = findViewById(R.id.title);
        mDate = findViewById(R.id.date);
        mTimer = findViewById(R.id.time);
        mOwner = findViewById(R.id.owner);
        mDescription = findViewById(R.id.description);
        mRecyclerView = findViewById(R.id.recyclerViewMembresInscrits);

        String date_text = formatD.format(EVENT_LOAD.getDate().toDate());
        String final_date_text = "";
        mTitle.setText(EVENT_LOAD.getName());
        mTimer.setText(EVENT_LOAD.getDate().toString());
        mOwner.setText(EVENT_LOAD.getOwner());
        mDescription.setText(EVENT_LOAD.getDescription());

        for (int i = 0; i < date_text.length() - 5; i++) {
            char character = date_text.charAt(i);
            if (i == 0) {
                character = Character.toUpperCase(character);
            }
            final_date_text += character;
        }

        mDate.setText(final_date_text);

        mStoreBase.collection("events").document(EVENT_LOAD.getUid())
                .collection("members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    members.add(doc.toObject(ModelData.class).getUser().getId());
                }

                Query query = mStoreBase.collection("users").whereIn("uid", members);
                FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

                adapter = new FirestoreRecyclerAdapter<User, InfosEvenementsActivity.UserViewHolder>(options) {
                    @NonNull
                    @Override
                    public InfosEvenementsActivity.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_user, parent, false);
                        return new InfosEvenementsActivity.UserViewHolder(view);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    protected void onBindViewHolder(@NonNull final InfosEvenementsActivity.UserViewHolder userViewHolder, int i, @NonNull final User u) {
                        Constants.setUserImage(u, InfosEvenementsActivity.this, userViewHolder.imagePerson);
                    }
                };

                mRecyclerView.setAdapter(adapter);
                adapter.startListening();


                mLayoutManager = new LinearLayoutManager(
                        InfosEvenementsActivity.this,
                        LinearLayoutManager.HORIZONTAL,
                        false);
                mRecyclerView.setLayoutManager(mLayoutManager);
            }
        });

    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagePerson;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePerson = itemView.findViewById(R.id.imageUser);


        }

    }
}