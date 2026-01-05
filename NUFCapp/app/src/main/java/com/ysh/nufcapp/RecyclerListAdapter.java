package com.ysh.nufcapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>
{
    String[] pname;
    Context context;

    public RecyclerListAdapter( Context context, String[] pname)
    {
        this.pname = pname;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listlayout_one,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerListAdapter.ViewHolder holder, int position)
    {
       holder.textView.setText(pname[position]);
    }

    @Override
    public int getItemCount()
    {
        return pname.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.pnames);
        }
    }
}
