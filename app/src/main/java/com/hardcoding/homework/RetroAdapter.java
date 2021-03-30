package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class RetroAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ModelListView> dataModelArrayList;
    String idBtn, rating;
    String lang = Locale.getDefault().getLanguage();

    public RetroAdapter(Context context, ArrayList<ModelListView> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
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

    @SuppressLint("ResourceAsColor")
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

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        // Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.pid.setText(dataModelArrayList.get(position).getId());
        holder.title_task.setText(dataModelArrayList.get(position).getTitle());
        holder.inactive.setText(dataModelArrayList.get(position).getInactive());
        if(holder.inactive.getText().equals("1")){
            holder.inactive.setText("Активно");
            holder.inactive.setTextColor(R.color.FFF);
            holder.inactive.setBackgroundResource(R.drawable.blacklable);
        }else  if(holder.inactive.getText().equals("2")){
            holder.inactive.setText("Не активно");
            holder.inactive.setBackgroundResource(R.drawable.orangelable);
            holder.inactive.setTextColor(R.color.black);

        }
        holder.desc_task.setText(Html.fromHtml(dataModelArrayList.get(position).getDecsript()));
        holder.lavel.setText("уровень "+dataModelArrayList.get(position).getLavel());
        holder.cat.setText(dataModelArrayList.get(position).getCat());

        //   holder.tvcity.setText(dataModelArrayList.get(position).getEnd_skidka());

        return convertView;
    }

    private class ViewHolder {

        protected TextView pid, title_task, desc_task,lavel, inactive,cat;
        protected Button asc, desc;
        protected ImageView iv;

    }

}
