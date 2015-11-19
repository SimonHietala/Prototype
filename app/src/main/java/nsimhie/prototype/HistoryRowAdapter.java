package nsimhie.prototype;

import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nsimhie.prototype.Fragments.CurrentTaskFragment;
import nsimhie.prototype.Fragments.HistoryEditFragment;
import nsimhie.prototype.MainActivity;

/**
 * Created by nsimhie on 2015-10-27.
 */
public class HistoryRowAdapter extends BaseAdapter
{
    private final Context context;
    private ArrayList<WorkTask> workTasks;
    private FragmentManager manager;
    private LayoutInflater inflater;
    private CurrentTaskFragment currentTaskFragment;

    public HistoryRowAdapter(ArrayList<WorkTask> workTasks, Context context, FragmentManager manager, CurrentTaskFragment currentTaskFragment)
    {
        this.workTasks = workTasks;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.manager = manager;
        this.currentTaskFragment = currentTaskFragment;
    }

    @Override
    public int getCount() {
        return workTasks.size();
    }

    @Override
    public WorkTask getItem(int position) {
        return workTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return workTasks.get(position).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int finalPosition = position;
        final RowViewHolder rowViewHolder;
        final WorkTask wt = getItem(position);

        //Creates the view the first time
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.history_row, parent, false);
            rowViewHolder = new RowViewHolder(convertView);
            convertView.setTag(rowViewHolder);
        }

        //Recycle the view
        else
        {
            rowViewHolder = (RowViewHolder) convertView.getTag();
            rowViewHolder.main.setBackgroundColor(Color.TRANSPARENT);
            rowViewHolder.expansion.setVisibility(View.GONE);
        }

        //Set the color of the rowitem
        if (wt.isEdited())
        {
            rowViewHolder.main.setBackgroundColor(Color.RED);
        }

        //Set the fieds of the view
        rowViewHolder.etTask.setText(wt.getTask());
        rowViewHolder.etTime.setText(wt.getTime());
        rowViewHolder.etStart.setText(wt.getStartTime());
        rowViewHolder.etStop.setText(wt.getStopStime());
        rowViewHolder.etLocation.setText(wt.getLocation());
        rowViewHolder.etGps.setText(wt.getGps());
        rowViewHolder.etNotes.setText(wt.getNotes());

        //Listener for a click-event on a single row in the history
        rowViewHolder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Showes the clicked row
                if(rowViewHolder.expansion.getVisibility() == View.GONE)
                {

                    rowViewHolder.expansion.setVisibility(View.VISIBLE);
                    RelativeLayout main = rowViewHolder.main;
                    main.setBackgroundColor(Color.LTGRAY);
                }

                //Hides the clicked row
                else if (rowViewHolder.expansion.getVisibility() == View.VISIBLE)
                {
                    rowViewHolder.expansion.setVisibility(View.GONE);
                    RelativeLayout main = rowViewHolder.main;

                    if(wt.isEdited())
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
        rowViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the edit history fragment
                HistoryEditFragment fragment = new HistoryEditFragment(workTasks, finalPosition);
                String backStateName =  fragment.getClass().getName();
                manager.beginTransaction().replace(R.id.frame_container, fragment, backStateName).addToBackStack(backStateName).commit();
            }
        });

        //Listener for play-button.
        rowViewHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                currentTaskFragment.setArgumentsOwn(writeJson(position).toString());
                replaceFragment(currentTaskFragment);

                //String backStateName = currentTaskFragment.getClass().getName();
                //manager.beginTransaction().replace(R.id.frame_container, currentTaskFragment, backStateName).addToBackStack(backStateName).commit();
            }
        });

        return convertView;
    }

    private JSONObject writeJson(int position)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("task", workTasks.get(position).getTask());
            jsonObject.put("location", workTasks.get(position).getLocation());
            jsonObject.put("gps", workTasks.get(position).getGps());
            jsonObject.put("notes", workTasks.get(position).getNotes());
            jsonObject.put("inmotion",workTasks.get(position).isInMotion());
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObject;
    }


    //Viewholder för the rows.
    private  class RowViewHolder{
        TextView etTask, etTime, etStart, etStop, etLocation, etGps, etNotes;
        RelativeLayout main;
        TableLayout expansion;
        ImageButton edit, play;

        public RowViewHolder(View item){
            etTask = (TextView) item.findViewById(R.id.rowMain).findViewById(R.id.rowEtTask);
            etTime = (TextView) item.findViewById(R.id.rowMain).findViewById(R.id.rowEtTime);
            etStart = (TextView) item.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtStart);
            etStop = (TextView) item.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtStop);
            etLocation = (TextView) item.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtLocation);
            etGps = (TextView) item.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtGps);
            etNotes = (TextView) item.findViewById(R.id.rowExpansion).findViewById(R.id.rowEtNotes);
            main = (RelativeLayout) item.findViewById(R.id.historyRowLayout);
            expansion = (TableLayout) item.findViewById(R.id.rowExpansion);
            edit = (ImageButton) item.findViewById(R.id.rowEditBtn);
            play = (ImageButton) item.findViewById(R.id.rowPlayBtn);
        }
    }

    //Function that changes the fragment. And adds to the backstack.
    public void replaceFragment (Fragment fragment){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        //fragment not in back stack, create it.
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
