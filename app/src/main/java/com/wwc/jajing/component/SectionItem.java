package com.wwc.jajing.component;

/**
 * This SectionItem class is used in ListView to display each items.
 */
public class SectionItem {
    private long id;
    private int title;
    private String icon;

    /**
     * Constructor
     *
     * @param id
     * @param title
     * @param icon
     */
    public SectionItem(long id, int title, String icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    /**
     * return title
     *
     * @return
     */
    public int getTitle() {
        return title;
    }

    /**
     * This method is to set title text.
     *
     * @param title
     */
    public void setTitle(int title) {
        this.title = title;
    }

    /**
     * return icon
     *
     * @return
     */
    public String getIcon() {
        return icon;
    }

    /**
     * This method is to set icon.
     *
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * return id
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * This method is to set item id.
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }
}
