package com.example.nalone.ui.evenements.display;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.nalone.R;
import com.example.nalone.adapter.SearchEvenementAdapter;
import com.example.nalone.adapter.TypeEventAdapter;
import com.example.nalone.enumeration.FiltreDate;
import com.example.nalone.enumeration.FiltreEvent;
import com.example.nalone.enumeration.FiltreSort;
import com.example.nalone.enumeration.TypeEvent;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.objects.Evenement;
import com.example.nalone.ui.evenements.InfosEvenementsActivity;
import com.example.nalone.util.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.nalone.util.Constants.USER;

public class EventListFragment extends Fragment {

    private LinearLayout linearLayout, linearNoResult;
    private SearchView searchView;
    private ImageView imageViewFiltreSearch;
    private RecyclerView recyclerTypeEvent;

    private TextView textViewSort, textViewDate, textViewLocation, textViewPrice;
    private BottomSheetBehavior bottomSheetBehaviorDate, bottomSheetBehaviorSort, bottomSheetBehaviorPrice, bottomSheetBehaviorLocation, bottomSheetBehaviorParticipation;
    private View viewGrey, bottomSheetType, bottomSheetDate, bottomSheetSort, bottomSheetPrice, bottomSheetLocation, bottomSheetParticipation;
    private TypeEvent currentType;
    private FiltreSort currentSort;
    private FiltreDate currentDate;
    private int currentLocation, currentPrice;
    private TextView textViewSortPertinence, textViewSortPriceASC, textViewSortPriceDESC, textViewSortLocation, textViewSortDate;
    private TextView textViewDateToday, textViewDateTomorrow, textViewDateWeek, textViewDateMonth, textViewDateOther;
    private SearchEvenementAdapter mAdapter;
    private RecyclerView mRecycler;
    private List<Evenement> evenementList;
    private boolean hasChange;
    private CardView cardViewLoading;
    private ImageView buttonBack;
    private View rootView;

