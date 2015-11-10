package nsimhie.prototype.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.NodeChangeListener;

import nsimhie.prototype.HistoryRowAdapter;
import nsimhie.prototype.InternetConnection;
import nsimhie.prototype.R;
import nsimhie.prototype.WorkTask;


public class HistoryFragment extends Fragment implements Observer
{
    public ArrayList<WorkTask> workTasksHistory = new ArrayList<>();
    public InternetConnection ic;
    public HistoryRowAdapter adapter;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.historyListView);

        ic = new InternetConnection(getActivity());
        ic.addObserver(this);

        adapter = new HistoryRowAdapter(workTasksHistory, getActivity(), getActivity().getFragmentManager());
        listView.setAdapter(adapter);

        if (workTasksHistory.size() == 0)
        {
            ic.getRequest("/worktasks");
        }

        return rootView;
    }


    public void parseData(String data)
    {
        try
        {
            JSONObject jo = new JSONObject(data);
            JSONArray array = jo.getJSONArray("array");
            for (int i = 0; i < array.length(); i++) {
                WorkTask wt = new WorkTask();
                JSONObject row = array.getJSONObject(i);
                wt.setId(row.getInt("id"));
                wt.setTask(row.getString("task"));
                wt.setLocation(row.getString("location"));
                wt.setStartTime(row.getString("starttime"));
                wt.setStartTime(row.getString("stoptime"));
                wt.setStartTime(row.getString("time"));
                wt.setNotes(row.getString("notes"));
                wt.setGps(row.getString("gps"));
                wt.setEdited(row.getBoolean("inmotion"));
                wt.setEdited(row.getBoolean("edited"));
                workTasksHistory.add(wt);
                }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void fakeData()
    {
        WorkTask wt = new WorkTask();
        wt.setTask("jobba");
        wt.setTime("1h");
        wt.setStartTime("06.30");
        wt.setStopStime("15.00");
        wt.setLocation("kontoret");
        wt.setGps("65 , 22");
        wt.setNotes("Jag har jobbat lite.");
        wt.setEdited(false);
        workTasksHistory.add(wt);

        WorkTask wt1 = new WorkTask();
        wt1.setTask("Plugga");
        wt1.setTime("2h");
        wt1.setStartTime("19.00");
        wt1.setStopStime("21.33");
        wt1.setLocation("skolan");
        wt1.setGps("65 , 22");
        wt1.setNotes("Asta blev arg.");
        workTasksHistory.add(wt1);

        WorkTask wt2 = new WorkTask();
        wt2.setTask("jobba");
        wt2.setTime("3h");
        wt2.setStartTime("06.30");
        wt2.setStopStime("15.00");
        wt2.setLocation("kontoret");
        wt2.setGps("65 , 22");
        wt2.setNotes("Jag har jobbat väldigt hårt idag, men jag undrar hur det ser ut om jag skriver något väldigt långt här.");
        workTasksHistory.add(wt2);

        WorkTask wt3 = new WorkTask();
        wt3.setTask("hoppa");
        wt3.setTime("4h");
        wt3.setStartTime("06.30");
        wt3.setStopStime("15.00");
        wt3.setLocation("kontoret");
        wt3.setGps("65 , 22");
        wt3.setNotes("Jag har jobbat väldigt hårt idag.");
        wt3.setEdited(true);
        workTasksHistory.add(wt3);

        WorkTask wt4 = new WorkTask();
        wt4.setTask("jobba");
        wt4.setTime("5h");
        wt4.setStartTime("06.30");
        wt4.setStopStime("15.00");
        wt4.setLocation("kontoret");
        wt4.setGps("65 , 22");
        wt4.setNotes("Jag har jobbat väldigt hårt idag.");
        workTasksHistory.add(wt4);

    }

    @Override
    public void update(Observable observable, Object data)
    {
        workTasksHistory.clear();
        parseData(ic.getMyResponse());
        adapter.notifyDataSetChanged();
    }
}
