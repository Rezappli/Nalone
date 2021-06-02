package com.nolonely.mobile.ui.recherche;

import androidx.fragment.app.Fragment;

public class RechercheFragment extends Fragment {

/*    private RecyclerView recyclerAmis;
    private SearchUserAdapter adapterAmis;
    private RechercheGroupeAdapter adapterGroup;
    private RecyclerView recyclerGroup;
    private CardView cardViewRechercheAmis;
    private CardView cardViewRechercheGroupes;
    private View rootView;
    private NavController navController;

    private TextView textViewRechercheAmis;
    private TextView textViewRechercheGroupes;
    private ProgressBar loading;

    private List<User> friends;
    private List<User> myGroups;

    private RelativeLayout relativeSansAmis, relativeSansGroup;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recherche, container, false);

        return rootView;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createFragment() {
        buttonBack.setVisibility(View.GONE);
        myGroups = new ArrayList<>();
        recyclerAmis = rootView.findViewById(R.id.recyclerViewRechercheAmis);
        recyclerGroup = rootView.findViewById(R.id.recyclerViewRechercheGroupes);
        cardViewRechercheAmis = rootView.findViewById(R.id.cardViewRechercheAmis);
        cardViewRechercheGroupes = rootView.findViewById(R.id.cardViewRechercheGroupes);
        textViewRechercheAmis = rootView.findViewById(R.id.textViewRechercheAmis);
        textViewRechercheGroupes = rootView.findViewById(R.id.textViewRechercheGroupes);
        loading = rootView.findViewById(R.id.search_loading);
        cardViewRechercheAmis.setVisibility(View.INVISIBLE);
        cardViewRechercheGroupes.setVisibility(View.INVISIBLE);
        relativeSansAmis = rootView.findViewById(R.id.relativeSansAmis);
        relativeSansGroup = rootView.findViewById(R.id.relativeSansGroup);

        textViewRechercheAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_recherche_to_navigation_recherche_amis);
            }
        });

        textViewRechercheGroupes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navController.navigate(R.id.action_navigation_recherche_to_navigation_rcherche_groupe);
            }
        });

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        getFriends();
        getGroups();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getFriends() {
        friends = new ArrayList<>();

        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());
        params.putCryptParameter("limit", 3); //fix a limit to 10 users

        JSONController.getJsonArrayFromUrl(Constants.URL_USER_WHITHOUT_ME, getContext(), params, new JSONArrayListener() {
            @Override
            public void onJSONReceived(JSONArray jsonArray) {
                Log.w("Response", "Valeur" + jsonArray.toString());
                try {
                    if (jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            friends.add((User) JSONController.convertJSONToObject(jsonArray.getJSONObject(i), User.class));
                        }

                        configureRecyclerViewAmis();

                        relativeSansAmis.setVisibility(View.GONE);
                    } else {
                        relativeSansAmis.setVisibility(View.VISIBLE);
                        // cardViewRechercheAmis.setVisibility(View.GONE);
                    }

                    loading.setVisibility(View.GONE);
                    cardViewRechercheAmis.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    Log.w("Response", "Erreur:" + e.getMessage());
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
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
    private void getGroups() {
        myGroups = new ArrayList<>();

        relativeSansGroup.setVisibility(View.VISIBLE);
        cardViewRechercheGroupes.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    private void configureRecyclerViewAmis() {
        this.adapterAmis = new SearchUserAdapter(this.friends);
        this.recyclerAmis.setAdapter(this.adapterAmis);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.recyclerAmis.setLayoutManager(llm);
        adapterAmis.setOnItemClickListener(new SearchUserAdapter.OnItemClickListener() {
            @Override
            public void onDisplayClick(int position) {
                showPopUpProfil(friends.get(position));
            }
        });
    }

    private void configureRecyclerViewGroup() {
        this.recyclerGroup.setAdapter(this.adapterGroup);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.recyclerGroup.setLayoutManager(llm);
    }


    public void showPopUpGroup(final Group g) {
        PopUpGroupFragment.GROUP_LOAD = g;
        // navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_group);
    }

    public void showPopUpProfil(User u) {

        PopupProfilFragment.USER_LOAD = u;
        PopupProfilFragment.button = R.drawable.ic_baseline_add_circle_outline_24;

        PopupProfilFragment.type = "recherche";
        navController.navigate(R.id.action_navigation_recherche_to_navigation_popup_profil);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        createFragment();
    }*/
}