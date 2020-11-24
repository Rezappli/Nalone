package com.example.nalone;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import static com.example.nalone.util.Constants.USER;

public class QRCodeFragment extends DialogFragment {

    private ImageView qrcode;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dialog dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_qrcode, null);
        qrcode = view.findViewById(R.id.qr_code_image);
        qrcode.setImageBitmap(QrCode.generate(USER.getUid()));
        dialog.setContentView(view);

        return dialog;
    }

}
