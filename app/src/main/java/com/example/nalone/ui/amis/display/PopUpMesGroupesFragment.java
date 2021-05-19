package com.example.nalone.ui.amis.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.nalone.R;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.objects.Group;
import com.example.nalone.util.Constants;

import java.util.List;

import static com.example.nalone.HomeActivity.buttonBack;
import static com.example.nalone.util.Constants.USER;


public class PopUpMesGroupesFragment extends Fragment {

    public static Group GROUP_LOAD;
    NavController navController;

    public static String type;
    TextView nameGroup, ownerGroup;
    TextView descriptionGroup;
    TextView nbCreateGroup;
    TextView nbParticipateGroup;
    RelativeLayout relativeDesc;

    ImageView imageGroup;
    TextView visibilityGroup, textViewNbMembers;
    Button buttonAdd;
    CardView cardViewPhotoPerson;
    private EditText groupDesc;
    private int nbMembers;
    private List<String> friends;
    RelativeLayout relativeLayout;
    private TextView ajoutDesc;

    private CardView cardViewPhotoEdit, cardViewPhotoEditDesc, cardViewProfilEdit, cardViewProfilMembers, getCardViewPhotoEditDesc;
    private ImageView imageViewEditDescription;
    FrameLayout fenetrePrincipal;
    View root;
    private boolean editDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pop_up_mes_groupes, container, false);

        createFragment();

        return root;
    }

    private void createFragment() {

    }
}
