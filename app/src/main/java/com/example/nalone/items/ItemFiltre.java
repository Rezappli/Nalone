package com.example.nalone.items;

import com.example.nalone.R;

public class ItemFiltre {

    boolean click;
    public String filtre;
    public int mBackground = R.drawable.custom_button_signup;

    public ItemFiltre(String filtre){

        this.filtre = filtre;
        this.click = click;
        this.mBackground = mBackground;
    }

    public String getFiltre() {
        return filtre;
    }

    public void setFiltre(String filtre) {
        this.filtre = filtre;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public int getBackground() {
        return mBackground;
    }

    public void setBackground(int background) {
        this.mBackground = background;
    }

    public void changeBackground(int background){
        mBackground = background;
    }
}
