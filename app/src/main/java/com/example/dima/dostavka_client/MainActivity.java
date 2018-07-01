package com.example.dima.dostavka_client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dima.dostavka_client.Helper.AdapterMainOrder;

import java.util.ArrayList;
import java.util.List;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;

public class MainActivity extends AppCompatActivity {
    private String COLLECTION_CUSTOMER_BALASHIHA = "customer_balashiha";
    private String COLLECTION_FOR_WORK_BALASHIHA = "for_work_balashiha";

    final String SAVED_TEXT_NUMBER = "saved_number";
    final String SAVED_TEXT_ID_CUSTOMER = "saved_id_customer";
    final String SAVED_TEXT_NAME_CUSTOMER = "saved_name_customer";
    final String SAVED_TEXT_ADDRESS_CUSTOMER = "saved_address_customer";

    private String idCustomer;
    private String nameCustomer;
    private String addressCustomer;
    private String logIN;

    public SharedPreferences preferences;
    private Boolean checkPref;

    ListView listMainOrder;
    AdapterMainOrder adapterMainOrder;
    private Adapter adapterT;
    List<DocumentInfo> listInfo10 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listMainOrder = findViewById(R.id.listMainActivity);

        checkPref = checkPref();
        if (!checkPref) initCustomer(preferences.getString(SAVED_TEXT_NUMBER, ""));

        startWork();


        FloatingActionButton button = findViewById(R.id.fabMain);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StartOrderActivity.class);
                intent.putExtra("iD", preferences.getString(SAVED_TEXT_ID_CUSTOMER, ""));
                intent.putExtra("NameCustom", preferences.getString(SAVED_TEXT_NAME_CUSTOMER, ""));
                intent.putExtra("AddressCustom", preferences.getString(SAVED_TEXT_ADDRESS_CUSTOMER, ""));
                intent.putExtra("login", preferences.getString(SAVED_TEXT_NUMBER, ""));
                startActivity(intent);
            }
        });

        listMainOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "В разработке", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startWork() {
            String id = preferences.getString(SAVED_TEXT_ID_CUSTOMER, "");

            Query query = new Query(COLLECTION_FOR_WORK_BALASHIHA);
            query.equalTo("idCustomer", id);
            query.findDocuments(new CallbackFindDocument() {
                @Override
                public void onDocumentFound(List<DocumentInfo> documentInfos) {
                    setAdapter(documentInfos);
                }

                @Override
                public void onDocumentNotFound(String errorCode, String errorMessage) {
                    listMainOrder.setAdapter((ListAdapter) adapterT);
                    Toast.makeText(MainActivity.this, "Заказов нет", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void initCustomer(String login) {
        Query query = new Query(COLLECTION_CUSTOMER_BALASHIHA);
        query.equalTo("loginCustomer", login);

        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
               String idCustomer = documentInfos.get(0).getId();
               String nameCustomer = documentInfos.get(0).getFields().get("nameCustomer").toString();
               String addressCustomer = documentInfos.get(0).getFields().get("addressCustomer").toString();
               newPref(idCustomer,nameCustomer,addressCustomer );
               startWork();
            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                    Toast.makeText(MainActivity.this, "Пользователь не найден. Обратитесь в службу " +
                            "поддержки", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setAdapter(List<DocumentInfo> listInfo){
        int z = listInfo.size()-1;
        for (int i = 0; i<listInfo.size() ; i++){
             listInfo10.add(i, listInfo.get(z-i));
        }
        adapterMainOrder = new AdapterMainOrder(this, listInfo10 );
        listMainOrder.setAdapter(adapterMainOrder);
    }

    private void newPref(String id, String nameCustomer, String addressCustomer){

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = preferences.edit();

        ed.putString(SAVED_TEXT_ID_CUSTOMER, id);
        ed.putString(SAVED_TEXT_NAME_CUSTOMER, nameCustomer);
        ed.putString(SAVED_TEXT_ADDRESS_CUSTOMER, addressCustomer);
        ed.commit();
}

    private boolean checkPref(){
         preferences = PreferenceManager.getDefaultSharedPreferences(this);
         String login = preferences.getString(SAVED_TEXT_NUMBER, "");
         String id = preferences.getString(SAVED_TEXT_ID_CUSTOMER, "");
         String name = preferences.getString(SAVED_TEXT_NAME_CUSTOMER, "");
         String address = preferences.getString(SAVED_TEXT_ADDRESS_CUSTOMER, "");

            if (!login.isEmpty()&&!id.isEmpty()&&!name.isEmpty()&&!address.isEmpty()){
                return true;
            }
    else return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bottom_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int settings = item.getItemId();
        switch (settings){
            case R.id.settings : {
                Toast.makeText(MainActivity.this, "В разработке", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startWork();
    }

    @Override
    protected void onPause() {
        super.onPause();
        listInfo10.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
       listInfo10.clear();
    }

}

