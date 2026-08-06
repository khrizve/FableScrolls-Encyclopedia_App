package com.example.fablescrolls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Search functionality
        setupSearchView();

        // Navigation button
        setupNavButton();

        // All 12 category buttons
        setupAllCategoryButtons();
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        ImageButton searchBtn = findViewById(R.id.imageButton2);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchWikipedia(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchBtn.setOnClickListener(v -> {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty()) {
                searchWikipedia(query);
            }
        });
    }

    private void searchWikipedia(String query) {
        Intent intent = new Intent(this, WikipediaActivity.class);
        intent.putExtra("QUERY", query);
        startActivity(intent);
    }

    private void setupNavButton() {
        ImageButton navBtn = findViewById(R.id.imageButton3);
        navBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, NewCategoryActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void setupAllCategoryButtons() {
        // Button 1: ইতিহাস (History)
        findViewById(R.id.button1).setOnClickListener(v ->
                openSubCategory("ইতিহাস", R.drawable.history_image));

        // Button 2: বিজ্ঞান (Science)
        findViewById(R.id.button2).setOnClickListener(v ->
                openSubCategory("বিজ্ঞান", R.drawable.science_image));

        // Button 3: প্রযুক্তি (Technology)
        findViewById(R.id.button3).setOnClickListener(v ->
                openSubCategory("প্রযুক্তি", R.drawable.technology_image));

        // Button 4: কৃষি (Agriculture)
        findViewById(R.id.button4).setOnClickListener(v ->
                openSubCategory("কৃষি", R.drawable.agriculture_image));

        // Button 5: প্রাণীজগৎ (Animal Kingdom)
        findViewById(R.id.button5).setOnClickListener(v ->
                openSubCategory("প্রাণীজগৎ", R.drawable.animal_image));

        // Button 6: ভূগোল (Geography)
        findViewById(R.id.button6).setOnClickListener(v ->
                openSubCategory("ভূগোল", R.drawable.geography_image));

        // Button 7: পরিবেশ ও প্রকৃতি (Environment)
        findViewById(R.id.button7).setOnClickListener(v ->
                openSubCategory("পরিবেশ ও প্রকৃতি", R.drawable.environment_image));

        // Button 8: ভ্রমণ ও পর্যটন (Travel)
        findViewById(R.id.button8).setOnClickListener(v ->
                openSubCategory("ভ্রমণ ও পর্যটন", R.drawable.travel_image));

        // Button 9: সংস্কৃতি (Culture)
        findViewById(R.id.button9).setOnClickListener(v ->
                openSubCategory("সংস্কৃতি", R.drawable.culture_image));

        // Button 10: বিনোদন (Entertainment)
        findViewById(R.id.button10).setOnClickListener(v ->
                openSubCategory("বিনোদন", R.drawable.entertainment_image));

        // Button 11: মনোবিজ্ঞান (Psychology)
        findViewById(R.id.button11).setOnClickListener(v ->
                openSubCategory("মনোবিজ্ঞান", R.drawable.psychology_image));

        // Button 12: রহস্য ও অলৌকিক (Mystery)
        findViewById(R.id.button12).setOnClickListener(v ->
                openSubCategory("রহস্য ও অলৌকিক", R.drawable.mystery_image));
    }

    private void openSubCategory(String category, int imageRes) {
        Intent intent = new Intent(this, SubCategoryActivity.class);
        intent.putExtra("CATEGORY", category);
        intent.putExtra("IMAGE_RES", imageRes);
        startActivity(intent);
    }
}