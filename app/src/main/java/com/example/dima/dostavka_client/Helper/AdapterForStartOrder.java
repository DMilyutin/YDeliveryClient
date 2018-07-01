package com.example.dima.dostavka_client.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dima.dostavka_client.R;

import java.util.List;


public class AdapterForStartOrder extends BaseAdapter {

    LayoutInflater inflater;
    List<Addresser> list;

    public AdapterForStartOrder(Context context, List<Addresser> listS){
        list = listS;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){view = inflater.inflate(R.layout.for_list_start_order, viewGroup, false);}


        view.findViewById(R.id.etNameForList)   ;
        view.findViewById(R.id.etPhoneForList)  ;
        view.findViewById(R.id.etAddressForList);

        return view;
    }

    public Addresser getAddressFromList(int poss){
        return list.get(poss);

    }

}
