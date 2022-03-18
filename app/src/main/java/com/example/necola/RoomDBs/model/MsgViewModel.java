package com.example.necola.RoomDBs.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.necola.RoomDBs.repository.MsgRepository;
import com.example.necola.entity.Msg;

import java.util.List;

//////////////////////////////Message ViewModel/////////////////
public class MsgViewModel extends AndroidViewModel {
    private MsgRepository msgRepository;
    private final LiveData<List<Msg>> mAllMsgs;


    public MsgViewModel(Application application) {
        super(application);
        msgRepository = new MsgRepository(application);
        mAllMsgs = msgRepository.getAllMsgs();

    }

    public MsgViewModel(Application application, String currentUsername, String currentContactID) {
        super(application);
        msgRepository = new MsgRepository(application, currentUsername, currentContactID);
        mAllMsgs = msgRepository.getOwnedMsgs();

    }

    public LiveData<List<Msg>> getAllMsgs() {
        return mAllMsgs;
    }

    public void insert(Msg msg) {
        msgRepository.insert(msg);
    }

}
