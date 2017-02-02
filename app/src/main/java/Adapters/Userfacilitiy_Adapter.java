package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Models.User_data_item;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;


/**
 * Created by Creadigol on 12-09-2016.
 */
public class Userfacilitiy_Adapter extends RecyclerView.Adapter<Userfacilitiy_Adapter.MyViewHolder> {

    Context context;
    ArrayList<User_data_item> user_data_items;

    public Userfacilitiy_Adapter(Context context, ArrayList<User_data_item> user_data_items) {
        this.context = context;
        this.user_data_items = user_data_items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_user_data, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final User_data_item list=user_data_items.get(position);

        holder.tv_name.setText(list.getFacility_name());

        if(MeettoApplication.facility!=null||MeettoApplication.facility.size()>0)
        {
            for (int i=0;i<MeettoApplication.facility.size();i++)
            {
                if (MeettoApplication.facility.get(i).equalsIgnoreCase(list.getFacility_id()))
                {
                    Log.e("attendess",""+MeettoApplication.facility);
                    holder.ll_category.setBackgroundResource(R.drawable.textview_click);
                    holder.tv_name.setTextColor(Color.WHITE);
                }
            }
        }

        holder.ll_category.setTag(position);
        holder.ll_category.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                   int position = (int) v.getTag();
                   User_data_item user_data_item=user_data_items.get(position);
                   if(holder.tv_name.getCurrentTextColor()!=Color.WHITE){
                   holder.ll_category.setBackgroundResource(R.drawable.textview_click);
                   holder.tv_name.setTextColor(Color.WHITE);
                   MeettoApplication.facility.add(user_data_item.getFacility_id());
               }else{
                   holder.ll_category.setBackgroundResource(R.drawable.textview_background);
                   holder.tv_name.setTextColor(Color.GRAY);
                   MeettoApplication.facility.remove(MeettoApplication.facility.indexOf(user_data_item.getFacility_id()));
               }
//                MeettoApplication.industry=industry;
                Log.e("facility",""+MeettoApplication.facility);
            }
        });
}


    @Override
    public int getItemCount() {
        return user_data_items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;
        public LinearLayout  ll_category;

        public MyViewHolder(View v) {
            super(v);
            tv_name = (TextView) v.findViewById(R.id.txt_category);
            ll_category = (LinearLayout) v.findViewById(R.id.ll_category);

        }

    }

    public void modifyDataSet(ArrayList<User_data_item> user_data_items) {
        this.user_data_items = user_data_items;
        this.notifyDataSetChanged();
    }

}