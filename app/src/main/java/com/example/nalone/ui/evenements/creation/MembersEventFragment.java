package com.example.nalone.ui.evenements.creation;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.dialog.ListAmisFragment;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.util.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;

public class MembersEventFragment extends Fragment {

    private List<String> adds = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private NavController navController;
    private Button buttonMoreInvit;
    private Button next;
    private boolean haveFriends;
    private BottomSheetBehavior bottomSheetBehavior;


    public MembersEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_members_event, container, false);
        initialiserImageView(root);
        mRecyclerView = root.findViewById(R.id.recyclerView1);
        next = root.findViewById(R.id.buttonNextFragmentMembers);
        buttonMoreInvit = root.findViewById(R.id.buttonMoreInvit);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment2);
        initFragment(root);
        checkValidation();
        return root;
    }

    private void initFragment(View root) {
        View bottomSheet = root.findViewById(R.id.sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        buttonMoreInvit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(final View v) {

                JSONObjectCrypt params = new JSONObjectCrypt();
                params.putCryptParameter("uid", USER.getUid());

                JSONController.getJsonArrayFromUrl(Constants.URL_FRIENDS, getContext(), params, new JSONArrayListener() {
                    @Override
                    public void onJSONReceived(JSONArray jsonArray) {
                        if (jsonArray.length() > 0) {
                            ListAmisFragment.type = "event";
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            ListAmisFragment.EVENT_LOAD = MainCreationEventActivity.currentEvent;
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.no_friends), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onJSONReceivedError(VolleyError volleyError) {
                        Log.w("Response", "Erreur:" + volleyError.toString());
                        Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                });
                /*mStoreBase.collection("users").document(USER_ID).collection("friends").whereEqualTo("status", "add")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                haveFriends = true;
                                Log.w("event", " Ajout list");
                            }

                        }
                        if(haveFriends){
                            ListAmisFragment.type = "event";
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            ListAmisFragment.EVENT_LOAD = MainCreationEventActivity.currentEvent;
                        }else
                            Toast.makeText(getContext(), "Vous n'avez pas d'ami", Toast.LENGTH_SHORT).show();

                    }
                });*/
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                validateMembers();
            }
        });
    }

    private ImageView imageProgessCreationPhoto, imageProgessCreationDate, imageProgessCreationPosition, imageProgessCreationName,
            imageProgessCreationMembers, imageProgressCreationCost;

    private void initialiserImageView(View root) {
        imageProgessCreationDate = root.findViewById(R.id.imageProgessCreationDate);
        imageProgessCreationMembers = root.findViewById(R.id.imageProgessCreationMembers);
        imageProgessCreationName = root.findViewById(R.id.imageProgessCreationName);
        imageProgessCreationPosition = root.findViewById(R.id.imageProgessCreationPosition);
        imageProgessCreationPhoto = root.findViewById(R.id.imageProgessCreationPhoto);
        imageProgressCreationCost = root.findViewById(R.id.imageProgessCreationCost);
        imageProgressCreationCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_costEventFragment);
            }
        });

        imageProgessCreationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDate();
            }
        });
        imageProgessCreationPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPhoto();
            }
        });
        imageProgessCreationPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAdress();
            }
        });
        imageProgessCreationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goName();
            }
        });


    }


    private class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView nomInvit;
        private TextView villePers;
        private ImageView button, imagePerson;
        private CardView cardView;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            nomInvit = itemView.findViewById(R.id.nomInvit);
            villePers = itemView.findViewById(R.id.villePers);
            button = itemView.findViewById(R.id.buttonImage);
            imagePerson = itemView.findViewById(R.id.imagePerson);
            cardView = itemView.findViewById(R.id.cardViewProfil);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkValidation() {
        if (MainCreationEventActivity.addressValidate) {
            imageProgessCreationPosition.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_adress_focused));
        }
        if (MainCreationEventActivity.dateValidate) {
            imageProgessCreationDate.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_date_focused));
        }
        if (MainCreationEventActivity.membersValidate) {
            imageProgessCreationMembers.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_members_focused));
        }
        if (MainCreationEventActivity.nameValidate) {
            imageProgessCreationName.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_name_focused));
        }
        if (MainCreationEventActivity.photoValidate) {
            imageProgessCreationPhoto.setImageDrawable(getResources().getDrawable(R.drawable.creation_event_photo_focused));
        }
        if (MainCreationEventActivity.costValidate) {
            imageProgressCreationCost.setImageDrawable(getResources().getDrawable(R.drawable.cost_event_focused));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validateMembers() {
        MainCreationEventActivity.membersValidate = true;
        MainCreationEventActivity.currentEvent.setNbMembers(adds.size() + 1);
        if (MainCreationEventActivity.isAllValidate(getContext())) {
            MainCreationEventActivity.createEvent(getContext());
        } else if (!MainCreationEventActivity.photoValidate) {
            goPhoto();
        } else if (!MainCreationEventActivity.nameValidate) {
            goName();
        } else if (!MainCreationEventActivity.dateValidate) {
            goDate();
        } else if (!MainCreationEventActivity.addressValidate) {
            goAdress();
        } else if (!MainCreationEventActivity.costValidate) {
            goCost();
        }
    }

    private void goDate() {
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_dateEventFragment);
    }

    private void goAdress() {
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_adressEventFragment);
    }

    private void goName() {
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_nameEventFragment);
    }

    private void goPhoto() {
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_photoEventFragment);
    }

    private void goCost() {
        MainCreationEventActivity.navController.navigate(R.id.action_membersEventFragment_to_costEventFragment);
    }
}