package com.example.user.mana_livechatv2;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Pencarian_Narasumber extends AppCompatActivity implements OnMapReadyCallback, OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static final String TAGS = Pencarian_Narasumber.class.getSimpleName();
    private GoogleMap mMap;
    private RadioButton radioSexButton;
    private Button btnDisplay;
    private RadioGroup radioSexGroup;
    boolean permisi = false;
    protected LocationListener locationListener;
    LatLng saya;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    CheckBox pakar;
    CheckBox penyuluh;
    CheckBox peneliti;
    ArrayList<String> result = new ArrayList<String>();
    ArrayList<LatLng> posisi = new ArrayList<LatLng>();
    ArrayList<String> nama = new ArrayList<String>();
    ArrayList<String> jabatans = new ArrayList<String>();
    ArrayList<Integer> x = new ArrayList<Integer>();
    ArrayList<Integer> y = new ArrayList<Integer>();
    ArrayList<String> address = new ArrayList<String>();
    TextView tv1;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    HashMap<String, String[]> abc = new HashMap<String, String[]>();
    private static String TAG = "kampret";
    Location lokasi;
    Button btnClosePopup;
    Button chatting;

    // json object response url
//    private String urlJsonObj = "http://202.56.170.37/mobile-agro/connection.php";
    //"http://api.androidhive.info/volley/person_object.json"
    // json array response url
    private String urlJsonArry = "http://202.56.170.37/mobile-agro/narasumber.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_pencarian__narasumber);

        makeJsonArrayRequest();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Jagung");
        categories.add("Padi");
        categories.add("Cabai");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        find_and_modify_text_view();
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        String text = mySpinner.getSelectedItem().toString();

//        //intentnya harus ngasih data user juga, belom dihandle
//        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent intent = new Intent(Pencarian_Narasumber.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        boolean kampret = isLocationEnabled(Pencarian_Narasumber.this);
        if (!kampret) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Log.i(TAG, "All location settings are satisfied.");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result
                                // in onActivityResult().
                                status.startResolutionForResult(Pencarian_Narasumber.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }


            });
        }

//        Toast.makeText(getApplicationContext(),
//                String.valueOf(kampret), Toast.LENGTH_SHORT).show();

           //displayLocationSettingsRequest(Pencarian_Narasumber.this);



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

        result.add("penyuluh");
        result.add("peneliti");
        result.add("pakar");




    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
//        Toast.makeText(getApplicationContext(),
//                "masuk2", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }


    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        saya = latLng;
//        Toast.makeText(getApplicationContext(),
//                "masuk handled", Toast.LENGTH_SHORT).show();
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
        float zoomLevel = 15; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }


    private PopupWindow pwindo;

    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject narasumber = (JSONObject) response
                                        .get(i);

                                String name = narasumber.getString("username");
                                String jabatan = narasumber.getString("jabatan");
                                String unit = narasumber.getString("unit");
                                String bidang = narasumber.getString("bidang");
                                String komoditas = narasumber.getString("komoditas");
                                String role = narasumber.getString("role");
                                String alamat = narasumber.getString("alamat");


                                String[] kampret = new String[7];


                                kampret[0] = name;
                                kampret[1] = jabatan;
                                kampret[2] = unit;
                                kampret[3] = bidang;
                                kampret[4] = komoditas;
                                kampret[5] = role;
                                kampret[6] = alamat;

                                abc.put(name, kampret);
                                nama.add(name);
                                jabatans.add(role);
                                String tai = alamat;
                                LatLng apa = getLocationFromAddress(Pencarian_Narasumber.this, tai);
                                posisi.add(apa);

                            }


                            //txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

        MyApplication.getInstance().addToRequestQueue(req);
    }

    private void initiatePopupWindow(Marker m, HashMap<String, String[]> x) {
        try {


// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) Pencarian_Narasumber.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.tes,
                    (ViewGroup) findViewById(R.id.popup_element));
            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            pwindo = new PopupWindow(layout, layout.getMeasuredWidth() + layout.getMeasuredWidth(), layout.getMeasuredHeight(), true);
            String[] apa = x.get(m.getTitle());
            String app = apa[2];
            String app2 = apa[3];
            String app3 = apa[4];
            String app4 = apa[5];
            String jabat = apa[1];

            ((TextView) pwindo.getContentView().findViewById(R.id.txtView)).append(m.getTitle());
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView2)).append(jabat);
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView3)).append(app);
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView4)).append(app2);
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView5)).append(app3);
            ((TextView) pwindo.getContentView().findViewById(R.id.txtView6)).append(app4);

            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            chatting = (Button) layout.findViewById(R.id.buttonchat);
            chatting.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    // Intent i = new Intent(this, "apaaan".class);
                    //startActivity(i);
                }
            });
            btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
            btnClosePopup.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnClickListener cancel_button_click_listener = new OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();

        }
    };


    private void find_and_modify_text_view() {
        pakar = (CheckBox) findViewById(R.id.checkBox);
        penyuluh = (CheckBox) findViewById(R.id.checkBox2);
        peneliti = (CheckBox) findViewById(R.id.checkBox3);
        Button get_view_button = (Button) findViewById(R.id.button);
        get_view_button.setOnClickListener(get_view_button_listener);
    }

    private Button.OnClickListener get_view_button_listener = new Button.OnClickListener() {
        public void onClick(View v) {
            Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
//            Toast.makeText(Pencarian_Narasumber.this,
//                    mySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            //makeJsonArrayRequest();

            mMap.clear();

            result = new ArrayList<>();
            if (pakar.isChecked()) {
                result.add((String) pakar.getText());
            }
            if (penyuluh.isChecked()) {
                result.add((String) penyuluh.getText());
            }
            if (peneliti.isChecked()) {
                result.add((String) peneliti.getText());
            }
            showResult(saya);


        }
    };


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void showResult(LatLng x) {

        for (int i = 0; i < posisi.size(); i++) {

            //handleNewLocation(lokasi);
            MarkerOptions options = new MarkerOptions()
                    .position(x)
                    .title("I am here!");
            mMap.addMarker(options);
            float zoomLevel = 15; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x, zoomLevel));

            if ((result.contains(jabatans.get(i)))) {

                mMap.addMarker(new MarkerOptions()
                        .position(posisi.get(i))
                        .title(nama.get(i))
                        .snippet(jabatans.get(i))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.person)));

//                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
//                {
//
//                    @Override
//                    public boolean onMarkerClick(Marker arg0) {
//
//                        return true;
//                    }
//
//                });

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override

                    public void onInfoWindowClick(Marker marker) {

                        initiatePopupWindow(marker, abc);


                    }
                });

            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.{

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

//            Toast.makeText(getApplicationContext(),
//                    "masuk", Toast.LENGTH_SHORT).show();

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }

            else {Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            lokasi = location;
            if (location == null) {

//            Toast.makeText(getApplicationContext(),
//                    "masuk2", Toast.LENGTH_SHORT).show();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
            } else {
                handleNewLocation(location);
            }

    }}

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisi = true;
                    Toast.makeText(getApplicationContext(),
                            "masuk case 1", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    permisi = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
/*
     * Google Play services can resolve some errors it detects.
     * If the error has a resolution, try sending an Intent to
     * start a Google Play services activity that can resolve
     * error.
     */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(Pencarian_Narasumber.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }


        });
    }



}
