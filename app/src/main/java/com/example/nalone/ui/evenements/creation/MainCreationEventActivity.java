package com.example.nalone.ui.evenements.creation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.enumeration.ImageType;
import com.example.nalone.enumeration.TypeEvent;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.util.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.example.nalone.util.Constants.USER;

public class MainCreationEventActivity extends AppCompatActivity {

    public static NavController navController;
    public static ImageView buttonBack, buttonNotif;
    public static Evenement currentEvent;
    public static boolean photoValidate, dateValidate, nameValidate, membersValidate, addressValidate, costValidate;
    public static Uri image = null;

    private static Activity activity;

    private BottomSheetBehavior bottomSheetBehavior, bottomSheetBehaviorType;
    private View bottomSheetType,viewGrey,bottomSheet;
    private TextView textViewVisibility, textViewType;

    private CardView cardViewPublic, cardViewPrivate;

    private CardView cardViewEventArt, cardTypeEventSport,cardTypeEventParty,cardTypeEventMusic,cardTypeEventMovie,cardTypeEventGame
            ,cardTypeEventCar,cardTypeEventGather,cardTypeEventConference,cardTypeEventShop,cardTypeEventShow,cardTypeEventScience;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_creation_event);
        bottomSheet = findViewById(R.id.sheetCreateEvent);
        viewGrey = findViewById(R.id.viewGrey2);
        viewGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(bottomSheetBehaviorType.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        initFirstFiltre();
        initCardViewVisibility();
        initCardViewType();

        activity = this;
        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bottomSheetType = findViewById(R.id.sheetCreateEventType);
        bottomSheetBehaviorType= BottomSheetBehavior.from(bottomSheetType);
        bottomSheetBehaviorType.setHideable(false);
        bottomSheetBehaviorType.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        viewGrey.setVisibility(View.GONE);
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


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        viewGrey.setVisibility(View.GONE);
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        textViewVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewGrey.setVisibility(View.VISIBLE);
            }
        });
        navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
    }

    private void initCardViewType() {
        cardViewEventArt = findViewById(R.id.cardTypeEventArt);
        cardTypeEventSport = findViewById(R.id.cardTypeEventSport);
        cardTypeEventParty = findViewById(R.id.cardTypeEventCar);
        cardTypeEventMusic = findViewById(R.id.cardTypeEventMusic);
        cardTypeEventMovie = findViewById(R.id.cardTypeEventMovie);
        cardTypeEventGame = findViewById(R.id.cardTypeEventGame);
        cardTypeEventCar = findViewById(R.id.cardTypeEventCar);
        cardTypeEventGather = findViewById(R.id.cardTypeEventGather);
        cardTypeEventConference = findViewById(R.id.cardTypeEventConference);
        cardTypeEventShop = findViewById(R.id.cardTypeEventCar);
        cardTypeEventShow = findViewById(R.id.cardTypeEventShow);
        cardTypeEventScience = findViewById(R.id.cardTypeEventScience);
        cardViewEventArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.ART);             }
        });
        cardTypeEventSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.SPORT);             }
        });
        cardTypeEventParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.PARTY);             }
        });
        cardTypeEventMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.MUSIC);             }
        });
        cardTypeEventMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.MOVIE);             }
        });
        cardTypeEventGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.GAME);             }
        });
        cardTypeEventCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.CAR);             }
        });
        cardTypeEventGather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.GATHER);             }
        });
        cardTypeEventConference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.CONFERENCE);             }
        });
        cardTypeEventShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.SHOP);             }
        });
        cardTypeEventShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.SHOW);             }
        });
        cardTypeEventScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(TypeEvent.SCIENCE);            }
        });
    }

    private void initCardViewVisibility() {

        cardViewPrivate = findViewById(R.id.cardViewPrivate);
        cardViewPublic = findViewById(R.id.cardViewPublic);
        cardViewPrivate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeVisibility(Visibility.PRIVATE);
        }
    });

        cardViewPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility(Visibility.PUBLIC);
            }
        });
    }

    private void changeVisibility(Visibility v) {
        currentEvent.setVisibility(v);;
        initFirstFiltre();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void changeType(TypeEvent te) {
        currentEvent.setCategory(te);;
        initFirstFiltre();
        bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void initFirstFiltre() {
        textViewVisibility = findViewById(R.id.textViewVisibility);
        textViewType= findViewById(R.id.textViewType);
        textViewType.setCompoundDrawablesWithIntrinsicBounds(getDrawableType(currentEvent.getCategory()), 0, 0, 0);
        textViewType.setText(getTextType(currentEvent.getCategory()));
        textViewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                viewGrey.setVisibility(View.VISIBLE);
                bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        textViewVisibility.setCompoundDrawablesWithIntrinsicBounds(getDrawableVisibility(currentEvent.getVisibility()), 0, 0, 0);
        textViewVisibility.setText(getTextVisibility(currentEvent.getVisibility()));

    }

    private int getDrawableVisibility(Visibility v) {
        switch (v){
            case PUBLIC:return R.drawable.add_event_public;
            case PRIVATE:return R.drawable.add_event_prive;
            default:return 0;
        }
    }

    private String getTextVisibility(Visibility v) {
        switch (v){
            case PUBLIC:return getResources().getString(R.string.location_public);
            case PRIVATE:return getResources().getString(R.string.location_private);
            default:return "Error";
        }
    }

    private int getDrawableType(TypeEvent te) {
        switch (te){
            case ART:return R.drawable.event_art;
            case CAR:return R.drawable.event_car;
            case GAME:return R.drawable.event_game;
            case SHOP:return R.drawable.event_shop;
            case SHOW:return R.drawable.event_show;
            case MOVIE:return R.drawable.event_movie;
            case MUSIC:return R.drawable.event_music;
            case PARTY:return R.drawable.event_party;
            case SPORT:return R.drawable.event_sport;
            case GATHER:return R.drawable.event_gather;
            case SCIENCE:return R.drawable.event_science;
            case CONFERENCE:return R.drawable.event_conference;
            default:return 0;
        }
    }

    private String getTextType(TypeEvent te) {
        switch (te){
            case ART:return getResources().getString(R.string.event_art);
            case CAR:return getResources().getString(R.string.event_car);
            case GAME:return getResources().getString(R.string.event_game);
            case SHOP:return getResources().getString(R.string.event_shop);
            case SHOW:return getResources().getString(R.string.event_show);
            case MOVIE:return getResources().getString(R.string.event_movie);
            case MUSIC:return getResources().getString(R.string.event_music);
            case PARTY:return getResources().getString(R.string.event_party);
            case SPORT:return getResources().getString(R.string.event_sport);
            case GATHER:return getResources().getString(R.string.event_gather);
            case SCIENCE:return getResources().getString(R.string.event_science);
            case CONFERENCE:return getResources().getString(R.string.event_conference);
            default:return "Error";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isAllValidate(Context context){
        if (photoValidate && nameValidate && membersValidate && dateValidate && addressValidate){
            return true;
        }else{
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createEvent(final Context context) {
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
        params.putCryptParameter("ownerFirstName", USER.getFirst_name());
        params.putCryptParameter("ownerLastName", USER.getLast_name());
        params.putCryptParameter("nbMembers", currentEvent.getNbMembers());
        params.putCryptParameter("category", currentEvent.getCategory());
        params.putCryptParameter("price", currentEvent.getPrice());

        JSONController.getJsonObjectFromUrl(Constants.URL_ADD_EVENT, context, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if(jsonObject.length() == 3){
                    Toast.makeText(context, context.getResources().getString(R.string.event_create), Toast.LENGTH_SHORT).show();
                    activity.finish();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.event_error_create), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Toast.makeText(context, context.getResources().getString(R.string.event_error_create),Toast.LENGTH_SHORT).show();
                Log.w("Response", "Erreur: "+volleyError.toString());
            }
        });

        try {
            String imageData = BitMapToString(MediaStore.Images.Media.getBitmap(context.getContentResolver(), image));
            Constants.uploadImageOnServer(ImageType.EVENT, MainCreationEventActivity.currentEvent.getUid(), imageData, context); //upload image on web server
            MainCreationEventActivity.currentEvent.setImage_url(Constants.BASE_API_URL+"/"+ImageType.EVENT+"/"+MainCreationEventActivity.currentEvent.getUid());
        } catch (IOException e) {
            Log.w("Response", "Erreur: "+e.getMessage());
        }
    }

    private static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.event_not_save))
                .setPositiveButton(getResources().getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearEvent();
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.button_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    private void clearEvent(){
        currentEvent = null;
        photoValidate = false;
        dateValidate= false;
        nameValidate= false;
        membersValidate= false;
        addressValidate = false;
    }
}