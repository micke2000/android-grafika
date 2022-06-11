package com.example.grafika;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class FragmentList extends Fragment {
    private OverviewFragmentActivityListener listener;
    private List<Image> mImageList;
    private ImageListAdapter mImageListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // przypisujemy layout do fragmentu
        View view = inflater.inflate(R.layout.fragment_overview, container,
                false);
        // definiujemy listener dla poszczególnych elementów (buttonów)

        // przypisujemy elementom clickListener
        RecyclerView imagesRecycler = view.findViewById(R.id.images_recycler);
        mImageList = new ArrayList<>();
        mImageListAdapter = new ImageListAdapter(getActivity(),mImageList);
        getFileList();
        imagesRecycler.setAdapter(mImageListAdapter);
        imagesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
}
//    public void createTable(int iloscOcen, String[]przedmioty, ArrayList<Image> ListaImages){
//        for (int i = 0; i < iloscOcen; i++) {
//            ListaImages.add(new Image(i,));
//        }
//    }
// interfejs, który będzie implementować aktywność
public interface OverviewFragmentActivityListener {
    public void onItemSelected(String msg);

}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof
                OverviewFragmentActivityListener) {
            listener = (OverviewFragmentActivityListener)
                    activity;
        } else {
            throw new ClassCastException(activity.toString() + " musi implementować interfejs: OverviewFragment.OverviewFragmentActivityListener");
        }
    }

    // metoda wysyła dane do aktywności
    public void updateDetail(String msg) {
        listener.onItemSelected(msg);
    }
    private void getFileList() {
        Log.d("TAG", "getFileList()");
        Uri collection;
        String[] projection;
        String selection;
        String[] selectionArgs;
        //na Androidzie 10+ pobieramy z bazy tylko obrazki, które sami zapisaliśmy (dostęp
        //bez uprawnień
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            projection = new String[]{
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.OWNER_PACKAGE_NAME
            };
            selection = MediaStore.Images.Media.OWNER_PACKAGE_NAME + " = ?";
            selectionArgs = new String[]{getActivity().getApplicationContext().getPackageName()};
            //na Androidzie 9- nie ma informacji o właścicielu
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
            };
            selection = null;
            selectionArgs = null;
        }
        String sortOrder = MediaStore.Images.Media.DISPLAY_NAME + " ASC";
        try (Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            //zamieniamy nazwy kolumn na numery kolumn
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int packageColumn = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                packageColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.OWNER_PACKAGE_NAME);
            }
            mImageList.clear();
            while (cursor.moveToNext()) {
                //odczytujemy wartości z kolumn w poszczególnych rekordach
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                String packageName = cursor.getString(packageColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                //dodajemy informację o kolejnym obrazku do listy
                Log.d("TAG", "id: " + id + " name: " + name + " package: " + packageName);
                Image image = new Image(id, name,contentUri.toString());
                mImageList.add(image);
            }
            mImageListAdapter.setImageList(mImageList);
        }
    }

}