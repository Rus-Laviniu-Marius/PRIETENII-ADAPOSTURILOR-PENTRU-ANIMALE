package com.pet.shelter.friends.profile.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;
import com.squareup.picasso.Picasso;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ActiveServiceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MaterialToolbar materialToolbar;
    private ShapeableImageView providerProfileImage;
    private MaterialTextView providerName, providerCityStateCountry, providerDescription;
    private MaterialButton email, phone, address, webpageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_service_details);

        materialToolbar = findViewById(R.id.activeServiceDetails_materialToolbar);
        providerProfileImage = findViewById(R.id.activeServiceDetailsProviderProfileImage_shapeImageView);
        providerName = findViewById(R.id.activeServiceDetailsProviderName_materialTextView);
        providerCityStateCountry = findViewById(R.id.activeServiceDetailsProviderCityStateCountry_materialTextView);
        providerDescription = findViewById(R.id.activeServiceDetailsProviderAboutMeDescription_materialTextView);
        email = findViewById(R.id.activeServiceDetailsProviderEmail_materialButton);
        phone = findViewById(R.id.activeServiceDetailsProviderPhoneNumber_materialButton);
        address = findViewById(R.id.activeServiceDetailsProviderAddress_materialButton);
        webpageLink = findViewById(R.id.activeServiceDetailsProviderWebpage_materialButton);

        String profileImageToDisplay = getIntent().getStringExtra("providerUserProfileImage");
        String nameToDisplay = getIntent().getStringExtra("name");
        String emailToDisplay = getIntent().getStringExtra("email");
        String phoneNumberToDisplay = getIntent().getStringExtra("phoneNumber");
        String webpageLinkToDisplay = getIntent().getStringExtra("webpageLink");
        String cityStateCountryToDisplay = getIntent().getStringExtra("cityStateCountry");
        String addressToDisplay = getIntent().getStringExtra("address");
        String descriptionToDisplay = getIntent().getStringExtra("description");
        String serviceType = getIntent().getStringExtra("serviceType");

        Picasso.get().load(profileImageToDisplay).into(providerProfileImage);
        providerName.setText(nameToDisplay);
        providerCityStateCountry.setText(cityStateCountryToDisplay);
        providerDescription.setText(descriptionToDisplay);
        email.setText(emailToDisplay);
        phone.setText(phoneNumberToDisplay);
        address.setText(addressToDisplay);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailToDisplay});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PetShelterFriends posted services");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Just want to know about your services");
                emailIntent.setSelector( emailSelectorIntent );
                startActivity(emailIntent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneNumberToDisplay));
                startActivity(intent);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+addressToDisplay));
                startActivity(intent);
            }
        });

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActiveServiceDetailsActivity.this, ActiveServicesActivity.class));
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {
                    String message = nameToDisplay + " offers " + serviceType + " services." +
                            " Here are more details from the description " + descriptionToDisplay;
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
                return true;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}