package com.example.du_an_1_nhom_12.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.du_an_1_nhom_12.DTO.AllFileDTO;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AllFileDAO {
    @Insert
    void insertFile(AllFileDTO dto);
    @Insert
    void insertAllFile(ArrayList<AllFileDTO> list);

    @Update
    void updateFile(AllFileDTO dto);

    @Delete
    void deleteFile(AllFileDTO dto);

    @Query("DELETE FROM tb_file WHERE id = :id")
    void deleteByID(int id);

    @Query("DELETE FROM tb_file WHERE path = :path")
    void deleteByPath(String path);

    @Query("DELETE FROM tb_file")
    void  deleteAll();

    @Query("SELECT * FROM tb_file")
    List<AllFileDTO> getListFile();

    @Query("SELECT * FROM tb_file ORDER BY ten")
    List<AllFileDTO> getListAZ();

}
