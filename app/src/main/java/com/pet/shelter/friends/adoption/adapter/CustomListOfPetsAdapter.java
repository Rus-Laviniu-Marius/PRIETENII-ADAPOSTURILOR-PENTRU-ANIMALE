package com.pet.shelter.friends.adoption.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.pet.shelter.friends.R;
import com.pet.shelter.friends.adoption.model.Pet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomListOfPetsAdapter extends ArrayAdapter<Pet> {

    private List<Pet> petsList;
    private final int custom_pet_view_layout_id;

    public CustomListOfPetsAdapter(@NonNull Context context, int resource, @NonNull List<Pet> objects) {
        super(context, resource, objects);
        petsList = objects;
        custom_pet_view_layout_id = resource;
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
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(custom_pet_view_layout_id, null);
        }

        // initializing the card elements and setting data
        ImageView petImageView = v.findViewById(R.id.customPetView_imageView);
        TextView nameTextView = v.findViewById(R.id.customPetViewName_textView);
        TextView ageTextView = v.findViewById(R.id.customPetViewAge_textView);
        TextView sizeTextView = v.findViewById(R.id.customPetViewSize_textView);
        TextView sexTextView = v.findViewById(R.id.customPetViewSex_textView);
        TextView locationTextView = v.findViewById(R.id.customPetViewLocation_textView);
        TextView fitForChildrenTextView = v.findViewById(R.id.customPetViewFitForChildren_textView);
        TextView fitForGuardingTextView = v.findViewById(R.id.customPetViewFitForGuarding_textView);
        CardView containerPetView = v.findViewById(R.id.customPetViewContainer_relativeLayout);

        // get the item using the position param
        Pet pet = petsList.get(position);

        Picasso.get().load(pet.getImageDownloadLink()).into(petImageView);
        String age = pet.getAge() + " years";

        nameTextView.setText(pet.getName());
        if (Boolean.parseBoolean(pet.isFavorite())) {
            nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.filled_heart_32, 0);
        } else {
            nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.heart_32, 0);
        }
        if (Boolean.parseBoolean(pet.isSelected())) {
            containerPetView.setCardBackgroundColor(Color.WHITE);
        }


        ageTextView.setText(age);
        sizeTextView.setText(pet.getSize());
        sexTextView.setText(pet.getSex());
        locationTextView.setText(pet.getLocation());

        if (pet.isFitForChildren())
            fitForChildrenTextView.setText(R.string.fit_for_children);
        else
            fitForChildrenTextView.setText(R.string.not_fit_for_children);

        if (pet.isFitForGuarding())
            fitForGuardingTextView.setText(R.string.fit_for_guarding);
        else
            fitForGuardingTextView.setText(R.string.not_fit_for_guarding);
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

    public void upToDate(List<Pet> newList){
        petsList = new ArrayList<>();
        petsList.addAll(newList);
        notifyDataSetChanged();
    }
}
