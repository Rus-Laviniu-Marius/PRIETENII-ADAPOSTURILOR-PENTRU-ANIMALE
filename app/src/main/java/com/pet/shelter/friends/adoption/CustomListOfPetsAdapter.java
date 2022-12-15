package com.pet.shelter.friends.adoption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pet.shelter.friends.R;

import java.util.List;

public class CustomListOfPetsAdapter extends ArrayAdapter {

    private List<Pets> petsList;
    private int custom_pet_view_layout_id;

    public CustomListOfPetsAdapter(@NonNull Context context, int resource, @NonNull List<Pets> objects) {
        super(context, resource, objects);
        petsList = objects;
        custom_pet_view_layout_id = resource;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View v = convertView;

        if (v == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(custom_pet_view_layout_id, null);
        }

        // initializing the imageview and textview and setting data
        ImageView petImageView = v.findViewById(R.id.customPetView_imageView);
        TextView titleTextView = v.findViewById(R.id.customPetViewTitle_textView);
        TextView ageTextView = v.findViewById(R.id.customPetViewAge_textView);
        TextView sizeTextView = v.findViewById(R.id.customPetViewSize_textView);
        RelativeLayout containerPetView = v.findViewById(R.id.customPetViewContainer_relativeLayout);

        // get the item using the  position param
        Pets pet = petsList.get(position);
        String age = pet.getAge()+" years";
        petImageView.setImageResource(pet.getImage_id());
        titleTextView.setText(pet.getTitle());
        ageTextView.setText(age);
        sizeTextView.setText(pet.getSize());
        containerPetView.setBackgroundColor(pet.getBackgroundColor());
        return v;
    }
}
