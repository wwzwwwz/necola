package com.example.necola.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey
    public int accountID;

    @ColumnInfo(name = "username")
    public String username;
    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "portrait_url")
    public String portraitURL;

    @ColumnInfo(name = "sex")
    public String sex;

}