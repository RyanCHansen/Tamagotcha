/**
 * Fragment class used for displaying the information about the pet
 * in a graph form. Will show the user the pets happiness, hunger, and fitness
 * levels.
 *
 * @author Ryan Hansen
 */

package edu.tacoma.uw.css.team5.tamagotcha;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Fragment class used for displaying the information about the pet
 * in a graph form.
 */
public class GraphFragment extends Fragment {

    private BarChart barChart;
    private MainActivity mainActivityHolder;
    private AppCompatImageButton graphButton;
    private Fragment graphFragment;

    /**
     * constructor
     */
    public GraphFragment() {
        // Required empty public constructor
    }


    /**
     * on create view method called when starting the fragment.
     *
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        //used to access the activities assets
        mainActivityHolder = (MainActivity) getActivity();

        final TextView textDisplay = view.findViewById(R.id.graph_text_info);
        textDisplay.setTextSize(18f);
        textDisplay.setTypeface(Typeface.DEFAULT_BOLD);
        textDisplay.setTextColor(Color.parseColor("#000000"));
        String text = String.format(Locale.getDefault(),"+1 Hunger per 30 minutes\n" +
                "-1 Happiness per 45 minutes\n" +
                "-1 Fitness per 60 minutes");
        textDisplay.setText(text);

        graphFragment = this;

        //create the bar chart
        barChart = view.findViewById(R.id.graph);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        barChart.setGridBackgroundColor(Color.parseColor("#F2F2F2"));
        barChart.setScaleEnabled(false);
        barChart.setDescription(null);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);

        graphButton = view.findViewById(R.id.status_button_two);
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .remove(graphFragment)
                        .commit();
            }
        });

        populateGraph();

        return view;
    }

    /**
     * Method called to format the graph to our specifications.
     * modifies colors, size, values, and labels.
     */
    private void populateGraph() {

        //this is where the values are set for the bars
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,(float) mainActivityHolder.mPet.getHunger()));
        barEntries.add(new BarEntry(2,(float) mainActivityHolder.mPet.getHappiness()));
        barEntries.add(new BarEntry(3,(float) mainActivityHolder.mPet.getFitness()));

        //pick colors for bars
        int[] colors = {Color.parseColor("#DC143C"),
                Color.parseColor("#29AB87"),
                Color.parseColor("#FEDF00")};
        BarDataSet barDataSet = new BarDataSet(barEntries, "Max Value is 100");
        barDataSet.setColors(colors);

        //format bar data
        BarData theData = new BarData(barDataSet);
        theData.setBarWidth(0.6f);
        theData.setValueTextColor(Color.parseColor("#000000"));
        theData.setValueTextSize(15f);
        theData.setHighlightEnabled(false);
        barChart.setData(theData);

        //populate array with labels for X-Axis
        final ArrayList<String> names = new ArrayList<>();
        names.add("Filler"); //for some reason this needs to be here
        names.add("Hunger");
        names.add("Happiness");
        names.add("Fitness");

        //format the X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return names.get((int)value);
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.parseColor("#000000"));
        xAxis.setTextSize(10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        //format the Y-Axis(s)
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisLeft.setAxisMaximum(103f);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setTextColor(Color.parseColor("#000000"));
        yAxisLeft.setTextSize(8f);
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);


        yAxisRight.setAxisMaximum(103f);
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setTextColor(Color.parseColor("#000000"));
        yAxisRight.setTextSize(8f);
        yAxisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

    }


}
