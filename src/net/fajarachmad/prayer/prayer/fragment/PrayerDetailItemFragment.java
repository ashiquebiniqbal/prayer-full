package net.fajarachmad.prayer.prayer.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.common.fragment.AbstractPrayerFragment;
import net.fajarachmad.prayer.prayer.wrapper.Prayer;
import net.fajarachmad.prayer.prayer.wrapper.PrayerItem;

/**
 * Created by user on 3/22/2016.
 */
public class PrayerDetailItemFragment extends AbstractPrayerFragment{

    private PrayerItem prayerItem;

    public static PrayerDetailItemFragment newInstance(PrayerItem prayerItem) {
        PrayerDetailItemFragment f = new PrayerDetailItemFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        bundle.putString(PrayerItem.class.getName(), gson.toJson(prayerItem));
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        if (getArguments() != null) {
            prayerItem = gson.fromJson(getArguments().getString(PrayerItem.class.getName()), PrayerItem.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prayer_detail_item, container, false);

        WebView content = (WebView)view.findViewById(R.id.prayer_detail_item_content);
        String fontPath = "fonts/_PDMS_Saleem_QuranFont.ttf";
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
       // content.setTypeface(tf);
        //content.setText(Html.fromHtml(prayerItem.getContent()));

        content.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        String head = "<head><style>@font-face {font-family: 'arial';src: url('file:///android_asset/fonts/_PDMS_Saleem_QuranFont.ttf');}body {font-family: 'verdana';}</style></head>";
        String html = "<html>"+head
                + "<body style=\"font-family: arial\">" + Html.fromHtml(prayerItem.getContent()).toString()
                + "</body></html>";

        content.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

        return view;
    }
}
