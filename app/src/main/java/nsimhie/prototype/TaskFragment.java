package nsimhie.prototype;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskFragment extends Fragment {
    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%d:%02d", hours, minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        String json = getArguments().getString("json");
        setView(rootView, json);


        timerTextView = (TextView) rootView.findViewById(R.id.taskLlTimer).findViewById(R.id.TaskTvTimer);

        Button b = (Button) rootView.findViewById(R.id.button);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        //timerHandler.removeCallbacks(timerRunnable);
    }

    public void onResume(){
        super.onResume();
        timerHandler.removeCallbacks(timerRunnable);
    }

    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public void setView(View rootView, String json){
        TextView tvLocation = (TextView) rootView.findViewById(R.id.taskTvLocation);
        TextView tvTask = (TextView) rootView.findViewById(R.id.taskTvTask);
        TextView tvStart = (TextView) rootView.findViewById(R.id.taskTvStart);
        EditText etNotes = (EditText) rootView.findViewById(R.id.taskEtNotes);

        try
        {
            JSONObject jsonObject = new JSONObject(json);

            tvTask.setText(jsonObject.get("task").toString());

            String location = getString(R.string.task_location) + jsonObject.get("location").toString();
            tvLocation.setText(location);

            String startTime = getString(R.string.task_start_time) + getTime();
            tvStart.setText(startTime);

            String notes = jsonObject.get("notes").toString();
            etNotes.setText(notes);




        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
