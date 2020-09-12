package id.ac.bisnisdirektori.review;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.admin.Server;



public class AdapterListReview extends RecyclerView.Adapter<AdapterListReview.ViewHolder>{
    Context context;
    ArrayList<HashMap<String, String>> list_rev;
    private Activity activity;
    private LayoutInflater inflater;

    public AdapterListReview(ReviewActivity reviewActivity, ArrayList<HashMap<String, String>> list_rev) {
        this.context = reviewActivity;
        this.list_rev = list_rev;
    }

    @Override
    public AdapterListReview.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_review, null);
        return new AdapterListReview.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdapterListReview.ViewHolder holder, int position) {
        Glide.with(context)
                .load(Server.URL+list_rev.get(position).get("foto"))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.foto);
        holder.fullname.setText(list_rev.get(position).get("fullname"));
        holder.rate.setText(list_rev.get(position).get("rate"));
        holder.review.setText(list_rev.get(position).get("review"));
        holder.tanggal.setText(list_rev.get(position).get("tanggal"));
        holder.rating.setRating(Float.parseFloat(list_rev.get(position).get("rate")));
    }



    @Override
    public int getItemCount() {
        return list_rev.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullname, rate, review, tanggal;
        ImageView foto;
        RatingBar rating;

        public ViewHolder(View itemView) {
            super(itemView);

            fullname = (TextView) itemView.findViewById(R.id.name_user);
            rate = (TextView) itemView.findViewById(R.id.txt_rating);
            review = (TextView) itemView.findViewById(R.id.text_review);
            tanggal = (TextView) itemView.findViewById(R.id.txt_date);
            foto = (ImageView) itemView.findViewById(R.id.photo_user);
            rating = (RatingBar) itemView.findViewById(R.id.rating_product);

        }
    }
}
