package edu.lakeland.mealrater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MealAdaptor extends RecyclerView.Adapter{
    private ArrayList<Meal> mealData;
    private View.OnClickListener mOnItemClickListener;
    private Context parentContext;

    public class MealViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewRestaurant;
        public TextView textViewMeal;
        public TextView textViewRating;
        public ImageView imageViewMeal;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRestaurant = itemView.findViewById(R.id.tvRestaurant);
            textViewMeal = itemView.findViewById(R.id.tvMealName);
            textViewRating = itemView.findViewById(R.id.tvRatingValueList);
            imageViewMeal = itemView.findViewById(R.id.ivMealImage);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getRestaurantTextView() {
            return textViewRestaurant;
        }
        public TextView getMealTextView() {
            return textViewMeal;
        }
        public TextView getRatingTextView(){ return textViewRating; }
        public ImageView getImageImageView(){ return imageViewMeal; }
    }

    public MealAdaptor(ArrayList<Meal> arrayList, Context context) {
        mealData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MealViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MealViewHolder cvh = (MealViewHolder) holder;
        cvh.getRestaurantTextView().setText(mealData.get(position).getRestaurant());
        cvh.getMealTextView().setText(mealData.get(position).getMeal());
        cvh.getRatingTextView().setText(mealData.get(position).getRating());
        cvh.getImageImageView().setImageBitmap(mealData.get(position).getPicture());
    }

    @Override
    public int getItemCount() {
        return mealData.size();
    }

}

