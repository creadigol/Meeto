package Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Models.NotificationItem;
import Utils.CommonUtils;
import creadigol.com.Meetto.Database.DataBaseHelper;
import creadigol.com.Meetto.R;


/**
 * Created by Creadigol on 12-09-2016.
 */
public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyViewHolder> {

    Context context;
    DataBaseHelper dataBaseHelper;
    ArrayList<NotificationItem> notification_items;

    public Notification_Adapter(Context context, ArrayList<NotificationItem> notification_items) {
        this.context = context;
        this.notification_items = notification_items;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_notification_activity, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        dataBaseHelper=new DataBaseHelper(context);

        NotificationItem notificationobject = notification_items.get(position);
        holder.tv_notidate.setText(CommonUtils.getFormatedDate(notificationobject.getCreatedTime(), "MMM dd hh:mm aaa"));
        holder.tv_notisub.setText(CommonUtils.getCapitalize(notificationobject.getDescription()));
        holder.tv_notitype.setText(CommonUtils.getCapitalize(notificationobject.getType()));

    }

    @Override
    public int getItemCount() {
        return notification_items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_notidate,tv_notisub,tv_notitype;
        public ImageView imvImage;
        public CardView viewdeals;
        public MyViewHolder(View v) {
            super(v);

            tv_notidate = (TextView) v.findViewById(R.id.txt_notification_date);
            tv_notisub= (TextView) v.findViewById(R.id.txt_notification_sub);
            tv_notitype= (TextView) v.findViewById(R.id.txt_notification_type);
            imvImage = (ImageView) v.findViewById(R.id.img_notification_image);
            viewdeals=(CardView)v.findViewById(R.id.viewdeals);

        }


    }

    public void modifyDataSet(ArrayList<NotificationItem> notification_items) {
        this.notification_items = notification_items;
        this.notifyDataSetChanged();
    }

}
