package com.penseapp.acaocontabilidade.news.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.news.model.News;
import com.penseapp.acaocontabilidade.news.presenter.NewsPresenter;
import com.penseapp.acaocontabilidade.news.presenter.NewsPresenterImpl;

import java.util.List;


/**
 * Created by unity on 27/10/15.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>
        implements NewsAdapterView {//ItemTouchHelperAdapter {

    private NewsPresenter newsPresenter;

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    // Define onItemClickListener member variable
    private static OnItemClickListener onItemClickListener;

    private final List<News> mNews;

    public NewsAdapter(List<News> news) {
        mNews = news;
        newsPresenter = new NewsPresenterImpl(this);
    }

    // Define the onItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Allows the parent activity or fragment to define the onItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView name;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.list_item_chat_name_textview);

            // Setup the click onItemClickListener
            // itemView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get the data model based on position
        final News selectedNews = mNews.get(position);

        // Set item views based on your views and data model
        holder.name.setText(selectedNews.getDate() + "\n\n" + selectedNews.getTitle());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public News getItem(int position) {
        return mNews.get(position);
    }

    @Override
    public void subscribeForNewsUpdates() {
        newsPresenter.subscribeForNewsUpdates();
    }

    @Override
    public void unsubscribeForNewsUpdates() {
        newsPresenter.unsubscribeForNewsUpdates();
    }

    @Override
    public void onNewsAdded(News news) {
        Log.i(LOG_TAG, "View onNewsAdded called");
        mNews.add(news);
        notifyItemInserted(mNews.size() - 1);
    }

    @Override
    public void onNewsChanged(News news) {
        Log.i(LOG_TAG, "View onNewsChanged called");
        // TODO
        int index = getIndexForKey(news.getKey());
        mNews.set(index, news);
        notifyItemChanged(index);
    }

    @Override
    public void onNewsRemoved(String newsId) {
        Log.i(LOG_TAG, "View onNewsRemoved called");
        try {
            int index = getIndexForKey(newsId);
            mNews.remove(index);
            notifyItemRemoved(index);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // TODO this method should go somewhere else? Does it belong in the View?
    // TODO duplicado em ExerciseChooserActivity
    private int getIndexForKey(String key) {
        // TODO retirar getkey
//        return mNewsKeys.indexOf(key);
        int index = 0;
        for (News news : mNews) {
            if (news.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }
}