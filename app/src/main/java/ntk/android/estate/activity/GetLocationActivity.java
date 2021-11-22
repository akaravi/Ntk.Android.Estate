package ntk.android.estate.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.carto.graphics.Color;
import com.carto.styles.AnimationStyle;
import com.carto.styles.AnimationStyleBuilder;
import com.carto.styles.AnimationType;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.styles.TextStyle;
import com.carto.styles.TextStyleBuilder;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.internal.utils.BitmapUtils;
import org.neshan.mapsdk.model.Label;
import org.neshan.mapsdk.model.Marker;

import ntk.android.base.activity.BaseActivity;
import ntk.android.estate.R;

public class GetLocationActivity extends BaseActivity {
    private Marker marker;
    private Label label;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        findViewById(R.id.imgToolbarBack).setOnClickListener(view -> finish());
        MapView map = (MapView) findViewById(R.id.mapview);
        map.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null)
                    map.removeMarker(marker);
                map.addMarker(addMarker(latLng));
            }
        });
        map.setOnMapLongClickListener(latLng -> map.addLabel(addLabel(latLng)));
        map.setOnMyLocationClickListener(new MapView.OnMyLocationClickListener() {
            @Override
            public void OnMyLocationClick(LatLng latLng) {

            }
        });
    }

    private Label addLabel(LatLng latLng) {
        TextStyleBuilder textStyleBuilder = new TextStyleBuilder();
        textStyleBuilder.setFontSize(16f);
        textStyleBuilder.setColor(new Color((short) 0, (short) 0, (short) 0, (short) 255));
        TextStyle textStyle = textStyleBuilder.buildStyle();

        // Creating label
        label = new Label(latLng, textStyle, "مکان انتخاب شده");
        return label;
    }

    private Marker addMarker(LatLng latLng) {
        // Creating animation for marker. We should use an object of type AnimationStyleBuilder, set
        // all animation features on it and then call buildStyle() method that returns an object of type
        // AnimationStyle
        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
        animStBl.setPhaseInDuration(0.5f);
        animStBl.setPhaseOutDuration(0.5f);
        AnimationStyle animStyle = animStBl.buildStyle();
        MarkerStyleBuilder markStCr = new MarkerStyleBuilder();
        markStCr.setSize(90f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)));
        markStCr.setAnimationStyle(animStyle);
            marker = new Marker(latLng, markStCr.buildStyle());

        return marker;
    }
}
