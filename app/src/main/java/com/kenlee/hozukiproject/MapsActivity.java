package com.kenlee.hozukiproject;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private FirebaseDatabase database;

    private String[] perms = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        database = FirebaseDatabase.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initMap();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        mMap.setMapStyle(style);

        if (EasyPermissions.hasPermissions(this, perms)){
            mMap.setMyLocationEnabled(true);
        }else {
            initMap();
        }
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(36.235222, 137.184949) , 16.0f) );

        loadMarker();

    }

    @AfterPermissionGranted(112)
    public void initMap(){
        if (EasyPermissions.hasPermissions(this, perms)) {
//            Toast.makeText(this, "定位開啟", Toast.LENGTH_LONG).show();
        }else {
            EasyPermissions.requestPermissions(this, "定位需要GPS權限",
                    112, perms);
        }


    }

    public void loadMarker(){
        DatabaseReference Local = database.getReference("Local");
        Local.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                mMap.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshots) {
                    Local local = dataSnapshot1.getValue(Local.class);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(local.getLatitude(), local.getLongitude())).title(local.getName()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin)));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
