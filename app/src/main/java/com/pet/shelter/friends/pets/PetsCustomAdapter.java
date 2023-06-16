package com.pet.shelter.friends.pets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.news.NewsArticleData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PetsCustomAdapter extends ArrayAdapter<PetData> {

    private List<PetData> petsList;
    private final int custom_pets_view_layout_id;

    public PetsCustomAdapter(@NonNull Context context, int resource, @NonNull List<PetData> objects) {
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
        ImageView petImage = v.findViewById(R.id.petListItemLeading_shapeImageView);
        MaterialTextView petType = v.findViewById(R.id.petListItemType_materialTextView);
        MaterialTextView petName = v.findViewById(R.id.petListItemName_materialTextView);
        MaterialTextView petDescription = v.findViewById(R.id.petListItemDescription_materialTextView);

        // get the item using the position param
        PetData petData = petsList.get(position);
        Picasso.get().load(petData.getPetImage1DownloadLink()).into(petImage);
        petType.setText(petData.getPetType());
        petName.setText(petData.getPetName());
        petDescription.setText(petData.getPetDescription());


        return v;
    }

    public void upToDate(List<PetData> newList){
        petsList = new ArrayList<>();
        petsList.addAll(newList);
        notifyDataSetChanged();
    }
}