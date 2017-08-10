package com.quickenloans.referexpress.dialog;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quickenloans.referexpress.R;
import com.quickenloans.referexpress.data.User;
import com.quickenloans.referexpress.data.UserDAO;

import java.util.List;

/**
 * Created by jdeville on 6/30/17.
 */

public class  DialogListAdapter extends BaseAdapter {
    private Activity activity;
    private List<User> userList;
    private UserDAO userDAO;
    private int defaultUser;

    DialogListAdapter(Activity activity, List<User> userList){
        this.userList = userList;
        this.activity = activity;
        userDAO = new UserDAO(activity);
        defaultUser = userDAO.getDefaultUserId();
    }

    void dataSetChanged(){
        userList = userDAO.selectAll();
        defaultUser = userDAO.getDefaultUserId();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return userList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_user, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_setCheckmark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(userList.get(position).getFullName()); //todo remove id

        final int itemId = userList.get(position).getId();

        if (itemId == defaultUser){
            holder.ivSelected.setVisibility(View.VISIBLE);

        } else {
            holder.ivSelected.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }


    static class ViewHolder{
        TextView tvName;
        ImageView ivSelected;
    }
}
