package com.wwc.jajing.component;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private View title;
    private List<SectionItem> sectionItems = new ArrayList<>();

    public Section() {
    }

    public void addSectionItem(long id, int title, String icon) {
        this.sectionItems.add(new SectionItem(id, title, icon));
    }

    public View getTitle() {
        return title;
    }

    public void setTitle(View title) {
        this.title = title;
    }

    public List<SectionItem> getSectionItems() {
        return sectionItems;
    }

    public void setSectionItems(List<SectionItem> sectionItems) {
        this.sectionItems = sectionItems;
    }
}