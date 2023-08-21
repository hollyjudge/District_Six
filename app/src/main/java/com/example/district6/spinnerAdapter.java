package com.example.district6;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*
Defines set up of spinner object
 */
public class spinnerAdapter extends ArrayAdapter<itemData> {
    private final int groupid;
    private final ArrayList<itemData> list;
    private final LayoutInflater inflater;

    public spinnerAdapter(Activity context, int groupid, int id, ArrayList<itemData>
            list) {
        super(context, id, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupid, parent, false);
        ImageView imageView = itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView = itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());
     //   textView.setBackgroundColor(Color.BLACK);
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent) {
        return getView(position, convertView, parent);

    }
}
