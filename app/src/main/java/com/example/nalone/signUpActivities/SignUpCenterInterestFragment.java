package com.example.nalone.signUpActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.nalone.R;
import com.example.nalone.objects.TypeEventObject;


public class SignUpCenterInterestFragment extends SignUpFragment implements AdapterView.OnItemSelectedListener {

    private String inputCenterOfInterest;
    private TypeEventObject typeEventObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = R.layout.fragment_welcome_center_interest;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        typeEventObject = new TypeEventObject(getContext());
        Spinner spin = (Spinner) view.findViewById(R.id.spinner);

        spin.setOnItemSelectedListener(this);

        SpinnerAdapter customAdapter = new SpinnerAdapter(getContext(), typeEventObject.getListActivitiesImage(), typeEventObject.getListActivitiesName());
        spin.setAdapter(customAdapter);

    }

    @Override
    public void onNextClicked() {
        notifySignUpMainListenerChange();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        inputCenterOfInterest = typeEventObject.getListActivitiesName()[position];
    }

    private void notifySignUpMainListenerChange() {
        SignUpMainActivity.listenerMain.onFragmentValidate(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}