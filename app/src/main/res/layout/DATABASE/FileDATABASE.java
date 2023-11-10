package com.example.project_1.DATABASE;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.project_1.DAO.AllFileDAO;
import com.example.project_1.DTO.AllFileDTO;

@Database(entities = {AllFileDTO.class}, version = 3)
public abstract class FileDATABASE extends RoomDatabase {
    private static final String DB_NAME  = "db_file";
    private static FileDATABASE instance;

    public static synchronized FileDATABASE getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, FileDATABASE.class,DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build(); // cho phep cai dat query tren main thread
        }
        return instance;
    }

    public abstract AllFileDAO fileDAO();
}
