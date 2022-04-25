package com.luis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.luis.databinding.ActivityTrackEjBinding;
import com.luis.pojos.Localizacion;
import com.luis.pojos.Repository;

import java.util.ArrayList;

public class TrackEjActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityTrackEjBinding binding;
    Repository r;
    String name;
    String modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String[] datos = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

        binding = ActivityTrackEjBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = datos[0];
        modo = datos[1];

        r = Repository.getInstance(getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        ArrayList<Localizacion> locs = new ArrayList<>();

        if(modo.equals("correr")) {
             locs = r.getDeportista(name).getCarreras().get(r.getDeportista(name).getCarreras().size()-1).getPath();
        }else if(modo.equals("bici")){
            locs = r.getDeportista(name).getBicicletas().get(r.getDeportista(name).getBicicletas().size()-1).getPath();
        }

        ArrayList<LatLng> latLngs = new ArrayList<>();
        for(Localizacion l: locs){
            latLngs.add(new LatLng(l.getLat(), l.getLon()));
        }

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .width(20)
                .color(Color.RED)
                .addAll(latLngs)
        );

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(latLngs.get(0)).title("Inicio"));
        mMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size()-1)).title("Fin"));
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 15));

    }
}