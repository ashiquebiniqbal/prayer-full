package net.fajarachmad.prayer.prayer.wrapper;

import java.util.List;

/**
 * Created by user on 3/22/2016.
 */
public class Prayer {

    private String id;
    private String title;
    private String description;
    private List<PrayerItem> prayerItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PrayerItem> getPrayerItems() {
        return prayerItems;
    }

    public void setPrayerItems(List<PrayerItem> prayerItems) {
        this.prayerItems = prayerItems;
    }
}
