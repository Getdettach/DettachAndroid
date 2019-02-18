package com.wwc.jajing.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wwc.R;
import com.wwc.jajing.realmDB.ContactModel;
import com.wwc.jajing.realmDB.HasAppModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by infmac1 on 09/12/16.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>  {

    RealmResults<ContactModel> messageModelRealmResults;

    Context context;


    public ContactsAdapter(RealmResults<ContactModel> messageModelRealmResults,Context context) {
        this.messageModelRealmResults = messageModelRealmResults;
        this.context = context;
        setHasStableIds(true);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout bgLayout ;
        TextView name , number ,statusname;
        ImageView availState;
        RelativeLayout clickLayout;

        public MyViewHolder(final View view) {
            super(view);
            this.bgLayout = (LinearLayout)view.findViewById(R.id.contact_layout);
            this.clickLayout = (RelativeLayout)view.findViewById(R.id.click_layout);
            this.name = (TextView) view.findViewById(R.id.name);
            this.number = (TextView) view.findViewById(R.id.number);
            this.availState = (ImageView)view.findViewById(R.id.img_avail_status);
            this.statusname = (TextView)view.findViewById(R.id.status);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            dialNumber(number.getText().toString());
        }
    }

    private void dialNumber(String no) {
        Uri number = Uri.parse("tel:" + no);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        context.startActivity(callIntent);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_contacts_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {ContactModel obj = messageModelRealmResults.get(position);


        if (position%2 == 0){
            holder.bgLayout.setBackgroundColor(Color.parseColor("#20FFFFFF"));
        }
        else{
            holder.bgLayout.setBackgroundColor(Color.parseColor("#000000"));
        }

        String numberCheck =  obj.getNumber().length() > 8 ? obj.getNumber().substring(obj.getNumber().length() - 8) : obj.getNumber();

        Realm realm  = Realm.getInstance(context);

        RealmResults<HasAppModel> contactNumber = realm.where(HasAppModel.class).endsWith("number",numberCheck).findAll();

        try
        {
            Log.e("ContactAdapter", "contactNumber.size()==" + contactNumber.size());
            //Log.e("ContactAdapter", "contactNumber.size()==" + contactNumber.get(position).getIsActive());


            if(contactNumber.size()>0){
                Log.e("ContactAdapter", "EntercontactNumber.size()>0");
                String availDateTime=  contactNumber.get(0).getAvailTime();
                if(availDateTime.length()==0)
                {
                    Log.e("ContactAdapter", "availDateTime==" + availDateTime);
                }else
                {
                    String[] availTime=availDateTime.split(" ");
                    holder.statusname.setText(contactNumber.get(0).getStatusName()+" until "+availTime[1] +availTime[2]);
                    Log.e("ContactAdapter", "timestatus==" + contactNumber.get(0).getStatusName()+" until "+availTime[1] +availTime[2]);
                    if(contactNumber.get(0).getIsActive().compareTo("true")==0)
                    {
                        Log.e("ContactAdapter", "status==" + contactNumber.get(0).getIsActive());
                        holder.availState.setImageResource(R.drawable.icon_red);
                    }else{
                        Log.e("ContactAdapter", "icongreen==");
                        holder.availState.setImageResource(R.drawable.icon_green);
                        holder.statusname.setText("");
                    }
                }

            }else{
                holder.availState.setImageResource(R.drawable.icon_gray);
                Log.e("ContactAdapter", "icon_orangstatus");
            }


            holder.name.setText(obj.getName());
            holder.number.setText(obj.getNumber());
            holder.clickLayout.setTag(obj.getNumber());
            realm.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return messageModelRealmResults.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
