package com.example.nalone.ui.recherche;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.json.JSONArrayListener;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.objects.Group;
import com.example.nalone.R;
import com.example.nalone.objects.User;
import com.example.nalone.ui.amis.display.PopUpGroupFragment;
import com.example.nalone.ui.amis.display.PopupProfilFragment;
import com.example.nalone.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.mStoreBase;

public class RechercheFragment extends Fragment {

    private RecyclerView recyclerAmis;
    private RecyclerView recyclerGroupes;
    private CardView cardViewRechercheAmis;
    private CardView cardViewRechercheGroupes;
    private View rootView;
    private NavController navController;

    private TextView textViewRechercheAmis;
    private TextView textViewRechercheGroupes;
    private ProgressBar loading;

    private List<User> friends;
    private List<User> myGroups;

    private RelativeLayout relativeAmis, relativeGroup;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recherche, container, false);
        getFriends();
        return rootView;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createFragment() {
        myGroups = new ArrayList<>();
        recyclerAmis = rootView.findViewById(R.id.recyclerViewRechercheAmis);
        recyclerGroupes = rootView.findViewById(R.id.recyclerViewRechercheGroupes);
        cardViewRechercheAmis = rootView.findViewById(R.id.cardViewRechercheAmis);
        cardViewRechercheGroupes = rootView.findViewById(R.id.cardViewRechercheGroupes);
        textViewRechercheAmis = rootView.findViewById(R.id.textViewRechercheAmis);
        textViewRechercheGroupes = rootView.findViewById(R.id.textViewRechercheGroupes);
        loading = rootView.findViewById(R.id.search_loading);
        cardViewRechercheAmis.setVisibility(View.INVISIBLE);
        cardViewRechercheGroupes.setVisibility(View.INVISIBLE);
        relativeAmis = rootView.findViewById(R.id.relativeSansAmis);
        relativeGroup = rootView.findViewById(R.id.relativeSansGroup);


        textViewRechercheAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_to_navigation_recherche_amis);
            }
        });

        textViewRechercheGroupes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_to_navigation_rcherche_groupe);
            }
        });

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getFriends() {
        friends = new ArrayList<>();
        myGroups = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", USER.getUid());
        params.addParameter("limit", 10); //fix a limit to 10 users

        JSONController.getJsonArrayFromUrl(Constants.URL_FRIENDS, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {

                try {
                    Log.w("Response", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        friends.add((User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class));
                    }

                    for (int i = 0; i < friends.size(); i++) {
                        Log.w("Recherche", friends.get(i).getFirst_name()+friends.get(i).getLast_name());
                    }
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:"+e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:"+volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView nomGroup;
        private LinearLayout layoutGroup;
        private ImageView imageGroup;
        private ImageView button;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            nomGroup = itemView.findViewById(R.id.nomGroupe);
            layoutGroup = itemView.findViewById(R.id.layoutGroupe);
            imageGroup = itemView.findViewById(R.id.imageGroupe);

        }
    }


    public void showPopUpGroup(final Group g) {
        PopUpGroupFragment.GROUP_LOAD = g;
        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_group);
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private LinearLayout layoutProfil;
        private ImageView imagePerson;
        private ImageView button;
        private CardView cardViewPhotoPerson;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            layoutProfil = itemView.findViewById(R.id.layoutProfil);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            button = itemView.findViewById(R.id.buttonImage);
            cardViewPhotoPerson = itemView.findViewById(R.id.cardViewPhotoPerson);

        }

    }

    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;

        PopupProfilFragment.type = "recherche";
        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_profil);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}