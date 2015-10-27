package nsimhie.prototype;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class HistoryFragment extends Fragment
{
    private ListView listView;
    private ArrayList<WorkTask> workTasks = new ArrayList<>();
    private HistoryRowAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView) rootView.findViewById(R.id.historyListView);
        adapter = new HistoryRowAdapter(workTasks, getActivity());
        listView.setAdapter(adapter);

        //Fake data
        for(int i = 0; i<5; i++) {
            WorkTask wt = new WorkTask();
            wt.setTask(getText(R.string.row_task).toString() + "jobba");
            wt.setTime(getText(R.string.row_time).toString() + "8h");
            wt.setStartTime(getText(R.string.row_start_time).toString() + "06.30");
            wt.setStopStime(getText(R.string.row_stop_time).toString() + "15.00");
            wt.setLocation(getText(R.string.row_location).toString() + "kontoret");
            wt.setGps(getText(R.string.row_gps).toString() + "65 , 22");
            wt.setNotes(getText(R.string.row_notes).toString() + "Jag har jobbat väldigt hårt idag.");
            workTasks.add(wt);
        }

        adapter.notifyDataSetChanged();



        return rootView;
    }

}
