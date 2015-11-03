package nsimhie.prototype.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nsimhie.prototype.HelpRowAdapter;
import nsimhie.prototype.HistoryRowAdapter;
import nsimhie.prototype.R;

public class HelpFragment extends Fragment {
    private ListView listView;
    private ArrayList<Pair<String, String>> info = new ArrayList<>();
    private HelpRowAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        listView = (ListView) rootView.findViewById(R.id.helpListView);
        adapter = new HelpRowAdapter(info, getActivity());
        listView.setAdapter(adapter);

        setData();

        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "HelpFragment", Toast.LENGTH_SHORT).show();
        //Listener for when a row in the listview is clicked.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //parent.getChildAt(position).setBackgroundColor(Color.BLUE);

                TextView infoText = (TextView) view.findViewById(R.id.helpTvInfo);

                if (infoText.getVisibility() == View.GONE) {
                    infoText.setVisibility(View.VISIBLE);
                } else if (infoText.getVisibility() == View.VISIBLE) {
                    infoText.setVisibility(View.GONE);
                }
            }
        });





        return rootView;
    }


    private void setData()
    {
        info.add(new Pair<String, String>(getString(R.string.menu_create_tag),getString(R.string.help_create_tag)));
        info.add(new Pair<String, String>(getString(R.string.menu_erase_tag), getString(R.string.help_erase_tag)));
        info.add(new Pair<String, String>(getString(R.string.menu_statistics),getString(R.string.help_statistics)));
        info.add(new Pair<String, String>(getString(R.string.menu_history), getString(R.string.help_history)));
        info.add(new Pair<String, String>(getString(R.string.menu_settings), getString(R.string.help_settings)));
        info.add(new Pair<String, String>(getString(R.string.menu_current_task), getString(R.string.help_current_task)));

    }
}
