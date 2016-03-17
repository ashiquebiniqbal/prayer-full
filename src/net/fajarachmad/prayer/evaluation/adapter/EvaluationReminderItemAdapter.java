package net.fajarachmad.prayer.evaluation.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.evaluation.fragment.EvaluationItemEntryFragment;
import net.fajarachmad.prayer.evaluation.fragment.EvaluationReminderEntryFragment;
import net.fajarachmad.prayer.evaluation.fragment.EvaluationReminderFragment;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;
import net.fajarachmad.prayer.evaluation.wrapper.ReminderItemWrapper;

import java.util.List;

/**
 * Created by user on 3/3/2016.
 */
public class EvaluationReminderItemAdapter extends RecyclerView.Adapter<EvaluationReminderItemAdapter.EvaluationReminderItemViewHolder>{

    private List<ReminderItemWrapper> reminderItems;
    private EvaluationReminderFragment context;
    private Gson gson;

    public EvaluationReminderItemAdapter(EvaluationReminderFragment context, List<ReminderItemWrapper> reminderItems) {
        this.reminderItems = reminderItems;
        this.context = context;
        gson = new Gson();
    }

    @Override
    public int getItemCount() {
        return reminderItems.size();
    }

    @Override
    public void onBindViewHolder(final EvaluationReminderItemViewHolder reminderItemViewHolder, int i) {
        final ReminderItemWrapper reminderItem = reminderItems.get(i);

        reminderItemViewHolder.message.setText(reminderItem.getMessage());
        reminderItemViewHolder.time.setText(reminderItem.getTime());

        if (reminderItem.isSoundEnable()) {
            reminderItemViewHolder.soundIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.prayer_volume_up));
        }

        if (reminderItem.isRepeatSun()) {
            reminderItemViewHolder.sun.setImageDrawable(context.getResources().getDrawable(R.drawable.prayer_day_icon_s_checked));
        }

        if (reminderItem.isRepeatMon()) {
            reminderItemViewHolder.mon.setImageDrawable(context.getResources().getDrawable(R.drawable.prayer_day_icon_m_checked));
        }

        if (reminderItem.isRepeatTue()) {
            reminderItemViewHolder.tue.setImageDrawable(context.getResources().getDrawable(R.drawable.prayer_day_icon_t_checked));
        }

        if (reminderItem.isRepeatWed()) {
            reminderItemViewHolder.wed.setImageDrawable(context.getResources().getDrawable(R.drawable.prayer_day_icon_w_checked));
        }

        if (reminderItem.isRepeatThu()) {
            reminderItemViewHolder.thu.setImageDrawable(context.getResources().getDrawable(R.drawable.prayer_day_icon_t_checked));
        }

        if (reminderItem.isRepeatFri()) {
            reminderItemViewHolder.fri.setImageDrawable(context.getResources().getDrawable(R.drawable.prayer_day_icon_f_checked));
        }

        if (reminderItem.isRepeatSat()) {
            reminderItemViewHolder.sat.setImageDrawable(context.getResources().getDrawable(R.drawable.prayer_day_icon_s_checked));
        }

       if ((i+1) != reminderItems.size()) {
            reminderItemViewHolder.space.setVisibility(View.GONE);
       }

        reminderItemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment fragment = new EvaluationReminderEntryFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ReminderItemWrapper.class.getName(), gson.toJson(reminderItem));
                fragment.setArguments(bundle);
                context.getParentFragment().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
        public EvaluationReminderItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.evaluation_reminder_item_cardview, viewGroup, false);

        return new EvaluationReminderItemViewHolder(itemView);
    }

    public static class EvaluationReminderItemViewHolder extends RecyclerView.ViewHolder {

        protected TextView message;
        protected ImageView soundIcon;
        protected TextView time;
        protected CardView cardView;
        protected ImageButton sun;
        protected ImageButton mon;
        protected ImageButton tue;
        protected ImageButton wed;
        protected ImageButton thu;
        protected ImageButton fri;
        protected ImageButton sat;
        protected Space space;

        public EvaluationReminderItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.evaluation_reminder_item_cv);
            message = (TextView) itemView.findViewById(R.id.evaluation_reminder_item_message);
            soundIcon = (ImageView) itemView.findViewById(R.id.evaluation_reminder_item_ic_sound);
            time = (TextView) itemView.findViewById(R.id.evaluation_reminder_item_time);
            sun = (ImageButton) itemView.findViewById(R.id.evaluation_reminder_item_btn_sun);
            mon = (ImageButton) itemView.findViewById(R.id.evaluation_reminder_item_btn_mon);
            tue = (ImageButton) itemView.findViewById(R.id.evaluation_reminder_item_btn_tue);
            wed = (ImageButton) itemView.findViewById(R.id.evaluation_reminder_item_btn_wed);
            thu = (ImageButton) itemView.findViewById(R.id.evaluation_reminder_item_btn_thu);
            fri = (ImageButton) itemView.findViewById(R.id.evaluation_reminder_item_btn_fri);
            sat = (ImageButton) itemView.findViewById(R.id.evaluation_reminder_item_btn_sat);
            space = (Space) itemView.findViewById(R.id.evaluation_reminder_item_spacebottom);
        }

    }
    }