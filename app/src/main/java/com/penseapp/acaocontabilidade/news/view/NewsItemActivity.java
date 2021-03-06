package com.penseapp.acaocontabilidade.news.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.news.model.News;
import com.penseapp.acaocontabilidade.news.presenter.NewsPresenter;
import com.penseapp.acaocontabilidade.news.presenter.NewsPresenterImpl;

public class NewsItemActivity extends AppCompatActivity implements NewsItemView{

    public static final String SELECTED_NEWS_KEY = "selected_news_key";
    public static final String SELECTED_NEWS_TITLE = "selected_news_title";

    private TextView dateView;
    private TextView titleView;
    private TextView textView;

    private NewsPresenter newsPresenter;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // GUI
        titleView = (TextView) findViewById(R.id.news_title);
        dateView = (TextView) findViewById(R.id.news_date);
        textView = (TextView) findViewById(R.id.news_text);

        key = getIntent().getExtras().getString(SELECTED_NEWS_KEY);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to Presenters
        newsPresenter = new NewsPresenterImpl(this);
        newsPresenter.subscribeForSingleNewsItemUpdates(key);
    }

    @Override
    protected void onStop() {
        newsPresenter.unsubscribeForSingleNewsItemUpdates(key);
        super.onStop();
    }

    @Override
    public void onNewsItemChanged(News newsItem) {
        if (newsItem != null) {
            titleView.setText(newsItem.getTitle());
            dateView.setText(newsItem.getDate());
            textView.setText(newsItem.getText());
        }
    }
}
