package com.pet.shelter.friends.pets.lost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.R;

public class LostPetBottomSheetDialog extends BottomSheetDialogFragment {

    private final String email;
    private final String phoneNumber;
    private final String petName;

    public LostPetBottomSheetDialog(String email, String phoneNumber, String petName) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.petName = petName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lost_pet_bottom_sheet, container, false);

        MaterialTextView emailMaterialTextView = view.findViewById(R.id.lostPetBottomSheetEmail_materialTextView);
        MaterialTextView phoneNumberMaterialTextView = view.findViewById(R.id.lostPetBottomSheetPhone_materialTextView);
        MaterialButton emailOwner = view.findViewById(R.id.lostPetBottomSheetEmail_materialButton);
        MaterialButton phoneOwner = view.findViewById(R.id.lostPetBottomSheetPhone_materialButton);

        emailMaterialTextView.setText(email);
        phoneNumberMaterialTextView.setText(phoneNumber);

        emailOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = "Contact about lost " + petName + " pet";
                String text = "I found your lost pet " + petName + ". How can we meet?";
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("mailto:")); // ensure that only email apps would handle this
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                Intent intent = Intent.createChooser(sendIntent, "Select mailing app");
                startActivity(intent);
            }
        });

        phoneOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });

        return view;
    }
}
