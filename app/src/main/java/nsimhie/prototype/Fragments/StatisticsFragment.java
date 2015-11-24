package nsimhie.prototype.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import nsimhie.prototype.InternetConnection;
import nsimhie.prototype.R;

public class StatisticsFragment extends Fragment implements Observer{

    public StatisticsFragment(){}
    private InternetConnection ic;
    private Spinner statsSpinner;
    private View rootView;
    private HorizontalBarChart barChart;
    private PieChart pieChart;
    private ArrayList<String> headLines = new ArrayList<String>();

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        statsSpinner = (Spinner) rootView.findViewById(R.id.statsSpinner);

        ic = new InternetConnection(getActivity());
        ic.addObserver(this);
        ic.getRequest("/worktasks/headers");
        ic.getRequest("/worktasks/headerstime");

        statsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get competlete history
                if (statsSpinner.getSelectedItem().toString().equals(getString(R.string.history_spinner_base))) {
                    ic.getRequest("/worktasks");
                }

                //Get task specific history
                else {
                    ic.getRequest("/worktasks/headers/" + statsSpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }


    public void readHeaderData(String data)
    {
        headLines.clear();
        headLines.add(getString(R.string.history_spinner_base));
        try
        {
            JSONObject json = new JSONObject(data);
            JSONArray array = json.getJSONArray("array");
            for(int i=0; i < array.length(); i++)
            {
                headLines.add(array.getString(i));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        statsSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, headLines));

    }

    public void makeBarGraph(String data)
    {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        try
        {
            JSONObject jo = new JSONObject(data);
            JSONArray array = jo.getJSONArray("array");
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                float time = ((float) row.getDouble("timeinseconds"))/3600;
                entries.add(new BarEntry(time, i));
                labels.add(row.getString("task"));
            }

            BarDataSet dataset = new BarDataSet(entries, "Time Worked");
            //dataset.setColors(myColors());

            barChart = (HorizontalBarChart) rootView.findViewById(R.id.statsBarChart);
            barChart.setDescription("");

            BarData barData = new BarData(labels, dataset);
            barChart.setData(barData);

            barChart.invalidate();
            barChart.notifyDataSetChanged();
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void makePieChart(String data)
    {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        try
        {
            JSONObject jo = new JSONObject(data);
            JSONArray array = jo.getJSONArray("array");
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject row = array.getJSONObject(i);

                labels.add(row.getString("task"));
                float time = ((float) row.getDouble("time"))/3600;
                entries.add(new Entry(time,i));
            }

            pieChart = (PieChart) rootView.findViewById(R.id.statsPieChart);

            pieChart.setRotationAngle(0);
            pieChart.setRotationEnabled(true);
            pieChart.setDescription("Total time per task");

            PieDataSet pieDataSet = new PieDataSet(entries, "");

            pieDataSet.setColors(myColors());

            Legend l = pieChart.getLegend();
            l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

            PieData pieData = new PieData(labels, pieDataSet);
            pieChart.setData(pieData);

            pieChart.invalidate();
            pieChart.notifyDataSetChanged();

        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> myColors()
    {
        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        return colors;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if(ic.getResponseUrl().equals("/worktasks/headers"))
        {
            readHeaderData(ic.getMyResponse());
        }

        else if(ic.getResponseUrl().equals("/worktasks/headerstime"))
        {
            makePieChart(ic.getMyResponse());
        }

        else
        {
            makeBarGraph(ic.getMyResponse());
        }
    }
}
