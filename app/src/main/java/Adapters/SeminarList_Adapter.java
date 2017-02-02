package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import Models.SeminarListItem;
import Utils.PreferenceSettings;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;
import creadigol.com.Meetto.SeminarDetail_Activity;


/**
 * Created by Creadigol on 07-09-2016.
 */
public class SeminarList_Adapter extends RecyclerView.Adapter<SeminarList_Adapter.OutletViewHolder> {

    public Context context;
    View view;
    private ArrayList<SeminarListItem> seminarListItems;
    PreferenceSettings mPreferenceSettings;
    View.OnClickListener onClickListener;




    public SeminarList_Adapter(Context context, ArrayList<SeminarListItem> seminarListItems,View.OnClickListener onClickListener) {
        this.context = context;
        this.seminarListItems = seminarListItems;
        this.onClickListener=onClickListener;

    }

    @Override
    public OutletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_seminarlist_grid, parent, false);
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        OutletViewHolder outletViewHolder = new OutletViewHolder(view);

        return outletViewHolder;
    }

    @Override
    public void onBindViewHolder(OutletViewHolder holder, int position) {
        final SeminarListItem model = seminarListItems.get(position);

        // bitmap
        // setting title
        holder.tvPlaceName.setText(model.getSeminar_name());
        holder.seminaraddres.setText(model.getSeminar_addrress());

        if(model.getWishlist().equals("true"))
        {
            holder.iv_wishlist.setImageResource(R.drawable.starclick);
        }
        MeettoApplication.getInstance().getImageLoader().displayImage(model.getSeminar_image(), holder.ivseminarimage, getDisplayImageOptions());

        holder.cvOutlet.setTag(position);
        holder.cvOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(context, SeminarDetail_Activity.class);
                I.putExtra(SeminarDetail_Activity.EXTRA_KEY_SEMINAR_ID, model.getSeminar_id());
                I.putExtra("seminar_name",model.getSeminar_name());
                context.startActivity(I);
            }
        });

        holder.iv_wishlist.setTag(position);
        holder.iv_wishlist.setOnClickListener(onClickListener);
        /* {
            @Override
            public void onClick(View v) {
                seminarid=model.getSeminar_id();
                Log.e("adapterid",""+seminarid);

                reqAddWishlist();
            }
        });*/


    }

    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.nophoto)
                .showImageOnFail(R.drawable.nophoto)
                .showImageOnLoading(R.drawable.nophoto)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();
        return options;
    }

    @Override
    public int getItemCount() {
        return seminarListItems.size();
    }

    public class OutletViewHolder extends RecyclerView.ViewHolder {

        public TextView tvPlaceName, seminaraddres;
        public ImageView ivseminarimage, iv_wishlist;
        public CardView cvOutlet;

        public OutletViewHolder(View itemView) {
            super(itemView);

            cvOutlet = (CardView) itemView.findViewById(R.id.cv_outlet);
            seminaraddres = (TextView) itemView.findViewById(R.id.tv_seminar_address);
            tvPlaceName = (TextView) itemView.findViewById(R.id.tv_seminar_name);
            ivseminarimage = (ImageView) itemView.findViewById(R.id.iv_similierimage);
            iv_wishlist = (ImageView) itemView.findViewById(R.id.iv_wishlist);
        }
    }
}
