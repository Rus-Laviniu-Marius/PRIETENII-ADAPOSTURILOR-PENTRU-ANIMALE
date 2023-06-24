package com.pet.shelter.friends.pets.abandoned;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;
import com.squareup.picasso.Picasso;

public class AbandonedPetDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abandoned_pet_details);

        MaterialButton materialButton = findViewById(R.id.abandonedPetDetails_materialButton);
        MaterialToolbar materialToolbar = findViewById(R.id.abandonedPetDetails_materialToolbar);
        ShapeableImageView shapeableImageView = findViewById(R.id.abandonedPetDetails_shapeImageView);
        mapView = findViewById(R.id.abandonedPetDetailsLocation_mapView);
        MaterialTextView petDescriptionMaterialTextView = findViewById(R.id.abandonedPetDetailsDescription_materialTextView);
        MaterialTextView petPlaceDescriptionMaterialTextView = findViewById(R.id.abandonedPetDetailsPlaceDescription_materialTextView);
        ConstraintLayout constraintLayout = findViewById(R.id.abandonedPetDetails_constraintLayout);

        String petImage1DownloadLink = getIntent().getStringExtra("petImage1DownloadLink");
        String petDescription = getIntent().getStringExtra("petDescription");
        String placeDescription = getIntent().getStringExtra("placeDescription");
        String petLocationLatitude = getIntent().getStringExtra("petLocationLatitude");
        String petLocationLongitude = getIntent().getStringExtra("petLocationLongitude");
        latitude = Double.parseDouble(petLocationLatitude);
        longitude = Double.parseDouble(petLocationLongitude);
        petDescriptionMaterialTextView.setText(petDescription);
        petPlaceDescriptionMaterialTextView.setText(placeDescription);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {
                    String message = "Do you want to go on a scout mission for searching a pet like "
                            + petDescription + " this? Here are the pet's last seen location hints "
                            + placeDescription + ". Don't worry about how we'll get there, because " +
                            "here are the coordinates: latitude " + latitude + " and longitude " +
                            longitude + ".";
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setType("text/plain");
                    Intent chooserIntent = Intent.createChooser(sendIntent, "Select messaging apps");
                    startActivity(chooserIntent);
                }
                return false;
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(
                        constraintLayout,
                        "Please click the location mark from the map and click on directions " +
                                "button from bottom right corner", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        Picasso.get().load(petImage1DownloadLink).into(shapeableImageView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.addMarker(new MarkerOptions()
                .title("Last seen location")
                .position(latLng));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}