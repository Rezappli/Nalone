package com.nolonely.mobile.ui.recherche;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nolonely.mobile.R;
import com.nolonely.mobile.adapter.EvenementAdapter;
import com.nolonely.mobile.adapter.FilterAdapter;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONFragment;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.dialog.SelectDateFragment;
import com.nolonely.mobile.enumeration.filter.FiltreDate;
import com.nolonely.mobile.enumeration.filter.FiltreEvent;
import com.nolonely.mobile.enumeration.filter.FiltreOwner;
import com.nolonely.mobile.enumeration.filter.FiltrePrice;
import com.nolonely.mobile.enumeration.filter.FiltreSort;
import com.nolonely.mobile.enumeration.filter.FiltreType;
import com.nolonely.mobile.listeners.JSONArrayListener;
import com.nolonely.mobile.objects.Evenement;
import com.nolonely.mobile.ui.evenements.InfosEvenementsActivity;
import com.nolonely.mobile.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.nolonely.mobile.dialog.SelectDateFragment.ACTION_RECEIVE_DATE;
import static com.nolonely.mobile.dialog.SelectDateFragment.EXTRA_START_DATE;
import static com.nolonely.mobile.util.Constants.USER;

public class SearchEventFragment extends JSONFragment {

    private LinearLayout linearNoResult;

    private TextView textViewType, textViewSort, textViewDate, textViewLocation, textViewPrice, textViewOwner, titleFilter;
    private BottomSheetBehavior bottomSheetBehaviorFilter;
    private View viewGrey;
    private FiltreType currentType;
    private FiltreSort currentSort;
    private FiltreDate currentDate;
    private FiltreEvent currentFiltre;
    private FiltreOwner currentOwner;
    private FiltrePrice currentPrice;
    private String currentLocation;
    private EvenementAdapter mAdapter;
    private RecyclerView mRecycler;
    private RecyclerView recyclerFilter;
    private List<Evenement> evenementList;
    private boolean hasChange;
    private ProgressBar loading;
    private ImageView buttonBack;
    private View rootView;
    private List<String> listFilterName;
    private List<Drawable> listFilterImage;
    private SwipeRefreshLayout swipeContainer;
    private String searchQuery = null;

