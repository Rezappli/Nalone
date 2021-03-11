package com.example.nalone.ui.evenements.display;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.nalone.R;
import com.example.nalone.enumeration.FiltreDate;
import com.example.nalone.enumeration.FiltreEvent;
import com.example.nalone.enumeration.FiltreSort;
import com.example.nalone.enumeration.TypeEvent;
import com.example.nalone.util.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class SearchEventActivity extends AppCompatActivity {

    private TextView textViewType, textViewSort, textViewDate, textViewLocation, textViewPrice;
    private BottomSheetBehavior bottomSheetBehaviorDate, bottomSheetBehaviorType,bottomSheetBehaviorSort
            ,bottomSheetBehaviorPrice,bottomSheetBehaviorLocation;
    private View viewGrey, bottomSheetType,bottomSheetDate,bottomSheetSort,bottomSheetPrice,bottomSheetLocation;
    public static TypeEvent currentType;
    private FiltreSort currentSort;
    private FiltreDate currentDate;
    private int currentLocation, currentPrice;
    private TextView textViewSortPertinence, textViewSortPriceASC,textViewSortPriceDESC,textViewSortLocation ,textViewSortDate;
    private TextView textViewDateToday,textViewDateTomorrow,textViewDateWeek,textViewDateMonth,textViewDateOther;
    private TextView textViewEventArt, textViewEventSport,textViewEventParty,textViewEventMusic,textViewEventMovie,textViewEventGame
            ,textViewEventCar,textViewEventGather,textViewEventConference,textViewEventShop,textViewEventShow,textViewEventScience;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);
        currentSort = FiltreSort.PERTINENCE;
        currentDate = FiltreDate.NONE;
        currentLocation = Constants.range;

        viewGrey = findViewById(R.id.viewGrey);
        viewGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomExpandedToCollapsed();
            }
        });
        initTextView();
        initBottomSheet();

    }

    private void initTextView() {

        //TextView of event category
        initTextViewType();

        // TextView of sort filter
        initTextViewSort();
    }

    private void initTextViewSort(){
        textViewType = findViewById(R.id.textViewType);
        changeTextViewType();
        textViewSort = findViewById(R.id.textViewSort);
        textViewSortDate = findViewById(R.id.textViewSortDate);
        textViewSortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActualSort(FiltreSort.DATE);
            }
        });
        textViewSortLocation = findViewById(R.id.textViewSortLocation);
        textViewSortLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActualSort(FiltreSort.LOCATION);
            }
        });
        textViewSortPertinence = findViewById(R.id.textViewSortPertinence);
        textViewSortPertinence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActualSort(FiltreSort.PERTINENCE);
            }
        });
        textViewSortPriceASC = findViewById(R.id.textViewSortPriceASC);
        textViewSortPriceASC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActualSort(FiltreSort.PRICEASC);
            }
        });

        textViewSortPriceDESC = findViewById(R.id.textViewSortPriceDESC);
        textViewSortPriceDESC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActualSort(FiltreSort.PRICEDESC);
            }
        });

        textViewLocation = findViewById(R.id.textViewLocation);
        textViewPrice = findViewById(R.id.textViewPrice);

        textViewDate = findViewById(R.id.textViewDate);
        textViewDateToday = findViewById(R.id.textViewDateToday);
        textViewDateTomorrow = findViewById(R.id.textViewDateTomorrow);
        textViewDateWeek = findViewById(R.id.textViewDateWeek);
        textViewDateMonth = findViewById(R.id.textViewDateMonth);
        textViewDateOther = findViewById(R.id.textViewDateOther);

        textViewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomCollapsedToExpanded(FiltreEvent.TYPE);
            }
        });

        textViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomCollapsedToExpanded(FiltreEvent.LOCATION);
            }
        });

        textViewPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomCollapsedToExpanded(FiltreEvent.PRICE);
            }
        });

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomCollapsedToExpanded(FiltreEvent.DATE);
            }
        });

        textViewSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomCollapsedToExpanded(FiltreEvent.SORT);
            }
        });
    }

    private void initTextViewType() {
        textViewEventArt = findViewById(R.id.textViewTypeArt);
        textViewEventSport = findViewById(R.id.textViewTypeSport);
        textViewEventParty = findViewById(R.id.textViewTypeCar);
        textViewEventMusic = findViewById(R.id.textViewTypeMusic);
        textViewEventMovie = findViewById(R.id.textViewTypeMovie);
        textViewEventGame = findViewById(R.id.textViewTypeGame);
        textViewEventCar = findViewById(R.id.textViewTypeCar);
        textViewEventGather = findViewById(R.id.textViewTypeGather);
        textViewEventConference = findViewById(R.id.textViewTypeConference);
        textViewEventShop = findViewById(R.id.textViewTypeCar);
        textViewEventShow = findViewById(R.id.textViewTypeShow);
        textViewEventScience = findViewById(R.id.textViewTypeScience);
        textViewEventArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                           }
        });
        textViewEventSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                          }
        });
        textViewEventParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                          }
        });
        textViewEventMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            }
        });
        textViewEventMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            }
        });
        textViewEventGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                             }
        });
        textViewEventCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            }
        });
        textViewEventGather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                           }
        });
        textViewEventConference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                             }
        });
        textViewEventShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                       }
        });
        textViewEventShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                       }
        });
        textViewEventScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                         }
        });
    }

    private void initBottomSheet() {
        bottomSheetType = findViewById(R.id.sheetType);
        bottomSheetBehaviorType= BottomSheetBehavior.from(bottomSheetType);

        bottomSheetDate = findViewById(R.id.sheetDate);
        bottomSheetBehaviorDate= BottomSheetBehavior.from(bottomSheetDate);

        bottomSheetSort = findViewById(R.id.sheetSort);
        bottomSheetBehaviorSort= BottomSheetBehavior.from(bottomSheetSort);

        bottomSheetLocation = findViewById(R.id.sheetLocation);
        bottomSheetBehaviorLocation= BottomSheetBehavior.from(bottomSheetLocation);

        bottomSheetPrice = findViewById(R.id.sheetPrice);
        bottomSheetBehaviorPrice= BottomSheetBehavior.from(bottomSheetPrice);

    }

    private void changeActualType(TypeEvent typeEvent){
        currentType = typeEvent;
        textViewEventArt.setTextColor(Color.DKGRAY);
        textViewEventSport.setTextColor(Color.DKGRAY);
        textViewEventParty.setTextColor(Color.DKGRAY);
        textViewEventMusic.setTextColor(Color.DKGRAY);
        textViewEventMovie.setTextColor(Color.DKGRAY);
        textViewEventGame.setTextColor(Color.DKGRAY);
        textViewEventCar.setTextColor(Color.DKGRAY);
        textViewEventGather.setTextColor(Color.DKGRAY);
        textViewEventConference.setTextColor(Color.DKGRAY);
        textViewEventShop.setTextColor(Color.DKGRAY);
        textViewEventShow .setTextColor(Color.DKGRAY);
        textViewEventScience.setTextColor(Color.DKGRAY);

    }

    private void changeActualSort(FiltreSort filtreSort){
        currentSort = filtreSort;
        textViewSortPertinence.setTextColor(Color.DKGRAY);
        textViewSortPriceDESC.setTextColor(Color.DKGRAY);
        textViewSortPriceASC.setTextColor(Color.DKGRAY);
        textViewSortDate.setTextColor(Color.DKGRAY);
        textViewSortLocation.setTextColor(Color.DKGRAY);
        textViewSortPertinence.setCompoundDrawablesWithIntrinsicBounds(R.drawable.filtre_sort, 0, 0, 0);
        textViewSortPriceDESC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.filtre_sort, 0, 0, 0);
        textViewSortPriceASC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.filtre_sort, 0, 0, 0);
        textViewSortDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.filtre_sort, 0, 0, 0);
        textViewSortLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.filtre_sort, 0, 0, 0);

        switch (filtreSort){
            case PERTINENCE:
                selectedSort(textViewSortPertinence,getResources().getString(R.string.filter_sort_pertinence));
                break;
            case PRICEDESC:selectedSort(textViewSortPriceDESC,getResources().getString(R.string.filter_sort_price_desc));
                break;
            case PRICEASC:selectedSort(textViewSortPriceASC,getResources().getString(R.string.filter_sort_price_asc));
                break;
            case DATE:selectedSort(textViewSortDate,getResources().getString(R.string.filter_sort_date));
                break;
            case LOCATION:selectedSort(textViewSortLocation,getResources().getString(R.string.filter_sort_location));
            break;
        }

    }

    private void selectedSort(TextView textView, String text){
        textViewSort.setText(text);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort, 0, 0, 0);
    }

    private void bottomCollapsedToExpanded(FiltreEvent filtreEvent) {
        bottomExpandedToCollapsed();
        viewGrey.setVisibility(View.VISIBLE);
        switch (filtreEvent){
            case DATE:if(bottomSheetBehaviorDate.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehaviorDate.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case SORT:if(bottomSheetBehaviorSort.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheetBehaviorSort.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case TYPE:if(bottomSheetBehaviorType.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case PRICE:if(bottomSheetBehaviorPrice.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheetBehaviorPrice.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case LOCATION:if(bottomSheetBehaviorLocation.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheetBehaviorLocation.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    private void bottomExpandedToCollapsed() {

        if(bottomSheetBehaviorPrice.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehaviorPrice.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if(bottomSheetBehaviorDate.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehaviorDate.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if(bottomSheetBehaviorSort.getState() == BottomSheetBehavior.STATE_EXPANDED ){
            bottomSheetBehaviorSort.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if(bottomSheetBehaviorLocation.getState() == BottomSheetBehavior.STATE_EXPANDED ){
            bottomSheetBehaviorLocation.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if(bottomSheetBehaviorType.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehaviorType.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        viewGrey.setVisibility(View.GONE);
    }

    private void changeTextViewType(){
        textViewType.setCompoundDrawablesWithIntrinsicBounds(getDrawableType(currentType), 0, 0, 0);
        textViewType.setText(getTextType(currentType));
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
}