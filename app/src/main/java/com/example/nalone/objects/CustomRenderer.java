package com.example.nalone.objects;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomRenderer extends DefaultClusterRenderer<CustomMarker> {

    public CustomRenderer(Context context, GoogleMap googleMap, ClusterManager<CustomMarker> clusterManager) {
        super(context, googleMap, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(CustomMarker item, MarkerOptions markerOptions) {
        Log.w("Marker", "Color : " + item.getColor());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(item.getColor()));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() >= 3;
    }
}