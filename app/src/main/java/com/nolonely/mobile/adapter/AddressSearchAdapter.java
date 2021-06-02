package com.nolonely.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nolonely.mobile.R;
import com.nolonely.mobile.objects.AddressSearch;

import java.util.List;

public class AddressSearchAdapter extends BaseAdapter {

    private List<AddressSearch> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public AddressSearchAdapter(Context aContext, List<AddressSearch> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<AddressSearch> getList() {
        return listData;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        convertView = layoutInflater.inflate(R.layout.item_address_search, null);
        holder.addressText = convertView.findViewById(R.id.addressText);
        holder.addressText.setText(listData.get(position).getAddress() + "");

        return convertView;
    }

    static class ViewHolder {
        TextView addressText;
    }

}
