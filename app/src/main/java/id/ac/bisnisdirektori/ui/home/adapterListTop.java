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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.ac.bisnisdirektori.R;
import id.ac.bisnisdirektori.admin.ListInformationActivity;
import id.ac.bisnisdirektori.admin.adapterList;
import id.ac.bisnisdirektori.ui.home.HomeFragment;

public class adapterListTop extends BaseAdapter implements Filterable {
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    private Activity activity;
    public adapterListTop(Activity act) {
        this.activity = act;
    }


    public int getCount() {
        return HomeFragment.id_data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    static class ViewHolder {
        TextView txtNama,txtCat, txtEmail, txtAlamat, txtOtherinfo, txtIdAdmin, txtPrice ;
        ImageView imgThumb;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        adapterListTop.ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_list_top, null);
            holder = new adapterListTop.ViewHolder();
            convertView.setTag(holder);
        }else{
            holder = (adapterListTop.ViewHolder) convertView.getTag();
        }

        holder.txtNama = (TextView) convertView.findViewById(R.id.title_product);
        holder.txtCat = (TextView) convertView.findViewById(R.id.cat_product);
        holder.txtAlamat = (TextView) convertView.findViewById(R.id.loc_product);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
        holder.txtPrice = (TextView) convertView.findViewById(R.id.price_product);


        holder.txtNama.setText(HomeFragment.nama_bisnis.get(position));
        holder.txtCat.setText(HomeFragment.kategori.get(position));
        holder.txtPrice.setText(HomeFragment.price.get(position));
        holder.txtAlamat.setText(HomeFragment.alamat.get(position));
        Picasso.with(activity).load(ADMIN_PANEL_URL+"/"+HomeFragment.foto.get(position)).placeholder(R.drawable.imgthumb).into(holder.imgThumb);
        return convertView;


    }
}
