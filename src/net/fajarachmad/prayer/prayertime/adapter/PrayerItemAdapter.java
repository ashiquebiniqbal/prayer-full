package net.fajarachmad.prayer.prayertime.adapter;

import java.util.List;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.activity.NotificationSetupActivity;
import net.fajarachmad.prayer.prayertime.wrapper.PrayerItemWrapper;
import net.fajarachmad.prayer.common.constant.AppConstant;
import net.fajarachmad.prayer.prayertime.fragment.PrayerTimeFragment;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PrayerItemAdapter extends RecyclerView.Adapter<PrayerItemAdapter.PrayerItemViewHolder> implements AppConstant {
	
	private List<PrayerItemWrapper> prayerItems;
	private PrayerTimeFragment context;
	
	public PrayerItemAdapter(PrayerTimeFragment context, List<PrayerItemWrapper> prayerItems) {
		this.prayerItems = prayerItems;
		this.context = context;
	}
	
	@Override
	public int getItemCount() {
		return prayerItems.size();
	}

	@Override
	public void onBindViewHolder(final PrayerItemViewHolder prayerItemViewHolder, int i) {
		final PrayerItemWrapper prayerItem = prayerItems.get(i);
		prayerItemViewHolder.prayerName.setText(prayerItem.getPrayerName());
		prayerItemViewHolder.prayerTime.setText(prayerItem.getPrayerTime());
		
		prayerItemViewHolder.onPrayerTimeIcon.setImageDrawable(prayerItem.getOnPrayerAlarmIcon());
		prayerItemViewHolder.prayerIcon.setImageDrawable(prayerItem.getPrayerIcon());
		
		if (prayerItem.isBeforePrayerAlarm()) {
			prayerItemViewHolder.beforePrayerTimeIcon.setVisibility(View.VISIBLE);
			prayerItemViewHolder.beforePrayerTimeIcon.setText(prayerItem.getBeforePrayerTimeAlarm());
		} else {
			prayerItemViewHolder.beforePrayerTimeIcon.setVisibility(View.GONE);
		}
		
		prayerItemViewHolder.cardView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context.getContext(), NotificationSetupActivity.class);
				i.putExtra("PrayID", prayerItem.getPrayerId());
				i.putExtra("PrayName", prayerItem.getPrayerName());
				context.startActivityForResult(i, NOTIFICATION_SETTING_ID);
			}
		});
	}

	@Override
	public PrayerItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		 View itemView = LayoutInflater.
                 from(viewGroup.getContext()).
                 inflate(R.layout.prayertime_item_cardview, viewGroup, false);

         return new PrayerItemViewHolder(itemView);
	}
	
	public static class PrayerItemViewHolder extends RecyclerView.ViewHolder {
		
		protected TextView prayerName;
		protected TextView prayerTime;
		protected ImageView prayerIcon;
		protected ImageView onPrayerTimeIcon;
		protected TextView beforePrayerTimeIcon;
		protected CardView cardView;
		
		public PrayerItemViewHolder(View itemView) {
			super(itemView);
			cardView = (CardView) itemView.findViewById(R.id.card_view);
			prayerName = (TextView) itemView.findViewById(R.id.prayer_name);
			prayerTime = (TextView) itemView.findViewById(R.id.prayer_time);
			prayerIcon = (ImageView) itemView.findViewById(R.id.prayer_icon);
			onPrayerTimeIcon = (ImageView) itemView.findViewById(R.id.on_pray_icon);
			beforePrayerTimeIcon = (TextView) itemView.findViewById(R.id.before_pray_icon);
		}
		
	}
}
