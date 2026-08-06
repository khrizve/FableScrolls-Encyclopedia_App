package com.example.fablescrolls;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SubCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        String category = getIntent().getStringExtra("CATEGORY");
        int imageRes = getIntent().getIntExtra("IMAGE_RES", 0);

        // Set category image
        ImageView categoryImage = findViewById(R.id.imageView2);
        categoryImage.setImageResource(imageRes);

        // Initialize buttons
        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);
        Button btn4 = findViewById(R.id.button4);
        Button btn5 = findViewById(R.id.button5);

        // Set button texts based on category
        setButtonTexts(category, btn1, btn2, btn3, btn4, btn5);

        // Set click listeners
        btn1.setOnClickListener(v -> openContentActivity(btn1.getText().toString()));
        btn2.setOnClickListener(v -> openContentActivity(btn2.getText().toString()));
        btn3.setOnClickListener(v -> openContentActivity(btn3.getText().toString()));
        btn4.setOnClickListener(v -> openContentActivity(btn4.getText().toString()));
        btn5.setOnClickListener(v -> openContentActivity(btn5.getText().toString()));
    }

    private void setButtonTexts(String category, Button... buttons) {
        switch (category) {
            case "ইতিহাস":
                buttons[0].setText("প্রাচীন ইতিহাস");
                buttons[1].setText("মধ্যযুগের ইতিহাস");
                buttons[2].setText("বাংলার ইতিহাস");
                buttons[3].setText("বিশ্ব ইতিহাস");
                buttons[4].setText("ধর্মীয় ইতিহাস");
                break;
            case "বিজ্ঞান":
                buttons[0].setText("ভৌত বিজ্ঞান");
                buttons[1].setText("রসায়ন");
                buttons[2].setText("জীববিজ্ঞান");
                buttons[3].setText("জ্যোতির্বিদ্যা");
                buttons[4].setText("পৃথিবী বিজ্ঞান");
                break;
            case "প্রযুক্তি":
                buttons[0].setText("কৃত্রিম বুদ্ধিমত্তা");
                buttons[1].setText("রোবটিকস");
                buttons[2].setText("সফটওয়্যার উন্নয়ন");
                buttons[3].setText("কম্পিউটার বিজ্ঞান");
                buttons[4].setText("ব্লকচেইন");
                break;
            case "কৃষি":
                buttons[0].setText("ফসল ব্যবস্থাপনা");
                buttons[1].setText("টেকসই চাষাবাদ");
                buttons[2].setText("কৃষি প্রযুক্তি");
                buttons[3].setText("পশুপালন");
                buttons[4].setText("মাটি বিজ্ঞান");
                break;
            case "প্রাণীজগৎ":
                buttons[0].setText("স্তন্যপায়ী প্রাণী");
                buttons[1].setText("পাখি");
                buttons[2].setText("মেরিন জীবন");
                buttons[3].setText("পোকামাকড়");
                buttons[4].setText("বিপন্ন প্রজাতি");
                break;
            case "ভূগোল":
                buttons[0].setText("মহাদেশ এবং দেশ");
                buttons[1].setText("মানচিত্র");
                buttons[2].setText("প্রাকৃতিক বিস্ময়");
                buttons[3].setText("নগর উন্নয়ন");
                buttons[4].setText("আবহাওয়া");
                break;
            case "পরিবেশ ও প্রকৃতি":
                buttons[0].setText("জলবায়ু পরিবর্তন");
                buttons[1].setText("প্রাকৃতিক সম্পদ");
                buttons[2].setText("বনজ ও প্রাণিজ সম্পদ");
                buttons[3].setText("দূষণ");
                buttons[4].setText("পরিবেশ বান্ধব জীবনধারা");
                break;
            case "ভ্রমণ ও পর্যটন":
                buttons[0].setText("বিশ্বের বিখ্যাত স্থান");
                buttons[1].setText("বাংলাদেশের পর্যটন");
                buttons[2].setText("ভ্রমণ টিপস");
                buttons[3].setText("ঐতিহাসিক ভ্রমণ");
                buttons[4].setText("অ্যাডভেঞ্চার ট্যুরিজম");
                break;
            case "সংস্কৃতি":
                buttons[0].setText("ঐতিহ্য");
                buttons[1].setText("ভাষা");
                buttons[2].setText("শিল্প");
                buttons[3].setText("সঙ্গীত");
                buttons[4].setText("উৎসব");
                break;
            case "বিনোদন":
                buttons[0].setText("চলচ্চিত্র এবং টিভি শো");
                buttons[1].setText("সঙ্গীত");
                buttons[2].setText("গেমস");
                buttons[3].setText("সেলিব্রিটি");
                buttons[4].setText("বই এবং সাহিত্য");
                break;
            case "মনোবিজ্ঞান":
                buttons[0].setText("মনোবিজ্ঞানের শাখাগুলি");
                buttons[1].setText("মানসিক স্বাস্থ্য");
                buttons[2].setText("মানব আচরণ");
                buttons[3].setText("ব্যক্তিত্ব ও বৈশিষ্ট্য");
                buttons[4].setText("স্বপ্ন ও অবচেতন মন");
                break;
            case "রহস্য ও অলৌকিক":
                buttons[0].setText("ভূত ও আত্মার গল্প");
                buttons[1].setText("অলৌকিক স্থান");
                buttons[2].setText("প্রাচীন রহস্য");
                buttons[3].setText("ছায়া সংগঠন ও ষড়যন্ত্র");
                buttons[4].setText("অজানা প্রাণী");
                break;

            // Add all 12 cases similarly
            default:
                buttons[0].setText("উপশ্রেণী ১");
                buttons[1].setText("উপশ্রেণী ২");
                buttons[2].setText("উপশ্রেণী ৩");
                buttons[3].setText("উপশ্রেণী ৪");
                buttons[4].setText("উপশ্রেণী ৫");
        }
    }

    private void openContentActivity(String subCategory) {
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("SUBCATEGORY", subCategory);
        startActivity(intent);
    }
}
