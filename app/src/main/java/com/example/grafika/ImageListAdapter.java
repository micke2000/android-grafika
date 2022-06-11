package com.example.grafika;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grafika.Image;
import com.example.grafika.R;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends
        RecyclerView.Adapter<ImageListAdapter.OcenyViewHolder> {
    public List<Image> getImageList() {
        return mImageLista;
    }

    public void setImageList(List<Image> mImageLista) {
        this.mImageLista = mImageLista;
    }

    private List<Image> mImageLista;
    private Activity mActivity;
    private Image currentImage;

    //konstruktor
    public ImageListAdapter(Activity kontekst, List<Image>images) {
        this.mImageLista = images;
        this.mActivity = kontekst;
    }

    @NonNull
    @Override
    public OcenyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //utworzenie layoutu wiersza na podstawie XMLa
        LayoutInflater mPompka = mActivity.getLayoutInflater();
        View wiersz = mPompka.inflate(R.layout.zdjecie_view,viewGroup, false);
        //zwrócenie nowego obiektu holdera
        return new OcenyViewHolder(wiersz);
    }

    //wypełnia wiersz przechowywany w pojemniku danymi dla kreślonego wiersza
    @Override
    public void onBindViewHolder( @NonNull OcenyViewHolder ocenyViewHolder, int numerWiersza) {
        Image image = mImageLista.get(numerWiersza);
        ocenyViewHolder.mTextName.setText(image.getName());
        ocenyViewHolder.mButton.setTag(numerWiersza);
    }

    @Override
    public int getItemCount() {
        return mImageLista.size();
    }

    public class OcenyViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTextName;
        Button mButton;

       public OcenyViewHolder(@NonNull View glownyElementWiersza) {
            super(glownyElementWiersza);
           mButton = itemView.findViewById(R.id.button);
           mTextName = itemView.findViewById(R.id.nazwa);
           mButton.setOnClickListener(view -> {
               System.out.println("Wcisniety button, ide do okna z detalami");
               Intent intent = new Intent(mActivity,BrowseActivity.class);
               int index = (Integer) mButton.getTag();
               Image img = mImageLista.get(index);
               System.out.println(index);
               intent.putExtra("currentImage",img);
               intent.putExtra("showDetails",true);
               mActivity.finish();
               mActivity.startActivity(intent);
           });
        }

    }
}


