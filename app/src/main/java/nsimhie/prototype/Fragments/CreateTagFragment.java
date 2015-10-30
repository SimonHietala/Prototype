package nsimhie.prototype.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import nsimhie.prototype.GPS;
import nsimhie.prototype.R;

/**
 * Created by nsimhie on 2015-10-22.
 */
public class CreateTagFragment extends Fragment
{
    private boolean pushed;

    public CreateTagFragment(){}

    public void OnCreate(Bundle savedInstanceState)
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_create_tag, container, false);
        pushed = false;
        final EditText etGps = (EditText)rootView.findViewById(R.id.etGps);

        //Get the gps location
        GPS gps = new GPS(getActivity());
        final String text = Double.toString(gps.getLatitude()) + "\n" + Double.toString(gps.getLongitude());
        etGps.setText(text);
        gps.stopGPS();

        final CheckBox checkBox = (CheckBox)rootView.findViewById(R.id.cbMotion);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
                if(isChecked)
                {
                    etGps.setTextColor(Color.GRAY);
                    etGps.setFocusable(false);
                }

                else
                {
                    etGps.setTextColor(Color.BLACK);
                    etGps.setFocusable(true);
                }

            }
        });

        //Force user to push the button to enable writing of tag.
        Button create = (Button) rootView.findViewById(R.id.btnCreate);
        create.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start
                        pushed = true;
                        //Toast.makeText(getActivity(), "Held down", Toast.LENGTH_LONG).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        // End
                        pushed = false;
                        break;
                }
                return false;
            }
        });

        return rootView;
    }

    public boolean getButtonState()
    {
        return this.pushed;
    }

}
