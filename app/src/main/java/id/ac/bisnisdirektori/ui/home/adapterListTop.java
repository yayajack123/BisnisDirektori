package id.ac.bisnisdirektori.ui.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.admin.ListInformationActivity;
import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.admin.adapterList;
import id.ac.bisnisdirektori.ui.home.HomeFragment;

public class adapterListTop extends RecyclerView.Adapter<adapterListTop.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> list_top;
    private Activity activity;
    private LayoutInflater inflater;

    public adapterListTop(HomeFragment homeFragment, ArrayList<HashMap<String, String>> list_top) {
        this.context = homeFragment.getContext();
        this.list_top = list_top;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_top, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(adapterListTop.ViewHolder holder, int position) {
        Glide.with(context)
                .load(Server.URL+list_top.get(position).get("foto"))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.foto);
        holder.nama_bisnis.setText(list_top.get(position).get("nama_bisnis"));

        holder.kategori.setText(list_top.get(position).get("kategori"));
        holder.alamat.setText(list_top.get(position).get("alamat"));
        holder.no_telp.setText(list_top.get(position).get("no_telp"));
//        holder.rating.setRating(Float.parseFloat(list_top.get(position).get("rata")));
        holder.jumlah_review.setText(list_top.get(position).get("jumlah_review"));
        if(list_top.get(position).get("rata")=="null"){
            holder.rating.setRating(Float.parseFloat("0"));
            holder.rate.setText("0");
        }else{
            holder.rating.setRating(Float.parseFloat(list_top.get(position).get("rata")));
            holder.rate.setText(list_top.get(position).get("rata"));
        }
    }



    @Override
    public int getItemCount() {
        return list_top.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama_bisnis, kategori, alamat, no_telp, rate, jumlah_review;
        ImageView foto;
        RatingBar rating;

        public ViewHolder(View itemView) {
            super(itemView);

            nama_bisnis = (TextView) itemView.findViewById(R.id.title_product);
            kategori = (TextView) itemView.findViewById(R.id.cat_product);
            alamat = (TextView) itemView.findViewById(R.id.loc_product);
            no_telp = (TextView) itemView.findViewById(R.id.price_product);
            foto = (ImageView) itemView.findViewById(R.id.imgThumb);
            rate = (TextView) itemView.findViewById(R.id.text_review);
            jumlah_review = (TextView) itemView.findViewById(R.id.total_review);
            rating = (RatingBar) itemView.findViewById(R.id.rating_product);
        }
    }
}
