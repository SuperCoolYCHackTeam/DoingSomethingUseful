package com.ychack.doingsomethinguseful;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static com.ychack.doingsomethinguseful.CameraActivity.capturedImagePath;


public class CameraFragment extends Fragment {

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_camera, container, false);

        Button buttonClick = (Button) v.findViewById(R.id.cameraButton);
        Button b = (Button) v.findViewById(R.id.instapaintButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PATH OF IMAGE "+capturedImagePath);
                if(capturedImagePath != null) {
                    String url = InstaPaintingService.instapaintIt(capturedImagePath);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                else {
                    Toast.makeText(CameraFragment.this.getActivity(),"Please Take A Picture",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        buttonClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(CameraFragment.this.getActivity(), CameraActivity.class));
            }
        });

        Button button2 = (Button) v.findViewById(R.id.webButton);
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(new Intent (CameraFragment.this.getActivity(), WebFeedActivity.class));
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
