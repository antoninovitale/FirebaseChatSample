package com.fleetmatics.chat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.fleetmatics.chat.R;

/**
 * Created by antoninovitale 31/07/15.
 * Copyright © 2015. Fleetmatics Development Limited. All rights reserved.
 **/
public class HomeButtonsAdapter extends BaseAdapter {
        private Context mContext;

        public HomeButtonsAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.new_jobs_icon, R.drawable.active_jobs_icon,
                R.drawable.completed_jobs_icon, R.drawable.sync_docs_icon,
                R.drawable.clients_icon, R.drawable.add_jobs_icon,
                R.drawable.map_icon, R.drawable.quotes_icon,
                R.drawable.settings_icon, R.drawable.support_icon,
                R.drawable.logout_icon, R.drawable.chat_icon
        };

}
