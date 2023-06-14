package com.pet.shelter.friends.news.fragments.bottom_app_bar.home.tabs;

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
import com.pet.shelter.friends.news.NewsArticlesCustomAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class BottomAppBarHomeSupportNewsTabFragment extends Fragment {

    private DatabaseReference supportNewsArticlesReference, roles;
    private String loggedUserId;
    private RelativeLayout addNewsRelativeLayout, supportRelativeLayout;
    private MaterialTextView materialTextView;
    private ListView listView;

    private final ArrayList<NewsArticleData> newsArticlesList = new ArrayList<>();
    private NewsArticlesCustomAdapter newsArticlesCustomAdapter;

    public BottomAppBarHomeSupportNewsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_app_bar_home_support_news_tab, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        roles = firebaseDatabase.getReference("roles");
        supportNewsArticlesReference = firebaseDatabase.getReference("newsArticles");

        listView = layout.findViewById(R.id.bottomAppBarHomeSupportTabNews_listView);

        addNewsRelativeLayout = layout.findViewById(R.id.bottomAppBarHomeSupportTabAddNews_relativeLayout);
        supportRelativeLayout = layout.findViewById(R.id.bottomAppBarHomeSupport_relativeLayout);
        materialTextView = layout.findViewById(R.id.bottomAppBarHomeSupportNoNews_materialTextView);
        ExtendedFloatingActionButton addNewsExtendedFloatingActionButton = layout.findViewById(R.id.bottomAppBarHomeSupportTabAddNews_extendedFloatingActionButton);

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

            }
        });


        return layout;
    }

    private void getDataFromDatabase() {
        supportNewsArticlesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Support")) {
                    supportRelativeLayout.setVisibility(View.VISIBLE);
                    materialTextView.setVisibility(View.GONE);

                    newsArticlesList.clear();
                    for (DataSnapshot newsArticleSnapshot : snapshot.child("Support").getChildren()) {
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
                    supportRelativeLayout.setVisibility(View.GONE);
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
}