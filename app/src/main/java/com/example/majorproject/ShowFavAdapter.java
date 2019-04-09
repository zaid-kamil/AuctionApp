package com.example.majorproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ShowFavAdapter extends RecyclerView.Adapter<ShowFavAdapter.Holder> {


    Context context;
    LayoutInflater inflater;
    int layout;
    List<CreateModel> contactlist;


    public ShowFavAdapter(Context context, int layout, List<CreateModel>contactlist){
        this.context = context;
        this.layout = layout;
        this.contactlist = contactlist;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(inflater.inflate(layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        CreateModel model = contactlist.get(i);
        holder.title.setText(model.title);
        holder.discription.setText(model.discription);
        holder.minbid.setText(model.minibid);
        Glide.with(holder.imgLayout).load(model.url).into(holder.imgLayout);
        holder.btnbid.setTag(model);
        holder.btnbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateModel contact = (CreateModel) v.getTag();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("wishlist/userid/"+user.getUid()).whereEqualTo("title",contact.title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                document.getReference().delete();
                            }
                        }
                    }
                });

            }
        });

    }



    @Override
    public int getItemCount() {
        return contactlist.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        ImageView imgLayout;
        TextView title;
        TextView discription;
        TextView minbid;
        Button btnbid;
        TextView timer;
        ImageView fav;

        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            discription = itemView.findViewById(R.id.tvDiscript);
            minbid = itemView.findViewById(R.id.tvmbid);
            imgLayout = itemView.findViewById(R.id.imglayout);
            timer = itemView.findViewById(R.id.timer);
            btnbid = itemView.findViewById(R.id.bid);
            btnbid.setText("delete");
        }
    }
}
