package com.example.majorproject;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.service.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Holder> {

    Context context;
    LayoutInflater inflater;
    int layout;
    List<CreateModel> contactlist;


    public ProductAdapter(Context context, int layout, List<CreateModel> contactlist) {
        this.context = context;
        this.layout = layout;
        this.contactlist = contactlist;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(inflater.inflate(layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.Holder holder, int i) {

        CreateModel contact = contactlist.get(i);
        holder.title.setText(contact.title);
        holder.discription.setText(contact.discription);
        holder.minbid.setText(contact.minibid);
        Glide.with(holder.imgLayout).load(contact.url).into(holder.imgLayout);
        holder.btnbid.setTag(contact);
        holder.btnbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateModel contact = (CreateModel) v.getTag();
                Intent intent = new Intent(context, BidActivity.class);
                intent.putExtra("title", contact.title);
                intent.putExtra("discription", contact.discription);
                intent.putExtra("minibid", contact.minibid);
                intent.putExtra("time", contact.time);
                intent.putExtra("url", contact.url);
                context.startActivity(intent);
            }
        });
        try {
            Date date = getDate(Long.parseLong(contact.time));
            holder.timer.setText(date.toString());

        } catch (Exception e) {
            holder.timer.setText("missing");
        }
    }


    @Override
    public int getItemCount() {
        return contactlist.size();
    }

    private Date getDate(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time * 1000));
        Toast.makeText(context, localTime, Toast.LENGTH_SHORT).show();
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imgLayout;
        TextView title;
        TextView discription;
        TextView minbid;
        Button btnbid;
        TextView timer;

        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            discription = itemView.findViewById(R.id.tvDiscript);
            minbid = itemView.findViewById(R.id.tvmbid);
            imgLayout = itemView.findViewById(R.id.imglayout);
            timer = itemView.findViewById(R.id.timer);
            btnbid = itemView.findViewById(R.id.bid);
        }
    }
}
