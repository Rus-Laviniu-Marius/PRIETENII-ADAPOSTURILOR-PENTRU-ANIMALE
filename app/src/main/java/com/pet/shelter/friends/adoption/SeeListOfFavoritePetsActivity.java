package com.pet.shelter.friends.adoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.adoption.adapter.FavoritePetsAdapter;
import com.pet.shelter.friends.adoption.model.Pet;

import java.util.ArrayList;
import java.util.Objects;

public class SeeListOfFavoritePetsActivity extends AppCompatActivity {

    private RecyclerView favoritePetsRecyclerView;
    private final ArrayList<Pet> favoritePetsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_list_of_favorite_pets);

        DatabaseReference favoritePetsReference = FirebaseDatabase.getInstance().getReference("favoritePets");

        String loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        favoritePetsRecyclerView = findViewById(R.id.favoritePets_recyclerView);

        ImageView back = findViewById(R.id.favoritePetsBack_imageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SeeListOfFavoritePetsActivity.this, SeeListOfPetsActivity.class));
            }
        });

        Toast.makeText(this, loggedUserId, Toast.LENGTH_SHORT).show();

        favoritePetsReference.child(loggedUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoritePetsArrayList.clear();
                for (DataSnapshot petDataSnapshot : snapshot.getChildren()) {
                    Pet pet = petDataSnapshot.getValue(Pet.class);
                    favoritePetsArrayList.add(pet);
                }
                FavoritePetsAdapter.OnRecyclerViewItemClickListener petsListCallback = new FavoritePetsAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onRecyclerViewItemClick(int position) {
                        Pet pet = favoritePetsArrayList.get(position);

                        Intent intent = new Intent(SeeListOfFavoritePetsActivity.this,SeeFavoritePetDetailsActivity.class);

                        intent.putExtra("backgroundColor", pet.getBackgroundColor());
                        intent.putExtra("imageDownloadLink", pet.getImageDownloadLink());
                        intent.putExtra("petName", pet.getName());
                        intent.putExtra("petAge", pet.getAge());
                        intent.putExtra("petWeight", pet.getWeight());
                        intent.putExtra("petLocation", pet.getLocation());
                        intent.putExtra("petSize", pet.getSize());
                        intent.putExtra("petBreed", pet.getBreed());
                        intent.putExtra("petGender", pet.getSex());
                        intent.putExtra("petDescription", pet.getDescription());
                        intent.putExtra("favorite", pet.isFavorite());
                        startActivity(intent);
                    }
                };
                FavoritePetsAdapter favoritePetsAdapter = new FavoritePetsAdapter(favoritePetsArrayList, petsListCallback);
                favoritePetsRecyclerView.setAdapter(favoritePetsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}