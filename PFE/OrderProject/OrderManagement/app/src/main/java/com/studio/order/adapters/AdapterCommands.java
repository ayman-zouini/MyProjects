package com.studio.order.adapters;

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
import com.studio.order.models.ModelCommands;
import com.studio.order.ui.MainActivityCommandsDetails;

import java.util.List;

public class AdapterCommands extends RecyclerView.Adapter<AdapterCommands.CommandsViewHolder> {

    Context context;
    List<ModelCommands> modelCommands;

    public AdapterCommands(Context context, List<ModelCommands> modelCommands) {
        this.context = context;
        this.modelCommands = modelCommands;
    }

    @NonNull
    @Override
    public CommandsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_command, parent, false);
        return new CommandsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandsViewHolder holder, int position) {
        ModelCommands modelCommands1 = modelCommands.get(position);
        holder.TvName.setText(modelCommands1.getName());
        holder.TvDescription.setText(modelCommands1.getDescription());

        holder.RvClick.setOnClickListener(v -> {

            Intent intent = new Intent(context, MainActivityCommandsDetails.class);
            intent.putExtra("CMD_NAME",modelCommands1.getName());
            intent.putExtra("CMD_ID",String.valueOf(modelCommands1.getId()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modelCommands.size();
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
