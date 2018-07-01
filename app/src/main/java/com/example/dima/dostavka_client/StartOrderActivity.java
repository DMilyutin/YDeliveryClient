package com.example.dima.dostavka_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dima.dostavka_client.Helper.Order;

import java.util.List;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackDocumentSaved;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackUpdateDocument;
import ru.profit_group.scorocode_sdk.Responses.data.ResponseUpdate;
import ru.profit_group.scorocode_sdk.scorocode_objects.Document;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.Query;
import ru.profit_group.scorocode_sdk.scorocode_objects.Update;

public class StartOrderActivity extends AppCompatActivity {
    private String COLLECTION_WORK_BALASHIHA = "work_balashiha";
    private String COLLECTION_FOR_WORK_BALASHIHA = "for_work_balashiha";

    int RECVEST_CODE = 1;


    private EditText edNameCustomer;
    private EditText edPhoneCustomer;
    private EditText edAddressCustomer;
    private EditText timeCustomer;
    private EditText machAddressByers;
    private EditText coastOrder;
    private EditText nameOrder;

    private String idCustomer;
    private String nameCustomer;
    private String addressCustomer;
    private String loginCustomer;
    private String idOrder;

    private CheckBox chGetTimeCustomer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_order);
        edNameCustomer = findViewById(R.id.edNameCustomer);
        edPhoneCustomer = findViewById(R.id.edPhoneCustomer);
        edAddressCustomer = findViewById(R.id.edAddressCustomer);
        timeCustomer = findViewById(R.id.edGetTimeCustomer);
        machAddressByers = findViewById(R.id.edMachByers);
        coastOrder = findViewById(R.id.coastOrder);
        chGetTimeCustomer = findViewById(R.id.chGetTimeCustomer);
        nameOrder = findViewById(R.id.edNameOrder);
        Button btAddAddress = findViewById(R.id.btAddAddress);

        Intent intent = getIntent();
        idCustomer = intent.getStringExtra("iD");
        nameCustomer = intent.getStringExtra("NameCustom");
        addressCustomer = intent.getStringExtra("AddressCustom");
        loginCustomer = intent.getStringExtra("login");



        edNameCustomer.setText(nameCustomer);
        edAddressCustomer.setText(addressCustomer);
        //if(!addressCustomer.isEmpty()|| !addressCustomer.equals("")|| addressCustomer != null)
        edPhoneCustomer.setText(loginCustomer);


        btAddAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    Order order = new Order(
                                    edNameCustomer.getText().toString(),
                                    edPhoneCustomer.getText().toString(),
                                    edAddressCustomer.getText().toString(),
                                    timeCustomer.getText().toString(),
                                    machAddressByers.getText().toString(),
                                    coastOrder.getText().toString());
                    order.setNameOrder(nameOrder.getText().toString());

                if(!provOrder(order)){
                    Toast.makeText(StartOrderActivity.this, "Некорректные данные", Toast.LENGTH_SHORT).show();
                    return;
                }

                    inputStartOrder(order, idCustomer, COLLECTION_WORK_BALASHIHA, 0);
                    inputStartOrder(order, idCustomer, COLLECTION_FOR_WORK_BALASHIHA, 1);

            }
        });

        chGetTimeCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    timeCustomer.setText("");
                    timeCustomer.setVisibility(View.GONE);
                }
                else timeCustomer.setVisibility(View.VISIBLE);
            }
        });

        machAddressByers.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                    i == KeyEvent.KEYCODE_ENTER){
                    String coastS = machAddressByers.getText().toString();
                    setCoastOrder(coastS);
                }

                return false;
            }
        });

    }

    private void setCoastOrder(String coastS) {
        Double c = Double.parseDouble(coastS);
        switch (coastS){
            case "1" : c = c*125;
            break;
            case "2" : c = c*115;
            break;
            default: c = c * 105;
            break;
        }
        coastOrder.setText(c.toString());
    }

    private void stIntent(String machh) {
        Intent intenti = new Intent(StartOrderActivity.this, SendOrderActivity.class);
        intenti.putExtra("Mach" ,machh );
        intenti.putExtra("idOrderForWork" ,idOrder );
        startActivityForResult(intenti, RECVEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            finish();
        //onBackPressed();
    }

    private void inputStartOrder(final Order orderIn, String idCustomer, String collection, final int col) {
        Document newDocument = new Document(collection);

        String nameOrder;
        if (((orderIn.getNameOrder()).equals(""))){nameOrder = "Без названия";}
        else nameOrder = orderIn.getNameOrder();

        String timeCust = orderIn.getTimeFiled();
        if(timeCust == null || timeCust.isEmpty() || timeCust.equals(""))
            timeCust = "Ближайшее время";

        newDocument.setField("nameCustomer", orderIn.getNameCustomer());
        newDocument.setField("phoneCustomer", orderIn.getPhoneCustomer());
        newDocument.setField("addressCustomer", orderIn.getAddressCustomer());
        newDocument.setField("timeFilingCustomer", timeCust);
        newDocument.setField("numberOfAddresses", orderIn.getNumberOfAddress());
        newDocument.setField("coastOrder", orderIn.getCoastOrder());
        newDocument.setField("statusOrder", "Поиск курьера");
        newDocument.setField("addressForDriver", "Формируется");
        newDocument.setField("nameForDriver", "Формируется");
        newDocument.setField("phoneForDriver", "Формируется");
        newDocument.setField("nameOrder", nameOrder);
        newDocument.setField("idCustomer", idCustomer);

        newDocument.saveDocument(new CallbackDocumentSaved() {
           @Override
           public void onDocumentSaved() {
               if (col == 1){
               Toast.makeText(StartOrderActivity.this, "Мы уже ищем курьера", Toast.LENGTH_SHORT).show();
               idOrderStart(orderIn);

                }
           }

           @Override
           public void onDocumentSaveFailed(String errorCode, String errorMessage) {
               Toast.makeText(StartOrderActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
           }
       });
    }

    private boolean provOrder(Order orderIn) {
        if (orderIn.getCoastOrder().equals("")||
                orderIn.getNumberOfAddress().equals("")||
                (orderIn.getTimeFiled().equals("") && !chGetTimeCustomer.isChecked() )||
                orderIn.getNameCustomer().equals("")||
                orderIn.getPhoneCustomer().equals("")||
                orderIn.getAddressCustomer().equals("")) return false;

        else return true;
    }

    private void idOrderStart(final Order order) {

        Query query = new Query(COLLECTION_FOR_WORK_BALASHIHA);
        query.equalTo("idCustomer", idCustomer)
                    .equalTo("numberOfAddresses", order.getNumberOfAddress())
                    .equalTo("coastOrder", order.getCoastOrder())
                    .equalTo("addressForDriver", "Формируется");

        query.findDocuments(new CallbackFindDocument() {
            @Override
            public void onDocumentFound(List<DocumentInfo> documentInfos) {
                idOrder = documentInfos.get(0).getId();
                addIdForWorkIntoWork(idOrder, order);
                stIntent(order.getNumberOfAddress());
            }

            @Override
            public void onDocumentNotFound(String errorCode, String errorMessage) {
                Toast.makeText(StartOrderActivity.this, "errorMessage", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addIdForWorkIntoWork(String idOrder, Order order) {
        Query query = new Query(COLLECTION_WORK_BALASHIHA);
        query.equalTo("idCustomer", idCustomer).equalTo("numberOfAddresses", order.getNumberOfAddress())
                .equalTo("coastOrder", order.getCoastOrder());


        Update update = new Update();
        update.set("idForWorkBalashiha", idOrder);

        query.updateDocument(update, new CallbackUpdateDocument() {
            @Override
            public void onUpdateSucceed(ResponseUpdate responseUpdate) {
            }

            @Override
            public void onUpdateFailed(String errorCode, String errorMessage) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Loog", "StartActivity уничтожена");
    }
}
