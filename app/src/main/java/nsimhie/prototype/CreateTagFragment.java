package nsimhie.prototype;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        Button create = (Button) rootView.findViewById(R.id.btnCreate);
        create.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Start
                        pushed = true;
                        Toast.makeText(getActivity(), "Held down", Toast.LENGTH_LONG).show();
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
