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
    private ArrayList<WorkTask> workTasksHistory = new ArrayList<>();
    private InternetConnection ic;
    private HistoryRowAdapter adapter;
    private Context context;
    private CurrentTaskFragment currentTaskFragment;

    public HistoryFragment() {}

    public HistoryFragment(CurrentTaskFragment currentTaskFragment){
        this.currentTaskFragment = currentTaskFragment;
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.historyListView);

        ic = new InternetConnection(getActivity());
        ic.addObserver(this);

        adapter = new HistoryRowAdapter(workTasksHistory, getActivity(), getActivity().getFragmentManager(), currentTaskFragment);
        listView.setAdapter(adapter);

        ic.getRequest("/worktasks");

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
                wt.setStopStime(row.getString("stoptime"));
                wt.setTime(row.getString("time"));
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

    @Override
    public void update(Observable observable, Object data)
    {
        workTasksHistory.clear();
        parseData(ic.getMyResponse());
        adapter.notifyDataSetChanged();
    }
}
