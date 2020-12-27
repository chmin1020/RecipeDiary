package com.FallTurtle.recipediary.RecycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FallTurtle.recipediary.R;

import java.util.ArrayList;


public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.CustomViewHolder> {
    ArrayList<String> arrayList; //Contents
    ArrayList<String> arrayList2; //checkBox


    public ShoppingAdapter(ArrayList<String> arrayList, ArrayList<String> arrayList2){
        this.arrayList = arrayList;
        this.arrayList2 = arrayList2;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping,parent,false);
        CustomViewHolder holder = new CustomViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        holder.tv_name.setText(arrayList.get(position));

        if(arrayList2.get(position).equals("T"))
            holder.cb_check.setChecked(true);
        else
            holder.cb_check.setChecked(false);

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(holder.getAdapterPosition());
            }
        });

        holder.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    arrayList2.set(position, "T");
                else
                    arrayList2.set(position, "F");
            }


        });
    }

    @Override
    public int getItemCount() {
        return (arrayList!=null)?arrayList.size():0;
    }

    public void insert(String s){
        arrayList.add(s);
        arrayList2.add("F");
        notifyDataSetChanged();
    }

    public void delete(int position){
        try{
            arrayList.remove(position);
            arrayList2.remove(position);
            notifyItemRemoved(position);
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb_check;
        TextView tv_name;
        ImageView iv_delete;

        CustomViewHolder(View itemView){
            super(itemView);
            cb_check = itemView.findViewById(R.id.cb_check);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
