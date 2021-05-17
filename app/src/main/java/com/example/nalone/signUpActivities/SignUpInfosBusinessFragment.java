package com.example.nalone.signUpActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.nalone.R;
import com.example.nalone.objects.TypeEventObject;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpInfosBusinessFragment extends SignUpFragment implements AdapterView.OnItemSelectedListener {

    private TextInputEditText inputName;
    private TypeEventObject typeEventObject;
    private String businessActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = R.layout.fragment_sign_up_infos_business;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBusinessAccount();
    }

    private void initBusinessAccount() {

        typeEventObject = new TypeEventObject(getContext());
        inputName = (TextInputEditText) view.findViewById(R.id.signupName);
        Spinner spin = (Spinner) view.findViewById(R.id.spinner);

        spin.setOnItemSelectedListener(this);

        SpinnerAdapter customAdapter = new SpinnerAdapter(getContext(), typeEventObject.getListActivitiesImage(), typeEventObject.getListActivitiesName());
        spin.setAdapter(customAdapter);
        spin.setPrompt("Title");
        initFields();
    }

    @Override
    public void onNextClicked() {
        String nameEntered = inputName.getText().toString();

        if (nameEntered.length() == 1) {
            inputName.setError(getResources().getString(R.string.error_name_short), customErrorDrawable);
            return;
        }

        if (nameEntered.matches("")) {
            inputName.setError(getResources().getString(R.string.error_name_empty), customErrorDrawable);
            return;
        }

        user.setLast_name(nameEntered);
        user.setFirst_name(businessActivity);
        notifySignUpMainListenerChange();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        businessActivity = typeEventObject.getListActivitiesName()[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private enum sex {
        FEMME, HOMME
    }

    private void initFields() {

        if (user.getLast_name() != null)
            inputName.setText(user.getLast_name());

    }

    private void notifySignUpMainListenerChange() {
        SignUpMainActivity.listenerMain.onFragmentValidate(this);
    }
}