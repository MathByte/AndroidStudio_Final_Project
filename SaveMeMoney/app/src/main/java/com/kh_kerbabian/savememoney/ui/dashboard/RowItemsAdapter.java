package com.kh_kerbabian.savememoney.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kh_kerbabian.savememoney.R;
import com.kh_kerbabian.savememoney.ui.home.MoneyDataModel;

import java.util.ArrayList;

public class RowItemsAdapter extends RecyclerView.Adapter<RowItemsAdapter.ViewHolder> {

    private ArrayList<MoneyDataModel> rowModelArraylist;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    int lastPoss = -1;


    public RowItemsAdapter(Context context, ArrayList<MoneyDataModel> rowModelArraylist) {
        this.rowModelArraylist = rowModelArraylist;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.money_rv_row, parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoneyDataModel mdModle = rowModelArraylist.get(position);
        holder.accountText.setText(mdModle.getAccount());
        holder.categoryText.setText(mdModle.getCategory());
        holder.ammountText.setText(String.valueOf(mdModle.getAmmount()));
        holder.dateText.setText(mdModle.getDate());
        holder.typeText.setText(mdModle.getType());
        if(mdModle.getType().equals("Expenses"))
            holder.consLayout.setBackgroundColor(R.color.light_red_shade_1);

        setAnimation(holder.itemView, position);
    }


    private void setAnimation(View itemview, int possition){
        if(possition > lastPoss){
            Animation animation = AnimationUtils.loadAnimation(itemview.getContext(), R.anim.anim_r_to_l);
            itemview.setAnimation(animation);
            lastPoss = possition;
        }
    }







    @Override
    public int getItemCount() {

        return rowModelArraylist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dateText ;
        private TextView categoryText;
        private TextView accountText;
        private TextView ammountText;
        private TextView typeText;
        private ConstraintLayout consLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText  = itemView.findViewById(R.id.dateText);
            categoryText = itemView.findViewById(R.id.categoryText);
            accountText = itemView.findViewById(R.id.accountText);
            ammountText = itemView.findViewById(R.id.ammountText);
            typeText = itemView.findViewById(R.id.typeText);
            consLayout = itemView.findViewById(R.id.cardHolderid);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    MoneyDataModel getItem(int id) {
        return rowModelArraylist.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
