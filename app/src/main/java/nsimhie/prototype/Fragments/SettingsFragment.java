package nsimhie.prototype.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import nsimhie.prototype.R;


public class SettingsFragment extends Fragment {

    public SettingsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button languageSettings = (Button) rootView.findViewById(R.id.settingsBtnLanguage);
        languageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }
        });

        Switch notifications = (Switch) rootView.findViewById(R.id.settingsSwitchNotifications);
        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.app_prefs), 0);
                SharedPreferences.Editor editor = preferences.edit();
                if(isChecked)
                {
                    editor.putBoolean("notifications", true);
                }

                else
                {
                    editor.putBoolean("notifications", false);
                }
                editor.apply();
            }
        });

        return rootView;
    }
}