    @SuppressLint("CutPasteId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home_list, container, false);
        linearNoResult = rootView.findViewById(R.id.linearNoResult);
        imageViewFiltreSearch = rootView.findViewById(R.id.filtreSearch);
        searchView = rootView.findViewById(R.id.searchViewSheet);
        recyclerTypeEvent = rootView.findViewById(R.id.recyclerTypeEvent);
        searchView.setQueryHint("Recherche");
        initFiltres();

        //  buttonBack = rootView.findViewById(R.id.buttonBack);
        // buttonBack.setOnClickListener(v -> onBackPressed());
        currentSort = FiltreSort.PERTINENCE;
        currentDate = FiltreDate.NONE;
        currentType = null;
        currentLocation = Constants.range;
        cardViewLoading = rootView.findViewById(R.id.cardViewLoading);
        cardViewLoading.setVisibility(View.GONE);

        viewGrey = rootView.findViewById(R.id.viewGrey);
        viewGrey.setOnClickListener(v -> {
            if (hasChange) {
                getEventFiltred();
            }
            bottomExpandedToCollapsed();
        });

        initTextView();
        initBottomSheet();
        getEventFiltred();
        return rootView;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initFiltres() {
        TypeEventAdapter typeAdapter = new TypeEventAdapter(TypeEvent.listOfNames(getContext()), TypeEvent.listOfImages(getContext()));
        typeAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getContext(), SearchEventActivity.class);
            intent.putExtra("type", TypeEvent.values()[position].toString());
            startActivity(intent);
        });
        recyclerTypeEvent.setAdapter(typeAdapter);
        final LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerTypeEvent.setLayoutManager(llm2);
    }

    private void initRecyclerView() {
        mRecycler = rootView.findViewById(R.id.recyclerViewSearch);
        mAdapter = new SearchEvenementAdapter(evenementList);
        mAdapter.setOnItemClickListener(new SearchEvenementAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                Intent intent = new Intent(getContext(), InfosEvenementsActivity.class);
                intent.putExtra("event", evenementList.get(position));
                intent.putExtra("isRegistered", false);
                startActivity(intent);
            }

            @Override
            public void onParticipateClick(int position) {
                bottomSheetBehaviorParticipation.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewGrey.setVisibility(View.VISIBLE);
            }
        });
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getEventFiltred() {
        cardViewLoading.setVisibility(View.VISIBLE);
        linearNoResult.setVisibility(View.GONE);
        JSONObjectCrypt params = new JSONObjectCrypt();

        params.putCryptParameter("uid", USER.getUid());
        if (currentType != null) {
            params.putCryptParameter("type", currentType.toString());
        }
        params.putCryptParameter("sort", currentSort.toString());
        if (currentDate != FiltreDate.NONE)
            params.putCryptParameter("date", currentDate.toString());

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_FILTRE, requireContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    evenementList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        evenementList.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                    }
                    initRecyclerView();
                    if (evenementList.isEmpty()) {
                        linearNoResult.setVisibility(View.VISIBLE);
                    } else {
                        linearNoResult.setVisibility(View.GONE);
                    }
                    cardViewLoading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur:" + volleyError.toString());
                Toast.makeText(getContext(), getResources().getString(R.string.error_event), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Methode permettant l'initialisation des text view de la fenetre
     * Event category
     * Sort filter
     * Date filter
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTextView() {
        initTextViewSort();
        initTextViewDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTextViewDate() {
        textViewDateToday = rootView.findViewById(R.id.textViewDateToday);
        textViewDateToday.setOnClickListener(v -> changeActualDate(FiltreDate.TODAY));
        textViewDateTomorrow = rootView.findViewById(R.id.textViewDateTomorrow);
        textViewDateTomorrow.setOnClickListener(v -> changeActualDate(FiltreDate.TOMORROW));
        textViewDateWeek = rootView.findViewById(R.id.textViewDateWeek);
        textViewDateWeek.setOnClickListener(v -> changeActualDate(FiltreDate.WEEK));
        textViewDateMonth = rootView.findViewById(R.id.textViewDateMonth);
        textViewDateMonth.setOnClickListener(v -> changeActualDate(FiltreDate.MONTH));
        textViewDateOther = rootView.findViewById(R.id.textViewDateOther);
        textViewDateOther.setOnClickListener(v -> changeActualDate(FiltreDate.OTHER));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTextViewSort() {
        textViewSort = rootView.findViewById(R.id.textViewSort);
        textViewSortDate = rootView.findViewById(R.id.textViewSortDate);
        textViewSortDate.setOnClickListener(v -> changeActualSort(FiltreSort.DATE));
        textViewSortLocation = rootView.findViewById(R.id.textViewSortLocation);
        textViewSortLocation.setOnClickListener(v -> changeActualSort(FiltreSort.LOCATION));
        textViewSortPertinence = rootView.findViewById(R.id.textViewSortPertinence);
        textViewSortPertinence.setOnClickListener(v -> changeActualSort(FiltreSort.PERTINENCE));
        textViewSortPriceASC = rootView.findViewById(R.id.textViewSortPriceASC);
        textViewSortPriceASC.setOnClickListener(v -> changeActualSort(FiltreSort.PRICEASC));

        textViewSortPriceDESC = rootView.findViewById(R.id.textViewSortPriceDESC);
        textViewSortPriceDESC.setOnClickListener(v -> changeActualSort(FiltreSort.PRICEDESC));

        textViewLocation = rootView.findViewById(R.id.textViewLocation);
        textViewPrice = rootView.findViewById(R.id.textViewPrice);

        textViewDate = rootView.findViewById(R.id.textViewDate);
        textViewDateToday = rootView.findViewById(R.id.textViewDateToday);
        textViewDateTomorrow = rootView.findViewById(R.id.textViewDateTomorrow);
        textViewDateWeek = rootView.findViewById(R.id.textViewDateWeek);
        textViewDateMonth = rootView.findViewById(R.id.textViewDateMonth);
        textViewDateOther = rootView.findViewById(R.id.textViewDateOther);


        textViewLocation.setOnClickListener(v -> bottomCollapsedToExpanded(FiltreEvent.LOCATION));

        textViewPrice.setOnClickListener(v -> bottomCollapsedToExpanded(FiltreEvent.PRICE));

        textViewDate.setOnClickListener(v -> bottomCollapsedToExpanded(FiltreEvent.DATE));

        textViewSort.setOnClickListener(v -> bottomCollapsedToExpanded(FiltreEvent.SORT));
    }

    private void initBottomSheet() {
        bottomSheetType = rootView.findViewById(R.id.sheetType);

        bottomSheetDate = rootView.findViewById(R.id.sheetDate);
        bottomSheetBehaviorDate = BottomSheetBehavior.from(bottomSheetDate);

        bottomSheetSort = rootView.findViewById(R.id.sheetSort);
        bottomSheetBehaviorSort = BottomSheetBehavior.from(bottomSheetSort);

        bottomSheetLocation = rootView.findViewById(R.id.sheetLocation);
        bottomSheetBehaviorLocation = BottomSheetBehavior.from(bottomSheetLocation);

        bottomSheetPrice = rootView.findViewById(R.id.sheetPrice);
        bottomSheetBehaviorPrice = BottomSheetBehavior.from(bottomSheetPrice);

        bottomSheetParticipation = rootView.findViewById(R.id.sheetParticipate);
        bottomSheetBehaviorParticipation = BottomSheetBehavior.from(bottomSheetParticipation);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeActualDate(FiltreDate filtreDate) {
        if (filtreDate != currentDate) {
            currentDate = filtreDate;
            textViewDateToday.setTextColor(Color.DKGRAY);
            textViewDateTomorrow.setTextColor(Color.DKGRAY);
            textViewDateWeek.setTextColor(Color.DKGRAY);
            textViewDateMonth.setTextColor(Color.DKGRAY);
            textViewDateOther.setTextColor(Color.DKGRAY);

            textViewDateToday.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_event_24, 0, 0, 0);
            textViewDateTomorrow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_event_24, 0, 0, 0);
            textViewDateWeek.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_event_24, 0, 0, 0);
            textViewDateMonth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_event_24, 0, 0, 0);
            textViewDateOther.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_event_24, 0, 0, 0);


            switch (filtreDate) {
                case WEEK:
                    selectedDate(textViewDateWeek, getResources().getString(R.string.filter_date_week));
                    break;
                case MONTH:
                    selectedDate(textViewDateMonth, getResources().getString(R.string.filter_date_month));
                    break;
                case OTHER:
                    selectedDate(textViewDateOther, getResources().getString(R.string.filter_date_choose));
                    break;
                case TODAY:
                    selectedDate(textViewDateToday, getResources().getString(R.string.filter_date_today));
                    break;
                case TOMORROW:
                    selectedDate(textViewDateTomorrow, getResources().getString(R.string.filter_date_tomorrow));
                    break;
            }
            hasChange = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeActualSort(FiltreSort filtreSort) {
        if (filtreSort != currentSort) {
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

            switch (filtreSort) {
                case PERTINENCE:
                    selectedSort(textViewSortPertinence, getResources().getString(R.string.filter_sort_pertinence));
                    break;
                case PRICEDESC:
                    selectedSort(textViewSortPriceDESC, getResources().getString(R.string.filter_sort_price_desc));
                    break;
                case PRICEASC:
                    selectedSort(textViewSortPriceASC, getResources().getString(R.string.filter_sort_price_asc));
                    break;
                case DATE:
                    selectedSort(textViewSortDate, getResources().getString(R.string.filter_sort_date));
                    break;
                case LOCATION:
                    selectedSort(textViewSortLocation, getResources().getString(R.string.filter_sort_location));
                    break;
            }
            hasChange = true;
        }

    }

    private void selectedDate(TextView textView, String text) {
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_event_24_primary, 0, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        textViewDate.setText(text);
        textViewDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_event_24_primary, 0, 0, 0);
    }

    private void selectedSort(TextView textView, String text) {
        textViewSort.setText(text);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sort, 0, 0, 0);
    }

    private void bottomCollapsedToExpanded(FiltreEvent filtreEvent) {
        bottomExpandedToCollapsed();
        viewGrey.setVisibility(View.VISIBLE);
        switch (filtreEvent) {
            case DATE:
                if (bottomSheetBehaviorDate.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehaviorDate.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case SORT:
                if (bottomSheetBehaviorSort.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehaviorSort.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case PRICE:
                if (bottomSheetBehaviorPrice.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehaviorPrice.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case LOCATION:
                if (bottomSheetBehaviorLocation.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehaviorLocation.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
    }

    private void bottomExpandedToCollapsed() {

        if (bottomSheetBehaviorPrice.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorPrice.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if (bottomSheetBehaviorDate.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorDate.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if (bottomSheetBehaviorSort.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorSort.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if (bottomSheetBehaviorLocation.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorLocation.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        if (bottomSheetBehaviorParticipation.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorParticipation.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        viewGrey.setVisibility(View.GONE);
    }


}