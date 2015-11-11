package nsimhie.prototype.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import nsimhie.prototype.GPS;
import nsimhie.prototype.InternetConnection;
import nsimhie.prototype.R;
import nsimhie.prototype.WorkTask;

public class TaskFragment extends Fragment implements Observer {
    TextView timerTextView;
    long startTime = 0;
    WorkTask currentTask = new WorkTask();
    public InternetConnection ic;

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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        if (getArguments()!=null)
        {
            String json = getArguments().getString("json");
            setView(rootView, json);
        }

        ic = new InternetConnection(getActivity());
        ic.addObserver(this);


        timerTextView = (TextView) rootView.findViewById(R.id.taskLlTimer).findViewById(R.id.TaskTvTimer);

        Button start = (Button) rootView.findViewById(R.id.taskBtnSave);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic.postRequest(getView(rootView), "/post");

            }
        });


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

    public void setView(View rootView, String json)
    {
        TextView tvLocation = (TextView) rootView.findViewById(R.id.taskTvLocation);
        TextView tvTask = (TextView) rootView.findViewById(R.id.taskTvTask);
        TextView tvStart = (TextView) rootView.findViewById(R.id.taskTvStart);
        TextView tvGps = (TextView) rootView.findViewById(R.id.taskTvGps);
        EditText etNotes = (EditText) rootView.findViewById(R.id.taskEtNotes);

        try
        {
            JSONObject jsonObject = new JSONObject(json);

            String task = jsonObject.get("task").toString();
            tvTask.setText(task);

            String location = jsonObject.get("location").toString();
            tvLocation.setText(location);

            String startTime = getTime();
            tvStart.setText(startTime);

            if(jsonObject.getBoolean("inmotion"))
            {
                GPS gps = new GPS(getActivity());
                tvGps.setText(gps.getCoordinates());
                gps.stopGPS();
            }

            else
            {
                String gps = jsonObject.get("gps").toString();
                tvGps.setText(gps);
            }

            String notes = jsonObject.get("notes").toString();
            etNotes.setText(notes);

            currentTask.setTask(tvTask.getText().toString());
            currentTask.setLocation(tvLocation.getText().toString());
            currentTask.setStartTime(tvStart.getText().toString());
            currentTask.setInMotion(jsonObject.getBoolean("inmotion"));
            currentTask.setGps(tvGps.getText().toString());
            currentTask.setNotes(etNotes.getText().toString());
            currentTask.setEdited(false);
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getView(View rootView)
    {
        TextView tvLocation = (TextView) rootView.findViewById(R.id.taskTvLocation);
        TextView tvTask = (TextView) rootView.findViewById(R.id.taskTvTask);
        TextView tvStart = (TextView) rootView.findViewById(R.id.taskTvStart);
        EditText etNotes = (EditText) rootView.findViewById(R.id.taskEtNotes);
        TextView tvGps = (TextView) rootView.findViewById(R.id.taskTvGps);


        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("task", currentTask.getTask());
            jsonObject.put("location", currentTask.getLocation());
            jsonObject.put("gps", currentTask.getGps());
            jsonObject.put("starttime", currentTask.getStartTime());
            jsonObject.put("edited", currentTask.isEdited());
            jsonObject.put("inmotion", currentTask.isInMotion());
            jsonObject.put("notes", etNotes.getText());
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void update(Observable observable, Object data) {
        int i = Integer.parseInt(ic.getMyResponse());
        //Toast.makeText(getActivity(), "Given id: " + i , Toast.LENGTH_LONG).show();
        currentTask.setId(i);
    }
}
