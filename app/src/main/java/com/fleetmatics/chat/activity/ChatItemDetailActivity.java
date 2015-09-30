package com.fleetmatics.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.fleetmatics.chat.ChatApplication;
import com.fleetmatics.chat.R;
import com.fleetmatics.chat.utils.Constants;

import java.util.Map;

/**
 * An activity representing a single ChatItem detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ChatItemListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ChatItemDetailFragment}.
 */
@SuppressWarnings("unchecked")
public class ChatItemDetailActivity extends AppCompatActivity {
    private String chatWithUserId;
    private ValueEventListener mConnectedListener;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatitem_detail);
        chatWithUserId = getIntent().getStringExtra(Constants.PARAM_CHAT_WITH_USR);
        toolbar = (Toolbar) findViewById(R.id.title_bar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ChatApplication.getMyFirebaseRef().child(Constants.USERS).orderByChild(Constants.USER_ID).equalTo(chatWithUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> value = (Map<String, Object>) dataSnapshot.getChildren().iterator().next().getValue();
                    ((TextView) toolbar.findViewById(R.id.title)).setText("Chatting with " + value.get(Constants.USERNAME));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(Constants.PARAM_ROOM_ID, getIntent().getStringExtra(Constants.PARAM_ROOM_ID));
            arguments.putString(Constants.PARAM_ROOM_ID_REVERSE, getIntent().getStringExtra(Constants.PARAM_ROOM_ID_REVERSE));
            arguments.putString(Constants.PARAM_CHAT_WITH_USR, getIntent().getStringExtra(Constants.PARAM_CHAT_WITH_USR));
            ChatItemDetailFragment fragment = new ChatItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.chatitem_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Finally, a little indication of connection status
        mConnectedListener = ChatApplication.getMyFirebaseRef().getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    ((ImageView) toolbar.findViewById(R.id.userPresence)).setImageResource(R.drawable.green_circle);
                    toolbar.findViewById(R.id.userPresence).setVisibility(View.VISIBLE);
                } else {
                    ((ImageView) toolbar.findViewById(R.id.userPresence)).setImageResource(R.drawable.red_circle);
                    toolbar.findViewById(R.id.userPresence).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        ChatApplication.getMyFirebaseRef().getRoot().child(".info/connected").removeEventListener(mConnectedListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ChatItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
