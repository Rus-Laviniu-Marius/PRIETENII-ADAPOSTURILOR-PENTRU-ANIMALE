package com.pet.shelter.friends.pets.abandoned;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AbandonedPetsCustomAdapter extends ArrayAdapter<AbandonedPetData> {

    private List<AbandonedPetData> petsList;
    private final int custom_pets_view_layout_id;

    public AbandonedPetsCustomAdapter(@NonNull Context context, int resource, @NonNull List<AbandonedPetData> objects) {
        super(context, resource, objects);
        petsList = objects;
        custom_pets_view_layout_id = resource;
    }

    @Override
    public int getCount() {
        return petsList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(custom_pets_view_layout_id, null);
        }

        // initializing the card elements and setting data
        ImageView petImage = v.findViewById(R.id.abandonedPetListItemLeading_shapeImageView);
        MaterialTextView petType = v.findViewById(R.id.abandonedPetListItemType_materialTextView);
        MaterialTextView petDescription = v.findViewById(R.id.abandonedPetListItemDescription_materialTextView);

        // get the item using the position param
        AbandonedPetData abandonedPetData = petsList.get(position);
        Picasso.get().load(abandonedPetData.getPetImage1DownloadLink()).into(petImage);
        petType.setText(abandonedPetData.getPetType());
        petDescription.setText(abandonedPetData.getPetDescription());

        return v;
    }

    public void upToDate(List<AbandonedPetData> newList){
        petsList = new ArrayList<>();
        petsList.addAll(newList);
        notifyDataSetChanged();
    }
}

