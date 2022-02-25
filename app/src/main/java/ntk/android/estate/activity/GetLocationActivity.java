package ntk.android.estate.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

import es.dmoral.toasty.Toasty;
import ir.map.sdk_map.maps.MapView;
import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.estate.R;

public class GetLocationActivity extends BaseActivity {
    private Marker marker;
    private Label label;
    MapView map;
    FusedLocationProviderClient fusedLocationClient;
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

                    if (label != null) {
                        map.removeLabel(label);
                    }

                    //Place current location marker
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addLabel(addLabel(latLng));
                    //move map camera
                    map.moveCamera(latLng, 2);
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
        map = findViewById(R.id.mapview);
        map.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null)
                    map.removeMarker(marker);
                map.addMarker(addMarker(latLng));
                map.moveCamera(latLng, 2);
            }
        });
        map.setOnCameraMoveFinishedListener(i -> {
            if (marker != null)
                map.removeMarker(marker);
            map.addMarker(addMarker(map.getCameraTargetPosition()));
        });
        map.setOnMapLongClickListener(latLng -> map.addLabel(addLabel(latLng)));
        map.setMyLocationEnabled(false);
        findViewById(R.id.setLocationBtn).setOnClickListener(view -> {
            if (marker == null)
                Toasty.error(GetLocationActivity.this, "موقعیتی انتخاب نشده است").show();
            else {
                Intent i = new Intent();
                i.putExtra(Extras.EXTRA_FIRST_ARG, marker.getLatLng().getLatitude());
                i.putExtra(Extras.EXTRA_SECOND_ARG, marker.getLatLng().getLongitude());
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

//    public void getLastLocation() {
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, location -> {
//                    // Got last known location. In some rare situations this can be null.
//                    if (location != null) {
//                        LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
//                        map.moveCamera(myLoc, 500);
//                        addLabel(myLoc);
//                    }
//                }).addOnFailureListener(runnable ->{
//                    String s;
//        });
//    }

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
                map.moveCamera(myLoc, 2);
                addLabel(myLoc);
            }
        });

    }

    private Label addLabel(LatLng latLng) {
        TextStyleBuilder textStyleBuilder = new TextStyleBuilder();
        textStyleBuilder.setFontSize(16f);
        textStyleBuilder.setColor(new Color((short) 0, (short) 0, (short) 0, (short) 255));
        TextStyle textStyle = textStyleBuilder.buildStyle();

        // Creating label
        label = new Label(latLng, textStyle, "مکان تقریبی شما");
        return label;
    }

    private Marker addMarker(LatLng latLng) {
        // Creating animation for marker. We should use an object of type AnimationStyleBuilder, set
        // all animation features on it and then call buildStyle() method that returns an object of type
        // AnimationStyle
        marker = MakeMarker(this, latLng);

        return marker;
    }

    public static Marker MakeMarker(Context context, LatLng loc) {
        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
        animStBl.setPhaseInDuration(0.5f);
        animStBl.setPhaseOutDuration(0.5f);
        AnimationStyle animStyle = animStBl.buildStyle();
        MarkerStyleBuilder markStCr = new MarkerStyleBuilder();
        markStCr.setSize(90f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo)));
        markStCr.setAnimationStyle(animStyle);
        Marker marker = new Marker(loc, markStCr.buildStyle());
        return marker;
    }

    public static void REGISTER_FOR_RESULT(BaseActivity activity, ActivityResultCallback<ActivityResult> callback) {
        Intent intent = new Intent(activity, GetLocationActivity.class);
        activity.lunchActivityForResult(intent, callback);
    }
}
