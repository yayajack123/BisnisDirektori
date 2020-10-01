package id.ac.bisnisdirektori;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import id.ac.bisnisdirektori.admin.Server;
import id.ac.bisnisdirektori.review.AdapterListReview;


public class AdapterListProductCategory extends RecyclerView.Adapter<AdapterListProductCategory.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> list_cat;
    private Activity activity;
    private LayoutInflater inflater;

    public AdapterListProductCategory(DetailCategory detailCategory, ArrayList<HashMap<String, String>> list_cat) {
        this.context = detailCategory;
        this.list_cat = list_cat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_top, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(Server.URL+list_cat.get(position).get("foto"))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.foto);
        holder.nama_bisnis.setText(list_cat.get(position).get("nama_bisnis"));

        holder.kategori.setText(list_cat.get(position).get("kategori"));
        holder.alamat.setText(list_cat.get(position).get("alamat"));
        holder.no_telp.setText(list_cat.get(position).get("no_telp"));
//        holder.rating.setRating(Float.parseFloat(list_top.get(position).get("rata")));
        holder.jumlah_review.setText(list_cat.get(position).get("jumlah_review"));
        if(list_cat.get(position).get("rata")=="null"){
            holder.rating.setRating(Float.parseFloat("0"));
            holder.rate.setText("0");
        }else{
            holder.rating.setRating(Float.parseFloat(list_cat.get(position).get("rata")));
            holder.rate.setText(list_cat.get(position).get("rata"));
        }
    }



    @Override
    public int getItemCount() {
        return list_cat.size();
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
