package nsimhie.prototype.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;

import nsimhie.prototype.R;

public class CurrentTaskFragment extends Fragment {

    private Chronometer chronometer;
    private long timeWhenPaused = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            timeWhenPaused = savedInstanceState.getLong("timeWhenPaused");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);

        if(timeWhenPaused == 0)
        {
            chronometer = (Chronometer) rootView.findViewById(R.id.ctaskChronometer);
            chronometer.setBase(SystemClock.elapsedRealtime()+ timeWhenPaused);
            chronometer.start();
        }

        ImageButton btnStop = (ImageButton) rootView.findViewById(R.id.ctaskImgStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeWhenPaused = 0;
                chronometer.stop();
            }
        });


        final ImageButton btnPause = (ImageButton) rootView.findViewById(R.id.ctaskImgPause);
        btnPause.setTag("pause");
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnPause.getTag() == "play") {
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                    btnPause.setTag("pause");
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
                    chronometer.start();
                } else {
                    btnPause.setImageResource(android.R.drawable.ic_media_play);
                    btnPause.setTag("play");
                    timeWhenPaused = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    chronometer.clearAnimation();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("timeWhenPaused", chronometer.getBase() - SystemClock.elapsedRealtime());
    }

}
