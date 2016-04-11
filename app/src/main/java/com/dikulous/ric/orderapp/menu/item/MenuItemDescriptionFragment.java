package com.dikulous.ric.orderapp.menu.item;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.MenuDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.util.CurrencyUtil;
import com.example.ric.myapplication.backend.api.menuApi.model.MenuItemEntity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuItemDescriptionFragment.OnDescriptionFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuItemDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuItemDescriptionFragment extends Fragment {

    private static final String MENU_ITEM_PK = "menuItemPk";
    private long mMenuItemPk;

    private OnDescriptionFragmentInteractionListener mListener;

    private MenuDbHelper mDbHelper;
    private OrderDbHelper mOrderDbHelper;
    private MenuItemEntity mMenuItem;

    private TextView mFoodName;
    private TextView mDescription;
    private TextView mPrice;
    private TextView mIngredients;
    private TextView mAllergens;
    private Button mSelectButton;


    public MenuItemDescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param menuItemPk Parameter 1.
     * @return A new instance of fragment MenuItemDescriptionFragment.
     */
    public static MenuItemDescriptionFragment newInstance(long menuItemPk) {
        MenuItemDescriptionFragment fragment = new MenuItemDescriptionFragment();
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
            mOrderDbHelper = new OrderDbHelper(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_item_description, container, false);
        mFoodName = (TextView) view.findViewById(R.id.food_name);
        mDescription = (TextView) view.findViewById(R.id.description);
        mPrice = (TextView) view.findViewById(R.id.price);
        mIngredients = (TextView) view.findViewById(R.id.ingredients);
        mAllergens = (TextView) view.findViewById(R.id.allergens);
        mSelectButton = (Button) view.findViewById(R.id.select_button);

        mFoodName.setText(mMenuItem.getName());
        mDescription.setText(mMenuItem.getDescription());

        String priceString = getResources().getString(R.string.price);
        String ingredientsString = mMenuItem.getIngredients().toString();
        mIngredients.setText("Ingredients "+ingredientsString);
        if(mMenuItem.getAllergens() !=null){
            mAllergens.setText("Allergens: "+ mMenuItem.getAllergens().toString());
            mAllergens.setVisibility(View.VISIBLE);
        }
        priceString = String.format(priceString, CurrencyUtil.longCentsToBigDecimal(mMenuItem.getPrice()));
        mPrice.setText(priceString);

        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(Uri.EMPTY);
            }
        });

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
        if (context instanceof OnDescriptionFragmentInteractionListener) {
            mListener = (OnDescriptionFragmentInteractionListener) context;
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
    public interface OnDescriptionFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
