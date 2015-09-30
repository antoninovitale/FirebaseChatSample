package com.fleetmatics.chat.activity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.fleetmatics.chat.ChatApplication;
import com.fleetmatics.chat.R;
import com.fleetmatics.chat.adapter.ChatListAdapter;
import com.fleetmatics.chat.model.ChatMessage;
import com.fleetmatics.chat.model.Room;
import com.fleetmatics.chat.model.RoomStatus;
import com.fleetmatics.chat.model.RoomType;
import com.fleetmatics.chat.utils.Constants;
import com.fleetmatics.chat.utils.Utils;

import java.util.Date;

public class ChatItemDetailFragment extends Fragment {
    private Firebase mFirebaseChatRef;
    private Firebase mFirebaseRef;
    private ChatListAdapter mChatListAdapter;

    private String username, roomId;
    private String roomIdReverse;
    private String chatWithUserId;
    private Firebase mFirebaseRoomsRef;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatItemDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chatitem_detail, container, false);
        mFirebaseRef = ChatApplication.getMyFirebaseRef();
        mFirebaseRoomsRef = mFirebaseRef.child(Constants.ROOMS);
        username = Utils.getUsername(getActivity());
        // Chat ref
        roomId = getArguments().getString(Constants.PARAM_ROOM_ID);
        roomIdReverse = getArguments().getString(Constants.PARAM_ROOM_ID_REVERSE);
        chatWithUserId = getArguments().getString(Constants.PARAM_CHAT_WITH_USR);
        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        listView.setEmptyView(rootView.findViewById(android.R.id.empty));
        mFirebaseRoomsRef.orderByChild(Constants.ROOM_ID).equalTo(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    mFirebaseRoomsRef.orderByChild(Constants.ROOM_ID).equalTo(roomIdReverse).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() == 0) {
                                mFirebaseChatRef = mFirebaseRoomsRef.child(roomId);
                                Room room = new Room(roomId, RoomType.PRIVATE, RoomStatus.ACTIVE);
                                mFirebaseChatRef.setValue(room);
                                mFirebaseRef.child(Constants.USERS).child(Utils.getUserId(getActivity())).child(Constants.ROOMS).push().setValue(room);
                                mFirebaseRef.child(Constants.USERS).child(chatWithUserId).child(Constants.ROOMS).push().setValue(room);
                            } else {
                                mFirebaseChatRef = mFirebaseRoomsRef.child(roomIdReverse);
                            }
                            // Tell our list adapter that we only want 50 messages at a time
                            mChatListAdapter = new ChatListAdapter(mFirebaseChatRef.child(Constants.MESSAGES), getActivity(), R.layout.chat_message, username);
                            listView.setAdapter(mChatListAdapter);
                            mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
                                @Override
                                public void onChanged() {
                                    super.onChanged();
                                    listView.setSelection(mChatListAdapter.getCount() - 1);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                } else {
                    mFirebaseChatRef = mFirebaseRoomsRef.child(roomId);
                    // Tell our list adapter that we only want 50 messages at a time
                    mChatListAdapter = new ChatListAdapter(mFirebaseChatRef.child(Constants.MESSAGES), getActivity(), R.layout.chat_message, username);
                    listView.setAdapter(mChatListAdapter);
                    mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
                        @Override
                        public void onChanged() {
                            super.onChanged();
                            listView.setSelection(mChatListAdapter.getCount() - 1);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        final EditText inputText = (EditText) rootView.findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage(inputText);
                }
                return true;
            }

        });

        rootView.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(inputText);
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mChatListAdapter.cleanup();
    }

    private void sendMessage(EditText inputText) {
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            ChatMessage chatMessage = new ChatMessage(input, username, new Date());
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseChatRef.child(Constants.MESSAGES).push().setValue(chatMessage);
            inputText.setText("");
        }
    }
}
