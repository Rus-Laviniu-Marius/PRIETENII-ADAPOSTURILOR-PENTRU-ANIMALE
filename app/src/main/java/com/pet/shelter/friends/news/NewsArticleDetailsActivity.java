package com.pet.shelter.friends.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.pet.shelter.friends.HomeActivity;
import com.pet.shelter.friends.R;
import com.squareup.picasso.Picasso;

public class NewsArticleDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article_details);

        MaterialToolbar materialToolbar = findViewById(R.id.newsArticleDetails_materialToolbar);
        ShapeableImageView mediaImage = findViewById(R.id.newsArticleDetailsMedia_shapeImageView);
        ShapeableImageView authorProfileImage = findViewById(R.id.newsArticleDetailsAuthorProfile_shapeImageView);
        MaterialTextView title = findViewById(R.id.newsArticleDetailsTitle_materialTextView);
        MaterialTextView subcategory = findViewById(R.id.newsArticleDetailsSubcategory_materialTextView);
        MaterialTextView description = findViewById(R.id.newsArticleDetailsDescription_materialTextView);
        MaterialTextView authorName = findViewById(R.id.newsArticleDetailsAuthorName_materialTextView);

        String authorNameToDisplay = getIntent().getStringExtra("authorName");
        String descriptionToDisplay = getIntent().getStringExtra("description");
        String subcategoryToDisplay = getIntent().getStringExtra("subcategory");
        String titleToDisplay = getIntent().getStringExtra("title");
        String mediaImageToDisplay = getIntent().getStringExtra("mediaImageDownloadLink");
        String authorProfileImageToDisplay = getIntent().getStringExtra("newsArticleAuthorProfileImage");
        String categoryToSend = getIntent().getStringExtra("category");
        authorName.setText(authorNameToDisplay);
        description.setText(descriptionToDisplay);
        subcategory.setText(subcategoryToDisplay);
        title.setText(titleToDisplay);
        Picasso.get().load(mediaImageToDisplay).into(mediaImage);
        Picasso.get().load(authorProfileImageToDisplay).into(authorProfileImage);

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {
                    String message = '\"' + titleToDisplay + '\"' + " is related to "
                            + categoryToSend + " and " + subcategoryToDisplay + " . Here is what " + authorNameToDisplay
                            + " said: "+ descriptionToDisplay;
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }

                return true;
            }
        });
    }
}