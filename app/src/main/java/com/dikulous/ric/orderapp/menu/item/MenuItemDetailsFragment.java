package com.dikulous.ric.orderapp.menu.item;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.model.OrderItem;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderItemEntity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuItemDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuItemDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuItemDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String MENU_ITEM_PK = "menuItemPk";
    private static final String TAG = "Item detail";

    private long mMenuItemPk;
    private MenuItemEntity mMenuItem;
    private MenuItemDetailsAdapter mAdapter;
    private MenuDbHelper mDbHelper;

    private RecyclerView mRecyclerView;
    private Button mAddToOrderButton;
    private NumberPicker mNumberPicker;

    private OrderDbHelper mOrderDbHelper;

    private OnFragmentInteractionListener mListener;

    public MenuItemDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param menuItemPk Primary key of the menu item.
     * @return A new instance of fragment MenuItemDetailsFragment.
     */
    public static MenuItemDetailsFragment newInstance(long menuItemPk) {
        MenuItemDetailsFragment fragment = new MenuItemDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(MENU_ITEM_PK, menuItemPk);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMenuItemPk = getArguments().getLong(MENU_ITEM_PK);
            mDbHelper = new MenuDbHelper(getContext());
            mMenuItem = mDbHelper.readMenuItemByPk(mMenuItemPk);
            mAdapter = new MenuItemDetailsAdapter(getContext(), mMenuItem);
            mOrderDbHelper = new OrderDbHelper(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_item_details, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ingredients_list);
        mAddToOrderButton = (Button) view.findViewById(R.id.add_to_order_button);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(10);

        mAddToOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "BUtton clicked");
                List<String> ingredientsExcluded = mAdapter.getIngredientsExcluded();
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderFk(mOrderDbHelper.readCurrentOrderPk());
                orderItem.setIngredientsExcluded(ingredientsExcluded);
                orderItem.setMenuItemKeyString(mMenuItem.getKeyString());
                orderItem.setMenuItemFk(mMenuItemPk);
                orderItem.setAmount(mNumberPicker.getValue());
                mOrderDbHelper.insertOrderItem(orderItem);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView (){
        super.onDestroyView();
    }

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
