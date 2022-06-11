package com.example.grafika;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentDetails extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_details, container,
                        false);
        return view;
    }
    public void setImage(Image image) {
        //Ważne – URI zawiera id obrazka a nie nazwę
        Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Long.toString(image.id));
        ImageView imageView = getView().findViewById(R.id.imageView);
        Button backBtn = getView().findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),BrowseActivity.class);
            intent.putExtra("showDetails",false);
            getActivity().startActivity(intent);
            getActivity().finish();

        });
        imageView.setImageURI(imageUri);
        Log.d("TAG","setImage() - uri: "+imageUri);
    }
}