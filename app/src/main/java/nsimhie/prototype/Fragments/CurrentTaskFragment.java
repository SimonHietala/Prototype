package nsimhie.prototype.Fragments;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import nsimhie.prototype.GPS;
import nsimhie.prototype.InternetConnection;
import nsimhie.prototype.MainActivity;
import nsimhie.prototype.R;
import nsimhie.prototype.WorkTask;

public class CurrentTaskFragment extends Fragment implements Observer {

    private Chronometer chronometer;
    private long startTime;
    //private long pauseTime;
    private static WorkTask currentTask = new WorkTask();
    private InternetConnection ic;
    private View currentView;
    private EditText ctEtNotes;
    private String json = null;
    private boolean newTask;
    private boolean isCounting = false;

    @Override
    public void onPause() {
        super.onPause();
        saveToPrefs();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        setMyView(currentView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);
        readFromPrefs();
        //setNotification();
        currentView = rootView;
        chronometer = (Chronometer) rootView.findViewById(R.id.ctaskChronometer);
        ctEtNotes = (EditText) rootView.findViewById(R.id.ctaskEtNotes);
        ic = new InternetConnection(getActivity());
        ic.addObserver(this);

        if(this.newTask)
        {
            chronometer.setBase(startTime);
            if(isCounting()) {
                finishTask(currentView);
            }
            this.newTask = false;
        }

        setMyView(currentView);
        //Chronometer setup
        if(startTime == 0)
        {
            startTime = SystemClock.elapsedRealtime();
        }

        chronometer.setBase(startTime);
        chronometer.start();
        setIsCounting(true);

        /*
        final ImageButton btnPause = (ImageButton) rootView.findViewById(R.id.ctaskImgPause);
        btnPause.setTag("pause");
        */

        ImageButton btnStop = (ImageButton) rootView.findViewById(R.id.ctaskImgStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCounting()) {
                    finishTask(rootView);

                    FragmentManager.BackStackEntry backEntry;
                    try {
                        backEntry = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2);
                    }

                    catch (IndexOutOfBoundsException e)
                    {
                        backEntry = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount()-1);
                    }
                        String str = backEntry.getName();
                        Fragment fragment = getFragmentManager().findFragmentByTag(str);
                        Helpers h = new Helpers(getActivity(), CurrentTaskFragment.this);
                        h.mySetTitle(fragment);

                    getFragmentManager().popBackStack();
                }
            }
        });

        Button save = (Button) rootView.findViewById(R.id.ctaskBtnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTask.setNotes(ctEtNotes.getText().toString());
                Toast.makeText(getActivity(), getResources().getString(R.string.ctask_saved_note), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the pause button is showing
                if(btnPause.getTag()=="pause")
                {
                    pauseTime = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();

                    btnPause.setTag("play");
                    btnPause.setImageResource(android.R.drawable.ic_media_play);
                }

                //If play button is showing
                else
                {
                    chronometer.setBase(SystemClock.elapsedRealtime() + pauseTime);
                    chronometer.start();

                    btnPause.setTag("pause");
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
        */
        return rootView;
    }

    @Override
    public void update(Observable observable, Object data) {
        int i = Integer.parseInt(ic.getMyResponse());
        currentTask.setId(i);
    }

    //Takes the values from the currenttask and sets the view.
    public void setMyView(View rootView)
    {
        TextView tvLocation = (TextView) rootView.findViewById(R.id.ctaskTvLocation);
        TextView tvTask = (TextView) rootView.findViewById(R.id.ctaskTvTask);
        TextView tvStart = (TextView) rootView.findViewById(R.id.ctaskTvStart);
        TextView tvGps = (TextView) rootView.findViewById(R.id.ctaskTvGps);

        if(json!=null) {
            try {
                JSONObject jsonObject = new JSONObject(json);

                currentTask.setTask(jsonObject.get("task").toString());
                currentTask.setLocation(jsonObject.get("location").toString());
                currentTask.setStartTime(currentTask.getCurrentTime());
                currentTask.setNotes(jsonObject.get("notes").toString());
                currentTask.setEdited(false);

                if (jsonObject.getBoolean("inmotion")) {
                    GPS gps = new GPS(getActivity());
                    currentTask.setGps(gps.getCoordinates());
                    currentTask.setInMotion(true);
                    gps.stopGPS();
                } else {
                    currentTask.setGps(jsonObject.get("gps").toString());
                    currentTask.setInMotion(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        json = null;

        tvTask.setText(currentTask.getTask());
        tvLocation.setText(currentTask.getLocation());
        tvStart.setText(currentTask.getStartTime());
        tvGps.setText(currentTask.getGps());
        ctEtNotes.setText(currentTask.getNotes());
    }

    //Makes a jsonobject of the currentTask
    public JSONObject ownGetView()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("task", currentTask.getTask());
            jsonObject.put("location", currentTask.getLocation());
            jsonObject.put("starttime", currentTask.getStartTime());
            currentTask.setStopStime(currentTask.getCurrentTime());
            jsonObject.put("stoptime", currentTask.getStopStime());
            currentTask.recalculateTime();
            jsonObject.put("time",currentTask.getTime());
            jsonObject.put("timeinseconds",currentTask.getTimeInSeconds());
            jsonObject.put("gps", currentTask.getGps());
            jsonObject.put("notes", currentTask.getNotes());
            jsonObject.put("inmotion", currentTask.isInMotion());
            jsonObject.put("edited", currentTask.isEdited());
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void setArgumentsOwn(String json)
    {
        this.json = json;
        this.newTask = true;
    }

    public void saveToPrefs()
    {
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.app_prefs), 0);
        SharedPreferences.Editor editor = preferences.edit();
        //editor.putLong("pauseTime", pauseTime);
        editor.putLong("startTime", startTime);
        editor.apply();
    }

    private void readFromPrefs()
    {
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.app_prefs), 0);
        //pauseTime = preferences.getLong("pauseTime",0);
        startTime = preferences.getLong("startTime",0);

    }

    private void finishTask(View view)
    {
        ic.postRequest(ownGetView(), getString(R.string.URL_POST_WORKTASK));
        currentTask = new WorkTask();
        //pauseTime = 0;
        startTime = 0;
        chronometer.stop();
        setIsCounting(false);
        removeNotification();
        //btnPause.setTag("play");
        //btnPause.setImageResource(android.R.drawable.ic_media_play);
    }

    public boolean isCounting()
    {
        return this.isCounting;
    }

    public void setIsCounting(boolean state)
    {
        if(state)
        {
            setNotification();
        }

        this.isCounting = state;
    }

    private void setNotification() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.app_prefs), 0);
        boolean saved = preferences.getBoolean("notifications", true);

        if(saved)
        {
            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getActivity())
                            .setSmallIcon(R.drawable.ic_ticking)
                            .setContentTitle(getString(R.string.ctask_notification_title))
                            .setContentText(getString(R.string.ctask_notification_text));
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(getActivity(), MainActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(200, mBuilder.build());
        }
    }

    private void removeNotification(){
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }
}
