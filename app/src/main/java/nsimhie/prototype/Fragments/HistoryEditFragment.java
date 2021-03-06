package nsimhie.prototype.Fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import nsimhie.prototype.InternetConnection;
import nsimhie.prototype.R;
import nsimhie.prototype.WorkTask;

public class HistoryEditFragment extends Fragment implements Observer {

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

        final InternetConnection ic = new InternetConnection(getActivity());
        ic.addObserver(this);

        Button save = (Button) rootView.findViewById(R.id.rowSaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save the edited text
                if(!workTasks.get(position).checkDateFormat(etStart.getText().toString()))
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.history_edit_starttime), Toast.LENGTH_SHORT).show();
                }

                else if(!workTasks.get(position).checkDateFormat(etStop.getText().toString()))
                {
                    Toast.makeText(getActivity(),getResources().getString(R.string.history_edit_stoptime) , Toast.LENGTH_SHORT).show();
                }

                else if(!workTasks.get(position).startStopRight(etStart.getText().toString(), etStop.getText().toString()))
                {
                    Toast.makeText(getActivity(), getResources().getString(R.string.history_edit_timeorder), Toast.LENGTH_SHORT).show();
                }

                else
                {
                    workTasks.get(position).setEdited(true);
                    workTasks.get(position).setTask(etTask.getText().toString());
                    workTasks.get(position).setStartTime(etStart.getText().toString());
                    workTasks.get(position).setStopStime(etStop.getText().toString());
                    workTasks.get(position).setLocation(etLocation.getText().toString());
                    workTasks.get(position).setGps(etGps.getText().toString());
                    workTasks.get(position).setNotes(etNotes.getText().toString());
                    workTasks.get(position).recalculateTime();

                    ic.putRequest(makeJson(), getString(R.string.URL_PUT_WORKTASK) + workTasks.get(position).getId());

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStack();
                }
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

    @Override
    public void update(Observable observable, Object data) {
        Toast.makeText(getActivity(), getString(R.string.history_edit_saved), Toast.LENGTH_SHORT).show();
    }

    private JSONObject makeJson()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("task", workTasks.get(position).getTask().trim());
            jsonObject.put("location", workTasks.get(position).getLocation().trim());
            jsonObject.put("starttime", workTasks.get(position).getStartTime().trim());
            jsonObject.put("stoptime", workTasks.get(position).getStopStime().trim());
            jsonObject.put("time", workTasks.get(position).getTime().trim());
            jsonObject.put("timeinseconds", workTasks.get(position).getTimeInSeconds());
            jsonObject.put("gps", workTasks.get(position).getGps().trim());
            jsonObject.put("notes", workTasks.get(position).getNotes().trim());
            jsonObject.put("inmotion", workTasks.get(position).isInMotion());
            jsonObject.put("edited", workTasks.get(position).isEdited());
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }
}