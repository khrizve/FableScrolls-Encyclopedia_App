package com.example.fablescrolls;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View contentView;
    private View offlineView;
    private TextView factTextView;
    private TextView quoteTextView;
    private ImageButton refreshFactButton;
    private ImageButton refreshQuoteButton;
    private RequestQueue requestQueue;
    private static final String FACT_API_URL = "https://uselessfacts.jsph.pl/random.json?language=en";
    private static final String QUOTE_API_URL = "https://zenquotes.io/api/random";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        contentView = findViewById(R.id.contentView);
        offlineView = findViewById(R.id.offlineView);
        factTextView = findViewById(R.id.factTextView);
        quoteTextView = findViewById(R.id.quoteTextView);
        refreshFactButton = findViewById(R.id.refreshFactButton);
        refreshQuoteButton = findViewById(R.id.refreshQuoteButton);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Check internet connection
        if (!isNetworkAvailable()) {
            showOfflineView();
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        } else {
            showOnlineView();
            fetchRandomFact();
            fetchRandomQuote();
        }

        // Set click listener for menu button
        findViewById(R.id.imageButton).setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        // Set click listener for refresh buttons
        refreshFactButton.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                fetchRandomFact();
            } else {
                showOfflineView();
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        });

        refreshQuoteButton.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                fetchRandomQuote();
            } else {
                showOfflineView();
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        });

        // Setup navigation drawer
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Handle home click
                Toast.makeText(this, "Welcome to FableScrolls", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_explore) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_about) {
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
            }

            if (!isNetworkAvailable()) {
                showOfflineView();
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void fetchRandomFact() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FACT_API_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String fact = jsonObject.getString("text");
                        factTextView.setText(fact);
                    } catch (JSONException e) {
                        factTextView.setText(R.string.error_parsing_fact);
                        e.printStackTrace();
                    }
                },
                error -> factTextView.setText(R.string.error_fetching_fact));

        requestQueue.add(stringRequest);
    }

    private void fetchRandomQuote() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, QUOTE_API_URL,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String quote = jsonObject.getString("q");
                        String author = jsonObject.getString("a");
                        quoteTextView.setText(String.format("\"%s\"\n\n— %s", quote, author));
                    } catch (JSONException e) {
                        quoteTextView.setText(R.string.error_parsing_quote);
                        e.printStackTrace();
                    }
                },
                error -> quoteTextView.setText(R.string.error_fetching_quote));

        requestQueue.add(stringRequest);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showOfflineView() {
        contentView.setVisibility(View.GONE);
        offlineView.setVisibility(View.VISIBLE);
    }

    private void showOnlineView() {
        offlineView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
    }
}


