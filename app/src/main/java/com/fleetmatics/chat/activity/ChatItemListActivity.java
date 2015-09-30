package com.fleetmatics.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.fleetmatics.chat.utils.Utils;

public class ChatItemListActivity extends AppCompatActivity
        implements ChatItemListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ValueEventListener mConnectedListener;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatitem_list);
        toolbar = (Toolbar) findViewById(R.id.title_bar);
        ((TextView) toolbar.findViewById(R.id.title)).setText("Chatting as " + Utils.getUsername(this));
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.chatitem_detail_container) != null) {
            mTwoPane = true;
            ((ChatItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.chatitem_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Callback method from {@link ChatItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String selectedUserId) {
        String roomId = "room_" + Utils.getUserId(this) + "_" + selectedUserId;
        String roomIdReverse = "room_" + selectedUserId + "_" + Utils.getUserId(this);
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(Constants.PARAM_ROOM_ID, roomId);
            arguments.putString(Constants.PARAM_ROOM_ID_REVERSE, roomIdReverse);
            arguments.putString(Constants.PARAM_CHAT_WITH_USR, selectedUserId);
            ChatItemDetailFragment fragment = new ChatItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.chatitem_detail_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, ChatItemDetailActivity.class);
            detailIntent.putExtra(Constants.PARAM_ROOM_ID, roomId);
            detailIntent.putExtra(Constants.PARAM_ROOM_ID_REVERSE, roomIdReverse);
            detailIntent.putExtra(Constants.PARAM_CHAT_WITH_USR, selectedUserId);
            startActivity(detailIntent);
        }
    }

//    @Override
//    public void onBackPressed() {
//        ChatApplication.getMyFirebaseRef().unauth();
//        Utils.cleanup(getApplicationContext());
//        Intent login = new Intent(this, LoginActivity.class);
//        startActivity(login);
//        super.onBackPressed();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        // Finally, a little indication of connection status
        mConnectedListener = ChatApplication.getMyFirebaseRef().getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    toolbar.findViewById(R.id.userPresence).setVisibility(View.VISIBLE);
                    ((ImageView) toolbar.findViewById(R.id.userPresence)).setImageResource(R.drawable.green_circle);
                } else {
                    toolbar.findViewById(R.id.userPresence).setVisibility(View.VISIBLE);
                    ((ImageView) toolbar.findViewById(R.id.userPresence)).setImageResource(R.drawable.red_circle);
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

}