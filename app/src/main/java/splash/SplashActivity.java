package splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.nalone.HomeActivity;
import com.example.nalone.MainActivity;
import com.example.nalone.R;
import com.example.nalone.json.JSONController;
import com.example.nalone.json.JSONObjectCrypt;
import com.example.nalone.listeners.JSONArrayListener;
import com.example.nalone.listeners.JSONObjectListener;
import com.example.nalone.objects.User;
import com.example.nalone.ui.profil.ParametresFragment;
import com.example.nalone.util.Constants;
import com.example.nalone.util.CryptoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.nalone.util.Constants.USER;
import static com.example.nalone.util.Constants.range;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences settings = this.getSharedPreferences(ParametresFragment.SHARED_PREFS, MODE_PRIVATE);
        range = settings.getInt(ParametresFragment.sharedRange, 50);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init() {

        SharedPreferences loginPreferences = getSharedPreferences("login", MODE_PRIVATE);

        if(loginPreferences.contains("mail") && loginPreferences.contains("password")){
            try {
                verifyUserData(loginPreferences);
            } catch (JSONException e) {
                Log.w("Response", e.getMessage());
                launchMainActivity();
            }
        }else{
            launchMainActivity();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void verifyUserData(SharedPreferences loginPreferences) throws JSONException {
        JSONObjectCrypt object = new JSONObjectCrypt();
        object.put("mail", loginPreferences.getString("mail", null)); //just put and not crypt because already mail and password crypt
        object.put("password", loginPreferences.getString("password", null));


        JSONController.getJsonObjectFromUrl(Constants.URL_SIGN_IN, SplashActivity.this, object, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if(jsonObject.length() == 1){
                    try {
                        loadUserData(jsonObject);
                    } catch (JSONException e) {
                        Log.w("Response", e.getMessage());
                        launchMainActivity();
                    }
                    launchHomeActivity();
                }else{
                    launchMainActivity();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                launchMainActivity();
                Log.w("Response", "Erreur : "+volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUserData(final JSONObject json) throws JSONException {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.addParameter("uid", json.getString("uid"));

        JSONController.getJsonObjectFromUrl(Constants.URL_ME, this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                USER = (User)JSONController.convertJSONToObject(jsonObject, User.class);
                launchHomeActivity();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur : "+volleyError.toString());
                launchMainActivity();
            }
        });
    }

    private void launchMainActivity(){
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

    private void launchHomeActivity(){
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
    }
}

