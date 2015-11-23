package nsimhie.prototype.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

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
    private Spinner sortSpinner;

    public HistoryFragment() {}

    public HistoryFragment(CurrentTaskFragment currentTaskFragment){
        this.currentTaskFragment = currentTaskFragment;
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.historyListView);

        sortSpinner = (Spinner) rootView.findViewById(R.id.historySpinner);

        ic = new InternetConnection(getActivity());
        ic.addObserver(this);

        adapter = new HistoryRowAdapter(workTasksHistory, getActivity(), getActivity().getFragmentManager(), currentTaskFragment);
        listView.setAdapter(adapter);

        ic.getRequest("/worktasks/headers");

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Get competlete history
                if(sortSpinner.getSelectedItem().toString().equals(getString(R.string.history_spinner_base)))
                {
                    ic.getRequest("/worktasks");
                }

                //Get task specific history
                else
                {
                    ic.getRequest("/worktasks/headers/" + sortSpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        return rootView;
    }


    public void readHeaderData(String data)
    {

        ArrayList<String> headLines = new ArrayList<String>();
        headLines.add(getString(R.string.history_spinner_base));
        try
        {
            JSONObject json = new JSONObject(data);
            JSONArray array = json.getJSONArray("array");
            for(int i=0; i < array.length(); i++)
            {
                headLines.add(array.getString(i));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        sortSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, headLines));

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
        if(ic.getResponseUrl().equals("/worktasks"))
        {
            workTasksHistory.clear();
            parseData(ic.getMyResponse());
            adapter.notifyDataSetChanged();
        }

        else if(ic.getResponseUrl().equals("/worktasks/headers"))
        {
            readHeaderData(ic.getMyResponse());
        }

        else
        {
            workTasksHistory.clear();
            parseData(ic.getMyResponse());
            adapter.notifyDataSetChanged();
        }
    }
}
