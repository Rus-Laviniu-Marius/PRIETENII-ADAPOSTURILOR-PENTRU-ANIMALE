package com.pet.shelter.friends.adoption.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.adoption.model.Pet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomListOfPetsAdapter extends ArrayAdapter<Pet> {

    private final List<Pet> petList;
    private final int custom_pet_view_layout_id;

    private DatabaseReference favoritePetsReference = FirebaseDatabase.getInstance().getReference("favoritePets");

    public CustomListOfPetsAdapter(@NonNull Context context, int resource, @NonNull List<Pet> objects) {
        super(context, resource, objects);
        petList = objects;
        custom_pet_view_layout_id = resource;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(custom_pet_view_layout_id, null);
        }

        // initializing the imageview and textview and setting data
        ImageView petImageView = v.findViewById(R.id.customPetView_imageView);
        TextView nameTextView = v.findViewById(R.id.customPetViewName_textView);
        TextView ageTextView = v.findViewById(R.id.customPetViewAge_textView);
        TextView sizeTextView = v.findViewById(R.id.customPetViewSize_textView);
        TextView sexTextView = v.findViewById(R.id.customPetViewSex_textView);
        TextView locationTextView = v.findViewById(R.id.customPetViewLocation_textView);
        CardView containerPetView = v.findViewById(R.id.customPetViewContainer_relativeLayout);

        // get the item using the  position param
        Pet pet = petList.get(position);

        Picasso.get().load(pet.getImageDownloadLink()).into(petImageView);
        String age = pet.getAge()+" years";
        nameTextView.setText(pet.getName());
        if (Boolean.parseBoolean(pet.isFavorite())) {
            nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.filled_heart_32, 0);
        } else {
            nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.heart_32, 0);
        }
        ageTextView.setText(age);
        sizeTextView.setText(pet.getSize());
        sexTextView.setText(pet.getSex());
        locationTextView.setText(pet.getLocation());
        switch (pet.getBackgroundColor()) {
            case "linen":
                containerPetView.setBackgroundColor(v.getResources().getColor(R.color.linen));
                break;
            case "water":
                containerPetView.setBackgroundColor(v.getResources().getColor(R.color.water));
                break;
            case "magic_mint":
                containerPetView.setBackgroundColor(v.getResources().getColor(R.color.magic_mint));
                break;
            case "yellow_crayola":
                containerPetView.setBackgroundColor(v.getResources().getColor(R.color.yellow_crayola));
                break;
            default:
                break;
        }

        return v;
    }
}
