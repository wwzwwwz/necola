package com.example.necola.RoomDBs.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.necola.entity.Msg;

import java.util.List;

@Dao
public interface MsgDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Msg msg);

    @Delete
    void delete(Msg user);


    @Query("SELECT * FROM msg_table ORDER BY  id") //按msg_id新旧排
    LiveData<List<Msg>> getMsgBy();


    @Query("SELECT * FROM msg_table WHERE owner=:currentUsername AND contact=:currentContact ORDER BY  id")
    LiveData<List<Msg>> getMsgByUser(String currentUsername,
                                         String currentContact);

    @Query("SELECT * FROM msg_table GROUP by contact ORDER BY  max(id) desc ")
    LiveData<List<Msg>> getAllContactsByRecentMsg();

}
