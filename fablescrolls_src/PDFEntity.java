package com.example.fablescrolls;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pdfs")
public class PDFEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String subCategory;  // e.g., "প্রাচীন ইতিহাস"
    public String filePath;     // Where PDF is stored
    public String downloadUrl;  // Original download URL

    public PDFEntity(String subCategory, String filePath, String downloadUrl) {
        this.subCategory = subCategory;
        this.filePath = filePath;
        this.downloadUrl = downloadUrl;
    }
}