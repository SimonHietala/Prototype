package nsimhie.prototype.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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
    private Spinner statsSpinner, timeSpinner;
    private View rootView;
    private HorizontalBarChart barChart;
    private PieChart pieChart;
    private float timeUnit = 1.0f;
    private String pieString = null, barString = null;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        statsSpinner = (Spinner) rootView.findViewById(R.id.statsSpinner);
        timeSpinner = (Spinner) rootView.findViewById(R.id.statsTimeSpinner);
        ArrayList<String> times = new ArrayList<>();
        times.add(getString(R.string.stats_time_hours));
        times.add(getString(R.string.stats_time_minutes));
        times.add(getString(R.string.stats_time_seconds));
        timeSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, times));


        ic = new InternetConnection(getActivity());
        ic.addObserver(this);
        ic.getRequest(getString(R.string.URL_GET_HEADERS));


        statsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get competlete history
                if (statsSpinner.getSelectedItem().toString().equals(getString(R.string.history_spinner_base))) {
                    ic.getRequest(getString(R.string.URL_POST_WORKTASK));
                }

                //Get task specific history
                else {
                    ic.getRequest(getString(R.string.URL_GET_HEADERS_SPECIFIC) + statsSpinner.getSelectedItem().toString());
                }
                ic.getRequest(getString(R.string.URL_GET_HEADERSTIME));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (timeSpinner.getSelectedItem().toString().equals(getString(R.string.stats_time_hours))) {
                    timeUnit = 3600.0f;
                } else if (timeSpinner.getSelectedItem().toString().equals(getString(R.string.stats_time_minutes))) {
                    timeUnit = 60.0f;
                } else if (timeSpinner.getSelectedItem().toString().equals(getString(R.string.stats_time_seconds))) {
                    timeUnit = 1.0f;
                }

                if (pieString != null && barString != null) {
                    makePieChart(pieString);
                    makeBarGraph(barString);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                timeUnit = 3600.0f;
            }
        });

        return rootView;
    }


    public void readHeaderData(String data)
    {
        ArrayList<String> headLines = new ArrayList<String>();
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

        if(headLines.size() > 0) {
            statsSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, headLines));
        }
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
                float time = ((float) row.getDouble("timeinseconds"))/timeUnit;
                entries.add(new BarEntry(time, i));
                labels.add(row.getString("task"));
            }

            if(entries.size() > 0)
            {
                BarDataSet dataset = new BarDataSet(entries, getString(R.string.stats_barchart_lbl));
                //dataset.setColors(myColors());

                barChart = (HorizontalBarChart) rootView.findViewById(R.id.statsBarChart);
                barChart.setDescription("");

                BarData barData = new BarData(labels, dataset);
                barChart.setData(barData);

                barChart.invalidate();
                barChart.notifyDataSetChanged();
            }
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
                float time = ((float) row.getDouble("time"))/timeUnit;
                entries.add(new Entry(time,i));
            }

            if(entries.size() > 0) {
                pieChart = (PieChart) rootView.findViewById(R.id.statsPieChart);

                pieChart.setRotationAngle(0);
                pieChart.setRotationEnabled(true);
                pieChart.setDescription(getString(R.string.stats_piechart_lbl));

                PieDataSet pieDataSet = new PieDataSet(entries, "");

                pieDataSet.setColors(myColors());

                Legend l = pieChart.getLegend();
                l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

                PieData pieData = new PieData(labels, pieDataSet);
                pieChart.setData(pieData);

                pieChart.invalidate();
                pieChart.notifyDataSetChanged();
            }

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
        if(ic.getResponseUrl().equals(getString(R.string.URL_GET_HEADERS)))
        {
            readHeaderData(ic.getMyResponse());
        }

        else if(ic.getResponseUrl().equals(getString(R.string.URL_GET_HEADERSTIME)))
        {
            pieString = ic.getMyResponse();
            makePieChart(pieString);
        }

        else
        {
            barString = ic.getMyResponse();
            makeBarGraph(barString);
        }
    }
}
