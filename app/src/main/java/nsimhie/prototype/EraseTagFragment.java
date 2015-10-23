package nsimhie.prototype;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by nsimhie on 2015-10-22.
 */
public class EraseTagFragment extends Fragment
{
    private boolean pushed;

    public EraseTagFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_erase_tag, container, false);
        pushed=false;

        Button erase = (Button) rootView.findViewById(R.id.btnErase);
        erase.setOnTouchListener(new View.OnTouchListener() {
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
        return pushed;
    }
}
