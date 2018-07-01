package com.example.dima.dostavka_client;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ru.profit_group.scorocode_sdk.Callbacks.CallbackLoginUser;
import ru.profit_group.scorocode_sdk.Responses.user.ResponseLogin;
import ru.profit_group.scorocode_sdk.ScorocodeSdk;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.User;

public class LoginActivity extends AppCompatActivity {

    private static final String APPLICATION_ID = "c312b5a7b1794220a85b89079250e64e";
    private static final String CLIENT_KEY = "aec9813472954766897c74a55815d4e1";
    private static final String MASTER_KEY = "ec67c7fce9fb4f63a234d2d708f3a9c6";

    final String SAVED_TEXT_NUMBER = "saved_number";
    final String SAVED_TEXT_ID_CUSTOMER = "saved_id_customer";
    final String SAVED_TEXT_NAME_CUSTOMER = "saved_name_customer";
    final String SAVED_TEXT_ADDRESS_CUSTOMER = "saved_address_customer";

    private static final int PERMISSIONS_INTERNET = 60;

    public SharedPreferences preferences;


    private final String TEXT_PASS = "1";

    private EditText edLogin;

    private Button btLogin;
    private ProgressBar prBarLogin;
    private Boolean checkPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        edLogin =  findViewById(R.id.edLogin);
        btLogin = findViewById(R.id.btLobin);
        prBarLogin = findViewById(R.id.prBarLogin);

        checkPermissions();

        ScorocodeSdk.initWith(APPLICATION_ID, CLIENT_KEY,MASTER_KEY,
                null,null,null,null);

        checkPref = checkPref();


        if(checkPref)
            edLogin.setText(preferences.getString(SAVED_TEXT_NUMBER, ""));

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btLogin.setClickable(false);
                prBarLogin.setVisibility(ProgressBar.VISIBLE);

                loging(TEXT_PASS);
            }
        });

    }

    private void loging(String pass){
        User user = new User();
        final String textLogin = edLogin.getText().toString();
        user.login(textLogin, pass, new CallbackLoginUser() {
            @Override
            public void onLoginSucceed(ResponseLogin responseLogin) {
                if (!checkPref){
                   newPref();
                }

                prBarLogin.setVisibility(ProgressBar.INVISIBLE);
                btLogin.setClickable(true);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onLoginFailed(String errorCode, String errorMessage) {
                prBarLogin.setVisibility(ProgressBar.INVISIBLE);
                switch (errorCode){
                    case "-1" : errorMessage = "Ошибка соединения! Проверьте подключение к интернету :(";
                }
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                btLogin.setClickable(true);
            }
        });
    }

    private void checkPermissions(){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.INTERNET}, PERMISSIONS_INTERNET);
        }

    }



    private void newPref(){
        String login = edLogin.getText().toString();

        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(SAVED_TEXT_NUMBER, login);
        ed.commit();
    }

    private boolean checkPref(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String login = preferences.getString(SAVED_TEXT_NUMBER, "");
        if (!login.isEmpty()){
            edLogin.setText(login);
            return true;
        }
        else return false;
    }


}
