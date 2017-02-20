package com.chenguang.materialpalette.fragment;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

import com.chenguang.materialpalette.BuildConfig;
import com.chenguang.materialpalette.R;

public class AboutAppFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private AboutAppClickListener aboutAppClickListener;

    public interface AboutAppClickListener {

        void onCopyrightClicked();
    }

    public static AboutAppFragment newInstance() {
        AboutAppFragment aboutAppFragment = new AboutAppFragment();
        aboutAppFragment.setRetainInstance(true);
        return aboutAppFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            aboutAppClickListener = (AboutAppClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent activity must implement " + AboutAppClickListener.class.getName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about_app);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findPreference(getString(R.string.key_app_version))
                .setSummary(getString(R.string.value_app_version,
                        BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));

        findPreference(getString(R.string.key_copyright))
                .setOnPreferenceClickListener(this);
    }

    @Override
    public void onDetach() {
        aboutAppClickListener = null;
        super.onDetach();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getString(R.string.key_copyright))) {
            aboutAppClickListener.onCopyrightClicked();
        }

        return false;
    }
}
