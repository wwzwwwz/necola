package com.example.section2.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "msg_table")
public class Msg {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    @ColumnInfo(name = "receiver_id")
    public int receiver_id;
    @ColumnInfo(name = "sender_id")
    public int sender_id;
    @ColumnInfo(name = "content")
    private String content;
    //@ColumnInfo(name = "date")
    //public Data
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    private int type;



    public Msg(String content,int type){
        this.content=content;
        this.type=type;
    }

    public String getContent(){
        return content;
    }
    public int getType(){
        return  type;
    }
}
