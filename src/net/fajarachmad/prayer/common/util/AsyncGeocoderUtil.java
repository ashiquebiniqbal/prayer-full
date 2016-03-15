package net.fajarachmad.prayer.common.util;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import net.fajarachmad.prayer.prayertime.wrapper.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Achmad_R on 3/15/2016.
 */
public class AsyncGeocoderUtil extends AsyncTask<Geocoder, Void, String> {

    public static final String HTTP_RESPONSE = "httpResponse";

    private Context context;
    private String searchKey;
    private String action;
    private Gson gson;

    public AsyncGeocoderUtil(Context context, String searchKey, String action) {
        this.context = context;
        this.searchKey = searchKey;
        this.action = action;
        gson = new Gson();
    }

    @Override
    protected String doInBackground(Geocoder... params) {
       try {
            List<Address> addresses = params[0].getFromLocationName(searchKey, 50);
            List<Location> locations = new ArrayList<Location>();
            for (Address address : addresses) {
                if (address.getFeatureName() != null && address.getCountryName() != null) {
                    Location location = new Location();
                    location.setAddressLine(address.getAddressLine(0));
                    location.setCity(address.getFeatureName());
                    location.setCountry(address.getCountryName());
                    location.setPostalCode(address.getPostalCode());
                    location.setLatitude(address.getLatitude());
                    location.setLongitude(address.getLongitude());
                    locations.add(location);
                }
            }
            return gson.toJson(locations);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        Intent intent = new Intent(action);
        intent.putExtra(HTTP_RESPONSE, result);

        // broadcast the completion
        context.sendBroadcast(intent);
    }
}
