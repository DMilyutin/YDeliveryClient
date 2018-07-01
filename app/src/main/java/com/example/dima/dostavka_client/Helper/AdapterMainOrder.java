package com.example.dima.dostavka_client.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dima.dostavka_client.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;

public class AdapterMainOrder extends BaseAdapter {
    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    LayoutInflater inflater;
    List<DocumentInfo> list;

    public AdapterMainOrder(Context context1, List<DocumentInfo> list1){

        list = list1;

        inflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view1, ViewGroup viewGroup) {
        int res = R.drawable.ic_search_blue_24dp;
        View view = view1;
        if(view == null){view = inflater.inflate(R.layout.for_list_main, viewGroup, false);}

        String dat = format.format(list.get(position).getDate("createdAt"));
        String status = list.get(position).getFields().get("statusOrder").toString();

        if(status.equals("Выполняется"))res =R.drawable.ic_leave_green_24dp;
        if(status.equals("Завершен"))res = R.drawable.ic_check_green_24dp;
        if(status.equals("Отменен"))res = R.drawable.ic_cancel_red_24dp;



        ((TextView) view.findViewById(R.id.nameOrderMainList)).setText(list.get(position).getFields().get("nameOrder").toString());

        ((TextView) view.findViewById(R.id.dataOrderMainList)).setText(dat);
        ((TextView) view.findViewById(R.id.coastOrderMainList)).setText(list.get(position).getFields().get("coastOrder").toString());
        ((ImageView) view.findViewById(R.id.ivStatusOrder)).setImageResource(res);




        return view;
    }

    public Order getOrder(int position){


        Order order = new Order(list.get(position).getFields().get("nameCustomer").toString(),
                list.get(position).getFields().get("addressCustomer").toString(),
                list.get(position).getFields().get("coastOrder").toString(),
                list.get(position).getFields().get("numberOfAddresses").toString(),
                list.get(position).getId(),
                list.get(position).getFields().get("addressForDriver").toString());

        return order;
    }
}
