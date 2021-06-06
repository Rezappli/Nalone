package com.nolonely.mobile;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.nolonely.mobile.bdd.json.JSONObjectCrypt;

import static com.nolonely.mobile.util.Constants.USER;

public class ReportActivity extends AppCompatActivity {

    private TextView report_title;
    private RadioButton radioButtonName, radioButtonImage, radioButtonDescription, radioButtonOther;
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

        radioButtonName = findViewById(R.id.radioButtonName);
        radioButtonDescription = findViewById(R.id.radioButtonDescription);
        radioButtonImage = findViewById(R.id.radioButtonImage);
        radioButtonOther = findViewById(R.id.radioButtonOther);

        checkBoxDeposit = findViewById(R.id.checkBoxDeposit);
        validateButton = findViewById(R.id.buttonValidateReport);

        radioButtonName.setOnClickListener(v -> {
            if (reportLinearLayoutDetails.getVisibility() == View.GONE) {
                reportLinearLayoutDetails.setVisibility(View.VISIBLE);
            }
        });

        radioButtonImage.setOnClickListener(v -> {
            if (reportLinearLayoutDetails.getVisibility() == View.GONE) {
                reportLinearLayoutDetails.setVisibility(View.VISIBLE);
            }
        });

        radioButtonDescription.setOnClickListener(v -> {
            if (reportLinearLayoutDetails.getVisibility() == View.GONE) {
                reportLinearLayoutDetails.setVisibility(View.VISIBLE);
            }
        });

        radioButtonOther.setOnClickListener(v -> {
            if (reportLinearLayoutDetails.getVisibility() == View.GONE) {
                reportLinearLayoutDetails.setVisibility(View.VISIBLE);
            }
        });

        checkBoxDeposit.setOnClickListener(v -> {
            if (checkBoxDeposit.isSelected()) {
                validateButton.setClickable(true);
            } else {
                validateButton.setClickable(false);
            }
        });

        validateButton.setOnClickListener(v -> {
            onValidate();
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
            if (radioButtonName.isSelected()) {
                reportString += "name;";
            }

            if (radioButtonDescription.isSelected()) {
                reportString += "description;";
            }

            if (radioButtonImage.isSelected()) {
                reportString += "image;";
            }

            if (radioButtonOther.isSelected()) {
                reportString += "other;";
            }

            JSONObjectCrypt params = new JSONObjectCrypt();
            params.putCryptParameter("uid", USER.getUid());
            params.putCryptParameter("type", type);
            params.putCryptParameter("uid_report", uid_report);
            params.putCryptParameter("message", messageEditText.getText().toString());
            params.putCryptParameter("report", reportString);

            Toast.makeText(ReportActivity.this, getResources().getString(R.string.report_sent), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ReportActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }
}