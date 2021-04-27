package com.example.nalone.ui.amis.display;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.dialog.ListAmisFragment;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.Group;
import com.example.nalone.util.Constants;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;

public class CreateGroupFragment extends Fragment {

    private TextInputEditText event_name;
    private TextInputEditText event_resume;

    private ImageView imageViewPrivate;
    private ImageButton imageButtonAddInvit;

    private ImageView imageViewPublic;

    public static List<String> adds = new ArrayList<>();
    public static boolean edit;

    private NavController navController;
    private ImageView buttonMoreGroup;

    private View rootView;

    public static boolean save;

    public static Group groupAttente;

    private final int RESULT_LOAD_IMG = 1;

    private final boolean hasSelectedImage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        createFragment();

        return rootView;
    }

    private void createFragment() {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        ImageView imageGroup = rootView.findViewById(R.id.imageGroupCreation);
        imageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_creat_group_to_navigation_amis);
            }
        });

        if (groupAttente == null) {
            groupAttente = new Group(UUID.randomUUID().toString(), USER.getFirst_name() + " " + USER.getLast_name(), "", "", Visibility.PUBLIC);
        }

        CardView cardViewPrivate = rootView.findViewById(R.id.cardViewPrivate);
        CardView cardViewPublic = rootView.findViewById(R.id.cardViewPublic);
        imageButtonAddInvit = rootView.findViewById(R.id.buttonMoreGroup);
        imageViewPublic = rootView.findViewById(R.id.imageViewPublic);
        imageViewPrivate = rootView.findViewById(R.id.imageViewPrivate);

        event_name = rootView.findViewById(R.id.groupName);
        event_resume = rootView.findViewById(R.id.groupResume);
        Button buttonValidEvent = rootView.findViewById(R.id.buttonCreateGroup);

        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPrivate();
            }
        });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPublic();
            }
        });

        imageButtonAddInvit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFields();
                ListAmisFragment.type = "group";
                navController.navigate(R.id.action_navigation_creat_group_to_navigation_list_amis);
            }
        });


        buttonValidEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                saveGroup();
            }
        });
    }


    private void updateFields() {
        groupAttente.setName(event_name.getText().toString());
        groupAttente.setDescription(event_resume.getText().toString());

        if (!groupAttente.getName().matches(""))
            event_name.setText(groupAttente.getName());
        if (!groupAttente.getDescription().matches(""))
            event_resume.setText(groupAttente.getDescription());
        if (groupAttente.getVisibility() != null) {
            if (groupAttente.getVisibility().equals(Visibility.PUBLIC)) {
                selectPublic();
            } else {
                selectPrivate();
            }
        }
    }

    private void selectPublic() {
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_focused);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_24);
        groupAttente.setVisibility(Visibility.PUBLIC);
    }

    private void selectPrivate() {
        imageButtonAddInvit.setVisibility(View.VISIBLE);
        imageViewPrivate.setImageResource(R.drawable.ic_baseline_lock_focused);
        imageViewPublic.setImageResource(R.drawable.ic_baseline_public_24);
        groupAttente.setVisibility(Visibility.PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveGroup() {
        if (event_name.getText().toString().matches("")) {
            event_name.setError(getResources().getString(R.string.edittext_field_required));
        }

        if (!event_name.getText().toString().matches("")) {
            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", USER.getUid());
            params.putCryptParameter("uid_group", groupAttente.getUid());
            params.putCryptParameter("name", event_name.getText());
            params.putCryptParameter("description", event_resume.getText());
            params.putCryptParameter("visibility", groupAttente.getVisibility());

            Log.w("Response", "Params send :" + params.toString());

            JSONController.getJsonObjectFromUrl(Constants.URL_ADD_GROUP, getContext(), params, new JSONObjectListener() {
                @Override
                public void onJSONReceived(JSONObject jsonObject) {
                    if (jsonObject.length() == 3) {
                        Toast.makeText(getContext(), getResources().getString(R.string.group_create), Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_navigation_creat_group_to_navigation_amis);
                        groupAttente = null;
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onJSONReceivedError(VolleyError volleyError) {
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    Log.w("Response", "Erreur: " + volleyError.toString());
                }
            });
        }
    }
}
