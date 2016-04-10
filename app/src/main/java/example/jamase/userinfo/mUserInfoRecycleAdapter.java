package example.jamase.userinfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jamase on 2016-04-09.
 */
public class mUserInfoRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public String[] str;


    public mUserInfoRecycleAdapter(String[] datas) {
        str = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_active_item, parent, false);
        RecyclerView.ViewHolder hoder = new MyViewHolder1(v);
        return hoder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder1 mholder = (MyViewHolder1) holder;
//        mholder.text.setText(str[position]);

    }

    @Override
    public int getItemCount() {
        return str.length;
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        public TextView text;
        public View view;

        public MyViewHolder1(View itemView) {
            super(itemView);
            view = itemView;
            text = (TextView) itemView.findViewById(R.id.text);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    text.setTextColor(v.getResources().getColor(R.color.colorAccent));
                }
            });
        }
    }


}
