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


        //Listener for when a row in the listview is clicked.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //parent.getChildAt(position).setBackgroundColor(Color.BLUE);

                RelativeLayout rl = (RelativeLayout) parent.getChildAt(position).findViewById(R.id.expansion);

                if(rl.getVisibility() == View.GONE)
                {
                    rl.setVisibility(View.VISIBLE);
                }
                else if (rl.getVisibility() == View.VISIBLE)
                {
                    rl.setVisibility(View.GONE);
                }
            }
        });

        return rootView;
    }

    public void fakeData()
    {
        WorkTask wt = new WorkTask();
        wt.setTask(getText(R.string.row_task).toString() + "jobba");
        wt.setTime(getText(R.string.row_time).toString() + "1h");
        wt.setStartTime(getText(R.string.row_start_time).toString() + "06.30");
        wt.setStopStime(getText(R.string.row_stop_time).toString() + "15.00");
        wt.setLocation(getText(R.string.row_location).toString() + "kontoret");
        wt.setGps(getText(R.string.row_gps).toString() + "65 , 22");
        wt.setNotes(getText(R.string.row_notes).toString() + "Jag har jobbat lite.");
        wt.setEdited(false);
        workTasks.add(wt);

        WorkTask wt1 = new WorkTask();
        wt1.setTask(getText(R.string.row_task).toString() + "Plugga");
        wt1.setTime(getText(R.string.row_time).toString() + "2h");
        wt1.setStartTime(getText(R.string.row_start_time).toString() + "19.00");
        wt1.setStopStime(getText(R.string.row_stop_time).toString() + "21.33");
        wt1.setLocation(getText(R.string.row_location).toString() + "skolan");
        wt1.setGps(getText(R.string.row_gps).toString() + "65 , 22");
        wt1.setNotes(getText(R.string.row_notes).toString() + "Asta blev arg.");
        workTasks.add(wt1);

        WorkTask wt2 = new WorkTask();
        wt2.setTask(getText(R.string.row_task).toString() + "jobba");
        wt2.setTime(getText(R.string.row_time).toString() + "3h");
        wt2.setStartTime(getText(R.string.row_start_time).toString() + "06.30");
        wt2.setStopStime(getText(R.string.row_stop_time).toString() + "15.00");
        wt2.setLocation(getText(R.string.row_location).toString() + "kontoret");
        wt2.setGps(getText(R.string.row_gps).toString() + "65 , 22");
        wt2.setNotes(getText(R.string.row_notes).toString() + "Jag har jobbat väldigt hårt idag.");
        workTasks.add(wt2);

        WorkTask wt3 = new WorkTask();
        wt3.setTask(getText(R.string.row_task).toString() + "hoppa");
        wt3.setTime(getText(R.string.row_time).toString() + "4h");
        wt3.setStartTime(getText(R.string.row_start_time).toString() + "06.30");
        wt3.setStopStime(getText(R.string.row_stop_time).toString() + "15.00");
        wt3.setLocation(getText(R.string.row_location).toString() + "kontoret");
        wt3.setGps(getText(R.string.row_gps).toString() + "65 , 22");
        wt3.setNotes(getText(R.string.row_notes).toString() + "Jag har jobbat väldigt hårt idag.");
        wt3.setEdited(true);
        workTasks.add(wt3);

        WorkTask wt4 = new WorkTask();
        wt4.setTask(getText(R.string.row_task).toString() + "jobba");
        wt4.setTime(getText(R.string.row_time).toString() + "5h");
        wt4.setStartTime(getText(R.string.row_start_time).toString() + "06.30");
        wt4.setStopStime(getText(R.string.row_stop_time).toString() + "15.00");
        wt4.setLocation(getText(R.string.row_location).toString() + "kontoret");
        wt4.setGps(getText(R.string.row_gps).toString() + "65 , 22");
        wt4.setNotes(getText(R.string.row_notes).toString() + "Jag har jobbat väldigt hårt idag.");
        workTasks.add(wt4);

    }
}
