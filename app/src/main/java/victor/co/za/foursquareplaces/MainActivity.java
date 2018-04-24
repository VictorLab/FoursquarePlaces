package victor.co.za.foursquareplaces;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import helper.Constants;
import helper.CurrentLocation;
import helper.CustomDialogs;
import helper.PlaceCategoryBuilder;
import helper.PlaceHelper;
import listners.FindPlaceListiner;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private PlaceHelper placeHelper;
    private CurrentLocation currentLocation;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Places");

        initialize();

    }

    private void initialize() {

        mContext = getApplicationContext();

        placeHelper = new PlaceHelper(this,findPlaceErrorListiner);

        currentLocation = new CurrentLocation(this, location -> {

        String loc = location.getLatitude() + "," + location.getLongitude();

        placeHelper.setGeoLocation(loc);

        placeHelper.getPlaces();

        });

        LinearLayout queryWrapper = findViewById(R.id.queryWrapper);

        PlaceCategoryBuilder queryBuilder = new PlaceCategoryBuilder(this, category -> {
            placeHelper.setCategory(category);
            currentLocation.getCurrentLocation();
        });

        //Add Category view to main_layout
        View view = queryBuilder.createCategoryType();
        queryWrapper.addView(view);

        currentLocation.getCurrentLocation();

        refreshLayout = findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(() -> currentLocation.getCurrentLocation());

    }

    private FindPlaceListiner findPlaceErrorListiner = new FindPlaceListiner() {
        @Override
        public void onFindPlaceError() {

            refreshLayout.setRefreshing(false);

            CustomDialogs.showSnackBar(
                    MainActivity.this,
                    getString(R.string.connect_internet),
                    getString(R.string.try_again), v -> currentLocation.getCurrentLocation()
            );
        }

        @Override
        public void onFindPlaceSuccess() {
            refreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case Constants.REQUEST_PERMISSION_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    currentLocation.getCurrentLocation();
                }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        placeHelper.dispose();
    }
    @Override
    public void onResume() {
        super.onResume();

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int result = apiAvailability.isGooglePlayServicesAvailable(mContext);

        if (result != ConnectionResult.SUCCESS
                && result
                != ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            Toast.makeText(mContext, getString(R.string.error_google_api), Toast.LENGTH_SHORT).show();
        }
    }

}
