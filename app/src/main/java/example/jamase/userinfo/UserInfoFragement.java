package example.jamase.userinfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jamase on 2016-04-08.
 */
public class UserInfoFragement extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    String[] s = new String[]{"dwda", "dwhnudh", "dwhnudh",
            "dwhnudh", "dwhnudh", "dwhnudh", "dwhnudh", "dwhnudh", "dwhnudh"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycleadapterxml, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycle);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mUserInfoRecycleAdapter madapter = new mUserInfoRecycleAdapter(s);
        mRecyclerView.setAdapter(madapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        return root;
    }


}
