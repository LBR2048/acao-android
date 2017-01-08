package com.penseapp.acaocontabilidade.chat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.model.Chat;

import java.util.List;


/**
 * Created by unity on 27/10/15.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
//        implements ItemTouchHelperAdapter {

    private static final String LOG_TAG = ChatListAdapter.class.getSimpleName();

    // Define onItemClickListener member variable
    private static OnItemClickListener onItemClickListener;
//    private static OnItemDismissListener onItemDismissListener;
//    private static OnWorkoutShareClickListener onWorkoutShareClickListener;
//    private static OnWorkoutRenameClickListener onWorkoutRenameClickListener;

    private final List<Chat> mChats;
//    private final OnStartDragListener mDragStartListener;

//    public ChatListAdapter(OnStartDragListener dragStartListener, List<Chat> chats) {
//        mDragStartListener = dragStartListener;
//        mMessages = chats;
//    }

    public ChatListAdapter(List<Chat> chats) {
        mChats = chats;
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
        TextView name;
        TextView exerciseCount;
        ImageView handleView;
        ImageView shareView;
        ImageView renameView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.list_item_chat_name_textview);
//            exerciseCount = (TextView) itemView.findViewById(R.id.list_item_workout_exercise_count_textview);
//            handleView = (ImageView) itemView.findViewById(R.id.list_item_workout_handle);
//            shareView = (ImageView) itemView.findViewById(R.id.list_item_workout_share_imageview);
//            renameView = (ImageView) itemView.findViewById(R.id.list_item_workout_rename_imageview);

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

//            shareView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onWorkoutShareClickListener != null)
//                        onWorkoutShareClickListener.onWorkoutShareClick(itemView, getLayoutPosition());
//                }
//            });
//
//            renameView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (onWorkoutRenameClickListener != null)
//                        onWorkoutRenameClickListener.onWorkoutRenameClick(itemView, getLayoutPosition());
//                }
//            });

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
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get the data model based on position
        final Chat selectedChat = mChats.get(position);

        // Set item views based on your views and data model
        holder.name.setText(selectedChat.getName());
//        holder.exerciseCount.setText(String.format("%d", selectedChat.getExerciseCount()) + " exercícios");

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
        return mChats.size();
    }

    public Chat getItem(int position) {
        return mChats.get(position);
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