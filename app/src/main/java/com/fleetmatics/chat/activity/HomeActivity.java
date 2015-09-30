package com.fleetmatics.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fleetmatics.chat.ChatApplication;
import com.fleetmatics.chat.R;
import com.fleetmatics.chat.adapter.HomeButtonsAdapter;
import com.fleetmatics.chat.utils.Utils;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.title_bar);
        ((TextView) toolbar.findViewById(R.id.title)).setText(R.string.title_activity_home);
        toolbar.findViewById(R.id.userPresence).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new HomeButtonsAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(position==10) {
                    onBackPressed();
                }
                else if(position==11) {
                    Intent main = new Intent(HomeActivity.this, ChatItemListActivity.class);
                    startActivity(main);
                }
                else {
                    Snackbar.make(gridview, "Come on man, it's just a demo!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        Button clock_in_button = (Button) findViewById(R.id.clock_in_button);
        clock_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Seriously?!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        ChatApplication.getMyFirebaseRef().unauth();
        Utils.cleanup(getApplicationContext());
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        super.onBackPressed();
    }

}