package com.example.section2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.section2.entity.Msg;
import com.example.section2.repository.MsgRepository;

import java.util.List;

public class MsgViewModel extends AndroidViewModel {
    private MsgRepository msgRepository;
    private final LiveData<List<Msg>> mAllMsgs;
    int msgSize;

    public MsgViewModel(Application application) {
        super(application);
        msgRepository=new MsgRepository(application);
        mAllMsgs=msgRepository.getAllMsgs();

    }
    //Added a getAllWords() method to return a cached list of words.
    public LiveData<List<Msg>> getAllMsgs(){return mAllMsgs; }

    public void insert(Msg msg){msgRepository.insert(msg);}

}
