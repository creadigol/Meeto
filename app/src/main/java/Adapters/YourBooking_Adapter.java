package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import Models.YourBookinglist_item;
import Utils.CommonUtils;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;

/**
 * Created by Ashfaq on 7/8/2016.
 * to display Deals& Offers in activity
 */
public class YourBooking_Adapter extends RecyclerView.Adapter<YourBooking_Adapter.MyViewHolder> {

    Context context;
    ArrayList<YourBookinglist_item> yourBookinglistItems;
    View.OnClickListener onClickListener;
    View.OnClickListener onClickdownload;
    View.OnLongClickListener onLongClickListener;

    public YourBooking_Adapter(Context context, ArrayList<YourBookinglist_item> yourBookinglistItems, View.OnClickListener onClickListener, View.OnClickListener onClickdownload, View.OnLongClickListener onLongClickListener) {
        this.context = context;
        this.yourBookinglistItems = yourBookinglistItems;
        this.onClickListener = onClickListener;
        this.onClickdownload = onClickdownload;
        this.onLongClickListener = onLongClickListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_booking_activity, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final YourBookinglist_item list = yourBookinglistItems.get(position);

        holder.tvseminarname.setText(list.getSeminar_name());
        holder.tvhostname.setText(list.getHost_name());
        if (list.getFromdate() != null && list.getFromdate().length() > 0) {
            holder.txt_fromdate.setText(CommonUtils.getFormatedDate(Long.parseLong(list.getFromdate()), "dd-MM-yy"));
        }
        if (list.getTodate() != null && list.getTodate().length() > 0) {
            holder.txt_todate.setText(CommonUtils.getFormatedDate(Long.parseLong(list.getTodate()), "dd-MM-yy"));
        }
        if (list.getBookingdate() != null && list.getBookingdate().length() > 0) {
            holder.tvbooking_date.setText(CommonUtils.getFormatedDate(Long.parseLong(list.getBookingdate()), "dd-MM-yy"));
        }
//        holder.txt_todate.setText(list.getTodate());
        holder.txt_no.setText(list.getBooking_no());
        if (list.getHost_approval().equalsIgnoreCase("pending")) {
            holder.tvhost_approved.setTextColor(Color.RED);
            holder.tvhost_approved.setText(list.getHost_approval());
            holder.btn_dwnld.setVisibility(View.GONE);
        } else {
//           holder.tvhost_approved.setTextColor(Color.parseColor(String.valueOf(R.color.colorgreen)));
            holder.tvhost_approved.setTextColor(context.getResources().getColor(R.color.colorgreen));
            holder.tvhost_approved.setText(list.getHost_approval());
        }


        MeettoApplication.getInstance().getImageLoader().displayImage(list.getBooking_pic(), holder.imvEventImage, getDisplayImageOptions());

        holder.viewdeals.setTag(position);
        holder.viewdeals.setOnClickListener(onClickListener);
        holder.viewdeals.setOnLongClickListener(onLongClickListener);
        holder.btn_dwnld.setTag(position);
        holder.btn_dwnld.setOnClickListener(onClickdownload);
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
        return yourBookinglistItems.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvbooking_date, tvseminarname, tvhostname, tvdate, tvhost_approved, txt_fromdate, txt_todate, txt_no;
        public ImageView imvEventImage, btn_dwnld;
        public CardView viewdeals;

        public MyViewHolder(View v) {
            super(v);

            tvbooking_date = (TextView) v.findViewById(R.id.txt_booked_bdate);
            tvseminarname = (TextView) v.findViewById(R.id.txt_booked_name);
            imvEventImage = (ImageView) v.findViewById(R.id.img_book_image);
            tvhostname = (TextView) v.findViewById(R.id.txt_booked_hostname);
            txt_fromdate = (TextView) v.findViewById(R.id.txt_fromdate);
            txt_todate = (TextView) v.findViewById(R.id.txt_todate);
            txt_no = (TextView) v.findViewById(R.id.txt_no);
            tvhost_approved = (TextView) v.findViewById(R.id.txt_booked_approval);
            viewdeals = (CardView) v.findViewById(R.id.cv_booking);
            btn_dwnld = (ImageView) v.findViewById(R.id.btn_dwnld);

        }
    }

    public void modifyDataSet(ArrayList<YourBookinglist_item> searchObjects) {
        this.yourBookinglistItems = searchObjects;
        this.notifyDataSetChanged();
    }

}