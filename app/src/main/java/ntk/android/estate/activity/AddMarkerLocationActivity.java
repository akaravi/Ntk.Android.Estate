package ntk.android.estate.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.carto.styles.AnimationStyle;
import com.carto.styles.AnimationStyleBuilder;
import com.carto.styles.AnimationType;
import com.carto.styles.MarkerStyle;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.model.Marker;
import org.neshan.mapsdk.style.NeshanMapStyle;

import ntk.android.base.activity.BaseActivity;
import ntk.android.estate.R;


class AddMarkerLocationActivity extends BaseActivity {

//    // layer number in which map is added
//    final int BASE_MAP_INDEX = 0;
//
//    // map UI element
//    MapView map;
//    // Marker that will be added on map
//    Marker marker;
//    // an id for each marker
//    long markerId = 0;
//    // marker animation style
//    AnimationStyle animSt;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // starting app in full screen
//        setContentView(R.layout.activity_add_marker);
//        findViewById(R.id.setPointBtn).setVisibility(View.GONE);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        // everything related to ui is initialized here
//        initLayoutReferences();
//    }
//
//    // Initializing layout references (views, map and map events)
//    private void initLayoutReferences() {
//        // Initializing views
//        initViews();
//        // Initializing mapView element
//        initMap();
//
//        // when long clicked on map, a marker is added in clicked location
//        // MapEventListener gets all events on map, including single tap, double tap, long press, etc
//        // we should check event type by calling getClickType() on mapClickInfo (from ClickData class)
//        map.setMapEventListener(new MapEventListener() {
//            @Override
//            public void onMapClicked(ClickData mapClickInfo) {
//                super.onMapClicked(mapClickInfo);
//                if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
//                    // by calling getClickPos(), we can get position of clicking (or tapping)
//                    LngLat clickedLocation = mapClickInfo.getClickPos();
//                    // addMarker adds a marker (pretty self explanatory :D) to the clicked location
//                    addMarker(clickedLocation, markerId);
//                    // increment id
//                    markerId++;
//                }
//            }
//        });
//    }
//
//    // We use findViewByID for every element in our layout file here
//    private void initViews() {
//        map = findViewById(R.id.map);
//    }
//
//
//    // Initializing map
//    private void initMap() {
//
//        // add Standard_day map to layer BASE_MAP_INDEX
//        map.setMapStyle(NeshanMapStyle.NESHAN);
//
//        // Setting map focal position to a fixed position and setting camera zoom
//        map.moveCamera(new LatLng(51.330743, 35.767234),200);
//        map.setZoom(14, 0);
//    }
//
//
//    // This method gets a LngLat as input and adds a marker on that position
//    private void addMarker(LatLng loc, long id) {
//        // If you want to have only one marker on map at a time, uncomment next line to delete all markers before adding a new marker
////        markerLayer.clear();
//
//        // Creating animation for marker. We should use an object of type AnimationStyleBuilder, set
//        // all animation features on it and then call buildStyle() method that returns an object of type
//        // AnimationStyle
//        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
//        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
//        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
//        animStBl.setPhaseInDuration(0.5f);
//        animStBl.setPhaseOutDuration(0.5f);
//        animSt = animStBl.buildStyle();
//
//        // Creating marker style. We should use an object of type MarkerStyleCreator, set all features on it
//        // and then call buildStyle method on it. This method returns an object of type MarkerStyle
//        Marker markSt =new Marker();
//        markSt.setTitle("مکان انتخابی");
//        markSt.setStyle(new MarkerStyle())
//        // Creating marker
//        marker = new Marker(loc, new MarkerStyle());
//        // Setting a metadata on marker, here we have an id for each marker
//
//        // Adding marker to markerLayer, or showing marker on map!
//        markerLayer.add(marker);
//
//        //handling events on markerLayer
//
//    }
//
//    private void changeMarkerToBlue(Marker redMarker) {
//        // create new marker style
//        MarkerStyleCreator markStCr = new MarkerStyleCreator();
//        markStCr.setSize(30f);
//        // Setting a new bitmap as marker
//        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker_blue)));
//        markStCr.setAnimationStyle(animSt);
//        MarkerStyle blueMarkSt = markStCr.buildStyle();
//
//        // changing marker style using setStyle
//        redMarker.setStyle(blueMarkSt);
//    }
}

