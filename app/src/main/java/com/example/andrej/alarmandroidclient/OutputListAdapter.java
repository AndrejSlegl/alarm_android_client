package com.example.andrej.alarmandroidclient;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Andrej on 26. 11. 2016.
 */

public class OutputListAdapter extends RecyclerView.Adapter<OutputListAdapter.ViewHolder>  {

    private ArrayList<String> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;

            mTextView = (TextView)v.findViewById(R.id.textView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OutputListAdapter() {
        mDataset = new ArrayList<String>();
    }

    public void add(int index, String text) {
        mDataset.add(index, text);
        notifyItemInserted(index);
    }

    public void removeItemAtIndex(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OutputListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.output_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
