package net.fajarachmad.prayer.prayer.wrapper;

/**
 * Created by user on 3/22/2016.
 */
public class PrayerItem {
    private String subtitle;
    private String content;

    public PrayerItem() {
    }

    public PrayerItem(String subtitle, String content) {
        this.subtitle = subtitle;
        this.content = content;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
