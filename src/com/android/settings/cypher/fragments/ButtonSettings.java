/*
 * Copyright (C) 2016 CypherOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cypher.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceScreen;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.dashboard.SummaryLoader;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import java.util.ArrayList;
import java.util.List;

public class ButtonSettings extends SettingsPreferenceFragment implements 
        Preference.OnPreferenceChangeListener {
			
    private static final String TAG = "ButtonSettings";
	
	private static final String KEY_VOLUME_KEY_CURSOR_CONTROL = "volume_key_cursor_control";

    private ListPreference mVolumeKeyCursorControl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.button_settings);
		final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        int cursorControlAction = Settings.System.getInt(resolver,
                Settings.System.VOLUME_KEY_CURSOR_CONTROL, 0);
        mVolumeKeyCursorControl = initActionList(KEY_VOLUME_KEY_CURSOR_CONTROL,
                cursorControlAction);
		
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.ADDITIONS;
    }
	
	private ListPreference initActionList(String key, int value) {
        ListPreference list = (ListPreference) getPreferenceScreen().findPreference(key);
        list.setValue(Integer.toString(value));
        list.setSummary(list.getEntry());
        list.setOnPreferenceChangeListener(this);
        return list;
    }

    private void handleActionListChange(ListPreference pref, Object newValue, String setting) {
        String value = (String) newValue;
        int index = pref.findIndexOfValue(value);
        pref.setSummary(pref.getEntries()[index]);
        Settings.System.putInt(getActivity().getContentResolver(), setting, Integer.valueOf(value));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mVolumeKeyCursorControl) {
            handleActionListChange(mVolumeKeyCursorControl, newValue,
                    Settings.System.VOLUME_KEY_CURSOR_CONTROL);
            return true;
        }
        return false;
    }
}