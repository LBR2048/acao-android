package com.penseapp.acaocontabilidade.chat.adapters;

import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.model.Message;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

import java.util.List;

import static com.penseapp.acaocontabilidade.R.id.left_arrow;
import static com.penseapp.acaocontabilidade.R.id.right_arrow;


/**
 * Created by unity on 27/10/15.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
//        implements ItemTouchHelperAdapter {

    private static final String LOG_TAG = MessagesAdapter.class.getSimpleName();

    // Define onItemClickListener member variable
    private static OnItemClickListener onItemClickListener;
//    private static OnItemDismissListener onItemDismissListener;
//    private static OnWorkoutShareClickListener onWorkoutShareClickListener;
//    private static OnWorkoutRenameClickListener onWorkoutRenameClickListener;

    private final List<Message> mMessages;
//    private final OnStartDragListener mDragStartListener;

//    public ChatListAdapter(OnStartDragListener dragStartListener, List<Chat> chats) {
//        mDragStartListener = dragStartListener;
//        mMessages = chats;
//    }

    public MessagesAdapter(List<Message> mMessages) {
        this.mMessages = mMessages;
    }


    // Define the onItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the OnItemDismissListener interface
    public interface OnItemDismissListener {
        void onItemDismiss(int position);
    }

    // Define the OnWorkoutShareClickListener interface
    public interface OnWorkoutShareClickListener {
        void onWorkoutShareClick(View itemView, int position);
    }

    // Define the OnWorkoutRenameClickListener interface
    public interface OnWorkoutRenameClickListener {
        void onWorkoutRenameClick(View itemView, int position);
    }


    // Allows the parent activity or fragment to define the onItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Allows the parent activity or fragment to define the onItemDismissListener
//    public void setOnItemDismissListener(OnItemDismissListener listener) {
//        this.onItemDismissListener = listener;
//    }

    // Allows the parent activity or fragment to define the onItemDismissListener
//    public void setOnWorkoutShareClickListener(OnWorkoutShareClickListener listener) {
//        this.onWorkoutShareClickListener = listener;
//    }

    // Allows the parent activity or fragment to define the onItemDismissListener
//    public void setOnWorkoutRenameClickListener(OnWorkoutRenameClickListener listener) {
//        this.onWorkoutRenameClickListener = listener;
//    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
//            implements ItemTouchHelperViewHolder {//, View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView text;
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
            text = (TextView) itemView.findViewById(R.id.list_item_contact_name_textview);
            leftArrow = (FrameLayout) itemView.findViewById(left_arrow);
            rightArrow = (FrameLayout) itemView.findViewById(right_arrow);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
            message = (LinearLayout) itemView.findViewById(R.id.message);

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

//        @Override
//        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
//        }
//
//        @Override
//        public void onItemClear() {
//            itemView.setBackgroundColor(0);
//        }

//        @Override
//        public void onClick(View view) {
//            Toast.makeText(view.getContext(), text.getText() + " clicked", Toast.LENGTH_SHORT).show();
//        }
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

        // Set item views based on your views and data model
        holder.text.setText(message.getText());

        int color;
        if (message.getSenderId().equals(FirebaseHelper.getInstance().getAuthUserId())) {
//            // Sent message
            holder.leftArrow.setVisibility(View.GONE);
            holder.rightArrow.setVisibility(View.VISIBLE);
            holder.messageContainer.setGravity(Gravity.END);
            color = ContextCompat.getColor(holder.messageContainer.getContext(), R.color.sentMessage);
        } else {
//            // Received message
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


        // Start a drag whenever the handle view it touched
//        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                    mDragStartListener.onStartDrag(holder);
//                }
//                return false;
//            }
//        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public Message getItem(int position) {
        return mMessages.get(position);
    }

//    @Override
//    public boolean onItemMove(int fromPosition, int toPosition) {
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mMessages, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mMessages, i, i - 1);
//            }
//        }
//        notifyItemMoved(fromPosition, toPosition);
//        return true;
//    }
//
//    @Override
//    public void onItemMoved(int fromPosition, int toPosition) {
//    }
//
//    @Override
//    public void onItemDrop(int mFrom, int mTo) {
//    }
//
//    @Override
//    public void onItemDismiss(int position) {
//        // Remove Workout from Firebase
//        if (onItemDismissListener != null)
//            onItemDismissListener.onItemDismiss(position);
//        // Remove Workout from List and notify the RecyclerView
//        mMessages.remove(position);
//        notifyItemRemoved(position);
//    }
}