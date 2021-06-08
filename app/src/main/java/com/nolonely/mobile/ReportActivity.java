package com.nolonely.mobile;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.nolonely.mobile.bdd.json.JSONController;
import com.nolonely.mobile.bdd.json.JSONObjectCrypt;
import com.nolonely.mobile.listeners.JSONObjectListener;
import com.nolonely.mobile.util.Constants;

import org.json.JSONObject;

import static com.nolonely.mobile.util.Constants.USER;

public class ReportActivity extends AppCompatActivity {

    private TextView report_title;
    private CheckBox checkBoxButtonName, checkBoxButtonImage, checkBoxButtonDescription, checkBoxButtonOther;
    private String uid_report;
    private LinearLayout reportLinearLayoutDetails;
    private String type;
    private EditText messageEditText;
    private CheckBox checkBoxDeposit;
    private Button validateButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        if (getIntent() != null) {
            type = getIntent().getStringExtra("type");
            uid_report = getIntent().getStringExtra("uid_report");
        }

        report_title = findViewById(R.id.report_title);
        reportLinearLayoutDetails = findViewById(R.id.reportLinearLayoutDetails);
        messageEditText = findViewById(R.id.editTextReportMessage);

        checkBoxButtonName = findViewById(R.id.checkBoxButtonName);
        checkBoxButtonDescription = findViewById(R.id.checkBoxDescription);
        checkBoxButtonImage = findViewById(R.id.checkBoxImage);
        checkBoxButtonOther = findViewById(R.id.checkBoxOther);

        checkBoxDeposit = findViewById(R.id.checkBoxDeposit);
        validateButton = findViewById(R.id.buttonValidateReport);

        validateButton.setClickable(false);

        checkBoxButtonName.setOnClickListener(v -> {
            if (reportLinearLayoutDetails.getVisibility() == View.GONE) {
                reportLinearLayoutDetails.setVisibility(View.VISIBLE);
            }
        });

        checkBoxButtonImage.setOnClickListener(v -> {
            if (reportLinearLayoutDetails.getVisibility() == View.GONE) {
                reportLinearLayoutDetails.setVisibility(View.VISIBLE);
            }
        });

        checkBoxButtonDescription.setOnClickListener(v -> {
            if (reportLinearLayoutDetails.getVisibility() == View.GONE) {
                reportLinearLayoutDetails.setVisibility(View.VISIBLE);
            }
        });

        checkBoxButtonOther.setOnClickListener(v -> {
            if (reportLinearLayoutDetails.getVisibility() == View.GONE) {
                reportLinearLayoutDetails.setVisibility(View.VISIBLE);
            }
        });


        validateButton.setOnClickListener(v -> {
            if (checkBoxDeposit.isChecked()) {
                if (checkBoxButtonName.isChecked() || checkBoxButtonDescription.isChecked()
                        || checkBoxButtonImage.isChecked() || checkBoxButtonOther.isChecked()) {
                    onValidate();
                } else {
                    Toast.makeText(ReportActivity.this, getResources().getString(R.string.report_no_select_section), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ReportActivity.this, getResources().getString(R.string.report_no_select_checkbox), Toast.LENGTH_SHORT).show();
            }
        });

        if (type.equalsIgnoreCase("event")) {
            report_title.setText(report_title.getText().toString() + " " + getResources().getString(R.string.report_event));
        } else if (type.equalsIgnoreCase("user")) {
            report_title.setText(report_title.getText().toString() + " " + getResources().getString(R.string.report_user));
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onValidate() {
        if (uid_report != null) {
            String reportString = "";
            if (checkBoxButtonName.isChecked()) {
                reportString += "name;";
            }

            if (checkBoxButtonDescription.isChecked()) {
                reportString += "description;";
            }

            if (checkBoxButtonImage.isChecked()) {
                reportString += "image;";
            }

            if (checkBoxButtonOther.isChecked()) {
                reportString += "other;";
            }

            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", USER.getUid());
            params.putCryptParameter("type", type);
            params.putCryptParameter("uid_report", uid_report);
            if (!messageEditText.getText().toString().equalsIgnoreCase("")) {
                params.putCryptParameter("message", messageEditText.getText().toString());
            }
            params.putCryptParameter("report_items", reportString);
            Log.w("Report", "Params : " + params.toString());

            JSONController.getJsonObjectFromUrl(Constants.URL_SEND_REPORT, ReportActivity.this, params, new JSONObjectListener() {
                @Override
                public void onJSONReceived(JSONObject jsonObject) {
                    if (jsonObject.length() == 3) {
                        Toast.makeText(ReportActivity.this, getResources().getString(R.string.report_sent), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ReportActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onJSONReceivedError(VolleyError volleyError) {
                    Toast.makeText(ReportActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(ReportActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }
}