    private final BroadcastReceiver receiverDate = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                currentDate = FiltreDate.OTHER;
                textViewDate.setText(intent.getStringExtra(EXTRA_START_DATE));
                bottomExpandedToCollapsed();
                linearNoResult.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                mRecycler.setVisibility(View.INVISIBLE);
                launchJSONCall(true);
            }
        }
    };

    @SuppressLint("CutPasteId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home_list, container, false);

        swipeContainer = rootView.findViewById(R.id.swipeRefreshListEvent);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onRefresh() {
                launchJSONCall(true);
            }
        });

        linearNoResult = rootView.findViewById(R.id.linearNoResult);

        listFilterImage = new ArrayList<>();
        listFilterName = new ArrayList<>();
        //  buttonBack = rootView.findViewById(R.id.buttonBack);
        // buttonBack.setOnClickListener(v -> onBackPressed());
        currentSort = FiltreSort.PERTINENCE;
        currentDate = null;
        currentType = null;
        currentOwner = FiltreOwner.PUBLIC;
        currentPrice = null;
        currentLocation = USER.getCity();

        loading = rootView.findViewById(R.id.loading);

        viewGrey = rootView.findViewById(R.id.viewGrey);
        viewGrey.setOnClickListener(v -> bottomExpandedToCollapsed());

        View bottomSheetFilter = rootView.findViewById(R.id.sheetFilter);
        bottomSheetBehaviorFilter = BottomSheetBehavior.from(bottomSheetFilter);
        bottomSheetBehaviorFilter.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (hasChange) {
                            linearNoResult.setVisibility(View.GONE);
                            loading.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.INVISIBLE);
                            launchJSONCall(true);
                        }
                        viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        viewGrey.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_EXPANDED:

                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        initTextViewSort();
        initRecyclerView();
        launchJSONCall(true);
        return rootView;
    }

    public void search(String query) {
        searchQuery = query;
        launchJSONCall(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_RECEIVE_DATE);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiverDate, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiverDate);
    }

    private void updateFilters(int position) {
        switch (currentFiltre) {
            case OWNER:
                currentOwner = FiltreOwner.values()[position];
                textViewOwner.setText(FiltreOwner.nameOfValue(FiltreOwner.values()[position]));
                break;
            case LOCATION:
                break;
            case PRICE:
                currentPrice = FiltrePrice.values()[position];
                textViewPrice.setText(FiltrePrice.nameOfValue(FiltrePrice.values()[position]));
                textViewPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_event_24_primary, 0, 0, 0);
                break;
            case TYPE:
                currentType = FiltreType.values()[position];
                textViewType.setText(FiltreType.nameOfValue(FiltreType.values()[position]));
                textViewType.setCompoundDrawablesWithIntrinsicBounds(FiltreType.imageOfValue(FiltreType.values()[position]), 0, 0, 0);
                Drawable[] drawables = textViewType.getCompoundDrawables();
                drawables[0].setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN));
                break;
            case DATE:
                if (FiltreDate.values()[position] == FiltreDate.values()[FiltreDate.values().length - 1]) {
                    DialogFragment newFragment = new SelectDateFragment();
                    SelectDateFragment.isStart = true;
                    newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                } else {
                    currentDate = FiltreDate.values()[position];
                    textViewDate.setText(FiltreDate.nameOfValue(FiltreDate.values()[position]));
                    bottomExpandedToCollapsed();
                }
                textViewDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_event_24_primary, 0, 0, 0);
                break;
            case SORT:
                currentSort = FiltreSort.values()[position];
                textViewSort.setText(FiltreSort.nameOfValue(FiltreSort.values()[position]));
                break;
        }


    }

    private void initRecyclerView() {
        recyclerFilter = rootView.findViewById(R.id.recyclerViewFilter);
        titleFilter = rootView.findViewById(R.id.titleFilter);
        evenementList = new ArrayList<>();
        mRecycler = rootView.findViewById(R.id.recyclerViewHomeList);
        mAdapter = new EvenementAdapter(this.evenementList, R.layout.item_evenement, false, getContext());
        mAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getContext(), InfosEvenementsActivity.class);
            intent.putExtra("event", evenementList.get(position));
            intent.putExtra("isRegistered", false);
            startActivity(intent);
        });
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getEventFiltred() {

        JSONObjectCrypt params = new JSONObjectCrypt();

        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("owner", currentOwner.toString());
        params.putCryptParameter("sort", currentSort.toString());

        if (currentType != null && currentType != FiltreType.NULL) {
            params.putCryptParameter("type", currentType.toString());
        }
        if (currentDate != null && currentDate != FiltreDate.NULL) {
            params.putCryptParameter("date", currentDate.toString());
            if (currentDate == FiltreDate.OTHER) {
                Date initDate = null;
                try {
                    initDate = new SimpleDateFormat("dd/MM/yyyy").parse(textViewDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String parsedDate = formatter.format(initDate);
                params.putCryptParameter("dateChosen", parsedDate);
            }
        }

        if (currentPrice != null) {
            params.putCryptParameter("price", currentPrice);
        }

        if (searchQuery != null) {
            params.putCryptParameter("search", searchQuery);
        }

        JSONController.getJsonArrayFromUrl(Constants.URL_EVENT_FILTRE, requireContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                try {
                    mRecycler.setVisibility(View.VISIBLE);
                    evenementList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        evenementList.add((Evenement) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), Evenement.class));
                    }
                    mAdapter.notifyDataSetChanged();
                    if (evenementList.isEmpty()) {
                        linearNoResult.setVisibility(View.VISIBLE);
                    } else {
                        linearNoResult.setVisibility(View.GONE);
                    }
                    loading.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);

                    hasChange = false;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTextViewSort() {
        textViewType = rootView.findViewById(R.id.textViewType);
        textViewSort = rootView.findViewById(R.id.textViewSort);
        textViewLocation = rootView.findViewById(R.id.textViewLocation);
        textViewOwner = rootView.findViewById(R.id.textViewOwner);
        textViewLocation.setText(currentLocation);

        textViewPrice = rootView.findViewById(R.id.textViewPrice);
        textViewDate = rootView.findViewById(R.id.textViewDate);

        textViewType.setOnClickListener(v -> {
            updateRecyclerView(FiltreEvent.TYPE);
            bottomCollapsedToExpanded();
        });

        textViewLocation.setOnClickListener(v -> {
            updateRecyclerView(FiltreEvent.LOCATION);
            bottomCollapsedToExpanded();
        });

        textViewPrice.setOnClickListener(v -> {
            updateRecyclerView(FiltreEvent.PRICE);
            bottomCollapsedToExpanded();
        });

        textViewDate.setOnClickListener(v -> {
            updateRecyclerView(FiltreEvent.DATE);
            bottomCollapsedToExpanded();
        });

        textViewSort.setOnClickListener(v -> {
            updateRecyclerView(FiltreEvent.SORT);
            bottomCollapsedToExpanded();
        });

        textViewOwner.setOnClickListener(v -> {
            updateRecyclerView(FiltreEvent.OWNER);
            bottomCollapsedToExpanded();
        });
    }

    private void updateRecyclerView(FiltreEvent filtreEvent) {
        int itemPosition = -1;
        currentFiltre = filtreEvent;
        listFilterImage.clear();
        listFilterName.clear();
        switch (filtreEvent) {
            case OWNER:
                titleFilter.setText(getString(R.string.visibility));
                listFilterName.addAll(FiltreOwner.listOfNames(getContext()));
                listFilterImage.addAll(FiltreOwner.listOfImages(getContext()));
                if (currentOwner != null)
                    itemPosition = FiltreOwner.indexOfValue(currentOwner);
                break;
            case DATE:
                titleFilter.setText(getString(R.string.dateEvent));
                listFilterName.addAll(FiltreDate.listOfNames(getContext()));
                listFilterImage.addAll(FiltreDate.listOfImages(getContext()));
                if (currentDate != null)
                    itemPosition = FiltreDate.indexOfValue(currentDate);
                break;
            case SORT:
                titleFilter.setText(getString(R.string.filter_sort));
                listFilterName.addAll(FiltreSort.listOfNames(getContext()));
                listFilterImage.addAll(FiltreSort.listOfImages(getContext()));
                if (currentSort != null)
                    itemPosition = FiltreSort.indexOfValue(currentSort);
                break;
            case TYPE:
                titleFilter.setText(getString(R.string.type));
                listFilterName.addAll(FiltreType.listOfNames(getContext()));
                listFilterImage.addAll(FiltreType.listOfImages(getContext()));
                if (currentType != null)
                    itemPosition = FiltreType.indexOfValue(currentType);
                break;
            case PRICE:
                titleFilter.setText(getString(R.string.price));
                listFilterName.addAll(FiltrePrice.listOfNames(getContext()));
                listFilterImage.addAll(FiltrePrice.listOfImages(getContext()));
                if (currentPrice != null)
                    itemPosition = FiltrePrice.indexOfValue(currentPrice);
                break;
            case LOCATION:
        }
        FilterAdapter filterAdapter = new FilterAdapter(listFilterName, listFilterImage, itemPosition);
        filterAdapter.setOnItemClickListener(position -> {
            hasChange = true;
            if (currentFiltre != FiltreEvent.DATE)
                bottomExpandedToCollapsed();
            updateFilters(position);
        });
        recyclerFilter.setAdapter(filterAdapter);
        recyclerFilter.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bottomCollapsedToExpanded() {
        viewGrey.setVisibility(View.VISIBLE);
        bottomSheetBehaviorFilter.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void bottomExpandedToCollapsed() {
        viewGrey.setVisibility(View.GONE);
        bottomSheetBehaviorFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void doInHaveInternetConnection() {
        getEventFiltred();
    }


}