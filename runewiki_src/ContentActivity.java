package com.example.fablescrolls;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ContentActivity extends AppCompatActivity {

    private static final String TAG = "PDFViewer";
    private PDFView pdfView;
    private Button btnDelete;
    private View fantasyProgressContainer;
    private TextView tvProgressText;
    private TextView tvSubCategoryTitle;
    private View offlineView;
    private String currentSubCategory;
    private String pdfUrl;
    private AppDatabase db;
    private Map<String, String> pdfLinksMap = new HashMap<>();
    private boolean isAfterDeletion = false; // New flag to track deletion state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        pdfView = findViewById(R.id.pdfView);
        btnDelete = findViewById(R.id.btnDelete);
        fantasyProgressContainer = findViewById(R.id.fantasyProgressContainer);
        tvProgressText = findViewById(R.id.tvProgressText);
        tvSubCategoryTitle = findViewById(R.id.tvSubCategoryTitle);
        offlineView = findViewById(R.id.offlineView);
        currentSubCategory = getIntent().getStringExtra("SUBCATEGORY");

        tvSubCategoryTitle.setText(currentSubCategory);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "info-library").build();

        initializePdfLinks();
        pdfUrl = pdfLinksMap.get(currentSubCategory);

        btnDelete.setOnClickListener(v -> deleteOfflinePdf());

        checkAndLoadPdf();
    }

    private void initializePdfLinks() {
        pdfLinksMap.put("প্রাচীন ইতিহাস", "https://drive.google.com/uc?export=download&id=1yQGgvRhveuTKxqrRDG5Sp_-mwuIanNdI");
        pdfLinksMap.put("মধ্যযুগের ইতিহাস", "https://drive.google.com/uc?export=download&id=1D_pjkjNcm4Ftx0J4bHSeDi9puQsDQZvA");
        pdfLinksMap.put("বাংলার ইতিহাস", "https://drive.google.com/uc?export=download&id=1zVaTTGsrVTtpxAx3T2aClqAITuhGYgTT");
        pdfLinksMap.put("বিশ্ব ইতিহাস", "https://drive.google.com/uc?export=download&id=1c-BUiEgMN-GwguGtTQgrpAk07SRamRbH");
        pdfLinksMap.put("ধর্মীয় ইতিহাস", "https://drive.google.com/uc?export=download&id=1q-LBmDPXin99ap1dcipBE1fF1eZV6RtZ");
    }

    private void checkAndLoadPdf() {
        if (pdfUrl == null) {
            Toast.makeText(this, "Knowledge not available for this category", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showFantasyProgress("Unraveling ancient knowledge...");

        new Thread(() -> {
            PDFEntity pdfEntity = db.pdfDao().getPDFBySubCategory(currentSubCategory);
            runOnUiThread(() -> {
                if (pdfEntity != null && new File(pdfEntity.filePath).exists()) {
                    displayPdfFromFile(pdfEntity.filePath);
                    btnDelete.setVisibility(View.VISIBLE);
                    hideFantasyProgress();
                } else {
                    if (isNetworkAvailable()) {
                        displayPdfFromUrl(pdfUrl);
                        if (!isAfterDeletion) { // Only download if not after deletion
                            new DownloadPdfTask().execute(pdfUrl);
                        }
                    } else {
                        showOfflineView();
                        hideFantasyProgress();
                        Toast.makeText(this,
                                "No offline scroll available. Connect to internet.",
                                Toast.LENGTH_LONG).show();
                    }
                }
                isAfterDeletion = false; // Reset the flag after load attempt
            });
        }).start();
    }

    private void showOfflineView() {
        pdfView.setVisibility(View.GONE);
        offlineView.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.GONE);
    }

    private void hideOfflineView() {
        offlineView.setVisibility(View.GONE);
        pdfView.setVisibility(View.VISIBLE);
    }

    private void showFantasyProgress(String message) {
        runOnUiThread(() -> {
            fantasyProgressContainer.setVisibility(View.VISIBLE);
            tvProgressText.setText(message);
        });
    }

    private void updateFantasyProgress(String message) {
        runOnUiThread(() -> tvProgressText.setText(message));
    }

    private void hideFantasyProgress() {
        runOnUiThread(() -> fantasyProgressContainer.setVisibility(View.GONE));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void displayPdfFromUrl(String url) {
        Log.d(TAG, "Loading Knowledge from The Library: " + url);
        hideOfflineView();
        pdfView.fromUri(Uri.parse(url))
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onLoad(nbPages -> {
                    Log.d(TAG, "Knowledge gathered successfully: " + nbPages + " pages");
                    hideFantasyProgress();
                    if (isAfterDeletion) {
                        // Skip the "Gathering knowledge" toast after deletion
                        return;
                    }
                    Toast.makeText(this, "Gathering knowledge... Please wait", Toast.LENGTH_SHORT).show();
                })
                .onError(t -> {
                    Log.e(TAG, "Knowledge gathering error: " + t.getMessage());
                    hideFantasyProgress();
                    if (!isAfterDeletion) {
                        Toast.makeText(this, "Gathering knowledge... Please wait", Toast.LENGTH_SHORT).show();
                    }
                })
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .enableAntialiasing(true)
                .spacing(10)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
    }

    private void displayPdfFromFile(String filePath) {
        Log.d(TAG, "Loading scrolls from archive: " + filePath);
        hideOfflineView();
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onLoad(nbPages -> {
                    Log.d(TAG, "Knowledge loaded successfully: " + nbPages + " pages");
                    hideFantasyProgress();
                })
                .onError(t -> {
                    Log.e(TAG, "knowledge load error: " + t.getMessage());
                    hideFantasyProgress();
                    Toast.makeText(this, "Gathering knowledge... Please wait", Toast.LENGTH_SHORT).show();
                })
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .enableAntialiasing(true)
                .spacing(10)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
    }

    private void deleteOfflinePdf() {
        showFantasyProgress("Burning old scrolls...");
        isAfterDeletion = true; // Set the flag when deletion starts

        new Thread(() -> {
            PDFEntity pdfEntity = db.pdfDao().getPDFBySubCategory(currentSubCategory);
            if (pdfEntity != null) {
                boolean deleted = new File(pdfEntity.filePath).delete();
                db.pdfDao().deleteBySubCategory(currentSubCategory);

                runOnUiThread(() -> {
                    hideFantasyProgress();
                    btnDelete.setVisibility(View.GONE);

                    if (deleted) {
                        // First toast
                        Toast.makeText(ContentActivity.this,
                                "Offline scroll deleted",
                                Toast.LENGTH_SHORT).show();

                        // Second toast after a delay
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Toast.makeText(ContentActivity.this,
                                    "Step back and re-enter to refresh the realm",
                                    Toast.LENGTH_LONG).show();

                            if (isNetworkAvailable()) {
                                displayPdfFromUrl(pdfUrl);
                            } else {
                                showOfflineView();
                            }
                        }, 1000); // 1 second delay
                    } else {
                        Toast.makeText(ContentActivity.this,
                                "Failed to delete scroll",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnUiThread(() -> {
                    hideFantasyProgress();
                    Toast.makeText(ContentActivity.this,
                            "No offline scroll found",
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private class DownloadPdfTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            showFantasyProgress("Summoning ancient wisdom...");
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                publishProgress("Deciphering runes...");
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                File directory = getFilesDir();
                File pdfFile = new File(directory, currentSubCategory + ".pdf");

                publishProgress("Gathering knowledge fragments...");
                InputStream input = new BufferedInputStream(connection.getInputStream());
                FileOutputStream output = new FileOutputStream(pdfFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                output.close();
                input.close();

                publishProgress("Binding the tome...");
                PDFEntity pdfEntity = new PDFEntity(
                        currentSubCategory,
                        pdfFile.getAbsolutePath(),
                        urls[0]);

                db.pdfDao().insert(pdfEntity);

                return true;
            } catch (Exception e) {
                Log.e(TAG, "Summon error: " + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateFantasyProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                updateFantasyProgress("Knowledge secured!");
                btnDelete.setVisibility(View.VISIBLE);
                Toast.makeText(ContentActivity.this,
                        "Knowledge saved for offline", Toast.LENGTH_SHORT).show();
                checkAndLoadPdf();
            } else {
                hideFantasyProgress();
                Toast.makeText(ContentActivity.this,
                        "Summon failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
