package com.nolonely.mobile.ui.evenements.creation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nolonely.mobile.R;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.enumeration.ImageType;
import com.nolonely.mobile.enumeration.TypeEvent;
import com.nolonely.mobile.enumeration.Visibility;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.signUpActivities.SpinnerAdapter;
import com.nolonely.mobile.util.Constants;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.nolonely.mobile.util.Constants.USER;

public class MainCreationEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static NavController navController;
    public static ImageView buttonBack, buttonNotif;
    public static Evenement currentEvent;
    public static Uri image = null;

    private static Activity activity;

    private BottomSheetBehavior bottomSheetBehavior, bottomSheetBehaviorType;
    private View bottomSheetType, viewGrey, bottomSheet;
    private TextView textViewVisibility, textViewType;

    private CardView cardViewPublic, cardViewPrivate;

    private View viewStepDate, viewStepName, viewStepMembers, viewStepPosition, viewStepFinal, viewStepCost;
    private ImageView imageProgressCreationDate, imageProgressCreationMembers,
            imageProgressCreationName, imageProgressCreationPosition, imageProgressCreationPhoto, imageProgressCreationCost;
    private CardView cardViewProgressCreationDate, cardViewProgressCreationMembers,
            cardViewProgressCreationName, cardViewProgressCreationPosition, cardViewProgressCreationPhoto, cardViewProgressCreationCost;
    private TextView textViewTitleEvent;
    public static String ACTION_RECEIVE_FRAGMENT = "ACTION_RECEIVE_FRAGMENT";
    public static String ACTION_RECEIVE_NEXT_CLICK = "ACTION_RECEIVE_NEXT_CLICK";

    private boolean isTypeChoosed;

    private final BroadcastReceiver receiverFragmentValidate = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                CurrentFragment currentFragment = CurrentFragment.valueOf(intent.getStringExtra("fragment"));
                Fragment nextFrag = null;
                switch (currentFragment) {
                    case DATE:
                        imageProgressCreationDate.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        nextFrag = new AddressEventFragment();
                        break;
                    case COST:
                        imageProgressCreationCost.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        nextFrag = currentEvent.getVisibility() == Visibility.PRIVATE ? new MembersEventPrivateFragment() : new MembersEventPublicFragment();
                        break;
                    case NAME:
                        imageProgressCreationName.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        nextFrag = new DateEventFragment();
                        break;
                    case PHOTO:
                        imageProgressCreationPhoto.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        nextFrag = new NameEventFragment();
                        break;
                    case ADRESS:
                        imageProgressCreationPosition.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        nextFrag = new CostEventFragment();
                        break;
                    case MEMBERS:
                        imageProgressCreationMembers.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                        createEvent();
                        break;

                }


                if (nextFrag != null) {
                    changeFragment(nextFrag, true);
                } /*else if (!photoValidate) {
                    nextFrag = new PhotoEventFragment();
                } else if (!nameValidate) {
                    nextFrag = new NameEventFragment();
                } else if (!dateValidate) {
                    nextFrag = new DateEventFragment();
                } else if (!addressValidate) {
                    nextFrag = new AddressEventFragment();
                } else if (!costValidate) {
                    nextFrag = new CostEventFragment();
                } else {
                    nextFrag = new MembersEventPrivateFragment();
                }*/


            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (isTypeChoosed) {
            changeType(TypeEvent.values()[position]);
        } else {
            isTypeChoosed = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public enum CurrentFragment {
        NAME, ADRESS,
        DATE, COST,
        PHOTO, MEMBERS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_creation_event);
        isTypeChoosed = false;
        textViewTitleEvent = findViewById(R.id.textViewTitleEvent);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentCreationEvent, new PhotoEventFragment(), "findThisFragment")
                .addToBackStack(null)
                .commit();

        initStepView();
        initImageView();
        initCardView();

        Button buttonValidate = findViewById(R.id.buttonNextFragmentMembers);
        buttonValidate.setOnClickListener(v -> {
            LocalBroadcastManager localBctMgr = LocalBroadcastManager.getInstance(this);
            localBctMgr.sendBroadcast(new Intent(ACTION_RECEIVE_NEXT_CLICK));
        });
        bottomSheet = findViewById(R.id.sheetCreateEvent);
        viewGrey = findViewById(R.id.viewGrey2);
        viewGrey.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (bottomSheetBehaviorType.getState() == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });


        activity = this;
        buttonBack = findViewById(R.id.buttonBack);


        initFirstFiltre();
        initCardViewVisibility();

        Spinner spin = (Spinner) findViewById(R.id.spinnerType);

        spin.setOnItemSelectedListener(this);

        SpinnerAdapter customAdapter = new SpinnerAdapter(getBaseContext(), TypeEvent.listOfImages(getBaseContext()), TypeEvent.listOfNames(getBaseContext()));
        spin.setAdapter(customAdapter);
        spin.setSelection(Arrays.asList(TypeEvent.values()).indexOf(currentEvent.getCategory()));

        buttonBack.setOnClickListener(v -> onFinishActivity());

        bottomSheetType = findViewById(R.id.sheetCreateEventType);
        bottomSheetBehaviorType = BottomSheetBehavior.from(bottomSheetType);
        bottomSheetBehaviorType.setHideable(false);
        bottomSheetBehaviorType.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_HIDDEN:
                        viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        textViewVisibility.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            viewGrey.setVisibility(View.VISIBLE);
        });
    }

    private void onFinishActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.event_not_save))
                .setPositiveButton(getResources().getString(R.string.button_yes), (dialog, id) -> {
                    finish();
                })
                .setNegativeButton(getResources().getString(R.string.button_no), (dialog, id) -> dialog.dismiss());
        builder.create();
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECEIVE_FRAGMENT);
        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(receiverFragmentValidate, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(receiverFragmentValidate);
    }

    private void initCardViewVisibility() {

        cardViewPrivate = findViewById(R.id.cardViewPrivate);
        cardViewPublic = findViewById(R.id.cardViewPublic);
        cardViewPrivate.setOnClickListener(v -> {
            changeVisibility(Visibility.PRIVATE);
        });

        cardViewPublic.setOnClickListener(v -> {
            changeVisibility(Visibility.PUBLIC);
        });
    }

    private void changeVisibility(Visibility v) {
        currentEvent.setVisibility(v);
        initFirstFiltre();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentCreationEvent);
        if (fragment instanceof MembersEventPrivateFragment || fragment instanceof MembersEventPublicFragment) {
            if (v == Visibility.PRIVATE)
                changeFragment(new MembersEventPrivateFragment(), true);
            else
                changeFragment(new MembersEventPublicFragment(), true);
        }
    }

    private void changeType(TypeEvent te) {
        currentEvent.setCategory(te);
        initFirstFiltre();
        bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void initFirstFiltre() {
        textViewVisibility = findViewById(R.id.textViewVisibility);
        textViewType = findViewById(R.id.textViewType);
        textViewType.setCompoundDrawablesWithIntrinsicBounds(TypeEvent.imageOfValue(currentEvent.getCategory()), 0, 0, 0);
        textViewType.setText(TypeEvent.nameOfValue(currentEvent.getCategory(), getBaseContext()));
        textViewType.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            viewGrey.setVisibility(View.VISIBLE);
            bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        textViewVisibility.setCompoundDrawablesWithIntrinsicBounds(getDrawableVisibility(currentEvent.getVisibility()), 0, 0, 0);
        textViewVisibility.setText(getTextVisibility(currentEvent.getVisibility()));


    }

    private int getDrawableVisibility(Visibility v) {
        switch (v) {
            case PUBLIC:
                return R.drawable.add_event_public;
            case PRIVATE:
                return R.drawable.add_event_prive;
            default:
                return 0;
        }
    }

    private String getTextVisibility(Visibility v) {
        switch (v) {
            case PUBLIC:
                return getResources().getString(R.string.location_public);
            case PRIVATE:
                return getResources().getString(R.string.location_private);
            default:
                return "Error";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createEvent() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("uid_event", currentEvent.getUid());
        params.putCryptParameter("address", currentEvent.getAddress());
        params.putCryptParameter("city", currentEvent.getCity());
        params.putCryptParameter("description", currentEvent.getDescription());
        params.putCryptParameter("startDate", currentEvent.getStartDate());
        params.putCryptParameter("endDate", currentEvent.getEndDate());
        params.putCryptParameter("visibility", currentEvent.getVisibility());
        params.putCryptParameter("latitude", currentEvent.getLatitude());
        params.putCryptParameter("longitude", currentEvent.getLongitude());
        params.putCryptParameter("name", currentEvent.getName());
        params.putCryptParameter("nbMembers", currentEvent.getNbMembers() + "");
        params.putCryptParameter("category", currentEvent.getCategory());
        params.putCryptParameter("price", currentEvent.getPrice() + "");

        Log.w("Prix", "Prix : " + currentEvent.getPrice());
        Log.w("nbMembers", "nbMembers : " + currentEvent.getNbMembers());
        Log.w("Params", params.toString());

        JSONController.getJsonObjectFromUrl(Constants.URL_ADD_EVENT, getBaseContext(), params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    Toast.makeText(getBaseContext(), getString(R.string.event_create), Toast.LENGTH_SHORT).show();
                    activity.finish();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.event_error_create), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), getString(R.string.event_error_create), Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur: " + volleyError.toString());
            }
        });

        if (currentEvent.getImage_url_event() != null) {
            try {
                String imageData = BitMapToString(MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), image));
                Constants.uploadImageOnServer(ImageType.EVENT, MainCreationEventActivity.currentEvent.getUid(), imageData, getBaseContext()); //upload image on web server
                MainCreationEventActivity.currentEvent.setImage_url_event(Constants.BASE_API_URL + "/" + ImageType.EVENT + "/" + MainCreationEventActivity.currentEvent.getUid());
            } catch (IOException e) {
                Log.w("Response", "Erreur: " + e.getMessage());
            }
        }
    }

    private static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("currentFragment");
        changeFragment(fragment, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentEvent = null;
    }

    private void initStepView() {
        viewStepCost = findViewById(R.id.viewStepCost);
        viewStepDate = findViewById(R.id.viewStepDate);
        viewStepMembers = findViewById(R.id.viewStepMembers);
        viewStepName = findViewById(R.id.viewStepName);
        viewStepPosition = findViewById(R.id.viewStepPosition);
        viewStepFinal = findViewById(R.id.viewStepFinal);
    }

    private void initImageView() {
        imageProgressCreationDate = findViewById(R.id.imageProgessCreationDate);
        imageProgressCreationMembers = findViewById(R.id.imageProgessCreationMembers);
        imageProgressCreationName = findViewById(R.id.imageProgessCreationName);
        imageProgressCreationPosition = findViewById(R.id.imageProgessCreationPosition);
        imageProgressCreationPhoto = findViewById(R.id.imageProgessCreationPhoto);
        imageProgressCreationCost = findViewById(R.id.imageProgessCreationCost);
    }

    private void initCardView() {
        cardViewProgressCreationDate = findViewById(R.id.cardViewProgressCreationDate);
        cardViewProgressCreationMembers = findViewById(R.id.cardViewProgressCreationMembers);
        cardViewProgressCreationName = findViewById(R.id.cardViewProgressCreationName);
        cardViewProgressCreationPhoto = findViewById(R.id.cardViewProgressCreationPhoto);
        cardViewProgressCreationPosition = findViewById(R.id.cardViewProgressCreationAdress);
        cardViewProgressCreationCost = findViewById(R.id.cardViewProgressCreationCost);

    }


    private void changeFragment(Fragment fragment, boolean isNext) {
        if (fragment instanceof NameEventFragment) {
            if (!isNext)
                fragment = new PhotoEventFragment();
            updateStep(cardViewProgressCreationName, viewStepName, isNext);
            textViewTitleEvent.setText(getString(R.string.title_creation_event_name));
        } else if (fragment instanceof AddressEventFragment) {
            if (!isNext)
                fragment = new DateEventFragment();
            updateStep(cardViewProgressCreationPosition, viewStepPosition, isNext);
            textViewTitleEvent.setText(getString(R.string.title_creation_event_adress));
        } else if (fragment instanceof DateEventFragment) {
            if (!isNext)
                fragment = new NameEventFragment();
            updateStep(cardViewProgressCreationDate, viewStepDate, isNext);
            textViewTitleEvent.setText(getString(R.string.title_creation_event_date));
        } else if (fragment instanceof CostEventFragment) {
            if (!isNext)
                fragment = new AddressEventFragment();
            updateStep(cardViewProgressCreationCost, viewStepCost, isNext);
            textViewTitleEvent.setText(getString(R.string.title_creation_event_cost));
        } else if (fragment instanceof MembersEventPrivateFragment || fragment instanceof MembersEventPublicFragment) {
            if (!isNext)
                fragment = new CostEventFragment();
            updateStep(cardViewProgressCreationMembers, viewStepMembers, isNext);
            textViewTitleEvent.setText(getString(R.string.title_creation_event_members));
        } else if (fragment instanceof PhotoEventFragment) {
            if (!isNext) {
                onFinishActivity();
            } else {
                textViewTitleEvent.setText(getString(R.string.title_creation_event_photo));
            }

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentCreationEvent, fragment, "currentFragment")
                .addToBackStack(null)
                .commit();
    }

    private void updateStep(CardView cardView, View view, boolean isNext) {
        int color = isNext ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.grey);
        cardView.setCardBackgroundColor(color);
        view.setBackgroundColor(color);
    }

}