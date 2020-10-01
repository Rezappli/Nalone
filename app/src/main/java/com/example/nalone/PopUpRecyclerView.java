package com.example.nalone;

public class PopUpRecyclerView extends RecyclerView.Adapter<PopupRecyclerViewAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<String> data;

    public PopupRecyclerViewAdapter(Context mContext, ArrayList<String> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_popup_card_item, parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    //View Holder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.tv_text2);
        }
    }
}
