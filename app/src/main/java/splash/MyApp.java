package splash;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;


public class MyApp extends Application {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
    }
}