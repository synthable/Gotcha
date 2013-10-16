package com.synthable.gotcha;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements
	LocationListener, OnMapLongClickListener, OnMapClickListener,
	ConnectionCallbacks, OnConnectionFailedListener {

	private static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 2;
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    private static final int FASTEST_INTERVAL_IN_SECONDS = 2;
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
    public static final int SELECT_ACCOUNT = 100;

	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private GoogleMap mMap;
	private int mMarkerCounter;

	static private class Colors {
		static protected int TRANS_RED = 1358888960;
		static protected int TRANS_BLUE = 1342177535;
		static protected int TRANS_GREEN = 1342242560;
		static protected int TRANS_YELLOW = 1358954240;
		static protected int TRANS_BLACK = 1342177280;
		static protected int TRANS_PURPLE = 1358889215;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationClient = new LocationClient(this, this, this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null);
		startActivityForResult(intent, SELECT_ACCOUNT);
    }

    @Override
	protected void onStart() {
		super.onStart();
		
		mLocationClient.connect();
	}

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
	protected void onStop() {
    	mLocationClient.disconnect();

		super.onStop();
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == SELECT_ACCOUNT && resultCode == RESULT_OK) {
			String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			Toast.makeText(this, accountName, Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
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
        }
    }

    private void setUpMap() {
    	LatLng point = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        mMap.addMarker(new MarkerOptions().position(point).title("Marker"));
        mMap.addCircle(new CircleOptions()
	        .center(point)
	        .radius(100)
	        .strokeWidth(2)
	        .fillColor(Colors.TRANS_BLUE)
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
		        .fillColor(Colors.TRANS_RED)
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
		        .fillColor(Colors.TRANS_YELLOW)
			);
		}
	}

    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        mCurrentLocation = location;
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        mCurrentLocation = mLocationClient.getLastLocation();

        // Check if we were successful in obtaining the map.
        if (mMap != null) {
        	setUpMap();
        }
    }
 
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }
 
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            //showErrorDialog(connectionResult.getErrorCode());
        	Toast.makeText(this, "Shit happened..."+ connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
        	finish();
        }
    }
}