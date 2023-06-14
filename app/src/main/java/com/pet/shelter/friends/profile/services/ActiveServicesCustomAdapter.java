package com.pet.shelter.friends.profile.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.news.NewsArticleData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActiveServicesCustomAdapter extends ArrayAdapter<NewsArticleData> {

    private List<ActiveServiceData> activeServicesList;
    private final int custom_active_services_view_layout_id;

    public ActiveServicesCustomAdapter(@NonNull Context context, int resource, @NonNull List<ActiveServiceData> objects) {
        super(context, resource);
        activeServicesList = objects;
        custom_active_services_view_layout_id = resource;
    }

    @Override
    public int getCount() {
        return activeServicesList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(custom_active_services_view_layout_id, null);
        }

        // initializing the card elements and setting data
        ShapeableImageView activeServiceProviderProfileImage = v.findViewById(R.id.activeServiceProviderProfileImage_shapeImageView);
        MaterialTextView activeServiceProviderName = v.findViewById(R.id.activeServiceProviderName_materialTextView);
        MaterialTextView activeServiceProviderLocationCityStateCountry = v.findViewById(R.id.activeServiceProviderLocationCityStateCountry_materialTextView);
        MaterialTextView activeServiceProviderDescription = v.findViewById(R.id.activeServiceProviderDescription_materialTextView);
        // get the item using the position param
        ActiveServiceData activeServiceData = activeServicesList.get(position);

        activeServiceProviderName.setText(activeServiceData.getName());
        activeServiceProviderLocationCityStateCountry.setText(activeServiceData.getCityStateCountry());
        activeServiceProviderDescription.setText(activeServiceData.getDescription());
        Picasso.get().load(activeServiceData.getProviderUserProfileImage()).into(activeServiceProviderProfileImage);
        return v;
    }

    public void upToDate(List<ActiveServiceData> newList){
        activeServicesList = new ArrayList<>();
        activeServicesList.addAll(newList);
        notifyDataSetChanged();
    }
}