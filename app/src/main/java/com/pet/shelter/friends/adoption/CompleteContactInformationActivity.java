package com.pet.shelter.friends.adoption;


import static android.content.Intent.EXTRA_SUBJECT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.pet.shelter.friends.BuildConfig;
import com.pet.shelter.friends.R;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CompleteContactInformationActivity extends AppCompatActivity {

    private EditText fullNameEditText, occupationEditText, addressEditText;
    private Button submitButton;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_contact_information);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("contact_information");


        fullNameEditText = findViewById(R.id.completeContactInformationFullName_editText);
        occupationEditText = findViewById(R.id.completeContactInformationOccupation_editText);
        addressEditText = findViewById(R.id.completeContactInformationAddress_editText);
        submitButton = findViewById(R.id.completeContactInformationSubmit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                myRef.child("full_name").setValue(fullName);
//                myRef.child("occupation").setValue(occupation);
//                myRef.child("address").setValue(address);

                try {
                    createPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

//                Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
//                startActivity(intent);

//                Intent sendEmail= new Intent(Intent.ACTION_SEND);
//                sendEmail.setType("pdf");
//                sendEmail.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new
//                        File("downloads/pet_adoption_papers.pdf")));
//                startActivity(Intent.createChooser(sendEmail, "Email:"));

                String mailTo="rusmarius0809@gmail.com";
//                Intent email_intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",mailTo, null));
//                email_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pet adoption papers");
//                email_intent.putExtra(android.content.Intent.EXTRA_TEXT,"Completed adoption papers");
//                startActivity(Intent.createChooser(email_intent, "Send email..."));

//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo});
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pet adoption papers");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "Completed adoption papers");
//                if (emailIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(emailIntent);
//                }

//                Uri uri = generateFile(context);
//                Uri uri;
//                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO);
//                emailIntent.setType("application/pdf");
//
//                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo});
//                emailIntent.putExtra(EXTRA_SUBJECT, "Send something");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "You receive attachment");
//                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                startActivity(emailIntent);

                String petAdoptionPapers = "pet_adoption_papers";
                //create the send intent
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                // File path
//                String fileName = "file://" + android.os.Environment.getExternalStorageDirectory() +
//                        "/" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + petAdoptionPapers + ".pdf";
//                Uri pdfUri = Uri.parse(fileName);
                //                    pdfUri = FileProvider.getUriForFile(context,
//                            context.getApplicationContext().getPackageName() + ".provider", createPDF());
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                        "pet_adoption_papers.pdf");
                Uri pdfUri = FileProvider.getUriForFile(CompleteContactInformationActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider", file);
                //set the type
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo});
                //add a subject
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, petAdoptionPapers);
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
        });
    }

    private void createPDF() throws FileNotFoundException {
        String fullName = fullNameEditText.getText().toString().trim(),
                occupation = occupationEditText.getText().toString().trim(),
                address = addressEditText.getText().toString().trim();


        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "pet_adoption_papers.pdf");

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        Drawable d = getDrawable(R.drawable.launcher_logo);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        image.setHeight(300);
        image.setWidth(300);
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(image);

        Text title = new Text("PET ADOPTION CERTIFICATE").setBold();
        Paragraph paragraph = new Paragraph();
        paragraph.add(title);
        document.add(paragraph);

        Paragraph paragraph1 = new Paragraph();
        Text text = new Text("This is to certify that\n")
                .setFontSize(20);
        Text text1 = new Text(fullName+"\n")
                .setFontSize(60)
                .setBackgroundColor(ColorConstants.GREEN)
                .setTextAlignment(TextAlignment.CENTER);
        Text text2 = new Text("who is a " + occupation + " and who lives in " + address + "\n");
        Text text3 = new Text("has adopted\n")
                .setFontSize(20);
        Text text4 = new Text("Papeto")
                .setFontSize(60)
                .setTextAlignment(TextAlignment.CENTER);
        paragraph1.add(text).add(text1).add(text2).add(text3).add(text4);

        document.add(paragraph1);


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

        Toast.makeText(this, "Pdf created", Toast.LENGTH_LONG).show();

//        return file;
    }

}