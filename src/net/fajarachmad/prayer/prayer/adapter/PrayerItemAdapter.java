package net.fajarachmad.prayer.prayer.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.evaluation.fragment.EvaluationDetailFragment;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;
import net.fajarachmad.prayer.prayer.fragment.PrayerDetailFragment;
import net.fajarachmad.prayer.prayer.fragment.PrayerFragment;
import net.fajarachmad.prayer.prayer.wrapper.Prayer;

import java.util.List;

/**
 * Created by user on 3/22/2016.
 */
public class PrayerItemAdapter extends RecyclerView.Adapter<PrayerItemAdapter.PrayerItemViewHolder>{

    private List<Prayer> prayerList;
    private Gson gson;
    private PrayerFragment context;


    public PrayerItemAdapter(PrayerFragment context, List<Prayer> prayerList) {
        this.prayerList = prayerList;
        this.context = context;
        this.gson = new Gson();
    }

    @Override
    public int getItemCount() {
        return prayerList.size();
    }

    @Override
    public PrayerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.prayer_item_cardview, parent, false);

        return new PrayerItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PrayerItemViewHolder holder, int position) {
        final Prayer prayer = prayerList.get(position);

        holder.prayerName.setText(prayer.getTitle());
        holder.description.setText(prayer.getDescription());

        if ((position+1) != prayerList.size()) {
            holder.space.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment fragment = PrayerDetailFragment.newInstance(prayer);
                context.getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public static class PrayerItemViewHolder extends RecyclerView.ViewHolder{

        protected TextView prayerName;
        protected TextView description;
        private CardView cardView;
        private Space space;


        public PrayerItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.prayer_item_cv);
            prayerName = (TextView) itemView.findViewById(R.id.prayer_item_name);
            description = (TextView) itemView.findViewById(R.id.prayer_item_description);
            space = (Space) itemView.findViewById(R.id.prayer_item_spacebottom);
        }


    }
}
