package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import Models.ImageslideItem;
import creadigol.com.Meetto.R;


/**
 * Created by Creadigol on 07-09-2016.
 */
public class ImageSliding_Adapter extends RecyclerView.Adapter<RecyclerViewHolder> {// Recyclerview will extend to
    private ArrayList<ImageslideItem> arrayList;
    private Context context;

    public ImageSliding_Adapter(Context context,
                                ArrayList<ImageslideItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_row_slide, parent, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final ImageslideItem model = arrayList.get(position);

        RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder
        Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                model.getImage());// This will convert drawbale image into
        // bitmap
        // setting title
        mainHolder.title.setText(model.getTitle());
        mainHolder.imageview.setImageBitmap(image);


    }
}
