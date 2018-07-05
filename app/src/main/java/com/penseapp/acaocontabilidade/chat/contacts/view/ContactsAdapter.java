package com.penseapp.acaocontabilidade.chat.contacts.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.contacts.presenter.ContactsPresenter;
import com.penseapp.acaocontabilidade.chat.contacts.presenter.ContactsPresenterImpl;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.login.model.User;

import java.util.List;

import static com.penseapp.acaocontabilidade.chat.chats.view.ChatsActivity.mChats;


/**
 * Created by unity on 27/10/15.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>
        implements ContactsAdapterView {//ItemTouchHelperAdapter {

    private static final String LOG_TAG = ContactsAdapter.class.getSimpleName();

    private final FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();

    // Define onItemClickListener member variable
    private static OnItemClickListener onItemClickListener;

    private final List<User> mContacts;
    private final ContactsPresenter contactsPresenter;

    public ContactsAdapter(List<User> contacts) {
        mContacts = contacts;
        contactsPresenter = new ContactsPresenterImpl(this);
    }


    // Define the onItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

      // Allows the parent activity or fragment to define the onItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat_contact, parent, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // Get the data model based on position
        final User selectedContact = mContacts.get(position);

        // Set item views based on your views and data model
        holder.name.setText(selectedContact.getName());

        String email = selectedContact.getEmail();
        String emailUser = email.substring(0, email.indexOf("@"));
        switch (emailUser) {
            case "contabil":
                holder.icon.setImageResource(R.drawable.ic_contabil);
                break;
            case "fiscal":
                holder.icon.setImageResource(R.drawable.ic_fiscal);
                break;
            case "pessoal":
                holder.icon.setImageResource(R.drawable.ic_pessoal);
                break;
            case "societario":
                holder.icon.setImageResource(R.drawable.ic_societario);
                break;
            default:
                holder.icon.setImageResource(R.drawable.ic_default);
                break;
        }

        holder.availability.setText(R.string.status_available);

//        int unreadMessageCount = selectedChat.getUnreadMessageCount();
        int unreadMessageCount = 5;
        if (unreadMessageCount != 0) {
            holder.unreadMessageCount.setText(Integer.toString(unreadMessageCount));
            holder.badge.setVisibility(View.VISIBLE);
        } else {
            holder.badge.setVisibility(View.GONE);
        }
    }

    public void setNotificationObservers() {
        DatabaseReference notifRef = mFirebaseHelperInstance.getNotificationsReference();
        Log.d(LOG_TAG, "notifRef " + notifRef.toString());
        for (User user : mContacts) {
            DatabaseReference userNotifRef = notifRef.child(user.getKey());
            Log.d(LOG_TAG, "userNotifRef " + userNotifRef.toString());
            userNotifRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    Integer value = dataSnapshot.getValue(int.class);
                    Log.d(LOG_TAG, "Added " + value);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                    Integer value = dataSnapshot.getValue(int.class);
                    Log.d(LOG_TAG, "Changed " + value);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public User getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public void subscribeForContactsUpdates() {
        contactsPresenter.subscribeForContactsUpdates();
    }

    @Override
    public void unsubscribeForContactsUpdates() {
        contactsPresenter.unsubscribeForContactsUpdates();
    }

    @Override
    public void onContactAdded(User contact) {
        Log.i(LOG_TAG, "View onContactAdded called");
        mContacts.add(contact);
        notifyItemInserted(mContacts.size() - 1);
        setNotificationObserver(contact);
    }

    @Override
    public void onContactChanged(User contact) {
        Log.i(LOG_TAG, "View onContactChanged called");
        int index = getIndexForKey(contact.getKey());
        mContacts.set(index, contact);
        notifyItemChanged(index);
    }

    @Override
    public void onContactRemoved(String contactId) {
        Log.i(LOG_TAG, "View onContactRemoved called");
        try {
            int index = getIndexForKey(contactId);
            mChats.remove(index);
            notifyDataSetChanged();
            notifyItemRemoved(index);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // TODO this method should go somewhere else? Does it belong in the View?
    // TODO duplicado em ExerciseChooserActivity
    private int getIndexForKey(String key) {
        int index = 0;
        for (User contact : mContacts) {
            if (contact.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    private void setNotificationObserver(User user) {
        DatabaseReference notifRef = mFirebaseHelperInstance.getNotificationsReference();
            DatabaseReference userNotifRef = notifRef.child(user.getKey());
            userNotifRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String key = dataSnapshot.getKey();
                    int position = getIndexForKey(key);
                    Integer value = dataSnapshot.getValue(int.class);

                    Log.d(LOG_TAG, "Posição " + String.valueOf(position) + ": Valor " +String.valueOf(value));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        final ImageView icon;
        final TextView name;
        final TextView unreadMessageCount;
        final FrameLayout badge;
        final TextView availability;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            icon = itemView.findViewById(R.id.list_item_chat_contact_icon);
            name = itemView.findViewById(R.id.list_item_chat_contact_name_textview);
            availability = itemView.findViewById(R.id.list_item_chat_contact_availability_textview);
            unreadMessageCount = itemView.findViewById(R.id.list_item_chat_contact_unread_messages_textview);
            badge = itemView.findViewById(R.id.list_item_chat_contact_unread_messages_badge);

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
//        for  user in users {
//            let userNotifRef = notifRef.child(user.key)
//            userNotifRef.removeAllObservers()
//            userNotifRef.observe(.value, with: { (snapshot) in
//                self.setCellNotification(forUser: user, snapshot)
//            })
//        }


}