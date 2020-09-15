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

public class adapterListDetailCall extends BaseAdapter {

    public static String ADMIN_PANEL_URL = "https://www.pantaucovid19.net/";
    private Activity activity;
    public adapterListDetailCall(Activity act) {
        this.activity = act;
    }


    public int getCount() {
        return ListCallActivity.id_call_user.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView txtIdData, txtFullname, txtPhonenumber, txtKeterangan, txtTime;
        ImageView imgThumb;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        adapterListDetailCall.ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_list_call, null);
            holder = new adapterListDetailCall.ViewHolder ();
            convertView.setTag(holder);
        }else{
            holder = (adapterListDetailCall.ViewHolder) convertView.getTag();
        }

        holder.txtIdData = (TextView) convertView.findViewById(R.id.idData);
        holder.txtFullname = (TextView) convertView.findViewById (R.id.txtNama);
        holder.txtPhonenumber = (TextView) convertView.findViewById (R.id.txtPhonenumber);
        holder.txtKeterangan = (TextView) convertView.findViewById (R.id.txtKeterangan);
        holder.txtTime = (TextView) convertView.findViewById (R.id.textTime);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);

        holder.txtFullname.setText (ListCallActivity.fullname.get(position));
        holder.txtPhonenumber.setText (ListCallActivity.phonenumber.get(position));
        holder.txtKeterangan.setText (ListCallActivity.keterangan.get(position));
        holder.txtTime.setText (ListCallActivity.time.get(position));
        holder.txtIdData.setText(ListCallActivity.id_data.get(position));
        Picasso.with(activity).load(ADMIN_PANEL_URL+"/"+ListCallActivity.foto.get(position)).placeholder(R.drawable.userthumb).into(holder.imgThumb);
        return convertView;


    }


}
