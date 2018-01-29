package com.penseapp.acaocontabilidade.chat.messages.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.messages.model.Message;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.squareup.picasso.Picasso;

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getPhotoURL() != null) {
                    Toast.makeText(mContext, "Abrindo imagem", Toast.LENGTH_SHORT).show();
                    FirebaseHelper.getInstance().getHttpFromGs(new FirebaseHelper.GetHttpFromGsCallback() {
                        @Override
                        public void showHttp(Uri http) {
                            // TODO check if device has an app available to open this type of file
                            Intent intent = new Intent(Intent.ACTION_VIEW, http);
                            mContext.startActivity(intent);
                        }
                    }, message.getPhotoURL());

                } else if (message.getPDF() != null){
                    Toast.makeText(mContext, "Abrindo documento", Toast.LENGTH_SHORT).show();
                    FirebaseHelper.getInstance().getPdfHttpFromGsWithoutPrefix(new FirebaseHelper.GetHttpFromGsCallback() {
                        @Override
                        public void showHttp(Uri http) {
                            // TODO check if device has an app available to open this type of file
                            Intent intent = new Intent(Intent.ACTION_VIEW, http);
                            mContext.startActivity(intent);
                        }
                    }, message.getPDF());

                } else {
                    // TODO log which file could not be opened
                    Toast.makeText(mContext, "Tipo de arquivo desconhecido", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
            // Workaround to avoid PDF icon from showing briefly before the image // TODO improve this
            holder.image.setImageResource(0);
            holder.image.setVisibility(View.VISIBLE);
            FirebaseHelper.getInstance().getHttpFromGs(new FirebaseHelper.GetHttpFromGsCallback() {
                @Override
                public void showHttp(Uri http) {
                    Picasso.with(mContext).load(http).into(holder.image);
                }
            }, photoURL);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        // Show PDF
        String pdf = message.getPDF();
        if (pdf != null) {
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setImageResource(R.drawable.ic_picture_as_pdf_black_24dp);
        }

        // Show time
        long timestamp = message.getTimestamp();
        String timeString = SimpleDateFormat.getDateTimeInstance().format(timestamp);
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