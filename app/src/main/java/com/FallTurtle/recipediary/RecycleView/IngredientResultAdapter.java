package com.FallTurtle.recipediary.RecycleView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FallTurtle.recipediary.R;

import java.util.ArrayList;

public class IngredientResultAdapter extends RecyclerView.Adapter<IngredientResultAdapter.customViewHolder> {
    ArrayList<String> arrayList;

    public IngredientResultAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public customViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_result, parent, false);
        customViewHolder holder = new customViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final customViewHolder holder, final int position) {
        holder.tv_name.setText(arrayList.get(position));
        holder.itemView.setTag(position); //아이템에 태그로 position을 설정
    }

    @Override
    public int getItemCount() {
        return (arrayList != null)?arrayList.size():0;
    }

    public class customViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;

        public customViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
