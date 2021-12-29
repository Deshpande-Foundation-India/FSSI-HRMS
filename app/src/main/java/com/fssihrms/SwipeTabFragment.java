package com.fssihrms;

/**
 * Created by User on 6/9/2017.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import hrms.com.hrm.R;

public class SwipeTabFragment extends Fragment {

    private String tab;
    private int color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        tab = bundle.getString("tab");
        //color = bundle.getInt("color");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_tab, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(tab);
       // view.setBackgroundResource(color);
        return view;
    }
}
