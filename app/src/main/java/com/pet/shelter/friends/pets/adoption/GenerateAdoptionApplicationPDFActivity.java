package com.pet.shelter.friends.pets.adoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.pet.shelter.friends.BuildConfig;
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GenerateAdoptionApplicationPDFActivity extends AppCompatActivity {

    private String loggedUserId;
    private DatabaseReference adoptionApplicationsReference;

    private String fullName, address, occupation, sittingTimeAtCurrentAddress, phoneNumber, email,
            bestTimeToCall, aloneHoursNumber, contactForSurrender, daytimePlaceDescription,
            nighttimePlaceDescription, regularHealthCare, petName, adultsNumber, childrenNumber,
            familyAgreement, householdDescription, householdSelectedType, knownAllergy,
            provideAdequateLoveAndAttention, rentingRulesRegardingPetOwnership, hadPetEuthanized,
            disciplineMode, hadPetEuthanizedDescription, ownedPetTypes, haveSurrenderedPet,
            haveSurrenderedPetDescription, vaccinesUpToDate, veterinarianClinicName,
            veterinarianClinicAddress, veterinarianClinicPhoneNumber, veterinarianName, veterinarianClinicEmail,
            referenceAddress, referenceName, referencePhoneNumber, referenceRelationship, referenceEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_adoption_application_pdfactivity);

        loggedUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        adoptionApplicationsReference = FirebaseDatabase.getInstance().getReference("adoptionApplications");
        MaterialToolbar materialToolbar = findViewById(R.id.generateAdoptionApplicationPDF_materialToolbar);
        ShapeableImageView shapeableImageView = findViewById(R.id.generateAdoptionApplicationPDF_shapeImageView);
        MaterialTextView petNameMaterialTextView = findViewById(R.id.generateAdoptionApplicationPDFPetName_materialTextView);
        MaterialButton generate = findViewById(R.id.generateAdoptionApplicationPDF_materialButton);

        petName = getIntent().getStringExtra("petName");
        String petImage1DownloadLink = getIntent().getStringExtra("petImage1DownloadLink");

        petNameMaterialTextView.setText(petName);
        Picasso.get().load(petImage1DownloadLink).into(shapeableImageView);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenerateAdoptionApplicationPDFActivity.this, CompleteAboutPersonalReferencesInformationActivity.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("address", getIntent().getStringExtra("address"));
                intent.putExtra("phoneNumber", getIntent().getStringExtra("phoneNumber"));
                intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("relationshipType", getIntent().getStringExtra("selectedRelationship"));
                startActivity(intent);
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_open_home) {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(GenerateAdoptionApplicationPDFActivity.this)
                            .setTitle("Are you sure you want to return at home activity?")
                            .setMessage("By returning to home activity all adoption completion progress will be lost and you have to repeat the whole adoption process!")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(GenerateAdoptionApplicationPDFActivity.this, HomeActivity.class);
                                    intent.putExtra("FromMainActivity", "1");
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    materialAlertDialogBuilder.create().show();

                }
                return true;
            }
        });

        readDataFromFirebase();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPDF();
                    sendPDFByEmail();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void readDataFromFirebase() {

        adoptionApplicationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(loggedUserId)) {
                    snapshot = snapshot.child(loggedUserId);
                    if (snapshot.hasChild("aboutPetAdopterInformation")) {
                        snapshot = snapshot.child("aboutPetAdopterInformation");
                        fullName = String.valueOf(snapshot.child("fullName").getValue());
                        email = String.valueOf(snapshot.child("email").getValue());
                        address = String.valueOf(snapshot.child("address").getValue());
                        phoneNumber = String.valueOf(snapshot.child("phoneNumber").getValue());
                        occupation = String.valueOf(snapshot.child("occupation").getValue());
                        sittingTimeAtCurrentAddress = String.valueOf(snapshot.child("sittingTimeAtCurrentAddress").getValue());
                        bestTimeToCall = String.valueOf(snapshot.child("bestTimeToCall").getValue());
                     }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adoptionApplicationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(loggedUserId)) {
                    snapshot = snapshot.child(loggedUserId);
                    if (snapshot.hasChild("familyAndHouseholdInformation")) {
                        snapshot = snapshot.child("familyAndHouseholdInformation");
                        adultsNumber = String.valueOf(snapshot.child("adultsNumber").getValue());
                        childrenNumber = String.valueOf(snapshot.child("childrenNumber").getValue());
                        householdSelectedType = String.valueOf(snapshot.child("householdSelectedType").getValue());
                        householdDescription = String.valueOf(snapshot.child("householdDescription").getValue());
                        rentingRulesRegardingPetOwnership = String.valueOf(snapshot.child("rentingRules").getValue());
                        knownAllergy = String.valueOf(snapshot.child("knownAllergy").getValue());
                        familyAgreement = String.valueOf(snapshot.child("allAgree").getValue());
                        provideAdequateLoveAndAttention = String.valueOf(snapshot.child("adequateLoveAndAttention").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adoptionApplicationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(loggedUserId)) {
                    snapshot = snapshot.child(loggedUserId);
                    if (snapshot.hasChild("otherOwnedPets")) {
                        snapshot = snapshot.child("otherOwnedPets");
                        disciplineMode = String.valueOf(snapshot.child("disciplineMode").getValue());
                        hadPetEuthanized = String.valueOf(snapshot.child("hadPetEuthanized").getValue());
                        hadPetEuthanizedDescription = String.valueOf(snapshot.child("hadPetEuthanizedDescription").getValue());
                        ownedPetTypes = String.valueOf(snapshot.child("ownedPetTypes").getValue());
                        haveSurrenderedPet = String.valueOf(snapshot.child("haveSurrenderedPet").getValue());
                        haveSurrenderedPetDescription = String.valueOf(snapshot.child("haveSurrenderedPetDescription").getValue());
                        vaccinesUpToDate = String.valueOf(snapshot.child("vaccinesUpToDate").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adoptionApplicationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(loggedUserId)) {
                    snapshot = snapshot.child(loggedUserId);
                    if (snapshot.hasChild("regularVeterinarian")) {
                        snapshot = snapshot.child("regularVeterinarian");
                        veterinarianName = String.valueOf(snapshot.child("name").getValue());
                        veterinarianClinicName = String.valueOf(snapshot.child("clinicName").getValue());
                        veterinarianClinicAddress = String.valueOf(snapshot.child("clinicAddress").getValue());
                        veterinarianClinicPhoneNumber = String.valueOf(snapshot.child("clinicPhoneNumber").getValue());
                        veterinarianClinicEmail = String.valueOf(snapshot.child("clinicPhoneNumber").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adoptionApplicationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(loggedUserId)) {
                    snapshot = snapshot.child(loggedUserId);
                    if (snapshot.hasChild("aboutWishedPet")) {
                        snapshot = snapshot.child("aboutWishedPet");
                        aloneHoursNumber = String.valueOf(snapshot.child("aloneHoursNumber").getValue());
                        contactForSurrender = String.valueOf(snapshot.child("contactForSurrender").getValue());
                        daytimePlaceDescription = String.valueOf(snapshot.child("daytimePlaceDescription").getValue());
                        nighttimePlaceDescription = String.valueOf(snapshot.child("nighttimePlaceDescription").getValue());
                        regularHealthCare = String.valueOf(snapshot.child("regularHealthCare").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adoptionApplicationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(loggedUserId)) {
                    snapshot = snapshot.child(loggedUserId);
                    if (snapshot.hasChild("personalReferences")) {
                        snapshot = snapshot.child("personalReferences");
                        referenceName = String.valueOf(snapshot.child("name").getValue());
                        referenceAddress = String.valueOf(snapshot.child("address").getValue());
                        referencePhoneNumber = String.valueOf(snapshot.child("phoneNumber").getValue());
                        referenceEmail = String.valueOf(snapshot.child("email").getValue());
                        referenceRelationship = String.valueOf(snapshot.child("relationshipType").getValue());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendPDFByEmail() {
        String mailTo="rusmarius0809@gmail.com";
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SENDTO);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "pet_adoption_papers.pdf");
        Uri pdfUri = FileProvider.getUriForFile(GenerateAdoptionApplicationPDFActivity.this,
                BuildConfig.APPLICATION_ID + ".provider", file);
        shareIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo});
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
        String shareMessage = getResources().getString(R.string.label_share_message);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, getResources().
                getString(R.string.label_chooser_title)));
    }

    private void createPDF() throws IOException{
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "pet_adoption_papers.pdf");

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        SolidLine solidLine = new SolidLine(1f);
        solidLine.setColor(ColorConstants.BLACK);
        LineSeparator lineSeparator = new LineSeparator(solidLine);
        lineSeparator.setWidth(500);
        lineSeparator.setMargins(10, 0,10,0);

        Text newLineText = new Text("\n");
        Paragraph newLineParagraph = new Paragraph(newLineText);

        Drawable d = AppCompatResources.getDrawable(GenerateAdoptionApplicationPDFActivity.this, R.drawable.launcher_logo);
        assert d != null;
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        image.setHeight(300);
        image.setWidth(300);
        image.setHeight(100);
        image.setWidth(100);
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(image);

//        Paragraph paragraph1 = new Paragraph();
//        Text text = new Text("This is to certify that ")
//                .setFontSize(20);
//        Text text1 = new Text(fullName)
//                .setFontSize(60)
//                .setBackgroundColor(ColorConstants.GREEN);
//        Text text2 = new Text("who is a student," + " and who lives in " + address);
//        Text text3 = new Text("has adopted ")
//                .setFontSize(20);
//        Text text4 = new Text(petName + "\n")
//                .setFontSize(60)
//                .setFontColor(ColorConstants.RED);
//        Text text5 = new Text("More about ");
//        paragraph1.add(text).add(text1).add(text2).add(text3).add(text4).add(text5);
//        document.add(paragraph1);

        // Title
        Text title = new Text("PET ADOPTION CERTIFICATE")
                .setBold().setFontSize(26)
                .setUnderline()
                .setTextAlignment(TextAlignment.CENTER);;
        Paragraph titleParagraph = new Paragraph();
        titleParagraph.add(title);
        titleParagraph.setTextAlignment(TextAlignment.CENTER);
        document.add(titleParagraph);

        // Contact information paragraph
        Text contactInformationText = new Text("Contact information").setBold().setFontSize(20);
        Paragraph contactInformationParagraph = new Paragraph(contactInformationText).add(newLineText);

        Text fullNameText = new Text("Full name: " + fullName);
        Paragraph fullNameParagraph = new Paragraph(fullNameText);

        Text occupationText = new Text("Occupation: " + occupation);
        Paragraph occupationParagraph = new Paragraph(occupationText);

        Text addressText = new Text("Address: " + address);
        Paragraph addressParagraph = new Paragraph(addressText);

        Text sittingTimeText = new Text("How long at this address? " + sittingTimeAtCurrentAddress);
        Paragraph sittingTimeParagraph = new Paragraph(sittingTimeText);

        Text phoneNumberText = new Text("Phone number: " + phoneNumber);
        Paragraph phoneNumberParagraph = new Paragraph(phoneNumberText);

        Text emailAddressText = new Text("Email address: " + email);
        Paragraph emailAddressParagraph = new Paragraph(emailAddressText);

        Text bestTimeToCallText = new Text("Best time to call: " + bestTimeToCall);
        Paragraph bestTimeToCallParagraph = new Paragraph(bestTimeToCallText);

        contactInformationParagraph.add(fullNameParagraph).add(newLineText)
                .add(occupationParagraph).add(newLineText)
                .add(addressParagraph).add(newLineText)
                .add(sittingTimeParagraph).add(newLineText)
                .add(phoneNumberParagraph).add(newLineText)
                .add(emailAddressParagraph).add(newLineText)
                .add(bestTimeToCallParagraph).add(newLineText);

        document.add(contactInformationParagraph);

        document.add(lineSeparator);

        // Family and housing paragraph
        Text familyAndHousing = new Text("Family & Household").setBold().setFontSize(20);
        Paragraph familyAndHousingParagraph = new Paragraph(familyAndHousing).add(newLineText);

        Text adultsNumberText = new Text("Adults number: " + adultsNumber);
        Paragraph adultsNumberParagraph = new Paragraph(adultsNumberText);

        Text childrenNumberText = new Text("Children number: " + childrenNumber);
        Paragraph childrenNumberParagraph = new Paragraph(childrenNumberText);

        Text familyAgreementText = new Text("Do you have your family agreement? " + familyAgreement);
        Paragraph familyAgreementParagraph = new Paragraph(familyAgreementText);

        Text homeDescriptionText = new Text("Home description: " + householdDescription);
        Paragraph homeDescriptionParagraph = new Paragraph(homeDescriptionText);

        Text homeTypeText = new Text("Home type: " + householdSelectedType);
        Paragraph homeTypeParagraph = new Paragraph(homeTypeText);

        Text knownAllergyText = new Text("Known allergy: " + knownAllergy);
        Paragraph knownAllergyParagraph = new Paragraph(knownAllergyText);

        Text provideAdequateLoveAndAttentionText = new Text("Do you have enough time to provide adequate love and attention? " + provideAdequateLoveAndAttention);
        Paragraph provideAdequateLoveAndAttentionParagraph = new Paragraph(provideAdequateLoveAndAttentionText);

        Text rentingRulesRegardingPetOwnershipText = new Text("Renting rules regarding pet ownership: " + rentingRulesRegardingPetOwnership);
        Paragraph rentingRulesRegardingPetOwnershipParagraph = new Paragraph(rentingRulesRegardingPetOwnershipText);

        familyAndHousingParagraph.add(adultsNumberParagraph).add(newLineText)
                .add(childrenNumberParagraph).add(newLineText)
                .add(familyAgreementParagraph).add(newLineText)
                .add(homeDescriptionParagraph).add(newLineText)
                .add(homeTypeParagraph).add(newLineText)
                .add(knownAllergyParagraph).add(newLineText)
                .add(provideAdequateLoveAndAttentionParagraph).add(newLineText)
                .add(rentingRulesRegardingPetOwnershipParagraph).add(newLineText);

        document.add(familyAndHousingParagraph);

        document.add(lineSeparator);

        // Other pets information paragraph
        Text otherPetsInformation = new Text("Other pets information").setBold().setFontSize(20);
        Paragraph otherPetsInformationParagraph = new Paragraph(otherPetsInformation).add(newLineText);

        Text euthanizedPetText = new Text("Euthanized pet: " + hadPetEuthanized);
        Paragraph euthanizedPetParagraph = new Paragraph(euthanizedPetText);

        Text euthanizedPetDetailsText = new Text("Euthanized pet details: " + hadPetEuthanizedDescription);
        Paragraph euthanizedPetDetailsParagraph = new Paragraph(euthanizedPetDetailsText);

        Text otherPetsDetailsText = new Text("Other owned pet details: " + ownedPetTypes);
        Paragraph otherPetsDetailsParagraph = new Paragraph(otherPetsDetailsText);

        Text disciplineModeText = new Text("How do you discipline your pets? " + disciplineMode);
        Paragraph disciplineModeParagraph = new Paragraph(disciplineModeText);

        Text surrenderedPetText = new Text("Surrendered pet: " + haveSurrenderedPet);
        Paragraph surrenderedPetParagraph = new Paragraph(surrenderedPetText);

        Text surrenderedPetDetailsText = new Text("Surrendered pet details: " + haveSurrenderedPetDescription);
        Paragraph surrenderedPetDetailsParagraph = new Paragraph(surrenderedPetDetailsText);

        Text vaccinesUpToDateText = new Text("Vaccines up to date: " + vaccinesUpToDate);
        Paragraph vaccinesUpToDateParagraph = new Paragraph(vaccinesUpToDateText);


        otherPetsInformationParagraph.add(euthanizedPetParagraph).add(newLineText)
                .add(euthanizedPetDetailsParagraph).add(newLineText)
                .add(otherPetsDetailsParagraph).add(newLineText)
                .add(disciplineModeParagraph).add(newLineText)
                .add(surrenderedPetParagraph).add(newLineText)
                .add(surrenderedPetDetailsParagraph).add(newLineText)
                .add(vaccinesUpToDateParagraph).add(newLineText);

        document.add(otherPetsInformationParagraph);

        document.add(lineSeparator);

        // Veterinarian information paragraph
        Text veterinarianInformation = new Text("Veterinarian information").setBold().setFontSize(20);
        Paragraph veterinarianInformationParagraph = new Paragraph(veterinarianInformation).add(newLineText);

        Text veterinarianClinicNameText = new Text("Veterinarian clinic name: " + veterinarianClinicName);
        Paragraph veterinarianClinicNameParagraph = new Paragraph(veterinarianClinicNameText);

        Text veterinarianClinicAddressText = new Text("Veterinarian clinic address: " + veterinarianClinicAddress);
        Paragraph veterinarianClinicAddressParagraph = new Paragraph(veterinarianClinicAddressText);

        Text veterinarianClinicPhoneNumberText = new Text("Veterinarian clinic phone number: " + veterinarianClinicPhoneNumber);
        Paragraph veterinarianClinicPhoneNumberParagraph = new Paragraph(veterinarianClinicPhoneNumberText);

        Text veterinarianNameText = new Text("Veterinarian name: " + veterinarianName);
        Paragraph veterinarianNameParagraph = new Paragraph(veterinarianNameText);

        Text veterinarianClinicEmailText = new Text("Veterinarian clinic email: " + veterinarianClinicEmail);
        Paragraph veterinarianClinicEmailParagraph = new Paragraph(veterinarianClinicEmailText);

        veterinarianInformationParagraph.add(veterinarianClinicNameParagraph).add(newLineText)
                .add(veterinarianClinicAddressParagraph).add(newLineText)
                .add(veterinarianClinicPhoneNumberParagraph).add(newLineText)
                .add(veterinarianNameParagraph).add(newLineText)
                .add(veterinarianClinicEmailParagraph).add(newLineText);

        document.add(veterinarianInformationParagraph);

        document.add(lineSeparator);

        // About the pet information paragraph
        Text aboutThePetInformation = new Text("About the pet information").setBold().setFontSize(20);
        Paragraph aboutThePetInformationParagraph = new Paragraph(aboutThePetInformation).add(newLineText);

        Text aloneHoursText = new Text("Time pet will spend alone: " + aloneHoursNumber);
        Paragraph aloneHoursParagraph = new Paragraph(aloneHoursText);

        Text contactForSurrenderText = new Text("If you can no longer keep the pet, will call us to take him and not to surrender the pet? " + contactForSurrender);
        Paragraph contactForSurrenderParagraph = new Paragraph(contactForSurrenderText);

        Text daytimePlaceText = new Text("Where will your pet spend the day? " + daytimePlaceDescription);
        Paragraph daytimePlaceParagraph = new Paragraph(daytimePlaceText);

        Text nighttimePlaceText = new Text("Where will your pet spend the night? " + nighttimePlaceDescription);
        Paragraph nighttimePlaceParagraph = new Paragraph(nighttimePlaceText);

        Text petNameText = new Text("Pet name: " + petName);
        Paragraph petNameParagraph = new Paragraph(petNameText);

        Text regularHealthCareText = new Text("Regular health care: " + regularHealthCare);
        Paragraph regularHealthCareParagraph = new Paragraph(regularHealthCareText);

        aboutThePetInformationParagraph.add(aloneHoursParagraph).add(newLineText)
                .add(contactForSurrenderParagraph).add(newLineText)
                .add(daytimePlaceParagraph).add(newLineText)
                .add(nighttimePlaceParagraph).add(newLineText)
                .add(petNameParagraph).add(newLineText)
                .add(regularHealthCareParagraph).add(newLineText);

        document.add(aboutThePetInformationParagraph);

        document.add(lineSeparator);

        // Personal references information paragraph
        Text personalReferencesInformation = new Text("Personal references information").setBold().setFontSize(20);
        Paragraph personalReferencesParagraph = new Paragraph(personalReferencesInformation).add(newLineText);

        Text referenceAddressText = new Text("Reference address: " + referenceAddress);
        Paragraph referenceAddressParagraph = new Paragraph(referenceAddressText);

        Text referenceNameText = new Text("Reference name: " + referenceName);
        Paragraph referenceNameParagraph = new Paragraph(referenceNameText);

        Text referencePhoneNumberText = new Text("Reference phone number: " + referencePhoneNumber);
        Paragraph referencePhoneNumberParagraph = new Paragraph(referencePhoneNumberText);

        Text referenceRelationshipText = new Text("Reference relationship: " + referenceRelationship);
        Paragraph referenceRelationshipParagraph = new Paragraph(referenceRelationshipText);

        Text referenceEmailText = new Text("Reference email: " + referenceEmail);
        Paragraph referenceEmailParagraph = new Paragraph(referenceEmailText);


        personalReferencesParagraph.add(referenceAddressParagraph).add(newLineText)
                .add(referenceNameParagraph).add(newLineText)
                .add(referencePhoneNumberParagraph).add(newLineText)
                .add(referenceRelationshipParagraph).add(newLineText)
                .add(referenceEmailParagraph).add(newLineText);

        document.add(personalReferencesParagraph);

        document.add(newLineParagraph);

        // Signature field
        Table signaturesTable = new Table(3);

        Border signatureBottomBorder = new SolidBorder(ColorConstants.BLACK, 1f);
        Paragraph signatureParagraph = new Paragraph();
        signatureParagraph.setBorderBottom(signatureBottomBorder).setWidth(100);

        Text applicantSignatureText = new Text("Applicant signature");
        Paragraph applicantSignatureParagraph = new Paragraph(applicantSignatureText).add(newLineText).add(newLineText);

        Text shelterAdministratorSignatureText = new Text("Shelter administrator signature");
        Paragraph shelterAdministratorSignatureParagraph = new Paragraph(shelterAdministratorSignatureText).add(newLineText).add(newLineText);

        Text shelterVeterinarianSignatureText = new Text("Shelter veterinarian signature");
        Paragraph shelterVeterinarianSignatureParagraph = new Paragraph(shelterVeterinarianSignatureText).add(newLineText).add(newLineText);

        applicantSignatureParagraph.add(signatureParagraph);
        shelterAdministratorSignatureParagraph.add(signatureParagraph);
        shelterVeterinarianSignatureParagraph.add(signatureParagraph);

        signaturesTable.addCell(getCell(applicantSignatureParagraph, TextAlignment.LEFT));
        signaturesTable.addCell(getCell(shelterAdministratorSignatureParagraph, TextAlignment.CENTER));
        signaturesTable.addCell(getCell(shelterVeterinarianSignatureParagraph, TextAlignment.RIGHT));

        document.add(signaturesTable);

        document.close();

        Toast.makeText(this, "Pdf created", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "PDF was created in Downloads directory", Toast.LENGTH_LONG).show();
    }

    private Cell getCell(Paragraph signatureParagraph, TextAlignment alignment) {
        Cell cell = new Cell().add(signatureParagraph);
        cell.setPadding(10);
        cell.setMargins(10,10,10,10);
        cell.setTextAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}