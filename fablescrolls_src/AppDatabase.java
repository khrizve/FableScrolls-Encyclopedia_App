package com.example.fablescrolls;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PDFEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PDFDao pdfDao();
}