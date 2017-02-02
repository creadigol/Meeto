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
public class Userseminartype_Adapter extends RecyclerView.Adapter<Userseminartype_Adapter.MyViewHolder> {

    Context context;
    ArrayList<User_data_item> user_data_items;
    MyViewHolder veiwHolderPrev;

    public Userseminartype_Adapter(Context context, ArrayList<User_data_item> user_data_items) {
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

        holder.tv_name.setText(list.getSem_name());
        if(MeettoApplication.seminattype!=null||MeettoApplication.seminattype.size()>0)
        {
            for (int i=0;i<MeettoApplication.seminattype.size();i++)
            {
                if (MeettoApplication.seminattype.get(i).equalsIgnoreCase(list.getSem_id()))
                {
                    holder.ll_category.setBackgroundResource(R.drawable.textview_click);
                    holder.tv_name.setTextColor(Color.WHITE);
                    veiwHolderPrev = holder;
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
               if(holder.tv_name.getCurrentTextColor()!=Color.WHITE)
               {
                   holder.ll_category.setBackgroundResource(R.drawable.textview_click);
                   holder.tv_name.setTextColor(Color.WHITE);
                   MeettoApplication.seminattype.clear();
                   if (MeettoApplication.seminattype.size()>0)
                   MeettoApplication.seminattype.set(0,user_data_item.getSem_id());
                   else
                       MeettoApplication.seminattype.add(0,user_data_item.getSem_id());
                   if (veiwHolderPrev!=null)
                   {
                       veiwHolderPrev.ll_category.setBackgroundResource(R.drawable.textview_background);
                       veiwHolderPrev.tv_name.setTextColor(Color.GRAY);
                   }
                   veiwHolderPrev=holder;
               }
               else
                   {
                   holder.ll_category.setBackgroundResource(R.drawable.textview_background);
                   holder.tv_name.setTextColor(Color.GRAY);
                       if (MeettoApplication.seminattype!=null && MeettoApplication.seminattype.size()>0 && MeettoApplication.seminattype.contains(user_data_item.getSem_id()))
                       {
                           MeettoApplication.seminattype.remove(MeettoApplication.seminattype.indexOf(user_data_item.getSem_id()));
                           veiwHolderPrev = null;
                       }
               }
//                MeettoApplication.seminattype=seminartype;
                Log.e("seminattype",""+MeettoApplication.seminattype);
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