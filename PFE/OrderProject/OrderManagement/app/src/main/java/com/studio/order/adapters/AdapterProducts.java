package com.studio.order.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studio.order.R;
import com.studio.order.models.ModelProducts;
import com.studio.order.ui.MainActivityProductsDetails;

import java.util.List;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.CommandsViewHolder> {

    Context context;
    List<ModelProducts> modelProducts;

    public AdapterProducts(Context context, List<ModelProducts> modelProducts) {
        this.context = context;
        this.modelProducts = modelProducts;
    }

    @NonNull
    @Override
    public CommandsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new CommandsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommandsViewHolder holder, int position) {
        ModelProducts modelProducts1 = modelProducts.get(position);
        holder.TvName.setText(modelProducts1.getName());
        holder.TvDescription.setText("Prix : "+modelProducts1.getPrice()+ " / Quantité : " +modelProducts1.getQuantity());

        holder.RvClick.setOnClickListener(v -> {

            Intent intent = new Intent(context, MainActivityProductsDetails.class);
            intent.putExtra("PRO_ID",String.valueOf(modelProducts1.getId()));
            intent.putExtra("PRO_NAME",modelProducts1.getName());
            intent.putExtra("PRO_PRICE",String.valueOf(modelProducts1.getPrice()));
            intent.putExtra("PRO_QUANTITY",String.valueOf(modelProducts1.getQuantity()));
            context.startActivity(intent);


        });
    }

    @Override
    public int getItemCount() {
        return modelProducts.size();
    }

    public static class CommandsViewHolder extends RecyclerView.ViewHolder {
        TextView TvName,TvDescription;
        RelativeLayout RvClick;
        public CommandsViewHolder(@NonNull View itemView) {
            super(itemView);
            TvName = itemView.findViewById(R.id.TvName);
            TvDescription = itemView.findViewById(R.id.TvDescription);
            RvClick = itemView.findViewById(R.id.RvClick);
        }
    }

}
