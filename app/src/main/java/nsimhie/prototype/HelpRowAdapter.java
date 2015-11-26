package nsimhie.prototype;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nsimhie on 2015-10-30.
 */
public class HelpRowAdapter extends BaseAdapter
{
    private final Context context;
    private ArrayList<Pair<String, String>> info = new ArrayList<>();

    public HelpRowAdapter(ArrayList<Pair<String, String>> info, Context context)
    {
        this.info = info;
        this.context = context;
    }
    @Override
    public int getCount() {return info.size(); }

    @Override
    public Object getItem(int position) {return null; }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.help_row, null);
        }

        TextView helpHeader = (TextView) view.findViewById(R.id.helpTvHeader);
        TextView helpInfo = (TextView) view.findViewById(R.id.helpTvInfo);

        helpHeader.setText(info.get(position).first);
        helpInfo.setText(info.get(position).second);

        return view;
    }
}
