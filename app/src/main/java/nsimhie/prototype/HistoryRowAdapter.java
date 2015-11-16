package nsimhie.prototype;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import nsimhie.prototype.Fragments.HistoryEditFragment;

/**
 * Created by nsimhie on 2015-10-27.
 */
public class HistoryRowAdapter extends BaseAdapter
{
    private final Context context;
    private ArrayList<WorkTask> workTasks;
    private FragmentManager manager;

    public HistoryRowAdapter(ArrayList<WorkTask> workTasks, Context context, FragmentManager manager)
    {
        this.workTasks = workTasks;
        this.context = context;
        this.manager = manager;
    }

    @Override
    public int getCount() {
        return workTasks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return workTasks.get(position).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        final int finalPosition = position;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.history_row, null);
        }

        else
        {
            view = convertView;
        }

        TextView etTask = (TextView) view.findViewById(R.id.rowMain).findViewById(R.id.rowEtTask);

        TextView etTime = (TextView) view.findViewById(R.id.rowMain).findViewById(R.id.rowEtTime);
        TextView etStart = (TextView) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtStart);
        TextView etStop = (TextView) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtStop);
        TextView etLocation = (TextView) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtLocation);
        TextView etGps = (TextView) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtGps);
        TextView etNotes = (TextView) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtNotes);

        etTask.setText(workTasks.get(finalPosition).getTask());
        etTime.setText(workTasks.get(finalPosition).getTime());

        etStart.setText(workTasks.get(finalPosition).getStartTime());
        etStop.setText(workTasks.get(finalPosition).getStopStime());
        etLocation.setText(workTasks.get(finalPosition).getLocation());
        etGps.setText(workTasks.get(finalPosition).getGps());
        etNotes.setText(workTasks.get(finalPosition).getNotes());

        if (workTasks.get(finalPosition).isEdited())
        {
            view.setBackgroundColor(Color.RED);
        }

        //Listener for a click-event on a single row in the history
        RelativeLayout main = (RelativeLayout) view.findViewById(R.id.historyRowLayout);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TableLayout tableLayout = (TableLayout) view.findViewById(R.id.rowExpansion);

                //Showes the clicked row
                if(tableLayout.getVisibility() == View.GONE)
                {
                    tableLayout.setVisibility(View.VISIBLE);
                    RelativeLayout main = (RelativeLayout) view.findViewById(R.id.historyRowLayout);
                    main.setBackgroundColor(Color.LTGRAY);
                }

                //Hides the clicked row
                else if (tableLayout.getVisibility() == View.VISIBLE)
                {
                    tableLayout.setVisibility(View.GONE);
                    RelativeLayout main = (RelativeLayout) view.findViewById(R.id.historyRowLayout);

                    if(workTasks.get(finalPosition).isEdited())
                    {
                        main.setBackgroundColor(Color.RED);
                    }
                    else
                    {
                        main.setBackgroundColor(Color.TRANSPARENT);
                    }
                }

            }
        });

        //Listener for the click-event on the edit history button.
        ImageButton edit = (ImageButton) view.findViewById(R.id.rowEditBtn);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the edit history fragment
                HistoryEditFragment fragment = new HistoryEditFragment(workTasks, finalPosition);
                String backStateName =  fragment.getClass().getName();
                manager.beginTransaction().replace(R.id.frame_container, fragment, backStateName).addToBackStack(backStateName).commit();
            }
        });

        //Listener for play-button.
        ImageButton play = (ImageButton) view.findViewById(R.id.rowPlayBtn);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }

}
