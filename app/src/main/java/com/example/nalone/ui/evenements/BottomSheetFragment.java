package com.example.nalone.ui.evenements;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.nalone.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {



    public BottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog  = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View root = View.inflate(getContext(),R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(root);
        // Inflate the layout for this fragment

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View)root.getParent());
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        final CardView cardView = root.findViewById(R.id.cardViewTop);
        final RelativeLayout relativeLayout = root.findViewById(R.id.relativeBottomSheet);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if(BottomSheetBehavior.STATE_EXPANDED == 1){
                    showView(cardView,getActionSize());
                    hideView(relativeLayout);
                }

                if(BottomSheetBehavior.STATE_COLLAPSED == 1){
                    hideView(cardView);
                    showView(relativeLayout,getActionSize());
                }
                if(BottomSheetBehavior.STATE_HIDDEN == 1){
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        return dialog ;
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionSize(){
        final TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(new int[]{
            android.R.attr.actionBarSize
        });
        return (int) typedArray.getDimension(0,0);
    }
}