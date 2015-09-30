package com.fleetmatics.chat.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.fleetmatics.chat.R;
import com.fleetmatics.chat.model.User;
import com.fleetmatics.chat.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>User</code> class to encapsulate the
 * data for each individual chat message
 */
public class UsersListAdapter extends FirebaseListAdapter<User> implements Filterable {
    private List<User> originalData = null;
    private List<User> filteredData = null;

    public UsersListAdapter(Query ref, Activity activity, int layout) {
        super(ref, User.class, layout, activity);
    }

    /**
     * Bind an instance of the <code>User</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>User</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param user An instance representing the current user
     */
    @Override
    protected void populateView(View view, User user) {
        if (user.getUsername().equalsIgnoreCase(Utils.getUsername(view.getContext()))) {
            view.setVisibility(View.GONE);
        } else {
            String name = user.getName();
            if (TextUtils.isEmpty(name.trim())) {
                name = user.getUsername();
            }
            TextView usrFullName = (TextView) view.findViewById(R.id.usrFullName);
            usrFullName.setText(name);
            TextView usrStatus = (TextView) view.findViewById(R.id.usrStatus);
            usrStatus.setText(user.getUserStatus().name());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);
            ((TextView) view.findViewById(R.id.last_online_value)).setText(user.getLastOnline() != null ? simpleDateFormat.format(user.getLastOnline()) : "N/A");
        }
    }

    @Override
    public Filter getFilter() {
        return new ItemFilter();
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            originalData = getModels();
            final List<User> list = originalData;

            int count = list.size();
            final ArrayList<User> nlist = new ArrayList<>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getUsername();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }

    }
}