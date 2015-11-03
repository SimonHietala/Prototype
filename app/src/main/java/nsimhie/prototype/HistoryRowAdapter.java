package nsimhie.prototype;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nsimhie on 2015-10-27.
 */
public class HistoryRowAdapter extends BaseAdapter
{
    private final Context context;
    private ArrayList<WorkTask> workTasks;

    public HistoryRowAdapter(ArrayList<WorkTask> workTasks,Context context)
    {
        this.workTasks = workTasks;
        this.context = context;
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
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.history_row, null);
        }


        /*
       if (workTasks.get(position).isEdited())
        {
            view.setBackgroundColor(Color.RED);
        }

        */

        EditText etTask = (EditText) view.findViewById(R.id.rowMain).findViewById(R.id.rowEtTask);
        EditText etTime = (EditText) view.findViewById(R.id.rowMain).findViewById(R.id.rowEtTime);

        final EditText etStart = (EditText) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtStart);
        EditText etStop = (EditText) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtStop);
        EditText etLocation = (EditText) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtLocation);
        EditText etGps = (EditText) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtGps);
        EditText etNotes = (EditText) view.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtNotes);

        etTask.setText(workTasks.get(position).getTask());
        etTime.setText(workTasks.get(position).getTime());

        etStart.setText(workTasks.get(position).getStartTime());
        etStop.setText(workTasks.get(position).getStopStime());
        etLocation.setText(workTasks.get(position).getLocation());
        etGps.setText(workTasks.get(position).getGps());
        etNotes.setText(workTasks.get(position).getNotes());

        final View finalView = view;

        //Listener for a click-event on a single row in the history
        RelativeLayout main = (RelativeLayout) view.findViewById(R.id.historyRowLayout);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TableLayout tableLayout = (TableLayout) finalView.findViewById(R.id.rowExpansion);

                if(tableLayout.getVisibility() == View.GONE)
                {
                    tableLayout.setVisibility(View.VISIBLE);
                    RelativeLayout main = (RelativeLayout) finalView.findViewById(R.id.historyRowLayout);
                    main.setBackgroundColor(Color.LTGRAY);
                }
                else if (tableLayout.getVisibility() == View.VISIBLE)
                {
                    tableLayout.setVisibility(View.GONE);
                    RelativeLayout main = (RelativeLayout) finalView.findViewById(R.id.historyRowLayout);
                    main.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });

        //Listener for the click-event on the edit history button.
        ImageButton edit = (ImageButton) view.findViewById(R.id.rowEditBtn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout main = (RelativeLayout) finalView.findViewById(R.id.historyRowLayout);
                main.setBackgroundColor(Color.LTGRAY);

                TableLayout expansion = (TableLayout) finalView.findViewById(R.id.rowExpansion);
                expansion.setVisibility(View.VISIBLE);



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
