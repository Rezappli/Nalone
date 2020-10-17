package splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nalone.HomeActivity;
import com.example.nalone.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static com.example.nalone.util.Constants.settingsFile;
import static com.example.nalone.util.Constants.user_mail;

public class SplashActivity extends AppCompatActivity {

    public static Activity activity;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount acct = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;

        settingsFile = new File(getApplicationContext().getFilesDir(),"settings.dat");
        acct = GoogleSignIn.getLastSignedInAccount(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        /*try {
            if(acct == null) {
                if (checkFileSettings()) {
                    String line = readSettings();
                    String[] datas = line.split(";");
                    checkUserRegister(this, false, datas[0], datas[1]);
                } else {
                    createFileSettings();
                }
            }else{
                checkUserRegister(this, true, acct.getEmail().replace(".", ","), null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void createFileSettings() throws IOException {
        settingsFile.createNewFile();
    }

    private boolean checkFileSettings() throws FileNotFoundException {
        if(settingsFile.exists()){
            return true;
        }
        return false;
    }

    private String readSettings() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(settingsFile));
        String line = in.readLine();
        if(line != null){
            in.close();
            return line;
        }
        in.close();
        return null;
    }

    private void checkUserRegister(final Context context, final boolean googleConnection, final String mail, final String password){
        DatabaseReference id_users = FirebaseDatabase.getInstance().getReference("id_users");
        id_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String id_users_text = snapshot.getValue(String.class);
                final int nb_users = Integer.parseInt(id_users_text);
                for (int i = 0; i < nb_users; i++) {
                    DatabaseReference authentificationRef = FirebaseDatabase.getInstance().getReference("authentification/" + i);
                    final int finalI = i;
                    authentificationRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot snapshot) {
                            String m = snapshot.child("mail").getValue(String.class);
                            String p = snapshot.child("mail").getValue(String.class);
                            if(googleConnection){
                                if(m.equalsIgnoreCase(mail)){
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    startActivity(intent);
                                }
                            }else{
                                if(m.equalsIgnoreCase(mail) && p.equalsIgnoreCase(password)){
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}