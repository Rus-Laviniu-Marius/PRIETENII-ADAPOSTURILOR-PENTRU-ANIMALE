package com.pet.shelter.friends.adoption;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
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
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.pet.shelter.friends.BuildConfig;
import com.pet.shelter.friends.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GenerateAdoptionPapersPDFActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String fullName, address, occupation, sittingTime, phoneNumber, emailAddress, bestTimeToCall,
            aloneHours, contactForSurrender, daytimePlace, nighttimePlace, regularHealthCare, petName,
            adultsNumber, childrenNumber, familyAgreement, homeDescription, homeType, knownAllergy, provideAdequateLoveAndAttention, rentingRulesRegardingPetOwnership,
            euthanizedPet, euthanizedPetDetails, otherPetsDetails, surrenderedPet, surrenderedPetDetails, vaccinesUpToDate,
            regularVeterinarian, veterinarianClinicName, veterinarianClinicAddress, veterinarianClinicPhoneNumber, veterinarianName,
            referenceAddress, referenceName, referencePhoneNumber, referenceRelationship;

    private Button generatePDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_adoption_papers_pdfactivity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        generatePDF = findViewById(R.id.generatePDF_button);

        readDataFromFirebase();

        generatePDF.setOnClickListener(new View.OnClickListener() {
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
        String currentFirebaseUserUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        databaseReference.child("contactInformation").child(currentFirebaseUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullName = Objects.requireNonNull(snapshot.child("fullName").getValue()).toString();
                occupation = Objects.requireNonNull(snapshot.child("occupation").getValue()).toString();
                address = Objects.requireNonNull(snapshot.child("address").getValue()).toString();
                sittingTime = Objects.requireNonNull(snapshot.child("sittingTime").getValue()).toString();
                phoneNumber = Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString();
                emailAddress = Objects.requireNonNull(snapshot.child("emailAddress").getValue()).toString();
                bestTimeToCall = Objects.requireNonNull(snapshot.child("bestTimeToCall").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("aboutPet").child(currentFirebaseUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aloneHours = Objects.requireNonNull(snapshot.child("aloneHours").getValue()).toString();
                contactForSurrender = Objects.requireNonNull(snapshot.child("contactForSurrender").getValue()).toString();
                daytimePlace = Objects.requireNonNull(snapshot.child("daytimePlace").getValue()).toString();
                nighttimePlace = Objects.requireNonNull(snapshot.child("nighttimePlace").getValue()).toString();
                petName = Objects.requireNonNull(snapshot.child("petName").getValue()).toString();
                regularHealthCare = Objects.requireNonNull(snapshot.child("regularHealthCare").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("familyAndHousing").child(currentFirebaseUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adultsNumber = Objects.requireNonNull(snapshot.child("adultsNumber").getValue()).toString();
                childrenNumber = Objects.requireNonNull(snapshot.child("childrenNumber").getValue()).toString();
                familyAgreement = Objects.requireNonNull(snapshot.child("familyAgreement").getValue()).toString();
                homeDescription = Objects.requireNonNull(snapshot.child("homeDescription").getValue()).toString();
                homeType = Objects.requireNonNull(snapshot.child("homeType").getValue()).toString();
                knownAllergy = Objects.requireNonNull(snapshot.child("knownAllergy").getValue()).toString();
                provideAdequateLoveAndAttention = Objects.requireNonNull(snapshot.child("provideAdequateLoveAndAttention").getValue()).toString();
                rentingRulesRegardingPetOwnership = Objects.requireNonNull(snapshot.child("rentingRulesRegardingPetOwnership").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("otherPets").child(currentFirebaseUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                euthanizedPet = Objects.requireNonNull(snapshot.child("euthanizedPet").getValue()).toString();
                euthanizedPetDetails = Objects.requireNonNull(snapshot.child("euthanizedPetDetails").getValue()).toString();
                otherPetsDetails = Objects.requireNonNull(snapshot.child("otherPetsDetails").getValue()).toString();
                surrenderedPet = Objects.requireNonNull(snapshot.child("surrenderedPet").getValue()).toString();
                surrenderedPetDetails = Objects.requireNonNull(snapshot.child("surrenderedPetDetails").getValue()).toString();
                vaccinesUpToDate = Objects.requireNonNull(snapshot.child("vaccinesUpToDate").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("regularVeterinarian").child(currentFirebaseUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                regularVeterinarian = Objects.requireNonNull(snapshot.child("regularVeterinarian").getValue()).toString();
                veterinarianClinicName = Objects.requireNonNull(snapshot.child("veterinarianClinicName").getValue()).toString();
                veterinarianClinicAddress = Objects.requireNonNull(snapshot.child("veterinarianClinicAddress").getValue()).toString();
                veterinarianClinicPhoneNumber = Objects.requireNonNull(snapshot.child("veterinarianClinicPhoneNumber").getValue()).toString();
                veterinarianName = Objects.requireNonNull(snapshot.child("veterinarianName").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("personalReference").child(currentFirebaseUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                referenceAddress = Objects.requireNonNull(snapshot.child("referenceAddress").getValue()).toString();
                referenceName = Objects.requireNonNull(snapshot.child("referenceName").getValue()).toString();
                referencePhoneNumber = Objects.requireNonNull(snapshot.child("referencePhoneNumber").getValue()).toString();
                referenceRelationship = Objects.requireNonNull(snapshot.child("referenceRelationship").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendPDFByEmail() {
        String mailTo="rusmarius0809@gmail.com";
        //create the send intent
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SENDTO);
        // File path
//                String fileName = "file://" + android.os.Environment.getExternalStorageDirectory() +"/" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + petAdoptionPapers + ".pdf";
//                Uri pdfUri = Uri.parse(fileName);
//                Context context = CompleteContactInformationActivity.this;
//                pdfUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", createPDF());
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "pet_adoption_papers.pdf");
        Uri pdfUri = FileProvider.getUriForFile(GenerateAdoptionPapersPDFActivity.this,
                BuildConfig.APPLICATION_ID + ".provider", file);
        //set the type
        shareIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
//                shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo});
        //add a subject
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
        //build the body of the message to be shared
        String shareMessage = getResources().getString(R.string.label_share_message);
        //add the message
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
        //add the attachment
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //start the chooser for sharing
        startActivity(Intent.createChooser(shareIntent, getResources().
                getString(R.string.label_chooser_title)));
    }

    private void createPDF() throws IOException {

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "pet_adoption_papers.pdf");

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        SolidLine solidLine = new SolidLine(1f);
        solidLine.setColor(ColorConstants.BLACK);
        LineSeparator lineSeparator = new LineSeparator(solidLine);
        lineSeparator.setWidth(500);
        lineSeparator.setMargins(10, 0,10,0);

        Text newLineText = new Text("\n");
        Paragraph newLineParagraph = new Paragraph(newLineText);

        Drawable d = AppCompatResources.getDrawable(GenerateAdoptionPapersPDFActivity.this, R.drawable.launcher_logo);
        assert d != null;
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        image.setHeight(100);
        image.setWidth(100);
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(image);

        Paragraph titleParagraph = new Paragraph();
        Text title = new Text("PET ADOPTION CERTIFICATE")
                .setBold()
                .setFontSize(26)
                .setUnderline()
                .setTextAlignment(TextAlignment.CENTER);

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

        Text sittingTimeText = new Text("How long at this address? " + sittingTime);
        Paragraph sittingTimeParagraph = new Paragraph(sittingTimeText);

        Text phoneNumberText = new Text("Phone number: " + phoneNumber);
        Paragraph phoneNumberParagraph = new Paragraph(phoneNumberText);

        Text emailAddressText = new Text("Email address: " + emailAddress);
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
        Text familyAndHousing = new Text("Family & Housing").setBold().setFontSize(20);
        Paragraph familyAndHousingParagraph = new Paragraph(familyAndHousing).add(newLineText);

        Text adultsNumberText = new Text("Adults number: " + adultsNumber);
        Paragraph adultsNumberParagraph = new Paragraph(adultsNumberText);

        Text childrenNumberText = new Text("Children number: " + childrenNumber);
        Paragraph childrenNumberParagraph = new Paragraph(childrenNumberText);

        Text familyAgreementText = new Text("Family agreement: " + familyAgreement);
        Paragraph familyAgreementParagraph = new Paragraph(familyAgreementText);

        Text homeDescriptionText = new Text("Home description: " + homeDescription);
        Paragraph homeDescriptionParagraph = new Paragraph(homeDescriptionText);

        Text homeTypeText = new Text("Home type: " + homeType);
        Paragraph homeTypeParagraph = new Paragraph(homeTypeText);

        Text knownAllergyText = new Text("Email address: " + knownAllergy);
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

        Text euthanizedPetText = new Text("Euthanized pet: " + euthanizedPet);
        Paragraph euthanizedPetParagraph = new Paragraph(euthanizedPetText);

        Text euthanizedPetDetailsText = new Text("Euthanized pet details: " + euthanizedPetDetails);
        Paragraph euthanizedPetDetailsParagraph = new Paragraph(euthanizedPetDetailsText);

        Text otherPetsDetailsText = new Text("Euthanized pet details: " + otherPetsDetails);
        Paragraph otherPetsDetailsParagraph = new Paragraph(otherPetsDetailsText);

        Text surrenderedPetText = new Text("Surrendered pet: " + surrenderedPet);
        Paragraph surrenderedPetParagraph = new Paragraph(surrenderedPetText);

        Text surrenderedPetDetailsText = new Text("Surrendered pet details: " + surrenderedPetDetails);
        Paragraph surrenderedPetDetailsParagraph = new Paragraph(surrenderedPetDetailsText);

        Text vaccinesUpToDateText = new Text("Vaccines up to date: " + vaccinesUpToDate);
        Paragraph vaccinesUpToDateParagraph = new Paragraph(vaccinesUpToDateText);


        otherPetsInformationParagraph.add(euthanizedPetParagraph).add(newLineText)
                .add(euthanizedPetDetailsParagraph).add(newLineText)
                .add(otherPetsDetailsParagraph).add(newLineText)
                .add(surrenderedPetParagraph).add(newLineText)
                .add(surrenderedPetDetailsParagraph).add(newLineText)
                .add(vaccinesUpToDateParagraph).add(newLineText);

        document.add(otherPetsInformationParagraph);

        document.add(lineSeparator);

        // Veterinarian information paragraph
        Text veterinarianInformation = new Text("Veterinarian information").setBold().setFontSize(20);
        Paragraph veterinarianInformationParagraph = new Paragraph(veterinarianInformation).add(newLineText);

        Text regularVeterinarianText = new Text("Do you have a regular veterinarian? " + regularVeterinarian);
        Paragraph regularVeterinarianParagraph = new Paragraph(regularVeterinarianText);

        Text veterinarianClinicNameText = new Text("Veterinarian clinic name: " + veterinarianClinicName);
        Paragraph veterinarianClinicNameParagraph = new Paragraph(veterinarianClinicNameText);

        Text veterinarianClinicAddressText = new Text("Veterinarian clinic address: " + veterinarianClinicAddress);
        Paragraph veterinarianClinicAddressParagraph = new Paragraph(veterinarianClinicAddressText);

        Text veterinarianClinicPhoneNumberText = new Text("Veterinarian clinic phone number: " + veterinarianClinicPhoneNumber);
        Paragraph veterinarianClinicPhoneNumberParagraph = new Paragraph(veterinarianClinicPhoneNumberText);

        Text veterinarianNameText = new Text("Veterinarian name: " + veterinarianName);
        Paragraph veterinarianNameParagraph = new Paragraph(veterinarianNameText);

        veterinarianInformationParagraph.add(regularVeterinarianParagraph).add(newLineText)
                .add(veterinarianClinicNameParagraph).add(newLineText)
                .add(veterinarianClinicAddressParagraph).add(newLineText)
                .add(veterinarianClinicPhoneNumberParagraph).add(newLineText)
                .add(veterinarianNameParagraph).add(newLineText);

        document.add(veterinarianInformationParagraph);

        document.add(lineSeparator);

        // About the pet information paragraph
        Text aboutThePetInformation = new Text("About the pet information").setBold().setFontSize(20);
        Paragraph aboutThePetInformationParagraph = new Paragraph(aboutThePetInformation).add(newLineText);

        Text aloneHoursText = new Text("Time pet will spend alone: " + aloneHours);
        Paragraph aloneHoursParagraph = new Paragraph(aloneHoursText);

        Text contactForSurrenderText = new Text("If you can no longer keep the pet, will call us to take him and not to surrender the pet? " + contactForSurrender);
        Paragraph contactForSurrenderParagraph = new Paragraph(contactForSurrenderText);

        Text daytimePlaceText = new Text("Where will your pet spend the day? " + daytimePlace);
        Paragraph daytimePlaceParagraph = new Paragraph(daytimePlaceText);

        Text nighttimePlaceText = new Text("Where will your pet spend the night? " + nighttimePlace);
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

        Text referenceRelationshipText = new Text("Where will your pet spend the night? " + referenceRelationship);
        Paragraph referenceRelationshipParagraph = new Paragraph(referenceRelationshipText);

        personalReferencesParagraph.add(referenceAddressParagraph).add(newLineText)
                .add(referenceNameParagraph).add(newLineText)
                .add(referencePhoneNumberParagraph).add(newLineText)
                .add(referenceRelationshipParagraph).add(newLineText);

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

//        signaturesTable.getCell(1,1).setPadding(0).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER);
//        signaturesTable.getCell(1,2).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
//        signaturesTable.getCell(1,3).setPadding(0).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER);

        document.add(signaturesTable);

//        Border border = new GrooveBorder(2);

        // PARAGRAPHS
//        Paragraph paragraph = new Paragraph("Thank you!");
//
//        Text text1 = new Text("Bold ").setBold();
//        Text text2 = new Text("Italic ").setItalic();
//        Text text3 = new Text("Underline ").setUnderline();
//
//        Paragraph paragraph1 = new Paragraph();
//        paragraph1.add(text1)
//                .add(text2)
//                .add(text3);
//
//        document.add(paragraph);
//        document.add(paragraph1);

        // LISTS
//        List list = new List();
//        list.add("Android");
//        list.add("Java");
//        list.add("C++");
//        list.add("Kotlin");
//
//        document.add(list);

        // IMAGES
//        Drawable d = getDrawable(R.drawable.launcher_logo);
//        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100,byteArrayOutputStream);
//        byte[] bitmapData = byteArrayOutputStream.toByteArray();
//
//        ImageData imageData = ImageDataFactory.create(bitmapData);
//        Image image = new Image(imageData);

//        image.setHeight(100);
//        image.setWidth(100);
//
//        document.add(image);

        // TABLES
//        float[] columnWidth = {200f, 200f};
//        Table table = new Table(columnWidth);
//        table.addCell("Name");
//        table.addCell("Age");

//        table.addCell(new Cell(2,1).add(new Paragraph("Laviniu")));
//        table.addCell(new Cell().setBackgroundColor(ColorConstants.GREEN).add(new Paragraph("Laviniu")));
//        table.addCell(new Cell().setBackgroundColor(new DeviceRgb(244, 23, 213)).add(new Paragraph("Laviniu")));
//        table.addCell("23");
//
//        table.addCell("Marius");
//        table.addCell("23");
//
//        table.setBorder(border);
//
//        document.add(table);

        // CHANGE LIST SYMBOL
//        List list = new List();
//        list.setListSymbol("\u00A5 ");

//        Drawable d = getDrawable(R.drawable.yellow_tick_32);
//        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] bitmapData = byteArrayOutputStream.toByteArray();
//
//        ImageData imageData = ImageDataFactory.create(bitmapData);
//        Image image = new Image(imageData);

//        image.setHeight("15");
//        image.setWeight("15");
//
//        list.setListSymbol(image);
//
//        list.add("Apple");
//        list.add("Banana");
//        list.add("Strawberry");
//        list.add("Blueberry");
//        list.add("Peach");
//
//        document.add(list);

//        BarcodeQRCode qrCode = new BarcodeQRCode("Hello Laviniu");
//        BarcodeQRCode qrCode = new BarcodeQRCode("https://www.google.com/");
//        BarcodeQRCode qrCode = new BarcodeQRCode("mailto:rusmarius0809@gmail.com?subject=Pet%20Adoption%20Papers%body=Please check the pet adoption papers");
//        PdfFormXObject barcodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
//        Image barcodeImage = new Image(barcodeObject).setWidth(100f).setHeight(100f);

//        document.add(barcodeImage);

        document.close();

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
}