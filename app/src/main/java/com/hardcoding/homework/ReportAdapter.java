package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;

public class ReportAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModelListView> dataModelArrayList;
    String lang = Locale.getDefault().getLanguage();
    public List<Integer> reportfav = new ArrayList<>();
    public List<Integer> progress = new ArrayList<>();
    public List<Integer> OpenTask = new ArrayList<>();
    Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
    ArrayList<String> checked;
    //Integer progress = 0;

    int LavelOpen = 4;/////количество открытых уровней сначала при запуске
    private Button activitybutton;

    public ReportAdapter(Context context, ArrayList<ModelListView> dataModelArrayList) {
        this.context = context;
        Paper.init(context);
        checked = new ArrayList<>();
        reportfav = Paper.book().read("reportfav");
        if (Paper.book().read("checked") != null) {
            checked = Paper.book().read("checked");

            // Log.d("debug", "hasmap" + hashMap.get(0));

        }
        if (Paper.book().read("hashMap") != null) {
            hashMap = Paper.book().read("hashMap");
            // Log.d("debug", "hasmap" + hashMap.get(0));

        }
        if (Paper.book().read("LavelOpen") != null) {
            LavelOpen = Paper.book().read("LavelOpen");
        }
        if (Paper.book().read("progress") != null) {
            progress = Paper.book().read("progress");
        }

        this.dataModelArrayList = dataModelArrayList;
        OpenTask.add(LavelOpen);
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint({"ResourceAsColor", "SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_report, null, true);

            holder.pid = (TextView) convertView.findViewById(R.id.pid);
            holder.title_task = (TextView) convertView.findViewById(R.id.title_task);
            holder.desc_task = (TextView) convertView.findViewById(R.id.desc_task);
            holder.lavel = (TextView) convertView.findViewById(R.id.lavel);
            holder.cat = (TextView) convertView.findViewById(R.id.cat);
            holder.img = (ImageView) convertView.findViewById(R.id.imgstat);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.protext = (TextView) convertView.findViewById(R.id.pogtext);
            holder.calc = (TextView) convertView.findViewById(R.id.calc);
            holder.calc = (TextView) convertView.findViewById(R.id.calc);

            int finalPosition = position;
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checked.add(dataModelArrayList.get(position).getId());
                        dataModelArrayList.get(finalPosition).getId();
                        Paper.book().write("checked", checked);
                    } else {
                        checked.remove(dataModelArrayList.get(position).getId());
                        Paper.book().write("checked", checked);
                    }
                }
            });

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }
        holder.pid.setText(dataModelArrayList.get(position).getId());
        holder.title_task.setText(dataModelArrayList.get(position).getTitle());
        holder.lavel.setText("уровень " + dataModelArrayList.get(position).getLavel());
        holder.cat.setText(dataModelArrayList.get(position).getCat());
        if(checked!=null){
            for (int j = 0; j < checked.size(); j++) {
               // Log.d("debug","holder save"+holder.pid.getText());
              //  Log.d("debug","cheked save"+checked.get(j));
                if (holder.pid.getText().equals(checked.get(j))) {
                  //  Log.d("debug","cheked true"+checked.get(j));
                   holder.checkBox.setChecked(true);
                }

            }

        }

        switch (dataModelArrayList.get(position).getTime()) {
            case "1":

                holder.time.setText("21");
                double calculations = 21 * 20.0 / 100;
                int IntValue = (int) calculations;
                holder.calc.setText(String.valueOf(IntValue));
                // Log.d("debug","sqrt21  "+sqrt);
                break;
            case "2":
                holder.time.setText("84");

                double calculations2 = 84 * 20.0 / 100;
                int IntValue2 = (int) calculations2;
                holder.calc.setText((String.valueOf(IntValue2)));
                ///Log.d("debug","sqrt84  "+sqrt2);
                break;
            case "3":
                holder.time.setText("168");
                double sqrt3 = 168 * 20.0 / 100;
                double calculations3 = 84 * 20.0 / 100;
                int IntValue3 = (int) calculations3;
                holder.calc.setText(String.valueOf(IntValue3));
                break;
        }
        return convertView;
    }

    private static class ViewHolder {
        protected TextView pid, title_task, desc_task, lavel, inactive, cat, protext, period, lead_time, time, times, calc;
        ProgressBar progressBar;
        protected Button asc, btnReport;
        protected ImageView img;
        protected CheckBox checkBox;
    }

}
