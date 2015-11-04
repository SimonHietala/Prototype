package nsimhie.prototype.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import nsimhie.prototype.R;
import nsimhie.prototype.WorkTask;

public class HistoryEditFragment extends Fragment {

    private ArrayList<WorkTask> workTasks;
    private int position;

    public HistoryEditFragment(){}

    public HistoryEditFragment(ArrayList<WorkTask> workTasks, int position)
    {
        this.workTasks = workTasks;
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_history_edit, container, false);

        final EditText etTask = (EditText) rootView.findViewById(R.id.rowMain).findViewById(R.id.rowEtTask);
        TextView etTime = (TextView) rootView.findViewById(R.id.rowMain).findViewById(R.id.rowEtTime);
        final EditText etStart = (EditText) rootView.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtStart);
        final EditText etStop = (EditText) rootView.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtStop);
        final EditText etLocation = (EditText) rootView.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtLocation);
        final EditText etGps = (EditText) rootView.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtGps);
        final EditText etNotes = (EditText) rootView.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtNotes);

        etTask.setText(workTasks.get(position).getTask());
        etTime.setText(workTasks.get(position).getTime());

        etStart.setText(workTasks.get(position).getStartTime());
        etStop.setText(workTasks.get(position).getStopStime());
        etLocation.setText(workTasks.get(position).getLocation());
        etGps.setText(workTasks.get(position).getGps());
        etNotes.setText(workTasks.get(position).getNotes());


        Button save = (Button) rootView.findViewById(R.id.rowSaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save the edited text
                workTasks.get(position).setEdited(true);
                workTasks.get(position).setTask(etTask.getText().toString());
                workTasks.get(position).setStartTime(etStart.getText().toString());
                workTasks.get(position).setStopStime(etStop.getText().toString());
                workTasks.get(position).setLocation(etLocation.getText().toString());
                workTasks.get(position).setGps(etGps.getText().toString());
                workTasks.get(position).setNotes(etNotes.getText().toString());

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        Button cancel = (Button) rootView.findViewById(R.id.rowCancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return rootView;
    }
}