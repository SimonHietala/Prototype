package nsimhie.prototype;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

        TextView task = (TextView) view.findViewById(R.id.rowTvTask);
        TextView time = (TextView) view.findViewById(R.id.rowTvTime);

        TextView start = (TextView) view.findViewById(R.id.rowTvStart);
        TextView stop = (TextView) view.findViewById(R.id.rowTvStop);
        TextView location = (TextView) view.findViewById(R.id.rowTvLocation);
        TextView gps = (TextView) view.findViewById(R.id.rowTvGps);
        TextView notes = (TextView) view.findViewById(R.id.rowTvNotes);

        task.setText(workTasks.get(position).getTask());
        time.setText(workTasks.get(position).getTime());


        start.setText(workTasks.get(position).getStartTime());
        stop.setText(workTasks.get(position).getStopStime());
        location.setText(workTasks.get(position).getLocation());
        gps.setText(workTasks.get(position).getGps());
        notes.setText(workTasks.get(position).getNotes());

        return view;
    }
}
