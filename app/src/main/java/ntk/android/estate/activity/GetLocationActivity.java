package ntk.android.estate.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import es.dmoral.toasty.Toasty;
import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.estate.R;

public class GetLocationActivity extends BaseActivity {
    GoogleMap map;
    Marker myMarker;

    Marker myLocalMarker;

    FusedLocationProviderClient fusedLocationProviderClient;
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.get(
                                Manifest.permission.ACCESS_FINE_LOCATION);
                        Boolean coarseLocationGranted = result.get(
                                Manifest.permission.ACCESS_COARSE_LOCATION);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                            if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                //chek is Gps is On or Off
                                checkGPS();
                            }
                        } else {
                            // No location access granted.
                            Toasty.error(GetLocationActivity.this, "نیاز است دسترسی اپلیکیشن به موقعیت را تایید نمایید", Toasty.LENGTH_LONG, true).show();
                        }
                    }
            );

    private void checkGPS() {

        LocationRequest locationRequest = LocationRequest.create();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
            Log.d("GPS_main", "OnSuccess");
            // GPS is ON
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {
                Log.d("GPS_main", "GPS off");
                Toasty.info(GetLocationActivity.this, "gps" + " دستگاه شما غیرفعال است ").show();
                // GPS off
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(GetLocationActivity.this, 1);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }


    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {
                    //The last location in the list is the newest
                    Location location = locationList.get(locationList.size() - 1);

                    //Place current location marker
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    addMyLocationMarker(latLng);
                    //move map camera
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        findViewById(R.id.imgToolbarBack).setOnClickListener(view -> finish());
        //get last location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //location button
        findViewById(R.id.lastLocationFab).setOnClickListener(view -> getPermission());
        MapView mapView = findViewById(R.id.mapview);
        mapView.getMapAsync(googleMap -> {
            map = googleMap;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.689198, 51.388973), 12));
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng point) {
                    if (myMarker != null)
                        myMarker.remove();
                    addMarker(point);
                    map.animateCamera(CameraUpdateFactory.newLatLng(point));
                }
            });
        });


        findViewById(R.id.setLocationBtn).setOnClickListener(view -> {
            if (myMarker == null)
                Toasty.error(GetLocationActivity.this, "موقعیتی انتخاب نشده است").show();
            else {
                Intent i = new Intent();
                i.putExtra(Extras.EXTRA_FIRST_ARG, myMarker.getPosition().latitude);
                i.putExtra(Extras.EXTRA_SECOND_ARG, myMarker.getPosition().longitude);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //call request
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});

        } else {
            fusedLocationClient.requestLocationUpdates(LocationRequest.create()
                            .setInterval(36000).setFastestInterval(36000)
                            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                            .setMaxWaitTime(1000),
                    getLocationCallback(), Looper.myLooper());
            getCurrentLocation();
        }
    }


    public void getCurrentLocation() {
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
        }).addOnSuccessListener(this, location -> {
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng((myLoc)));
//                addLabel(myLoc);
            }
        });

    }


    private Marker addMarker(LatLng latLng) {
        myMarker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("مکان انتخابی")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo))
        );

        return myMarker;
    }

    private Marker addMyLocationMarker(LatLng latLng) {
        if (myLocalMarker != null)
            myLocalMarker.remove();
        myLocalMarker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("مکان تقریبی")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        );

        return myMarker;
    }

    public static MarkerOptions MakeMarker(Context context, LatLng loc) {
        MarkerOptions options = new MarkerOptions()
                .position(loc)
                .title("مکان انتخابی")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo));
        return options;
    }

    public static void REGISTER_FOR_RESULT(BaseActivity activity, ActivityResultCallback<ActivityResult> callback) {
        Intent intent = new Intent(activity, GetLocationActivity.class);
        activity.lunchActivityForResult(intent, callback);
    }
}
