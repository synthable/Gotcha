package com.synthable.gotcha;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapLongClickListener, OnMapClickListener {

	private GoogleMap mMap;
	private int mMarkerCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded(); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
            		.getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
    	LatLng point = new LatLng(38.791006, -121.237299);

        mMap.addMarker(new MarkerOptions().position(point).title("Marker"));
        mMap.addCircle(new CircleOptions()
	        .center(point)
	        .radius(100)
	        .strokeWidth(2)
	        .fillColor(Color.BLUE)
        );

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 15);
        mMap.animateCamera(cameraUpdate);

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);

        mMap.setMyLocationEnabled(true);
    }

	@Override
	public void onMapLongClick(LatLng point) {
		if(mMap != null) {
			mMap.addMarker(new MarkerOptions().position(point).title("Marker "+ ++mMarkerCounter));
			mMap.addCircle(new CircleOptions()
		        .center(point)
		        .radius(25)
		        .strokeWidth(2)
		        .fillColor(Color.RED)
			);
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		if(mMap != null) {
			mMap.addMarker(new MarkerOptions().position(point).title("Marker "+ ++mMarkerCounter));
			mMap.addCircle(new CircleOptions()
		        .center(point)
		        .radius(10)
		        .strokeWidth(2)
		        .fillColor(Color.YELLOW)
			);
		}
	}
}