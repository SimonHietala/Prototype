package nsimhie.prototype.Fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import nsimhie.prototype.GPS;
import nsimhie.prototype.R;

public class LaunchNewTaskFragment extends Fragment{

    private CurrentTaskFragment currentTaskFragment;
    private EditText etTask;
    private EditText etLocation;
    private EditText etGps;
    private EditText etNotes;

    public LaunchNewTaskFragment(){}


    public LaunchNewTaskFragment(CurrentTaskFragment currentTaskFragment){
        this.currentTaskFragment = currentTaskFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Get the gps location
        GPS gps = new GPS(getActivity());
        etGps.setText(gps.getCoordinates());
        gps.stopGPS();
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_launch_new_task, container, false);
        etGps = (EditText)rootView.findViewById(R.id.launchEtGps);
        etTask = (EditText) rootView.findViewById(R.id.launchEtTask);
        etLocation = (EditText) rootView.findViewById(R.id.launchEtLocation);
        etNotes = (EditText) rootView.findViewById(R.id.launchEtNotes);

        //Get the gps location
        GPS gps = new GPS(getActivity());
        etGps.setText(gps.getCoordinates());
        gps.stopGPS();

        ImageButton launch = (ImageButton) rootView.findViewById(R.id.launchBtn);
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(fieldsFilled())
                {
                    currentTaskFragment.setArgumentsOwn(writeJson().toString());
                    etNotes.setText("");
                    etLocation.setText("");
                    etTask.setText("");
                    etGps.setText("");
                    replaceFragment(currentTaskFragment);
                }
            }
        });

        return rootView;
    }

    private boolean fieldsFilled()
    {
        if(etTask.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(), getString(R.string.launch_task), Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(etLocation.getText().toString().trim().equals(""))
        {
            Toast.makeText(getActivity(),getString(R.string.launch_location), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private JSONObject writeJson()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("task", etTask.getText().toString().trim());
            jsonObject.put("location", etLocation.getText().toString().trim());
            jsonObject.put("gps", etGps.getText().toString().trim());
            jsonObject.put("notes", etNotes.getText().toString().trim());
            jsonObject.put("inmotion", true);
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //Function that changes the fragment. And adds to the backstack.
    public void replaceFragment (android.app.Fragment fragment){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager manager = getActivity().getFragmentManager();
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
