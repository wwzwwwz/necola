package com.example.section2.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.section2.entity.Msg;
import com.example.section2.entity.User;
import com.example.section2.entity.Word;

import java.util.List;

@Dao
public interface MsgDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Msg msg);

    @Delete
    void delete(Msg user);

    @Query("SELECT * FROM msg_table WHERE " +
            "sender_id=:sender_id and receiver_id=:receiver_id")
    List<Msg> loadAllByIds(int sender_id,int receiver_id);

    @Query("SELECT * FROM msg_table ORDER BY  id") //按msg_id新旧排
    LiveData<List<Msg>> getMsgBy();




}
