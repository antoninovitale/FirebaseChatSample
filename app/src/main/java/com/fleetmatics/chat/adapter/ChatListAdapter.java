package com.fleetmatics.chat.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;
import com.fleetmatics.chat.R;
import com.fleetmatics.chat.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<ChatMessage> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, ChatMessage.class, layout, activity);
        this.mUsername = mUsername;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view        A view instance corresponding to the layout we passed to the constructor.
     * @param chatMessage An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, ChatMessage chatMessage) {
        // Map a Chat object to an entry in our listview
        String author = chatMessage.getAuthor();
        TextView authorText = (TextView) view.findViewById(R.id.author);
        TextView timestampLabel = (TextView) view.findViewById(R.id.timestamp_label);
        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
//            authorText.setTextColor(Color.parseColor("#B8B8B8"));
            timestampLabel.setText(R.string.sent_timestamp_label);
            view.setBackgroundResource(R.drawable.user_chat_bubble);
        } else {
//            authorText.setTextColor(Color.parseColor("#80D0F7"));
            timestampLabel.setText(R.string.timestamp_label);
            view.setBackgroundResource(R.drawable.other_user_chat_bubble);
        }
        ((TextView) view.findViewById(R.id.message)).setText(chatMessage.getMessage());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);
        ((TextView) view.findViewById(R.id.timestamp_value)).setText(simpleDateFormat.format(chatMessage.getTimestamp() != null ? chatMessage.getTimestamp() : new Date()));
    }
}
