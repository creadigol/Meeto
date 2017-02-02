package Adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import Models.Review_items;

/**
 * Created by Ashfaq on 8/2/2016.
 * create myTag
 */

public abstract class Recycleadapter<VH extends RecyclerView.ViewHolder>
            extends RecyclerView.Adapter<VH> {
        private ArrayList<Review_items> items = new ArrayList<Review_items>();

        public Recycleadapter() {
            setHasStableIds(true);
        }

        public void add(Review_items object) {
            items.add(object);
            notifyDataSetChanged();
        }

        public void add(int index, Review_items object) {
            items.add(index, object);
            notifyDataSetChanged();
        }

        public void addAll(Collection<? extends Review_items> collection) {
            if (collection != null) {
                items.addAll(collection);
                notifyDataSetChanged();
            }
        }

        public void addAll(Review_items... items) {
            addAll(Arrays.asList(items));
        }

        public void clear() {
            items.clear();
            notifyDataSetChanged();
        }



        public Review_items getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
