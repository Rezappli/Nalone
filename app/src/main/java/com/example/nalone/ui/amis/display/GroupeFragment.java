package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nalone.objects.Group;
import com.example.nalone.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.USER_ID;
import static com.example.nalone.util.Constants.mStoreBase;
import static com.example.nalone.util.Constants.setGroupImage;

public class GroupeFragment extends Fragment {


    private NavController navController;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private ImageView addGroup;
    private CardView mesGroupes;
    private List<String> myGroups;
    private LinearLayout linearSansMesGroupes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_groupe, container, false);

        myGroups = new ArrayList<>();
        mRecyclerView = root.findViewById(R.id.recyclerViewGroupe);
        linearSansMesGroupes = root.findViewById(R.id.linearSansMesGroupes);
        addGroup = root.findViewById(R.id.create_group_button);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mesGroupes = root.findViewById(R.id.mesGroupes);

        mesGroupes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_amis_to_navigation_mes_groupes);
            }
        });

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_amis_to_navigation_creat_group);
            }
        });

        return root;
    }

        private class UserViewHolder extends RecyclerView.ViewHolder {

            private TextView nomGroup;
            private LinearLayout layoutGroup;
            private ImageView imageGroup;
            private ImageView button;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);

                nomGroup = itemView.findViewById(R.id.nomGroupe);
                layoutGroup = itemView.findViewById(R.id.layoutGroupe);
                imageGroup = itemView.findViewById(R.id.imageGroupe);

            }

        }

    public void showPopUpGroup(final Group g) {
        PopUpGroupFragment.GROUP_LOAD = g;
        navController.navigate(R.id.action_navigation_amis_to_navigation_popup_group);


    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}