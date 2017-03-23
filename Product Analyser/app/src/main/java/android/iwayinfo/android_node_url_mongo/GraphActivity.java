package android.iwayinfo.android_node_url_mongo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by IWAY on 23-03-2017.
 */

public class GraphActivity extends AppCompatActivity {

        PieChart pieChart ;
        ArrayList<Entry> entries ;
        ArrayList<String> PieEntryLabels ;
        PieDataSet pieDataSet ;
        PieData pieData ;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            pieChart = (PieChart) findViewById(R.id.pie_chart);

            entries = new ArrayList<>();

            PieEntryLabels = new ArrayList<String>();

            AddValuesToPIEENTRY();

            AddValuesToPieEntryLabels();

            pieDataSet = new PieDataSet(entries, "");

            pieData = new PieData(PieEntryLabels, pieDataSet);

            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            pieChart.setData(pieData);

            pieChart.animateY(3000);

        }

    public void AddValuesToPIEENTRY(){

        entries.add(new BarEntry(2f, 0));
        entries.add(new BarEntry(4f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(8f, 3));
        entries.add(new BarEntry(7f, 4));
        entries.add(new BarEntry(3f, 5));

    }

    public void AddValuesToPieEntryLabels(){

        PieEntryLabels.add("January");
        PieEntryLabels.add("February");
        PieEntryLabels.add("March");
        PieEntryLabels.add("April");
        PieEntryLabels.add("May");
        PieEntryLabels.add("June");

    }
}

