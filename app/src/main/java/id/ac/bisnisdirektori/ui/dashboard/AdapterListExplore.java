package id.ac.bisnisdirektori.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.admin.Server;


public class AdapterListExplore extends RecyclerView.Adapter<AdapterListExplore.ViewHolder>{
    Context context;
    ArrayList<HashMap<String, String>> list_explore;
    private Activity activity;
    private LayoutInflater inflater;

    public AdapterListExplore(DashboardFragment dashboardFragment, ArrayList<HashMap<String, String>> list_explore) {
        this.context = dashboardFragment.getContext();
        this.list_explore = list_explore;
    }

    @Override
    public AdapterListExplore.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_explore, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdapterListExplore.ViewHolder holder, int position) {
        Glide.with(context)
                .load(Server.URL+list_explore.get(position).get("foto_review"))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.foto_review);
        Glide.with(context)
                .load(Server.URL+list_explore.get(position).get("foto_user"))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.foto_user);
        holder.nama_bisnis.setText(list_explore.get(position).get("nama_bisnis"));
        holder.fullname.setText(list_explore.get(position).get("fullname"));
        holder.review.setText(list_explore.get(position).get("review"));
        holder.tanggal.setText(list_explore.get(position).get("tanggal"));
    }



    @Override
    public int getItemCount() {
        return list_explore.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullname, nama_bisnis, review, tanggal;
        ImageView foto_user, foto_review;

        public ViewHolder(View itemView) {
            super(itemView);

            fullname = (TextView) itemView.findViewById(R.id.name_product);
            nama_bisnis = (TextView) itemView.findViewById(R.id.name_bisnis);
            review = (TextView) itemView.findViewById(R.id.text_review);
            tanggal = (TextView) itemView.findViewById(R.id.time_post);
            foto_review = (ImageView) itemView.findViewById(R.id.photo_uploaded);
            foto_user = (ImageView) itemView.findViewById(R.id.profil_product);
        }
    }
}
