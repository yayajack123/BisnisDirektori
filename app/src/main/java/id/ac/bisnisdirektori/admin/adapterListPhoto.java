package id.ac.bisnisdirektori.admin;

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

public class adapterListPhoto extends BaseAdapter {
    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    private Activity activity;
    public adapterListPhoto(Activity act) {
        this.activity = act;
    }


    public int getCount() {
        return ListManagePhotoActivity.id_data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView txtNama,txtNotelp, txtEmail, txtAlamat, txtOtherinfo, txtIdAdmin, txtPrice ;
        ImageView imgThumb;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_list_manage_photo, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtNama = (TextView) convertView.findViewById(R.id.txtNama);
        holder.txtNotelp = (TextView) convertView.findViewById(R.id.txtNotelp);
        holder.txtAlamat = (TextView) convertView.findViewById(R.id.txtAlamat);
        holder.txtOtherinfo = (TextView) convertView.findViewById(R.id.txtOtherinfo);
        holder.txtIdAdmin = (TextView) convertView.findViewById(R.id.txtIdAdmin);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
        holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);


        holder.txtNama.setText(ListManagePhotoActivity.nama_bisnis.get(position));
        holder.txtNotelp.setText(ListManagePhotoActivity.no_telp.get(position));
        holder.txtAlamat.setText(ListManagePhotoActivity.alamat.get(position));
        holder.txtOtherinfo.setText(ListManagePhotoActivity.otherinfo.get(position));
        holder.txtIdAdmin.setText(ListManagePhotoActivity.id_admin.get(position));
        Picasso.with(activity).load(ADMIN_PANEL_URL+"/"+ListManagePhotoActivity.foto.get(position)).placeholder(R.drawable.imgthumb).into(holder.imgThumb);
        return convertView;


    }


}
