package android.esisa.projet.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.esisa.projet.R;
import android.esisa.projet.activities.MapsActivity;
import android.esisa.projet.models.Contract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder> {

    private List<Contract> data;
    private List<Contract> cache;
    private Context context;
    public static final String BUNDLE_MAP_ACTIVITY = "MAP_ACTIVITY";

    public ContractAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        cache = this.data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView contractNameTextView;

        public ViewHolder(@NonNull View view) {
            super(view);
            contractNameTextView = view.findViewById(R.id.contractNameTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_contract_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.contractNameTextView.setText(data.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra(BUNDLE_MAP_ACTIVITY, data.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void changeData(List<Contract> data) {
        this.data = data;
        cache = data;
        notifyDataSetChanged();
    }

    public void update(String text) {
        data = cache;
        List<Contract> filtredList = new ArrayList<>();
        for(Contract c : data) {
            if(c.getName().contains(text)) {
                filtredList.add(c);
            }
        }
        data = filtredList;
        notifyDataSetChanged();
    }
}
