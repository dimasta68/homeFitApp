package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class RetroAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModelListView> dataModelArrayList;
    String lang = Locale.getDefault().getLanguage();
    public List<Integer> positionfav = new ArrayList<>();
    public List<Integer> OpenTask = new ArrayList<>();

    public RetroAdapter(Context context, ArrayList<ModelListView> dataModelArrayList) {
        this.context = context;
        Paper.init(context);
        positionfav = Paper.book().read("positionfav");
        this.dataModelArrayList = dataModelArrayList;
        OpenTask.add(2);
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

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        // Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.pid.setText(dataModelArrayList.get(position).getId());
        holder.title_task.setText(dataModelArrayList.get(position).getTitle());
        holder.inactive.setText(dataModelArrayList.get(position).getInactive());
        if (holder.inactive.getText().equals("1")) {
            holder.inactive.setText("Активно");
            holder.inactive.setTextColor(R.color.FFF);
            holder.inactive.setBackgroundResource(R.drawable.blacklable);
        } else if (holder.inactive.getText().equals("2")) {
            holder.inactive.setText("Не активно");
            holder.inactive.setBackgroundResource(R.drawable.orangelable);
            holder.inactive.setTextColor(R.color.black);
        }
        holder.desc_task.setText(dataModelArrayList.get(position).getDecsript());
        holder.lavel.setText("уровень " + dataModelArrayList.get(position).getLavel());
        holder.cat.setText(dataModelArrayList.get(position).getCat());

        for (int j = 0; j < positionfav.size(); j++) {
            if (position == positionfav.get(j)) {
                holder.img.setImageResource(R.drawable.ic_run);
                holder.inactive.setText("Активно");
                holder.inactive.setTextColor(R.color.FFF);
                holder.inactive.setBackgroundResource(R.drawable.greanlable);
            }
        }

        for (int j = 0; j < OpenTask.size(); j++) {
            if (position >= OpenTask.get(j)) {
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
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        protected TextView pid, title_task, desc_task, lavel, inactive, cat;
        protected Button asc, desc;
        protected ImageView img;
    }
}
