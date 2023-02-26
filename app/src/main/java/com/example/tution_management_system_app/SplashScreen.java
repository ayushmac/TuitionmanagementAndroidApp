package com.example.tution_management_system_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    //shared preference variable
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "adminusrname";
    private static final String KEY_PSWD = "adminpswd";
    FirebaseAuth mAuth;
    private static String networkstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
         mAuth = FirebaseAuth.getInstance();
        //logout
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        //firebase student user preferences
        FirebaseUser user = mAuth.getCurrentUser();

        //admin user preferences
        String adusrname = sharedPreferences.getString(KEY_USERNAME,null);
        String adpswd = sharedPreferences.getString(KEY_PSWD,null);

        if(adusrname != null && adpswd != null){
            Intent intent = new Intent(SplashScreen.this,AdminMain.class);
            startActivity(intent);
            finishAffinity();
        }
        else if (user != null){
            Intent intent = new Intent(SplashScreen.this,StudentMain.class);
            startActivity(intent);
            finishAffinity();
        }
        else{
            if(checkConnection() == true){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }
                },3000);
            }
            else if(checkConnection() ==false ){
                new AlertDialog.Builder(this).setMessage("No Internet Connection !").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
            }
        }

    }


    public boolean checkConnection(){
        ConnectivityManager manager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if(null != activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this,"Welcome !",Toast.LENGTH_SHORT).show();
            }
            else if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this,"Welcome !",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else{
            return false;
        }

    }
}