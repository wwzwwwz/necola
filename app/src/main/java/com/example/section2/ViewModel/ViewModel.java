package com.example.section2.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.section2.entity.Word;
import com.example.section2.repository.WordRepository;

import java.util.List;

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */
class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;

    private final LiveData<List<Word>> mAllWords;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<List<Word>> getAllWords() { return mAllWords; }

    public void insert(Word word) { mRepository.insert(word); }
}

// Using LiveData and caching what getAlphabetizedWords returns has several benefits:
// - We can put an observer on the data (instead of polling for changes) and only update the
//   the UI when the data actually changes.
// - Repository is completely separated from the UI through the ViewModel.