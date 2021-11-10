package com.example.section2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.section2.R;



public class ContextFragment extends Fragment {

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.context_fragment, container, false);

        // 获取文本控件
        textView = view.findViewById(R.id.content_text);

        // 获取Bundle,该对象是Activity创建Fragment时，传入的
        Bundle bundle = getArguments();
        if (bundle != null) {
            String textValue = bundle.getString("textValue");// 将文本框的值赋值为传入的textValue
            textView.setText(textValue);
        }

        return view;
    }
}