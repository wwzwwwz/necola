package com.example.necola.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "msg_table")
public class Msg {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    @ColumnInfo(name = "contact")
    public String contact;
    @ColumnInfo(name = "owner")
    public String owner;
    @ColumnInfo(name = "content")
    private String content;
    //@ColumnInfo(name = "date")
    //public Data
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    private int type;



    public Msg(String content,int type,String owner,String contact){
        this.content=content;
        this.type=type;
        this.owner=owner;
        this.contact=contact;

    }

    public String getContent(){
        return content;
    }
    public int getType(){
        return  type;
    }
    public String getContact() {return contact;}
    public String getOwner() {return owner;}
}
