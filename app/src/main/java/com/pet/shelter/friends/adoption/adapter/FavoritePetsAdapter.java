package com.pet.shelter.friends.adoption.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pet.shelter.friends.R;
import com.pet.shelter.friends.adoption.model.Pet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritePetsAdapter extends RecyclerView.Adapter<FavoritePetsAdapter.PetViewHolder> {

    // INTERFACE
    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(int position);
    }

    private final List<Pet> petsList;
    private final OnRecyclerViewItemClickListener itemClickListener;

    public FavoritePetsAdapter(List<Pet> petsList, OnRecyclerViewItemClickListener itemClickListener) {
        this.petsList = petsList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout
        View petView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pet_view_layout, parent, false);
        PetViewHolder petViewHolder =  new PetViewHolder(petView);
        return petViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePetsAdapter.PetViewHolder holder, int position) {

        Pet pet = petsList.get(holder.getAdapterPosition());

        Picasso.get().load(pet.getImageDownloadLink()).into(holder.getPetImageView());
        holder.getNameTextView().setText(pet.getName());
        holder.getNameTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.filled_heart_32, 0);
        holder.getAgeTextView().setText(String.valueOf(pet.getAge()));
        holder.getSizeTextView().setText(pet.getSize());
        holder.getSexTextView().setText(pet.getSex());
        holder.getLocationTextView().setText(pet.getLocation());

        int myColor;
        switch (pet.getBackgroundColor()) {
            case "linen":
                myColor = ContextCompat.getColor(holder.getContainerPetView().getContext(), R.color.linen);
                holder.getContainerPetView().setBackgroundColor(myColor);
                break;
            case "water":
                myColor = ContextCompat.getColor(holder.getContainerPetView().getContext(), R.color.water);
                holder.getContainerPetView().setBackgroundColor(myColor);
                break;
            case "magic_mint":
                myColor = ContextCompat.getColor(holder.getContainerPetView().getContext(), R.color.magic_mint);
                holder.getContainerPetView().setBackgroundColor(myColor);
                break;
            case "yellow_crayola":
                myColor = ContextCompat.getColor(holder.getContainerPetView().getContext(), R.color.yellow_crayola);
                holder.getContainerPetView().setBackgroundColor(myColor);
                break;
            default:
                break;
        }
        final int POSITION = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                itemClickListener.onRecyclerViewItemClick(POSITION);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petsList.size();
    }

    // HOLDER
    public class PetViewHolder extends RecyclerView.ViewHolder{

        protected ImageView petImageView;
        protected TextView nameTextView;
        protected TextView ageTextView;
        protected TextView sizeTextView;
        protected TextView sexTextView;
        protected TextView locationTextView;
        protected CardView containerPetView;


        public PetViewHolder(@NonNull View itemView) {
            super(itemView);

            petImageView = itemView.findViewById(R.id.customPetView_imageView);
            nameTextView = itemView.findViewById(R.id.customPetViewName_textView);
            ageTextView = itemView.findViewById(R.id.customPetViewAge_textView);
            sizeTextView = itemView.findViewById(R.id.customPetViewSize_textView);
            sexTextView = itemView.findViewById(R.id.customPetViewSex_textView);
            locationTextView = itemView.findViewById(R.id.customPetViewLocation_textView);
            containerPetView = itemView.findViewById(R.id.customPetViewContainer_relativeLayout);
        }

        public ImageView getPetImageView() {
            return petImageView;
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getAgeTextView() {
            return ageTextView;
        }

        public TextView getSizeTextView() {
            return sizeTextView;
        }

        public TextView getSexTextView() {
            return sexTextView;
        }

        public TextView getLocationTextView() {
            return locationTextView;
        }

        public CardView getContainerPetView() {
            return containerPetView;
        }
    }


}
