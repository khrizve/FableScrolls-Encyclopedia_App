package com.example.fablescrolls;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PDFDao {
    @Insert
    void insert(PDFEntity pdf);

    @Query("SELECT * FROM pdfs WHERE subCategory = :subCategory LIMIT 1")
    PDFEntity getPDFBySubCategory(String subCategory);

    @Query("DELETE FROM pdfs WHERE subCategory = :subCategory")
    void deleteBySubCategory(String subCategory);
}