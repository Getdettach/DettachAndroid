package com.wwc.jajing.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.wwc.R;
import com.wwc.jajing.component.SectionItem;

import java.util.List;


/**
 * The adapter class to display navigation menu list.
 */
public class MenuListAdapter extends BaseAdapter {
    private Context context = null;
    private final LayoutInflater inflater;
    private final List<SectionItem> sections;

    /**
     * constructor
     *
     * @param context
     * @param sections
     */
    public MenuListAdapter(Context context, List<SectionItem> sections) {
        this.sections = sections;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return sections.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return sections.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)} to specify
     * a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose
     *                    view we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible
     *                    to convert this view to display the correct data, this method can create a
     *                    new view. Heterogeneous lists can specify their number of view types, so
     *                    that this View is always of the right type (see {@link
     *                    #getViewTypeCount()} and {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fragment_main,
                        parent, false);
            }

            SectionItem oSectionItem = this.sections.get(position);
            TextView tvMenuLabel = (TextView) convertView
                    .findViewById(R.id.slidingmenu_sectionitem_label);
            ImageView tvMenuIcon = (ImageView) convertView
                    .findViewById(R.id.slidingmenu_sectionitem_icon);
            convertView.setId((int) oSectionItem.getId());
            tvMenuLabel.setText(oSectionItem.getTitle());
            tvMenuIcon.setVisibility(View.VISIBLE);
            tvMenuIcon.setImageDrawable(getDrawableByName(
                    oSectionItem.getIcon(), this.context));
            return convertView;
        }
    }

    /**
     * returns the drawable resources based on name input.
     *
     * @param name
     * @param context
     * @return
     */
    private static Drawable getDrawableByName(String name, Context context) {
        int drawableResource = context.getResources().getIdentifier(
                name,
                "drawable",
                context.getPackageName());
        if (drawableResource == 0) {
            throw new RuntimeException("Can't find drawable with name: " + name);
        }
        return context.getResources().getDrawable(drawableResource);
    }

}

