package net.fajarachmad.prayer.common.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.fajarachmad.prayer.activity.MainActivity;
import net.fajarachmad.prayer.R;

/**
 * Created by user on 3/6/2016.
 */
public abstract class AbstractPrayerFragment extends Fragment {

    protected Toolbar toolbar;

    protected void setActivityTitle(int id) {
        ((MainActivity)getActivity()).setActivityTitle(getActivity().getResources().getString(id));
    }

    protected void setCustomToolbar(View view, String title) {
        setCustomToolbar(view, title, null);
    }

    protected void showParentToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setShowHideAnimationEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((DrawerLayout)((AppCompatActivity) getActivity()).findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    protected void hideParentToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setShowHideAnimationEnabled(false);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().hide();
            ((DrawerLayout)activity.findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }

    protected void setCustomToolbar(View view, String title, String subtitle) {
        toolbar = (Toolbar) view.findViewById(R.id.evaluation_detail_toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.prayer_back_icon);
        toolbar.setTitleTextColor(getContext().getResources().getColor(R.color.white));
        if (subtitle != null) {
            toolbar.setSubtitleTextColor(getContext().getResources().getColor(R.color.white));
            toolbar.setSubtitle(subtitle);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }
}
