package com.example.necola.RoomDBs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.necola.DAO.MsgDAO;
import com.example.necola.entity.Msg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.
@Database(entities = Msg.class,version=3,exportSchema = false)
public abstract class  MsgRoomDB extends RoomDatabase {
    public abstract MsgDAO msgDAO();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile MsgRoomDB INSTANCE;

    //thread pool
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MsgRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MsgRoomDB.class) {
                if(INSTANCE==null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        MsgRoomDB.class, "msg_database").fallbackToDestructiveMigration()//关闭改表结构就写迁移的要求
                        .build();
            }
        }

    }
    return INSTANCE;
}
    //重写onCreate方法来填充数据库来测试
    private  static RoomDatabase.Callback sRoomDatabaseCallback=new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        databaseWriteExecutor.execute(() -> {
            // Populate the database in the background.
            // If you want to start with more words, just add them.
            MsgDAO dao = INSTANCE.msgDAO();
            //dao.deleteAll();

            /*Msg msg= new Msg("Hello",1);
            dao.insert(msg);
            msg= new Msg("World",0);
            dao.insert(msg);*/
        });
    }
};

    }


