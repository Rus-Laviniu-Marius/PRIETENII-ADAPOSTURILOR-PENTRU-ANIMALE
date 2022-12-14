package com.pet.shelter.friends.adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.pet.shelter.friends.R;

import java.util.ArrayList;
import java.util.List;

public class SeeListOfPetsActivity extends AppCompatActivity {

    private LinearLayout petCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_list_of_pets);

        petCard = findViewById(R.id.listOfPetsCard_linearLayout);
        petCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeListOfPetsActivity.this, SeePetDetailsActivity.class);
                startActivity(intent);
            }
        });

//        List<Items> itemsList = new ArrayList<>();
//        itemsList.add(new Items(R.drawable.milo_dog, "Milo", 3, getResources().getColor(R.color.card1_background)));
//        itemsList.add(new Items(R.drawable.arthur_dog, "Arthur", 2, getResources().getColor(R.color.card2_background)));
//        itemsList.add(new Items(R.drawable.maya_cat, "Maya", 1, getResources().getColor(R.color.card3_background)));
//        itemsList.add(new Items(R.drawable.oscar_dog, "Oscar",2, getResources().getColor(R.color.card4_background)));
//
//
//        GridView gridView = findViewById(R.id.listOfPetsContentPets_gridView);
//        CustomListOfPetsAdapter customAdapter = new CustomListOfPetsAdapter(this, R.layout.custom_pet_view_layout, itemsList);
//        gridView.setAdapter(customAdapter);
    }
}