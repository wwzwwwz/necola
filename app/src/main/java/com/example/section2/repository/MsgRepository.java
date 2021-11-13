package com.example.section2.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.section2.DAO.MsgDAO;
import com.example.section2.RoomDBs.MsgRoomDB;
import com.example.section2.RoomDBs.WordRoomDB;
import com.example.section2.entity.Msg;
import com.example.section2.entity.Word;

import java.util.List;

public class MsgRepository {
    private MsgDAO mMsgDao;
    private LiveData<List<Msg>> mAllmsgs;

    //为了对MsgRepository进行单元测试，您必须删除Application
    //依赖。这增加了复杂性和更多的代码，并且本示例不是关于测试的
    public MsgRepository(Application application){
        MsgRoomDB db= MsgRoomDB.getDatabase(application);
        mMsgDao=db.msgDAO();
        mAllmsgs=mMsgDao.getMsgBy();

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

}
