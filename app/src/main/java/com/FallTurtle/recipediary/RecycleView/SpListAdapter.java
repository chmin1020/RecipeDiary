package com.FallTurtle.recipediary.RecycleView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FallTurtle.recipediary.Activity.DataShoppingActivity;
import com.FallTurtle.recipediary.R;

import java.util.List;

public class SpListAdapter extends RecyclerView.Adapter<SpListAdapter.CustomViewHolder> {
    private List<Shopping> list;

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_date.setText(list.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DataShoppingActivity.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("title",list.get(position).getTitle());
                intent.putExtra("date",list.get(position).getDate());
                intent.putExtra("itemList",list.get(position).getList());
                intent.putExtra("checkList",list.get(position).getList2());
                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return (list!=null)?list.size():0;
    }

    public void update(List<Shopping> shoppings){
        this.list = shoppings; //바뀐 리스트를 받아와서
        notifyDataSetChanged(); //화면에서 갱신해준다.
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_date;

        public CustomViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
