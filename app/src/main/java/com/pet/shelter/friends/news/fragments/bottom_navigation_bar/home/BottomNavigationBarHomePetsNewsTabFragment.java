package com.pet.shelter.friends.news.fragments.bottom_navigation_bar.home;

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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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

public class BottomNavigationBarHomePetsNewsTabFragment extends Fragment {

    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference shelterAdminReference, petsNewsArticlesReference;
    private String loggedUserId;
    private RelativeLayout addNewsRelativeLayout;
    private ListView listView;

    private final ArrayList<NewsArticleData> newsArticlesList = new ArrayList<>();
    private NewsArticlesCustomAdapter newsArticlesCustomAdapter;

    public BottomNavigationBarHomePetsNewsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_bottom_navigation_bar_home_pets_news_tab, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        shelterAdminReference = firebaseDatabase.getReference("shelterAdministrators");
        petsNewsArticlesReference = firebaseDatabase.getReference("newsArticles");

        listView = layout.findViewById(R.id.bottomNavigationBarHomePetsTabNews_listView);

        addNewsRelativeLayout = layout.findViewById(R.id.bottomNavigationBarHomePetsTabAddNews_relativeLayout);
        ExtendedFloatingActionButton addNewsExtendedFloatingActionButton = layout.findViewById(R.id.bottomNavigationBarHomePetsTabAddNews_extendedFloatingActionButton);

        loggedUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        shelterAdminReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(loggedUserId)) {
                    addNewsRelativeLayout.setVisibility(View.GONE);
                } else {
                    addNewsRelativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addNewsExtendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateNewsArticleActivity.class);
                startActivity(intent);
            }
        });

        getPetDataFromDatabase(loggedUserId);

        return layout;
    }

    private void getPetDataFromDatabase(String loggedUid) {

        petsNewsArticlesReference.child(loggedUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsArticlesList.clear();
                for (DataSnapshot newsArticleSnapshot : snapshot.getChildren()) {
                    NewsArticleData newsArticleData = newsArticleSnapshot.getValue(NewsArticleData.class);
                    newsArticlesList.add(newsArticleData);
                }

                newsArticlesCustomAdapter = new NewsArticlesCustomAdapter(getApplicationContext(),
                        R.layout.news_article_card,
                        newsArticlesList);
                newsArticlesCustomAdapter.notifyDataSetChanged();
                refresh();
                listView.setAdapter(newsArticlesCustomAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
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