package com.penseapp.acaocontabilidade.chat.messages.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.messages.model.Message;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.penseapp.acaocontabilidade.R.id.left_arrow;
import static com.penseapp.acaocontabilidade.R.id.right_arrow;


/**
 * Created by unity on 27/10/15.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private static final String LOG_TAG = MessagesAdapter.class.getSimpleName();

    // Define onItemClickListener member variable
    private static OnItemClickListener onItemClickListener;

    private final Context mContext;
    private final List<Message> mMessages;

    public MessagesAdapter(Context context, List<Message> mMessages) {
        this.mContext = context;
        this.mMessages = mMessages;
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
        TextView text;
        ImageView image;
        TextView time;
        FrameLayout leftArrow;
        FrameLayout rightArrow;
        RelativeLayout messageContainer;
        LinearLayout message;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.list_item_chat_contact_name_textview);
            image = (ImageView) itemView.findViewById(R.id.list_item_message_image);
            time = (TextView) itemView.findViewById(R.id.list_item_chat_contact_time_textview);
            leftArrow = (FrameLayout) itemView.findViewById(left_arrow);
            rightArrow = (FrameLayout) itemView.findViewById(right_arrow);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
            message = (LinearLayout) itemView.findViewById(R.id.message);

            // Setup the click onItemClickListener
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get the data model based on position
        final Message message = mMessages.get(position);

        // Show text
        String text = message.getText();
        if (text != null) {
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(text);
        } else {
            holder.text.setVisibility(View.GONE);
        }

        // Show image
        String photoURL = message.getPhotoURL();
        if (photoURL != null) {
            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(message.getFileDownloadUrl()).into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        // Show time
        long timestamp = message.getTimestamp();
        String timeString = SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(timestamp);
        holder.time.setText(timeString);

        // Show message bubble
        int color;
        if (message.getSenderId().equals(FirebaseHelper.getInstance().getAuthUserId())) {
            // Sent message
            holder.leftArrow.setVisibility(View.GONE);
            holder.rightArrow.setVisibility(View.VISIBLE);
            holder.messageContainer.setGravity(Gravity.END);
            color = ContextCompat.getColor(holder.messageContainer.getContext(), R.color.sentMessage);
        } else {
            // Received message
            holder.leftArrow.setVisibility(View.VISIBLE);
            holder.rightArrow.setVisibility(View.GONE);
            holder.messageContainer.setGravity(Gravity.START);
            color = ContextCompat.getColor(holder.messageContainer.getContext(), R.color.receivedMessage);
        }
        ((GradientDrawable) holder.message.getBackground()).setColor(color);
        ((RotateDrawable) holder.leftArrow.getBackground()).getDrawable()
                .setColorFilter(color, PorterDuff.Mode.SRC);
        ((RotateDrawable) holder.rightArrow.getBackground()).getDrawable()
                .setColorFilter(color, PorterDuff.Mode.SRC);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public Message getItem(int position) {
        return mMessages.get(position);
    }

}