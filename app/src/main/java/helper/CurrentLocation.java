package helper;


import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import listners.LocationRecievedListiner;
import victor.co.za.foursquareplaces.R;

/**
 * This class get the current location of the user and pass if to the MainActivity
 * using the LocationRecievedListiner lisiner
 *
 * Created by Victor on 2018/04/21.
 */

public class CurrentLocation {

	private Activity activity;
	private Location mCurrentLocation = null;
	private LocationRequest mLocationRequest;
	protected static long MIN_UPDATE_INTERVAl = 1000;
	private FusedLocationProviderClient mFusedLocationProviderClient;
	private LocationRecievedListiner mCallback;

	public CurrentLocation(Activity activity,LocationRecievedListiner mCallback){

		this.activity = activity;

		this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

		this.mCallback = mCallback;

		checkForLocationRequest();

		checkForLocationSettings();

	}

	public void getCurrentLocation() {

		try {
			if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED
					&& ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {

				requestPermissions( Constants.REQUEST_PERMISSION_LOCATION );

				return;
			}

			mFusedLocationProviderClient.requestLocationUpdates(
					mLocationRequest,
					locationCallback,
					Looper.myLooper()
			);


		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	LocationCallback locationCallback = new LocationCallback(){
		@Override
		public void onLocationResult(LocationResult locationResult) {
			mCurrentLocation = locationResult.getLastLocation();
			mCallback.onLocationRecieved(mCurrentLocation);
			mFusedLocationProviderClient.removeLocationUpdates(this);
		}
	};

	private void startLocationPermissionRequest(int requestCode) {

		ActivityCompat.requestPermissions(
				activity,
				new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
				requestCode
		);
	}

	private void requestPermissions(final int requestCode) {

		boolean shoulProvideReshionale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

		if (shoulProvideReshionale) {
			showSnackBar("Permission must be accepter to find location", "Ok", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Request permission
					startLocationPermissionRequest(requestCode);
				}
			});
		} else {
			startLocationPermissionRequest(requestCode);
		}
	}

	/**
	 *
	 * @param mainTextString
	 * @param actionString
	 * @param listener
	 */

	private void showSnackBar(final String mainTextString, final String actionString, View.OnClickListener listener) {
		View container = activity.findViewById(R.id.container);
		if (container != null) {
			Snackbar.make(container, mainTextString, Snackbar.LENGTH_INDEFINITE)
					.setAction(actionString, listener)
					.show();
		}
	}


	private void checkForLocationRequest() {

		mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(MIN_UPDATE_INTERVAl);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
	}

	//Check for the location setting
	private void checkForLocationSettings() {

		try {
			LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
			builder.addLocationRequest(mLocationRequest);
			SettingsClient settingsClient = LocationServices.getSettingsClient(activity);

			settingsClient.checkLocationSettings(builder.build())
					.addOnSuccessListener(r -> {
						Toast.makeText(activity, "Location setting enable successfully.", Toast.LENGTH_SHORT).show();
					})
					.addOnFailureListener(e -> {

						int statusCode = ((ApiException) e).getStatusCode();

						switch (statusCode) {
							case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
								try {
									ResolvableApiException rae = (ResolvableApiException) e;
									rae.startResolutionForResult(activity,  Constants.REQUEST_PERMISSION_LOCATION);
								} catch (IntentSender.SendIntentException sie) {
									sie.printStackTrace();
								}
								break;
							case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
								Toast.makeText(activity, "Setting change is not avaolable,Try another device", Toast.LENGTH_SHORT).show();
								break;

						}

					});
		} catch (Exception ex) {

		}
	}



}
