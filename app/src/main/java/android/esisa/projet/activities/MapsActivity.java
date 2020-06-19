package android.esisa.projet.activities;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.esisa.projet.R;
import android.esisa.projet.adapters.ContractAdapter;
import android.esisa.projet.models.Contract;
import android.esisa.projet.models.Position;
import android.esisa.projet.models.Station;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final float DEFAULT_ZOOM = 13;
    private Contract currentContract;
    public static final String URL_PART_1 = "https://api.jcdecaux.com/vls/v1/stations?contract=";
    public static final String URL_PART_2 = "&apiKey=0d91672a5b253e6f567e6c01a7952276a6e366a2";
    private String URL;
    private Map<Marker, Station> markerStationMap = new HashMap<Marker, Station>();
    private ImageButton refreshBtn;
    private Button departureBtn;
    private Button destinationBtn;
    private Station departure;
    private Station destination;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        currentContract = (Contract) getIntent().getSerializableExtra(ContractAdapter.BUNDLE_MAP_ACTIVITY);
        URL = URL_PART_1 + currentContract.getName() + URL_PART_2;
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    private void refresh() {
        departure = null;
        destination = null;
        mMap.clear();
        markerStationMap.clear();
        fetchStations();
    }

    private void fetchStations() {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("prj", "Failure!");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    final Response r = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Type listType = new TypeToken<ArrayList<Station>>(){}.getType();
                            List<Station> data = new Gson().fromJson(r.body().charStream(), listType);
                            //
                            for(Station s : data) {
                                markStation(s);
                            }
                        }
                    });
                }
            }
        });
    }

    private void markStation(Station station) {
        LatLng loc = new LatLng(station.getPosition().getLat(), station.getPosition().getLng());
        markerStationMap.put(mMap.addMarker(new MarkerOptions().position(loc)), station);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, DEFAULT_ZOOM));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                View view = LayoutInflater.from(MapsActivity.this).inflate(R.layout.custom_dialog, null);
                departureBtn = view.findViewById(R.id.departureBtn);
                departureBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //departure = markerStationMap.get(marker);
                        Station departure = new Gson().fromJson(sharedPreferences.getString("station", ""),
                                Station.class);
                        setDeparture(departure);
                    }
                });
                destinationBtn = view.findViewById(R.id.destinationBtn);
                destinationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Station destination = new Gson().fromJson(sharedPreferences.getString("station", ""),
                                Station.class);
                        setDestination(destination);
                        drawRoute();
                    }
                });

                final Station station = markerStationMap.get(marker);
                //
                sharedPreferences.edit().putString("station", new Gson().toJson(station)).apply();
                //
                TextView nameValue = view.findViewById(R.id.nameValue);
                nameValue.setText(station.getName());

                TextView addressValue = view.findViewById(R.id.addressValue);
                addressValue.setText(station.getAddress());

                TextView availableBikes = view.findViewById(R.id.availableBikesValue);
                availableBikes.setText(station.getAvailable_bikes() + "");

                TextView availableBikeStands = view.findViewById(R.id.availableBikeStandsValue);
                availableBikeStands.setText(station.getAvailable_bike_stands() + "");
                //
                //
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Station infos")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.setView(view);
                builder.show();

                return false;
            }
        });
        //
        fetchStations();
    }

    private void drawRoute() {
        Log.d("departure", departure.getName());
        Log.d("destination", destination.getName());
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + departure.getPosition().getLat() + "," + departure.getPosition().getLng() +
                        "&daddr=" + destination.getPosition().getLat() + "," + destination.getPosition().getLng() ));
        startActivity(intent);
    }

    public Station getDeparture() {
        return departure;
    }

    public void setDeparture(Station departure) {
        this.departure = departure;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }
}
