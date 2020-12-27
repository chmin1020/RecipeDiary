package com.FallTurtle.recipediary.RecycleView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FallTurtle.recipediary.Activity.DataActivity;
import com.FallTurtle.recipediary.MVVM.RecipeDTO;
import com.FallTurtle.recipediary.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeDtoAdapter extends RecyclerView.Adapter<RecipeDtoAdapter.customViewHolder> implements Filterable {
    private List<RecipeDTO> UfList;
    private List<RecipeDTO> FList;

    @Override
    public customViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe, parent, false);
        customViewHolder holder = new customViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull customViewHolder holder, final int position) {
        holder.tv_title.setText(FList.get(position).title);
        holder.tv_category.setText(FList.get(position).category);
        holder.tv_date.setText(FList.get(position).date);

        holder.itemView.getTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //아이템을 누르면 인텐트를 통해 내용 확인 란으로 이동
                Context context = v.getContext();

                //클릭 시 해당되는 데이터베이스 값들을 모두 dataActivity로 보낸다.
                Intent intent = new Intent(context, DataActivity.class);
                intent.putExtra("dbid", FList.get(position).dbId);
                intent.putExtra("image", FList.get(position).image);
                intent.putExtra("title", FList.get(position).title);
                intent.putExtra("date", FList.get(position).date);
                intent.putExtra("category", FList.get(position).category);
                intent.putExtra("ingredients", FList.get(position).ingredients);
                intent.putExtra("routines", FList.get(position).routines);
                intent.putExtra("memo", FList.get(position).memo);

                context.startActivity(intent);
            }
        });
    }

    @Override //어댑터가 가지고 있는 항목의 개수가 몇 개인지 알려주는 메소드
    public int getItemCount() {
        return (FList != null) ? FList.size() : 0;
    }


    public void update(List<RecipeDTO> recipes) {
        this.FList = recipes; //바뀐 리스트를 받아와서
        this.UfList = recipes;
        Log.d("tagtag","size "+FList.size());
        notifyDataSetChanged(); //화면에서 갱신해준다.
    }

    @Override//검색용 필터
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<RecipeDTO> filteringList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) { //검색 창에 입력된 내용이 없을 시 전체 리스트 출력
                    FList = UfList;
                } else {
                    String chk = constraint.toString().trim();

                    for (int i = 0; i < UfList.size(); i++) {  //필터되지 않은 전체 리스트에서 조건에 맞는 것만 filteringList에 추가
                        if (UfList.get(i).title.contains(chk) || UfList.get(i).category.contains(chk)) {
                            filteringList.add(UfList.get(i));
                        }
                    }
                    FList = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = FList;
                return filterResults;
            }

            @Override//완성된 filterResults를 출력
            protected void publishResults(CharSequence constraint, FilterResults results) {
                FList = (ArrayList<RecipeDTO>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class customViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_category;
        TextView tv_date;

        public customViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
