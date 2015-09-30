package com.fleetmatics.chat.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.fleetmatics.chat.ChatApplication;
import com.fleetmatics.chat.R;
import com.fleetmatics.chat.model.User;
import com.fleetmatics.chat.utils.Constants;
import com.fleetmatics.chat.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
    private static Firebase usersRef;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            if (value == null) {
                return false;
            }
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                listPreference.setValueIndex(index);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
                ((EditTextPreference) preference).setText(stringValue);
            }
            syncDataToFirebase(preference, stringValue);
            Utils.saveValueToPrefs(preference.getContext(), preference.getKey(), stringValue);
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void syncDataToFirebase(Preference pref, String value) {
        if (pref == null || value == null) {
            return;
        }
        if (pref.getKey().equalsIgnoreCase(pref.getContext().getString(R.string.pref_display_name))) {
            Map<String, Object> newValue = new HashMap<String, Object>();
            newValue.put("name", value);
            usersRef.updateChildren(newValue);
        } else if (pref.getKey().equalsIgnoreCase(pref.getContext().getString(R.string.pref_status))) {
            Map<String, Object> newValue = new HashMap<String, Object>();
            newValue.put("userStatus", !TextUtils.isEmpty(value) ? Integer.valueOf(value) : 0);
            usersRef.updateChildren(newValue);
        } else if (pref.getKey().equalsIgnoreCase(pref.getContext().getString(R.string.pref_profession))) {
            Map<String, Object> newValue = new HashMap<String, Object>();
            newValue.put("profession", value);
            usersRef.updateChildren(newValue);
        } else if (pref.getKey().equalsIgnoreCase(pref.getContext().getString(R.string.pref_address))) {
            Map<String, Object> newValue = new HashMap<String, Object>();
            newValue.put("address", value);
            usersRef.updateChildren(newValue);
        }
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(final Preference preference, final int key) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String value = null;
                switch (key) {
                    case R.string.pref_display_name:
                        value = !TextUtils.isEmpty(user.getName()) ? user.getName() : "";
                        break;
                    case R.string.pref_address:
                        value = !TextUtils.isEmpty(user.getAddress()) ? user.getAddress() : "";
                        break;
                    case R.string.pref_status:
                        value = String.valueOf(user.getUserStatus().getValue());
                        break;
                    case R.string.pref_profession:
                        value = !TextUtils.isEmpty(user.getProfession()) ? user.getProfession() : "";
                        break;
                }
                sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, value);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersRef = ChatApplication.getMyFirebaseRef().child(Constants.USERS).child(Utils.getUserId(getApplicationContext()));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar bar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            root.addView(bar, 0); // insert at top
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);

            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = bar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(bar);
        }
        final Drawable upArrow = bar.getNavigationIcon();
        if (upArrow != null) {
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            bar.setNavigationIcon(upArrow);
        }
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName(Constants.SHARED_PREFS);
            addPreferencesFromResource(R.xml.pref_general);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_display_name)), R.string.pref_display_name);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_address)), R.string.pref_address);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_status)), R.string.pref_status);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_profession)), R.string.pref_profession);
        }
    }

}