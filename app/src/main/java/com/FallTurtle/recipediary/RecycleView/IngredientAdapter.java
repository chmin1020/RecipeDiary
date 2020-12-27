package com.FallTurtle.recipediary.RecycleView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.FallTurtle.recipediary.R;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.customViewHolder> {
    ArrayList<String> arrayList;
    public IngredientAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public customViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient, parent, false);
        customViewHolder holder = new customViewHolder(v, new customViewHolder.ITextWatcher(){

            @Override
            public void beforeTextChanged(int position, CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(int position, CharSequence s, int start, int before, int count) {
                arrayList.set(position, s.toString());
            }

            @Override
            public void afterTextChanged(int position, Editable s) {

            }


        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final customViewHolder holder, final int position) {
        holder.et_name.setText(arrayList.get(position));
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null)?arrayList.size():0;
    }

    public void insert(){
        arrayList.add("");
        notifyItemInserted(arrayList.size());

    }

    public void delete(int position){
        try{
            if(arrayList.size() == 1)
                return;
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
        catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public static class customViewHolder extends RecyclerView.ViewHolder{
        private EditText et_name;
        private ImageView iv_delete;
        private ITextWatcher mTextWatcher;

        public interface ITextWatcher { //나만의 텍스트와쳐 인터페이스 생성(각각의 아이템에 적용하기 위해 포지션 추가)
            void beforeTextChanged(int position, CharSequence s, int start, int count, int after);

            void onTextChanged(int position, CharSequence s, int start, int before, int count);

            void afterTextChanged(int position, Editable s);
        }

        public customViewHolder(@NonNull View itemView, ITextWatcher textWatcher) {
            super(itemView);
            this.mTextWatcher = textWatcher;

            et_name = itemView.findViewById(R.id.et_name);
            iv_delete = itemView.findViewById(R.id.iv_delete);

            //et_name의 내용이 바뀔 때 홀더의 커스텀 텍스트와쳐를 통해 실행하게 함
            et_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mTextWatcher.beforeTextChanged(getAdapterPosition(), s, start, count, after);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mTextWatcher.onTextChanged(getAdapterPosition(), s, start, before, count);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    mTextWatcher.afterTextChanged(getAdapterPosition(), s);
                }
            });
        }
    }
}
