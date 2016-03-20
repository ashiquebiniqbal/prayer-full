package net.fajarachmad.prayer.common.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.prayertime.wrapper.Location;

import java.util.List;

/**
 * Created by user on 3/20/2016.
 */
public class LocationAdapter extends ArrayAdapter<Location> {

    private List<Location> locations;

    public LocationAdapter(Context context, int resource,
                           int textViewResourceId, List<Location> objects) {
        super(context, resource, textViewResourceId, objects);
        locations = objects;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.prayertime_location_item, null);
        }
        Location location = locations.get(position);
        if (location != null) {
            String locationName = new StringBuilder()
                    .append(location.getAddressLine())
                    .append(" ")
                    .append(location.getCity())
                    .append(" ")
                    .append(location.getCountry()).toString();
            ((TextView) view.findViewById(R.id.location_name))
                    .setText(locationName);
        }
        return view;
    }
}
