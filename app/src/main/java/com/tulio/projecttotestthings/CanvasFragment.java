package com.tulio.projecttotestthings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tulio on 3/31/16.
 */
public class CanvasFragment extends Fragment {

    public CanvasFragment() {
    }

    public static CanvasFragment newInstance() {
        return new CanvasFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_canvas, container, false);

        initComponents();

        return view;
    }

    private void initComponents() {

    }
}
