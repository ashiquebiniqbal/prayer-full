package net.fajarachmad.prayer.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.wrapper.KeyValue;

import java.util.List;

/**
 * Created by user on 3/9/2016.
 */
public class CommonPopupListviewAdapter extends ArrayAdapter<KeyValue> {

    private List<KeyValue> listData;
    private RadioButton selectedRadioButton;
    private int selectedPosition = 0;
    private CommonPopupListviewDelegate delegate;

    public CommonPopupListviewAdapter(Context context, int resource, List<KeyValue> objects) {
        super(context, resource, objects);
        this.listData = objects;
    }

    public void setDelegate(CommonPopupListviewDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.common_popup_listview_item, null);
        }
        final KeyValue keyValue = listData.get(position);
        if (keyValue != null) {
            ((TextView) view.findViewById(R.id.common_popup_listview_text)).setText(keyValue.getValue());
        }

        final RadioButton radioButton = (RadioButton)view.findViewById(R.id.common_popup_listview_rdbtn);
        TextView textView = (TextView)view.findViewById(R.id.common_popup_listview_text);

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedPosition != position && selectedRadioButton != null) {
                    selectedRadioButton.setChecked(false);
                }

                selectedPosition = position;
                selectedRadioButton = (RadioButton) view;

                if (delegate != null) {
                    delegate.onClickListItem(keyValue, position);
                }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPosition != position && selectedRadioButton != null) {
                    selectedRadioButton.setChecked(false);
                }
                radioButton.setChecked(true);
                selectedPosition = position;
                selectedRadioButton = radioButton;
                if (delegate != null) {
                    delegate.onClickListItem(keyValue, position);
                }
            }
        });

        return view;
    }
}
