package com.example.nalone.ui.message.display;

import androidx.fragment.app.Fragment;

public class MessagesGroupeFragment extends Fragment {


 /*   private NavController navController;
    private RecyclerView mRecyclerView;
    private ImageView addGroup;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_mes_groupe, container, false);
        createFragment();
        return rootView;
    }

    private void createFragment() {
        mRecyclerView = rootView.findViewById(R.id.recyclerViewGroupe);
        addGroup = rootView.findViewById(R.id.create_group_button);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mSwipeRefreshLayout = rootView.findViewById(R.id.messageGroupSwipeRefreshLayout);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFragment();
            }
        });

        adapterGroups();
    }

    private void adapterGroups() {

    }

    private class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView nomGroup;
        private LinearLayout layoutGroup;
        private ImageView imageGroup;
        private ImageView button;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            nomGroup = itemView.findViewById(R.id.nomGroupe);
            layoutGroup = itemView.findViewById(R.id.layoutGroupe);
            imageGroup = itemView.findViewById(R.id.imageGroupe);

        }

    }

    public void showPopUpGroup(final Group g) {
        PopUpMesGroupesFragment.GROUP_LOAD = g;
        navController.navigate(R.id.action_navigation_mes_groupes_to_navigation_popup_mes_groupes);
    }

    @Override
    public void onResume() {
        super.onResume();
        createFragment();
    }*/
}