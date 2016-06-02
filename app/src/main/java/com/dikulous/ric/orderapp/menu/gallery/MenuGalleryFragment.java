package com.dikulous.ric.orderapp.menu.gallery;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.example.ric.myapplication.backend.api.menuApi.MenuApi;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntityCollection;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuTypesEntity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuGalleryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuGalleryFragment extends Fragment {

    private static final String TYPE_FK = "typeFk";
    private static final String TAG = "gallery fragment";

    private long mTypeFk;

    private MenuGalleryAdapter mAdapter;
    private List<MenuItemEntity> mMenuItems;
    private MenuTypesEntity mMenuTypesEntity;
    private MenuDbHelper mDbHelper;

    private RecyclerView mRecyclerView;
    private ProgressBar mMenuSpinner;



    private OnFragmentInteractionListener mListener;

    public MenuGalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param typeFk Parameter 1.
     * @return A new instance of fragment MenuGalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuGalleryFragment newInstance(long typeFk) {
        MenuGalleryFragment fragment = new MenuGalleryFragment();
        Bundle args = new Bundle();
        args.putLong(TYPE_FK, typeFk);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTypeFk = getArguments().getLong(TYPE_FK);
        } else {
            Log.e(TAG, "Arguments Null");
        }
        mMenuItems = new ArrayList<>();
        mDbHelper = new MenuDbHelper(getContext());
        mMenuItems = mDbHelper.readMenuItemsByType(mTypeFk);

        mAdapter = new MenuGalleryAdapter(getContext(), mMenuItems);
        //mPhotoDbHelper = new PhotoDbHelper(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_gallery, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.menu_list);
        mMenuSpinner = (ProgressBar) view.findViewById(R.id.menu_spinner);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //new GetMenuItemsAsync().execute(getContext());
        //new GetMenuTypesAsync().execute(getContext());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
