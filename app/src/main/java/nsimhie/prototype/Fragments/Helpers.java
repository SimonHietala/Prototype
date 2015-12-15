package nsimhie.prototype.Fragments;

import android.app.Activity;
import android.app.Fragment;

import nsimhie.prototype.R;

/**
 * Created by nsimhie on 2015-12-15.
 */
public class Helpers {
    private Activity activity;
    private CurrentTaskFragment ctf;

    public Helpers(Activity activity, CurrentTaskFragment currentTaskFragment)
    {
        this.activity = activity;
        this.ctf = currentTaskFragment;
    }

    //Helper function for seting the title on back button pressed.
    public void mySetTitle(Fragment fragment)
    {
        String backStateName =  fragment.getClass().getName();
        if(backStateName.equals(new CreateTagFragment().getClass().getName()))
        {
            activity.setTitle(activity.getString(R.string.menu_create_tag));
        }

        else if(backStateName.equals(new EraseTagFragment().getClass().getName()))
        {
            activity.setTitle(activity.getString(R.string.menu_erase_tag));
        }

        else if(backStateName.equals(new HelpFragment().getClass().getName()))
        {
            activity.setTitle(activity.getString(R.string.menu_help));
        }

        else if(backStateName.equals(new HistoryFragment(ctf).getClass().getName()))
        {
            activity.setTitle(activity.getString(R.string.menu_history));
        }

        else if(backStateName.equals(new CurrentTaskFragment().getClass().getName()))
        {
            activity.setTitle(activity.getString(R.string.menu_current_task));
        }

        else if(backStateName.equals(new SettingsFragment().getClass().getName()))
        {
            activity.setTitle(activity.getString(R.string.menu_settings));
        }

        else if(backStateName.equals(new AboutFragment().getClass().getName()))
        {
            activity.setTitle(activity.getString(R.string.menu_about));
        }

        else if(backStateName.equals(new LaunchNewTaskFragment().getClass().getName()))
        {
            activity.setTitle(activity.getString(R.string.menu_launch_manual));
        }
    }
}
