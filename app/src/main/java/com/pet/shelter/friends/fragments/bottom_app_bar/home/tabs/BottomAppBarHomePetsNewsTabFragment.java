package com.pet.shelter.friends.fragments.bottom_app_bar.home.tabs;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pet.shelter.friends.R;
import com.pet.shelter.friends.news.CreateNewsArticleActivity;
import com.pet.shelter.friends.news.NewsArticleData;
import com.pet.shelter.friends.news.NewsArticleDetailsActivity;
import com.pet.shelter.friends.news.NewsArticlesCustomAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class BottomAppBarHomePetsNewsTabFragment extends Fragment {

    private DatabaseReference petsNewsArticlesReference, roles;
    private String loggedUserId;
    private RelativeLayout addNewsRelativeLayout, petsRelativeLayout;
    private MaterialTextView materialTextView;
    private ListView listView;

    private final ArrayList<NewsArticleData> newsArticlesList = new ArrayList<>();
    private NewsArticlesCustomAdapter newsArticlesCustomAdapter;

    public BottomAppBarHomePetsNewsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_home_pets_news_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        roles = firebaseDatabase.getReference("roles");
        petsNewsArticlesReference = firebaseDatabase.getReference("newsArticles");

        listView = layout.findViewById(R.id.bottomAppBarHomePetsTabNews_listView);

        addNewsRelativeLayout = layout.findViewById(R.id.bottomAppBarHomePetsTabAddNews_relativeLayout);
        petsRelativeLayout = layout.findViewById(R.id.bottomAppBarHomePets_relativeLayout);
        materialTextView = layout.findViewById(R.id.bottomAppBarHomePetsNoNews_materialTextView);
        ExtendedFloatingActionButton addNewsExtendedFloatingActionButton = layout.findViewById(R.id.bottomAppBarHomePetsTabAddNews_extendedFloatingActionButton);

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        getDataFromDatabase();

        addNewsExtendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateNewsArticleActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsArticleData newsArticleData = newsArticlesCustomAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), NewsArticleDetailsActivity.class);
                intent.putExtra("authorName", newsArticleData.getAuthorName());
                intent.putExtra("description", newsArticleData.getDescription());
                intent.putExtra("subcategory", newsArticleData.getSubcategory());
                intent.putExtra("title", newsArticleData.getTitle());
                intent.putExtra("mediaImageDownloadLink", newsArticleData.getMediaImageDownloadLink());
                intent.putExtra("newsArticleAuthorProfileImage", newsArticleData.getNewsArticleAuthorProfileImage());
                intent.putExtra("category", newsArticleData.getCategory());
                startActivity(intent);
            }
        });


        return layout;
    }

    private void getDataFromDatabase() {
        petsNewsArticlesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Pets")) {
                    petsRelativeLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    newsArticlesList.clear();
                    for (DataSnapshot newsArticleSnapshot : snapshot.child("Pets").getChildren()) {
                        NewsArticleData newsArticleData = newsArticleSnapshot.getValue(NewsArticleData.class);
                        newsArticlesList.add(newsArticleData);
                    }

                    newsArticlesCustomAdapter = new NewsArticlesCustomAdapter(getApplicationContext(),
                            R.layout.news_article_card,
                            newsArticlesList);
                    newsArticlesCustomAdapter.notifyDataSetChanged();
                    refresh();
                    listView.setAdapter(newsArticlesCustomAdapter);
                } else {
                    listView.setVisibility(View.GONE);
                    addNewsRelativeLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });

        roles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(loggedUserId).hasChild("shelterAdministrator")) {
                    addNewsRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    addNewsRelativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void refresh() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                newsArticlesCustomAdapter.notifyDataSetChanged();
                listView.invalidate();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}