package com.example.necola.RoomDBs.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.necola.RoomDBs.DAO.MsgDAO;
import com.example.necola.RoomDBs.MsgRoomDB;
import com.example.necola.entity.Msg;

import java.util.List;

public class MsgRepository {
    private MsgDAO mMsgDao;
    private LiveData<List<Msg>> mAllmsgs;
    private LiveData<List<Msg>> mOwnedmsgs;
    private LiveData<List<Msg>> mAllRecentcontacts;


    //供 ContactViewModel 使用
    public MsgRepository(Application application){
        MsgRoomDB db= MsgRoomDB.getDatabase(application);
        mMsgDao=db.msgDAO();
        mAllRecentcontacts=mMsgDao.getAllContactsByRecentMsg();
    }
    public MsgRepository(Application application,
                         String currentUsername,
                         String currentContactID ){
        MsgRoomDB db= MsgRoomDB.getDatabase(application);
        mMsgDao=db.msgDAO();
        mOwnedmsgs=mMsgDao.getMsgByUser(currentUsername,currentContactID);


    }
    //你必须在非ui线程上调用它，否则你的应用程序会抛出异常。Room可以确保
    //你没有在主线程上做任何长时间运行的操作，阻塞了UI
    public void insert(Msg msg){
        MsgRoomDB.databaseWriteExecutor.execute(()->{
            mMsgDao.insert(msg);
        });
    }
    //在一个单独的线程上执行所有的查询。
    //观察到的LiveData将在数据发生变化时通知观察者
    public LiveData<List<Msg>> getAllMsgs(){return mAllmsgs; }

    public LiveData<List<Msg>> getAllRecentContacts(){return mAllRecentcontacts;}

    public LiveData<List<Msg>> getOwnedMsgs(){return mOwnedmsgs; }

}
