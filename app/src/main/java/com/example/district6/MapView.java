package com.example.district6;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.RasterLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.raster.Raster;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/*
Handles the displaying of the new and old map, the exit button and the location services
Handles location-based pop-ups at site coordinates
Renders a static route from the ArcGis server
Renders an ArcGis map from an ArcGis server
Places white dots as markers for the historical sites
Handles user input: tapping on screen at sites to view pop up, clicking buttons, switches and spinners
and zooming in and out
 */
public class MapView extends AppCompatActivity {

    private static final int RED =0xFFFF0000;
    private com.esri.arcgisruntime.mapping.view.MapView mMapView;
    public ArcGISMap map;
    private Button feedback_btn;
    private Point latestPoint=null;
    private static final int WHITE = 0xFFFFFFFF;
    private final matchTapToPoint matchTap= new matchTapToPoint();
    private Callout mCallout;
    private LocationDisplay mLocationDisplay;
    private Spinner mSpinner;
    private MatchLocationToPoint matchLocationToPoint= new MatchLocationToPoint();
    private final manageSites sites= new manageSites();
    private final int requestCode = 2;
    private final String[] reqPermissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION };
    public static final String EXTRA_NUMBER= "com.example.app.EXTRA_NUMBER";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_main);

        // authentication with an API key or named user is required to access basemaps and other
        // location services
        ArcGISRuntimeEnvironment.setApiKey("AAPK806e5c41e4394e6cba293d4b658c66b32D0T_KFA82eU3YcBgsO6UpUZA5snkXKXr29yyysyoZfxbfY5ViWrYeovoE5rIjFf");

        mSpinner = findViewById(R.id.spinner);
        mMapView = findViewById(R.id.mapView);
        map = new ArcGISMap(BasemapStyle.ARCGIS_IMAGERY); // selected ArcGis map style
        mMapView.setMap(map);
        mMapView.setViewpoint(new Viewpoint(-33.93045, 18.43118, 1000));
        addPoints(map);
        addRoute(map);

        // get the MapView's LocationDisplay
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.startAsync();

        // Listen to changes in the status of the location data source.
        mLocationDisplay.addDataSourceStatusChangedListener(dataSourceStatusChangedEvent -> {

            // If LocationDisplay started OK, then continue.
            if (dataSourceStatusChangedEvent.isStarted())
                return;

            // No error is reported, then continue.
            if (dataSourceStatusChangedEvent.getError() == null)
                return;

            // If an error is found, handle the failure to start.
            // Check permissions to see if failure may be due to lack of permissions.
            boolean permissionCheck1 = ContextCompat.checkSelfPermission(this, reqPermissions[0]) ==
                    PackageManager.PERMISSION_GRANTED;
            boolean permissionCheck2 = ContextCompat.checkSelfPermission(this, reqPermissions[1]) ==
                    PackageManager.PERMISSION_GRANTED;

            if (!(permissionCheck1 && permissionCheck2)) {
                // If permissions are not already granted, request permission from the user.
                ActivityCompat.requestPermissions(this, reqPermissions, requestCode);
            } else {
                // Report other unknown failure types to the user - for example, location services may not
                // be enabled on the device.
                String message = String.format("Error: Turn location services on", dataSourceStatusChangedEvent
                        .getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        });

        /*
            Listen to users location, if the user within 5m radius of point display pop-up
            associated with that point
         */
        mLocationDisplay.addLocationChangedListener(locationChangedEvent -> {
            if (mMapView.getLocationDisplay() != null) {
                if (mMapView.getLocationDisplay().getLocation() != null) {
                    if (mMapView.getLocationDisplay().getLocation().getPosition() != null) {
                        Point tempUserLocation = mMapView.getLocationDisplay().getLocation().getPosition();
                        Point userLocation = new Point(tempUserLocation.getX(), tempUserLocation.getY(), SpatialReferences.getWgs84());
                        if (matchLocationToPoint.returnPoint(userLocation) != null && matchLocationToPoint.returnMessage() != null) {
                            if (!((matchLocationToPoint.returnPoint(userLocation)).equals(latestPoint))) {
                                latestPoint = matchLocationToPoint.returnPoint(userLocation);
                                displayMessage(matchLocationToPoint.returnPoint(userLocation), matchLocationToPoint.returnMessage(), matchLocationToPoint.getNumberForMediaArray());
                            }
                        }
                    }
                }
            }
        });

        /*
            Exit map and go to feedback page
         */
        feedback_btn = findViewById(R.id.feedback);
        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MapView.this, userFeedback.class);
                startActivity(intent);
            }
        });

        // Populate the list for the Location display options for the spinner's Adapter
        ArrayList<itemData> icons = new ArrayList<>();
        icons.add(new itemData("Location Services", R.drawable.locationdisplayon_round));
        icons.add(new itemData("Re-Center on Route", R.drawable.locationdisplayrecenter_round));
        icons.add(new itemData("Go to my Location", R.drawable.locationdisplayheading9_round));

        // Creating adapter for spinner
        spinnerAdapter adapter = new spinnerAdapter(this, R.layout.spinnerlayout, R.id.txt, icons);
        mSpinner.setAdapter(adapter);

        /*
            Set up spinner which contains location options (spinner at case 0 begins location display)
            Other two location options: center on user location, center on route
         */

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Start Location Display
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                    case 1:
                        // Re-Center MapView on route
                        mMapView.setViewpoint(new Viewpoint(-33.93045, 18.43118, 1800));
                        break;
                    case 2:
                        // Start Compass Mode
                        // This mode is suited for navigation when the user is walking.
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


        Switch sw =findViewById(R.id.oldMapSwitch);
        sw.setChecked(false);

        /*
            Listen for if the switch is toggled
            If it is, load tif from the assest file to add the tif to the map
            as a raster layer
         */
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    File f = new File(getCacheDir() + "/D6_Georef_1968.tif");
                    if (!f.exists()) try {

                        InputStream is = getAssets().open("D6_Georef_1968.tif");
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();

                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(buffer);
                        fos.close();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    Raster raster = new Raster(f.getPath());
                    final RasterLayer rasterLayer = new RasterLayer(raster);
                    map.getOperationalLayers().add(rasterLayer);
                    addRoute(map);
                } else {
                    setCurrentMap(map);
                }
            }
        });

        /*
            Listen for user tapping the screen on site icon,
            If icon tapped, the pop-up associated with it displays on screen
         */

        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mMapView) {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
              //  Intent intent = new Intent(MainActivityJava.this, imageUse.class);

                // get the point that was clicked and convert it to a point in map coordinates
                android.graphics.Point screenPoint = new android.graphics.Point(Math.round(motionEvent.getX()),
                        Math.round(motionEvent.getY()));
                // create a map point from screen point
                Point mapPoint = mMapView.screenToLocation(screenPoint);
                // convert to WGS84 for lat/lon format
                Point wgs84Point = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());

                if ((matchTap.getPoint(wgs84Point)!=null) && (matchTap.getMessage()!=null))
                    displayMessage(matchTap.getPoint(wgs84Point), matchTap.getMessage(), matchTap.getNumberForMediaArray());
                // center on tapped point
                mMapView.setViewpointCenterAsync(mapPoint);

                return true;
            }
        });

    }

    /*
        Handle location permission request
        If granted, start displaying location
        Else, send error message
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Location permission was granted. This would have been triggered in response to failing to start the
            // LocationDisplay, so try starting this again.
            mLocationDisplay.startAsync();
        } else {
            // If permission was denied, show toast to inform user what was chosen.
            Toast.makeText(this, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
        }
    }


    /*
    Display pop up message for each site containing:
    name of site, button to view media, button to close
     */
    public void displayMessage(Point mapPoint, String name, int siteIndex) {

        LinearLayout calloutContent = new LinearLayout(getApplicationContext());

        Button yesButton = new Button(getApplicationContext());
        yesButton.setTextColor(Color.BLACK);
        yesButton.setEnabled(true);
        yesButton.setText("Learn more");

        Button noButton = new Button(getApplicationContext());
        noButton.setTextColor(Color.BLACK);
        noButton.setEnabled(true);
        noButton.setText("Close");

        TextView text = new TextView(getApplicationContext());
        text.setText(name);

        text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        yesButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        noButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        calloutContent.addView(yesButton);
        calloutContent.addView(noButton);
        calloutContent.addView(text);

        mCallout = mMapView.getCallout();
        mCallout.setLocation(mapPoint);
        mCallout.setContent(calloutContent);
        mCallout.show();

        Intent intent = new Intent(MapView.this, MediaActivity.class);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(EXTRA_NUMBER, siteIndex); // this number is the index of the site in the media pages array
                startActivity(intent);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallout.dismiss();
            }
        });

    }

    /*
    sets up modern ArcGis map with points and route
    */
    public void setCurrentMap(ArcGISMap map){
        map.getOperationalLayers().clear();
        addPoints(map);
        addRoute(map);
    }

    /*
     create a graphics overlay and create a symbol for the sites
     call addOverlay for each site to add each site to the map
    */
    public void addPoints(ArcGISMap map){
        SimpleMarkerSymbol symbol =
               new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, WHITE, 18.0f);

        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(graphicsOverlay);

        addOverlay(sites.getBusStop(), graphicsOverlay, symbol);
        addOverlay(sites.getStMarks(), graphicsOverlay, symbol);
        addOverlay(sites.getStarCinema(), graphicsOverlay, symbol);
        addOverlay(sites.getHanoverStreet(), graphicsOverlay, symbol);
        addOverlay(sites.getSevenSteps(), graphicsOverlay, symbol);
        addOverlay(sites.getPublicWashHouse(), graphicsOverlay, symbol);
    }

    /*
        add each site to the map as a graphic
     */
    public void addOverlay(Point site, GraphicsOverlay graphicsOverlay, SimpleMarkerSymbol symbol){
        Graphic newSite = new Graphic(site, symbol);
        graphicsOverlay.getGraphics().add(newSite);
    }

    /*
    Renders and displays route from an arcgis feature table
     */
    public void addRoute(ArcGISMap map) {
        ServiceFeatureTable tableRoute = new ServiceFeatureTable("https://services6.arcgis.com/7l3ennsnMgGZwnJz/arcgis/rest/services/routeline2/FeatureServer/1");
        FeatureLayer layerRoute = new FeatureLayer(tableRoute);
        SimpleLineSymbol firstClassSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, RED, 5.0f);
        SimpleRenderer simple_renderer= new SimpleRenderer(firstClassSymbol);
        layerRoute.setRenderer(simple_renderer);
        map.getOperationalLayers().add(layerRoute);
    }

    @Override
    protected void onPause() {
        mMapView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onDestroy() {
        mMapView.dispose();
        super.onDestroy();
    }
}