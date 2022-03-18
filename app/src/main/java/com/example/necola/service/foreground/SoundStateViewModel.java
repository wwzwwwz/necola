package com.example.necola.service.foreground;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SoundStateViewModel extends ViewModel {

    private MutableLiveData<Integer> currentPlayerState;
    private MutableLiveData<Integer> currentPlayLength;
    private MutableLiveData<String> currentTitle;
    private MutableLiveData<String> CurrentArtists;
    private MutableLiveData<String> currentPicUrl;


    public MutableLiveData<Integer> getCurrentPlayerState() {
        if (currentPlayerState == null) {
            currentPlayerState = new MutableLiveData<Integer>();
        }return currentPlayerState;
    }

    public MutableLiveData<Integer> getCurrentPlayLength() {
        if (currentPlayLength== null) {
            currentPlayLength = new MutableLiveData<Integer>();
        }return currentPlayLength;
    }

    public MutableLiveData<String> getCurrentTitle() {
        if (currentTitle == null) {
            currentTitle = new MutableLiveData<String>();
        }return currentTitle;
    }

    public MutableLiveData<String> getCurrentArtists() {
        if (CurrentArtists == null) {
            CurrentArtists = new MutableLiveData<String>();
        }return CurrentArtists;
    }

    public MutableLiveData<String> getCurrentPicUrl() {
        if (currentPicUrl == null) {
            currentPicUrl= new MutableLiveData<String>();
        }
        return currentPicUrl;
    }

// Rest of the ViewModel...
}
