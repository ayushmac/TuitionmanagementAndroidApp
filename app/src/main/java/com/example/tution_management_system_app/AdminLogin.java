package com.example.tution_management_system_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdminLogin extends AppCompatActivity {

    private Button adminloginbtn1;
    private TextInputLayout textinputlayadminusrname;
    private TextInputLayout textinputlayadminpswd;
    private TextInputEditText adminusrname;
    private TextInputEditText adminpswd;
    private SharedPreferences sharedPreferences;

    //shared preference variables
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "adminusrname";
    private static final String KEY_PSWD = "adminpswd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_login);

        adminloginbtn1 = findViewById(R.id.adminloginbtn);
        adminusrname = findViewById(R.id.adminusername);
        adminpswd = findViewById(R.id.adminpswd);
        textinputlayadminusrname = findViewById(R.id.textinputlayouttitle);
        textinputlayadminpswd = findViewById(R.id.textInputLayoutresetpswd);
        adminusrname.requestFocus();

        //logout
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        //To check if data in sharedpref is present or not
        String adusrname = sharedPreferences.getString(KEY_USERNAME,null);
        String adpswd = sharedPreferences.getString(KEY_PSWD,null);
        if(adusrname != null && adpswd != null){
            Intent intent = new Intent(AdminLogin.this,AdminMain.class);
            startActivity(intent);
            finish();
        }

        //validation of admin username
        adminusrname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usrname = adminusrname.getText().toString().trim();
                if(usrname.isEmpty()){
                    textinputlayadminusrname.setHelperText("Don't Leave Empty !");
                }
                if(usrname.equals("neeta")){
                    textinputlayadminusrname.setHelperText("");
                    textinputlayadminusrname.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                if(!usrname.equals("neeta")){
                textinputlayadminusrname.setHelperText("Enter Correct username !");
                textinputlayadminusrname.setBoxStrokeColor(Color.RED);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrname = adminusrname.getText().toString().trim();
                if(usrname.isEmpty()){
                    textinputlayadminusrname.setHelperText("Don't Leave Empty !");
                }

            }
        });

        //validation of admin password
        adminpswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pswd = adminpswd.getText().toString().trim();

                if(pswd.equals("neeta123")){
                    textinputlayadminpswd.setHelperText("");
                    textinputlayadminpswd.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                if(!pswd.equals("neeta123")){
                    textinputlayadminpswd.setHelperText("Enter Correct password !");
                    textinputlayadminpswd.setBoxStrokeColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pswd = adminpswd.getText().toString().trim();
                if(pswd.isEmpty()){
                    textinputlayadminpswd.setHelperText("Don't Leave Empty !");
                }
            }
        });

        //admin login btn
        adminloginbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                //validation
                String usrname,pswd;
                usrname = adminusrname.getText().toString().trim();
                pswd = adminpswd.getText().toString().trim();
                if(usrname.equals("neeta") && pswd.equals("neeta123")){
                    textinputlayadminusrname.setHelperText("");
                    textinputlayadminpswd.setHelperText("");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_USERNAME,usrname);
                    editor.putString(KEY_PSWD,pswd);
                    editor.apply();
                    Toast.makeText(AdminLogin.this, "Logged in successfully !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLogin.this,AdminMain.class);
                    startActivity(intent);
                    finishAffinity();
                }
                else{
                    Toast.makeText(AdminLogin.this, "Enter correct credentials !", Toast.LENGTH_SHORT).show();
                    textinputlayadminusrname.setHelperText("Required");
                    textinputlayadminpswd.setHelperText("Required");
                }
            }
        });

    }





}