package com.penseapp.acaocontabilidade.news.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.penseapp.acaocontabilidade.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsItemActivityFragment extends Fragment {

    public NewsItemActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_item, container, false);
    }
}
