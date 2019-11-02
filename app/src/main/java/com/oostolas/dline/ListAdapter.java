package com.oostolas.dline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
public class ListAdapter extends ArrayAdapter<ListItem> {

    private final ArrayList<ListItem> items;
    private final Activity context;


    ListAdapter(Activity context, ArrayList<ListItem> items) {
        super(context, R.layout.list_row, items);
        this.context = context;
        this.items = items;
    }

    static class ViewHolder {
        TextView textViewItemName;
        TextView textViewItemDescription;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_row, null, true);
            holder = new ViewHolder();
            holder.textViewItemName = rowView.findViewById(R.id.textDate);
            holder.textViewItemDescription = rowView.findViewById(R.id.textComment);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        ListItem item = items.get(position);

        // get current item to be displayed
        String strDate = ListItem.timeFormat(item.date);
        String comment = item.name;


        //sets the text for item name and item description from the current item object
        holder.textViewItemName.setText(strDate);
        holder.textViewItemDescription.setText(comment);
        if(item.date.getTime() < 0) holder.textViewItemName.setTextColor(Color.rgb(160, 160, 160));
        else holder.textViewItemName.setTextColor(Color.rgb(255, 68, 68));

        // returns the view for the current row
        return rowView;
    }

}
