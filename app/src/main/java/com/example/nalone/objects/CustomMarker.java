package com.example.nalone.objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class CustomMarker implements ClusterItem {

    private LatLng pos;
    private Evenement tag;
    private float color;

    public CustomMarker(LatLng pos, float color, Evenement tag) {
        this.pos = pos;
        this.color = color;
        this.tag = tag;
    }

    @Override
    public LatLng getPosition() {
        return this.pos;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public Evenement getTag() {
        return tag;
    }

    public float getColor() {
        return color;
    }
}
