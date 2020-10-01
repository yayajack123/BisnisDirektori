package id.ac.bisnisdirektori.review;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.ac.bisnisdirektori.R;


public class AdapterListPhotoReview extends BaseAdapter {
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    private Activity activity;
    public AdapterListPhotoReview(Activity act) {
        this.activity = act;
    }


    public int getCount() {
        return ListPhotoReviewActivity.id_review.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView txtIdData ;
        ImageView imgThumb;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterListPhotoReview.ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_list_photo, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }else{
            holder = (AdapterListPhotoReview.ViewHolder) convertView.getTag();
        }

        holder.txtIdData = (TextView) convertView.findViewById(R.id.idData);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);


        holder.txtIdData.setText(ListPhotoReviewActivity.id_data.get(position));
        Picasso.with(activity).load(ADMIN_PANEL_URL+"/"+ListPhotoReviewActivity.foto.get(position)).placeholder(R.drawable.imgthumb).into(holder.imgThumb);
        return convertView;


    }
}
