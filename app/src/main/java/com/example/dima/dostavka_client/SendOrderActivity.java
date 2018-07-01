package com.example.dima.dostavka_client;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dima.dostavka_client.Helper.AdapterForStartOrder;
import com.example.dima.dostavka_client.Helper.Addresser;

import java.util.ArrayList;
import java.util.List;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackGetDocumentById;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackRemoveDocument;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackUpdateDocument;
import ru.profit_group.scorocode_sdk.Responses.data.ResponseRemove;
import ru.profit_group.scorocode_sdk.Responses.data.ResponseUpdate;
import ru.profit_group.scorocode_sdk.scorocode_objects.Document;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;
import ru.profit_group.scorocode_sdk.scorocode_objects.Update;

public class SendOrderActivity extends AppCompatActivity {
    private String COLLECTION_WORK_BALASHIHA = "work_balashiha";
    private String COLLECTION_FOR_WORK_BALASHIHA = "for_work_balashiha";

    private ListView listSendOrder;
    AdapterForStartOrder adapter;
    AlertDialog dlg;

    FloatingActionButton fubAddAddress;
    FloatingActionButton fabCancleOrder;

    List<Addresser> list;
    private String idOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_order);

        listSendOrder = findViewById(R.id.listSendOrder);

        fubAddAddress = findViewById(R.id.fabAddOrder);
        fabCancleOrder = findViewById(R.id.fabCancleOrder);

        final Intent intent = getIntent();
        idOrder = intent.getStringExtra("idOrderForWork");

        String mach2 = intent.getStringExtra("Mach");
        final int mach = Integer.parseInt(mach2);

        list = new ArrayList<>();



        startList(mach);



         fubAddAddress.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                getOllAddress(mach);
                rez();

             }
         });


         fabCancleOrder.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 removeOrderFromWorkBalashiha();
             }
         });


        listSendOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int poss, long l) {

                View dialog = getLayoutInflater().inflate(R.layout.for_dialog_start_order, null);
                final EditText name = dialog.findViewById(R.id.etNameForDialog);
                final EditText phone = dialog.findViewById(R.id.etPhoneForDialog);
                final EditText adress = dialog.findViewById(R.id.etAddressForDialog);


                final AlertDialog.Builder builder = new AlertDialog.Builder(SendOrderActivity.this);
                builder.setView(dialog).setTitle("Заказчик").setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String nameS = name.getText().toString();
                        String phoneS = phone.getText().toString();
                        String addressS = adress.getText().toString();

                        Addresser a = new Addresser();
                        a.setName(nameS);
                        a.setPhone(phoneS);
                        a.setAddress(addressS);

                        View view1 = adapter.getView(poss, view, null);

                        ((TextView) view1.findViewById(R.id.etNameForList)).setText(nameS);
                        ((TextView) view1.findViewById(R.id.etPhoneForList)).setText(phoneS);
                        ((TextView) view1.findViewById(R.id.etAddressForList)).setText(addressS);


                        list.remove(poss);
                        list.add(poss, a);

                        dlg.dismiss();
                    }
                });

                dlg = builder.create();
                dlg.show();
                dlg.getWindow().setBackgroundDrawableResource(R.color.colorCofe1);
            }
        });
    }

    private void cancleOrder(String idOrder) {
        Query query = new Query(COLLECTION_FOR_WORK_BALASHIHA);
        query.equalTo("_id", idOrder);

        Update update = new Update();
        update.set("statusOrder", "Отменен");
        query.updateDocument(update, new CallbackUpdateDocument() {
            @Override
            public void onUpdateSucceed(ResponseUpdate responseUpdate) {
                rez();
                Toast.makeText(SendOrderActivity.this, "Заказ успешно удален",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateFailed(String errorCode, String errorMessage) {

            }
        });

    }

    private  void removeOrderFromWorkBalashiha(){

        Query query = new Query(COLLECTION_WORK_BALASHIHA);
        query.equalTo("idForWorkBalashiha", idOrder);

        query.removeDocument(new CallbackRemoveDocument() {
            @Override
            public void onRemoveSucceed(ResponseRemove responseRemove) {
                cancleOrder(idOrder);
            }

            @Override
            public void onRemoveFailed(String errorCode, String errorMessage) {
                Toast.makeText(SendOrderActivity.this, "Ваш заказ принял водитель",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rez(){
        Intent intent1 = new Intent();
        intent1.putExtra("erf", "ref");
        setResult(RESULT_OK, intent1);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Loog", "SendActivity уничтожена");
    }

    private void getOllAddress(int mach) {
        Addresser addresser;
        String name = "";
        String phone = "";
        String address= "";
        for(int i = 0; i<mach; i++){
            addresser =  list.get(i);
            name +=    addresser.getName();
            phone +=   addresser.getPhone();
            address += addresser.getAddress();
            if(i!=mach-1){
                name += ";";
                phone += ";";
                address += ";";
            }
        }


        //addInfoInServer(name, phone, address, COLLECTION_WORK_BALASHIHA);
        addInfoInServer(name, phone, address, COLLECTION_FOR_WORK_BALASHIHA);
    }

    private void addInfoInServer(String name, String phone, String address, String collection) {
        Query query = new Query(collection);
        query.equalTo("_id", idOrder);

        Update update = new Update();
        update.set("nameForDriver", name).set("phoneForDriver", phone).set("addressForDriver", address);

        query.updateDocument(update, new CallbackUpdateDocument() {
            @Override
            public void onUpdateSucceed(ResponseUpdate responseUpdate) {
                Toast.makeText(SendOrderActivity.this, "Заказ создан", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateFailed(String errorCode, String errorMessage) {

            }
        });

    }


    private void startList(int machh) {
        Addresser a = new Addresser();
      adapter = new AdapterForStartOrder(this, list);
      listSendOrder.setAdapter(adapter);
      for(int i=0; i<machh; i++)
        list.add(a);
  }


    @Override
    public void onBackPressed() {
        Toast.makeText(SendOrderActivity.this, "Завершите или удалите заказ", Toast.LENGTH_SHORT).show();
        //removeOrderFromWorkBalashiha();
        //rez();
    }





}