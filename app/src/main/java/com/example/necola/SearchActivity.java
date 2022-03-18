package com.example.necola;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.necola.fragments.SearchContentFragment;
import com.example.necola.fragments.SearchTitleFragment;
import com.example.necola.utils.httpAPI.resource.netease.Search;


public class SearchActivity extends BaseActivity{

    SearchContentFragment searchContentFragment;
    SearchTitleFragment searchTitleFragment;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_search, menu);

        EditText keywordsEdit=(EditText) menu.findItem(R.id.search_input).getActionView();
        keywordsEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i== EditorInfo.IME_ACTION_SEARCH){
                    String keywords=keywordsEdit.getText().toString();
                    Search search=new Search.Builder(keywords).build();

                    search.doSearch(SearchActivity.this);
                    searchTitleFragment.refresh(search.songs);
                    return true;

                }
                return false;
            }
        });
        return true;
    }



    //action bar item
    public boolean onOptionsItemSelected(MenuItem item) {
        //code for item select event handling
        switch (item.getItemId()) {
            //android.R 系统内部预先定义好的资源
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.search_action:
                EditText keywordsEdit=findViewById(R.id.search_keywords);
                String keywords=keywordsEdit.getText().toString();
                Search search=new Search.Builder(keywords).build();

                search.doSearch(this);
                searchTitleFragment.refresh(search.songs);

                //onBackPressed();
                return true;
            //R 工程师自定义的资源
            default:
                break;
        }
        return false;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar mToolbar=findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        actionbar=getSupportActionBar();
        mToolbar.setNavigationIcon(R.drawable.close_menu);
        actionbar.setTitle("");
        searchTitleFragment=(SearchTitleFragment)getSupportFragmentManager().findFragmentById(R.id.music_title_fragment);
        searchContentFragment=(SearchContentFragment)getSupportFragmentManager().findFragmentById(R.id.music_content_fragment);





    }
}
