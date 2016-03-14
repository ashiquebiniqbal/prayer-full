package net.fajarachmad.prayer.evaluation.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.wrapper.KeyValue;
import net.fajarachmad.prayer.evaluation.wrapper.Achievment;

import java.text.SimpleDateFormat;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by user on 3/12/2016.
 */
public class EvaluationStatisticCalendarAdapter extends CaldroidGridAdapter {

    public static String ACHIEVMENT_LIST = "achievment_today";

    private SimpleDateFormat dateFormater;

    public EvaluationStatisticCalendarAdapter(Context context, int month, int year,
                                       Map<String, Object> caldroidData,
                                       Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
        dateFormater = new SimpleDateFormat("dd-MM-yyyy");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.evaluation_statistic_calendar_cell, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.evaluation_statistic_calendar_cell_tv1);
        TextView tv2 = (TextView) cellView.findViewById(R.id.evaluation_statistic_calendar_cell_tv2);

        tv1.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(resources
                    .getColor(com.caldroid.R.color.caldroid_sky_blue));

            tv1.setTextColor(Color.BLACK);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
            } else {
                cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
            }
        }

        tv1.setText("" + dateTime.getDay());

        Map<String, Object> achievmentMap = (Map<String, Object>) extraData.get(ACHIEVMENT_LIST);
        if (achievmentMap != null) {
            String key = new StringBuilder().append(dateTime.getDay()).append(dateTime.getMonth()).append(dateTime.getYear()).toString();
            if (achievmentMap.containsKey(key)) {
                Achievment achievment = (Achievment)achievmentMap.get(key);
                tv2.setText(new StringBuilder().append(achievment.getAchievment()).append(" ").append(achievment.getTargentUnit()));
            }

        }

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        return cellView;
    }
}
