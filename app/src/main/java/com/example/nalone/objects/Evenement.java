package com.example.nalone.objects;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nalone.R;
import com.example.nalone.enumeration.StatusEvent;
import com.example.nalone.enumeration.TypeEvent;
import com.example.nalone.enumeration.Visibility;
import com.example.nalone.util.Constants;
import com.example.nalone.util.TimeUtil;
import com.google.firebase.database.core.view.Event;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Evenement implements Serializable {

    private String uid;
    private String name;
    private String description;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private Visibility visibility;
    private String startDate;
    private String endDate;
    private String owner_uid;
    private int nbMembers;
    private StatusEvent status;
    private String ownerFirstName;
    private String ownerLastName;
    private int price;
    private TypeEvent category;
    private String image_url;

    public Evenement() {
        this.uid = null;
        this.name = null;
        this.description = null;
        this.address = null;
        this.city = null;
        this.visibility = null;
        this.startDate = null;
        this.endDate = null;
        this.owner_uid = null;
        this.latitude = null;
        this.longitude = null;
        this.nbMembers = 0;
        this.status = null;
        this.price = 0;
        this.category = null;
        this.ownerFirstName = null;
        this.ownerLastName = null;
        this.image_url = null;
    }


    public Evenement(String uid, StatusEvent status,String owner, String name, String description,
                     String address, String city, Visibility visibility, String startDate, String endDate,
                     double latitude, double longitude, int nbMembers, TypeEvent category,int price){
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.visibility = visibility;
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner_uid = owner;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nbMembers = nbMembers;
        this.status = status;
        this.category = category;
        this.price = price;
        this.ownerFirstName = Constants.USER.getFirst_name();
        this.ownerLastName = Constants.USER.getLast_name();
        this.image_url = null;
    }

    public void setOwner_uid(String owner_uid) {
        this.owner_uid = owner_uid;
    }

    public String getOwner_uid() {
        return owner_uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getNbMembers() {
        return nbMembers;
    }

    public void setNbMembers(int nbMembers) {
        this.nbMembers = nbMembers;
    }

    public StatusEvent getStatusEvent() {
        return status;
    }

    public void setStatusEvent(StatusEvent statusEvent) {
        this.status = statusEvent;
    }

    public String getOwner_first_name() {
        return ownerFirstName;
    }

    public String getOwner_last_name() {
        return ownerLastName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TypeEvent getCategory() {
        return category;
    }

    public int getImageCategory(){
        switch (this.getCategory()){
            case ART: return R.drawable.event_art;
            case CAR: return R.drawable.event_car;
            case GAME: return R.drawable.event_game;
            case SHOP: return R.drawable.event_shop;
            case SHOW: return R.drawable.event_show;
            case MOVIE: return R.drawable.event_movie;
            case MUSIC: return R.drawable.event_music;
            case PARTY: return R.drawable.event_party;
            case SPORT: return R.drawable.event_sport;
            case CONTEST: return R.drawable.event_contest;
            case SCIENCE: return R.drawable.event_science;
            case CONFERENCE: return R.drawable.event_conference;
            case GATHER: return R.drawable.event_gather;
        }
        return 0;
    }

    public void replaceFields(TextView tvName,TextView tvCity,TextView tvNbMembers,TextView tvDate,TextView tvTime, ImageView iv) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String final_date_text = "";
        tvName.setText(this.getName());
        tvCity.setText(this.getCity());
        tvNbMembers.setText(this.getNbMembers()+"");
        Date date = sdf.parse(this.getStartDate());
        String date_text = Constants.formatD.format(date);
        for (int i = 0; i < date_text.length() - 5; i++) {
            char character = date_text.charAt(i);
            if (i == 0) {
                character = Character.toUpperCase(character);
            }
            final_date_text += character;
        }
        tvDate.setText(final_date_text);
        tvTime.setText(TimeUtil.cutString(this.getStartDate(), 5, 11));
        tvName.setText(this.getName());
        iv.setImageResource(this.getImageCategory());
    }

    public void setCategory(TypeEvent eventType) {
        this.category = eventType;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString(){
        String date_text = Constants.formatD.format(this.startDate);
        String final_date_text = "";
        for(int i = 0; i < date_text.length(); i++){
            char character = date_text.charAt(i);
            if(i == 0) {
                character = Character.toUpperCase(character);
            }
            final_date_text += character;
        }

        return final_date_text;
    }
}
