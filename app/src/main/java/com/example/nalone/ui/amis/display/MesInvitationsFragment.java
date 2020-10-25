package com.example.nalone.ui.amis.display;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nalone.Adapter.ItemInvitAmisAdapter;
import com.example.nalone.CustomToast;
import com.example.nalone.ItemPerson;
import com.example.nalone.R;
import com.example.nalone.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USERS_DB_REF;
import static com.example.nalone.util.Constants.USERS_LIST;
import static com.example.nalone.util.Constants.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MesInvitationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MesInvitationsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ItemInvitAmisAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final List<ItemPerson> invits = new ArrayList<>();
    private View rootView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MesInvitationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_3.
     */
    // TODO: Rename and change types and number of parameters
    public static MesInvitationsFragment newInstance(String param1, String param2) {
        MesInvitationsFragment fragment = new MesInvitationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       rootView = inflater.inflate(R.layout.fragment_3, container, false);

        updateItems();

        return rootView;
    }

    private void updateItems() {
        for(int i = 0; i < USERS_LIST.size(); i++){
            String s = ""+i;
            User u = USERS_LIST.get(i);
            if(!s.equalsIgnoreCase(USER_ID)){
                invits.add(new ItemPerson(i,R.drawable.ic_baseline_account_circle_24,
                        u.getPrenom()+" "+u.getNom(), 0, u.getDescription(),
                        u.getVille(), u.getNbCreate(), u.getNbParticipate()));
            }
        }

        mRecyclerView = rootView.findViewById(R.id.recyclerViewMesAmis);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemInvitAmisAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(int position) {
                acceptFriendRequest(""+invits.get(position).getId());
            }

            @Override
            public void onRemoveClick(int position) {
                declineFriendRequest(""+invits.get(position).getId());
            }
        });
    }

    private void acceptFriendRequest(String id) {


        USERS_LIST.get(USER_ID).getAmis().add(id);
        USERS_LIST.get(id).getAmis().add(USER_ID);

        USERS_LIST.get(USER_ID).getDemande_amis_envoye().remove(id);
        USERS_LIST.get(id).getDemande_amis_envoye().remove(USER_ID);

        USERS_LIST.get(USER_ID).getDemande_amis_recu().remove(id);
        USERS_LIST.get(id).getDemande_amis_recu().remove(USER_ID);

        USERS_DB_REF.setValue(USERS_LIST);

        updateItems();
        CustomToast t = new CustomToast(getContext(), "Vous avez ajouté(e) cet utilisateur", false, true);
        t.show();
    }

    private void declineFriendRequest(String id) {

        USERS_LIST.get(USER_ID).getDemande_amis_envoye().remove(id);
        USERS_LIST.get(id).getDemande_amis_envoye().remove(USER_ID);

        USERS_LIST.get(USER_ID).getDemande_amis_recu().remove(id);
        USERS_LIST.get(id).getDemande_amis_recu().remove(USER_ID);

        USERS_DB_REF.setValue(USERS_LIST);

        updateItems();
        CustomToast t = new CustomToast(getContext(), "Vous n'avez pas accepté(e) cet utilisateur", false, true);
        t.show();
    }
}