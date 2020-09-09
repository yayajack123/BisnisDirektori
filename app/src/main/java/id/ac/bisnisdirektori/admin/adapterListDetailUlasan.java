package id.ac.bisnisdirektori.admin;

import android.app.Activity;
import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.ac.bisnisdirektori.R;

public class adapterListDetailUlasan extends BaseAdapter {
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    private Activity activity;
    public adapterListDetailUlasan(Activity act) {
        this.activity = act;
    }


    public int getCount() {
        return ListUlasanActivity.id_review.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView txtIdData, txtReview, txtTanggal, txtFullname, txtNumRate;
        ImageView imgThumb;
        RatingBar txtRate;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        adapterListDetailUlasan.ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_list_review_admin, null);
            holder = new adapterListDetailUlasan.ViewHolder ();
            convertView.setTag(holder);
        }else{
            holder = (adapterListDetailUlasan.ViewHolder) convertView.getTag();
        }

        holder.txtIdData = (TextView) convertView.findViewById(R.id.idData);
        holder.txtFullname = (TextView) convertView.findViewById(R.id.name_user);
        holder.txtRate = (RatingBar) convertView.findViewById (R.id.rating_product);
        holder.txtNumRate = (TextView) convertView.findViewById(R.id.txt_rating);
        holder.txtReview = (TextView) convertView.findViewById (R.id.text_review);
        holder.txtTanggal = (TextView) convertView.findViewById (R.id.txt_date);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.photo_user);


        holder.txtReview.setText (ListUlasanActivity.review.get(position));
        holder.txtFullname.setText (ListUlasanActivity.fullname.get(position));
        holder.txtTanggal.setText(ListUlasanActivity.tanggal.get(position));
        holder.txtRate.setRating (Float.parseFloat (ListUlasanActivity.rate.get(position)));
        holder.txtNumRate.setText(ListUlasanActivity.rate.get(position));
        holder.txtIdData.setText(ListUlasanActivity.id_data.get(position));
        Picasso.with(activity).load(ADMIN_PANEL_URL+"/"+ListUlasanActivity.foto.get(position)).placeholder(R.drawable.userthumb).into(holder.imgThumb);
        return convertView;


    }




}
