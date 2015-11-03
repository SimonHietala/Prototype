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

import java.util.ArrayList;

import nsimhie.prototype.HistoryRowAdapter;
import nsimhie.prototype.R;
import nsimhie.prototype.WorkTask;


public class HistoryFragment extends Fragment
{
    private ListView listView;
    private ArrayList<WorkTask> workTasks = new ArrayList<>();
    private HistoryRowAdapter adapter;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView) rootView.findViewById(R.id.historyListView);

        adapter = new HistoryRowAdapter(workTasks, getActivity());
        listView.setAdapter(adapter);

        //Fake data
        fakeData();

        adapter.notifyDataSetChanged();

        return rootView;
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
        workTasks.add(wt);

        WorkTask wt1 = new WorkTask();
        wt1.setTask("Plugga");
        wt1.setTime("2h");
        wt1.setStartTime("19.00");
        wt1.setStopStime("21.33");
        wt1.setLocation("skolan");
        wt1.setGps("65 , 22");
        wt1.setNotes("Asta blev arg.");
        workTasks.add(wt1);

        WorkTask wt2 = new WorkTask();
        wt2.setTask("jobba");
        wt2.setTime("3h");
        wt2.setStartTime("06.30");
        wt2.setStopStime("15.00");
        wt2.setLocation("kontoret");
        wt2.setGps("65 , 22");
        wt2.setNotes("Jag har jobbat väldigt hårt idag, men jag undrar hur det ser ut om jag skriver något väldigt långt här.");
        workTasks.add(wt2);

        WorkTask wt3 = new WorkTask();
        wt3.setTask("hoppa");
        wt3.setTime("4h");
        wt3.setStartTime("06.30");
        wt3.setStopStime("15.00");
        wt3.setLocation("kontoret");
        wt3.setGps("65 , 22");
        wt3.setNotes("Jag har jobbat väldigt hårt idag.");
        wt3.setEdited(true);
        workTasks.add(wt3);

        WorkTask wt4 = new WorkTask();
        wt4.setTask("jobba");
        wt4.setTime("5h");
        wt4.setStartTime("06.30");
        wt4.setStopStime("15.00");
        wt4.setLocation("kontoret");
        wt4.setGps("65 , 22");
        wt4.setNotes("Jag har jobbat väldigt hårt idag.");
        workTasks.add(wt4);

    }
}
