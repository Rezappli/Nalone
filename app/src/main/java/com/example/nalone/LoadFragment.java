package com.example.nalone;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import static com.example.nalone.util.Constants.USER;

public class LoadFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dialog dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_load, null);
        dialog.setContentView(view);

        return dialog;
    }

}
