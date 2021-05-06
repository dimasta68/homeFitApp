package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;

public class RetroAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModelListView> dataModelArrayList;
    String lang = Locale.getDefault().getLanguage();
    public List<Integer> positionfav = new ArrayList<>();
    public List<Integer> OpenTask = new ArrayList<>();
    Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
    Map<Integer, Integer> Lavel = new HashMap<Integer, Integer>();
    Map<Integer, Integer> ProgressMap = new HashMap<Integer, Integer>();
    Integer progress = 0;
    int lavelAll = 0;
    int progressAll = 0;
    int lavel2, lavel3;
    int LavelOpen = 3;/////количество открытых уровней сначала при запуске
    int Up = 0;

    public RetroAdapter(Context context, ArrayList<ModelListView> dataModelArrayList) {
        this.context = context;
        Paper.init(context);
        /// init lavel open map;
        Lavel.put(1, 1);
        Lavel.put(2, 0);
        Lavel.put(3, 0);
        if (Paper.book().read("Lavel") != null) {
            Lavel = Paper.book().read("Lavel");
          //  Log.d("debug", "Lavel start" + Lavel);
        }
        if (Paper.book().read("Up") != null) {
            Up = Paper.book().read("Up");
          //   Log.d("debug", "Up" + Up);
        }
        /// END init lavel open map;
        positionfav = Paper.book().read("positionfav");
        if (Paper.book().read("hashMap") != null) {
            hashMap = Paper.book().read("hashMap");
            // Log.d("debug", "hasmap" + hashMap.get(0));
        }
        if (Paper.book().read("ProgressMap") != null) {
            ProgressMap = Paper.book().read("ProgressMap");
            // Log.d("debug", "hasmap" + hashMap.get(0));
        }
        if (Paper.book().read("LavelOpen") != null) {
            LavelOpen = Paper.book().read("LavelOpen");
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
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"ResourceAsColor", "SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_task, null, true);
            holder.pid = (TextView) convertView.findViewById(R.id.pid);
            holder.title_task = (TextView) convertView.findViewById(R.id.title_task);
            holder.desc_task = (TextView) convertView.findViewById(R.id.desc_task);
            holder.inactive = (TextView) convertView.findViewById(R.id.inactive);
            holder.lavel = (TextView) convertView.findViewById(R.id.lavel);
            holder.cat = (TextView) convertView.findViewById(R.id.cat);
            holder.img = (ImageView) convertView.findViewById(R.id.imgstat);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progresBar);
            holder.protext = (TextView) convertView.findViewById(R.id.pogtext);
            holder.lead_time = (TextView) convertView.findViewById(R.id.lead_time);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.times = (TextView) convertView.findViewById(R.id.times);
            holder.period = (TextView) convertView.findViewById(R.id.period);
            holder.calc = (TextView) convertView.findViewById(R.id.calc);
            holder.lavelLabel = (TextView) convertView.findViewById(R.id.lableLv);
            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        // Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.pid.setText(dataModelArrayList.get(position).getId());
        holder.title_task.setText(dataModelArrayList.get(position).getTitle());
        holder.inactive.setText(dataModelArrayList.get(position).getInactive());
        holder.period.setText(dataModelArrayList.get(position).getPeriod());
        holder.times.setText(dataModelArrayList.get(position).getTime());
        holder.calc.setText(dataModelArrayList.get(position).getTime());
        // Log.d("debug","position   "+position);
        // Log.d("debug","size   "+dataModelArrayList.size());

        switch (dataModelArrayList.get(position).getTime()) {
            case "1":
                lavelAll = lavelAll + 21;
                holder.time.setText("21");
                holder.progressBar.setMax(21);
                double calculations = 21 * 20.0 / 100;
                //
                break;
            case "2":
                lavelAll = lavelAll + 84;
                holder.time.setText("84");
                holder.progressBar.setMax(84);
                double calculations2 = 84 * 20.0 / 100;
                break;
            case "3":
                lavelAll = lavelAll + 168;
                holder.time.setText("168");
                holder.progressBar.setMax(168);
                double sqrt3 = 168 * 20.0 / 100;
                break;
        }

        holder.lead_time.setText(dataModelArrayList.get(position).getLead_time());
        if (holder.inactive.getText().equals("1")) {
            holder.inactive.setText("Активно");
            holder.inactive.setTextColor(R.color.FFF);
            holder.inactive.setBackgroundResource(R.drawable.blacklable);
        } else if (holder.inactive.getText().equals("2")) {
            holder.inactive.setText("Не активно");
            holder.inactive.setBackgroundResource(R.drawable.orangelable);
            holder.inactive.setTextColor(R.color.black);
        }
        switch (dataModelArrayList.get(position).getLavel()) {
            case "1":
                holder.lavelLabel.setText("Легко");
                break;
            case "2":
                holder.lavelLabel.setText("Средне");
                break;
            case "3":
                holder.lavelLabel.setText("Сложно");
                break;
        }
        holder.desc_task.setText(dataModelArrayList.get(position).getDecsript());
        holder.lavel.setText(dataModelArrayList.get(position).getLavel());
        holder.cat.setText(dataModelArrayList.get(position).getCat());
        if (ProgressMap != null) {
            for (Integer key : ProgressMap.keySet()) {
                if (position + 1 == key) {
                    progress = ProgressMap.get(key);
                    //  Log.d("debug", "porgress + possitions " + progress + "  pos" + position);
                    //   Log.d("debug", "pid==" + holder.pid.getText());
                    holder.protext.setText("" + progress);
                    holder.progressBar.setProgress(progress);
                    progressAll = progressAll + progress;
                //    Log.d("debug", "progressAll==" + progressAll);


                }
            }
        }

        if (positionfav != null) {
            for (int j = 0; j < positionfav.size(); j++) {
                if (position == positionfav.get(j)) {
                    holder.img.setImageResource(R.drawable.ic_run);
                    holder.inactive.setText("Активно");
                    holder.inactive.setTextColor(R.color.FFF);
                    holder.inactive.setBackgroundResource(R.drawable.greanlable);
                    holder.protext.setText("" + progress);
                    holder.protext.setText("" + progress);
                }

            }
        }
        //Log.d("debug", "lavelAll   " + lavelAll);


        if (position + 1 == dataModelArrayList.size()) {
            String progressData="выполенно "+progressAll+" из "+lavelAll;
            Paper.book().write("progressData",progressData);
            lavel2 = lavelAll * 2 / 100; /// if 2%
            //Log.d("debug","lavel2  "+lavel2);
            if (lavel2 < progressAll & Up == 0) {
                Lavel.replace(2, 1);
                Paper.book().write("Lavel", Lavel);
                Up++;
                Paper.book().write("Up", Up);
                Toast.makeText(context, "Ура вам доступен новый уровень", Toast.LENGTH_SHORT).show();
            }
            lavel3 = lavelAll * 7 / 100; /// if 10%
          ///  Log.d("debug","lavel3  "+lavel3);
            if (lavel3 < progressAll & Up == 1) {
                Lavel.replace(3, 1);
                Paper.book().write("Lavel", Lavel);
                Up++;
                Paper.book().write("Up", Up);
                Toast.makeText(context, "Ура вам доступен новый уровень", Toast.LENGTH_SHORT).show();
            }
            //Log.d("debug","lavel2  "+lavel2);
        }
     //   Log.d("debug", "lavelMap" + Lavel);
        holder.lavel.getText();
        int progressint = Integer.parseInt((String) holder.lavel.getText());
        assert Lavel != null;
        for (Integer key : Lavel.keySet()) {
            if (progressint == key & Lavel.get(key) == 0) {
              ///  Log.d("debug", "lavel Debug" + progressint);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Закрыто , пройдите предыдущие уровни", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.inactive.setText("закрыто");
                holder.img.setImageResource(R.drawable.ic_lock);
                holder.inactive.setTextColor(R.color.FFF);
                holder.inactive.setBackgroundResource(R.drawable.blacklable);
                holder.progressBar.setVisibility(View.GONE);
                holder.protext.setVisibility(View.GONE);
            }
        }

        return convertView;


    }

    private static class ViewHolder {
        protected TextView pid, title_task, desc_task, lavel, inactive, cat, protext, period, lead_time, time, times, calc,lavelLabel;
        ProgressBar progressBar;
        protected Button asc, desc;
        protected ImageView img;

    }

}
