package splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.nolonely.mobile.HomeActivity;
import com.nolonely.mobile.MainActivity;
import com.nolonely.mobile.R;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.bdd.sql_lite.DatabaseManager;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.objects.User;
import com.nolonely.mobile.ui.profil.ParametresActivity;
import com.nolonely.mobile.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.nolonely.mobile.util.Constants.USER;
import static com.nolonely.mobile.util.Constants.range;

public class SplashActivity extends AppCompatActivity {

    DatabaseManager databaseManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        databaseManager = new DatabaseManager(getBaseContext());
        SharedPreferences settings = this.getSharedPreferences(ParametresActivity.SHARED_PREFS, MODE_PRIVATE);
        range = settings.getInt(ParametresActivity.sharedRange, 50);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        try {
            init();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init() throws JSONException {

        Constants.application = getApplication();
        SharedPreferences loginPreferences = getSharedPreferences("login", MODE_PRIVATE);

        if (loginPreferences.contains("mail") && loginPreferences.contains("password") && databaseManager.readMainUser() != null) {
            if (databaseManager.readMainUser() != null) {
                USER = databaseManager.readMainUser();
                updateUser();
                launchHomeActivity();
            } else
                verifyUserData(loginPreferences);
        } else {
            launchMainActivity();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateUser() {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", USER.getUid());

        JSONController.getJsonObjectFromUrl(Constants.URL_ME, this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Log.w("User", jsonObject.toString());
                USER = (User) JSONController.convertJSONToObject(jsonObject, User.class);
                databaseManager.insertUserConnect(USER);
                launchHomeActivity();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur : " + volleyError.toString());
                launchMainActivity();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void verifyUserData(SharedPreferences loginPreferences) throws JSONException {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.put("mail", loginPreferences.getString("mail", null)); //just put and not crypt because already mail and password crypt
        params.put("password", loginPreferences.getString("password", null));

        Log.w("Splash", "Params: " + params.toString());

        JSONController.getJsonObjectFromUrl(Constants.URL_SIGN_IN, SplashActivity.this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                if (jsonObject.length() == 3) {
                    try {
                        loadUserData(jsonObject);
                    } catch (JSONException e) {
                        Log.w("Response", e.getMessage());
                        launchMainActivity();
                    }
                } else {
                    launchMainActivity();
                }
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                launchMainActivity();
                Log.w("Response", "Erreur : " + volleyError.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUserData(final JSONObject json) throws JSONException {
        JSONObjectCrypt params = new JSONObjectCrypt();
        params.putCryptParameter("uid", json.getString("uid"));

        JSONController.getJsonObjectFromUrl(Constants.URL_ME, this, params, new JSONObjectListener() {
            @Override
            public void onJSONReceived(JSONObject jsonObject) {
                Log.w("User", jsonObject.toString());
                USER = (User) JSONController.convertJSONToObject(jsonObject, User.class);
                databaseManager.insertUserConnect(USER);
                launchHomeActivity();
            }

            @Override
            public void onJSONReceivedError(VolleyError volleyError) {
                Log.w("Response", "Erreur : " + volleyError.toString());
                launchMainActivity();
            }
        });
    }

    private void launchMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

    private void launchHomeActivity() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
    }
}

