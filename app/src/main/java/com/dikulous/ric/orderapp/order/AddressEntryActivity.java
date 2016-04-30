package com.dikulous.ric.orderapp.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.AddressDbHelper;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.model.Address;
import com.dikulous.ric.orderapp.model.OrderItem;
import com.dikulous.ric.orderapp.order.monitor.MonitorOrderActivity;
import com.dikulous.ric.orderapp.util.CurrencyUtil;
import com.dikulous.ric.orderapp.util.Globals;
import com.example.ric.myapplication.backend.api.orderApi.OrderApi;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderEntity;
import com.example.ric.myapplication.backend.api.orderApi.model.OrderReceiptEntity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressEntryActivity extends AppCompatActivity {

    private static final String TAG = "Address Activity";

    private EditText mUnitNumber;
    private EditText mStreetNumber;
    private EditText mStreet;
    private EditText mSuburb;
    private EditText mPostcode;
    private EditText mContactNumber;

    private AddressDbHelper mAddressDbHelper;

    private ProgressDialog mProgressDialog;

    private SharedPreferences mSharedPreferences;


    private long mOrderPk;


    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(Globals.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_entry);

        mUnitNumber = (EditText) findViewById(R.id.unit_number);
        mStreetNumber = (EditText) findViewById(R.id.street_number);
        mStreet = (EditText) findViewById(R.id.street);
        mSuburb = (EditText) findViewById(R.id.suburb);
        mPostcode = (EditText) findViewById(R.id.postcode);
        mContactNumber = (EditText) findViewById(R.id.contact_number);

        mAddressDbHelper = new AddressDbHelper(this);

        mOrderPk = getIntent().getLongExtra(Globals.EXTRA_ORDER_PK, 0);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Submitting");
        mProgressDialog.setMessage("Sending order...");
        mProgressDialog.setCancelable(false);


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(-34.880490, 150.184363),
                new LatLng(-32.858754, 152.229596)));

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();

        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, "Place: " + place.getAddress());
                Log.i(TAG, "Place: " + place.getId());
                String[] addressDetails = place.getAddress().toString().split(",");
                Log.i(TAG, "AddressDeet: " + addressDetails[1]);
                mStreet.setText(addressDetails[0]);
                mPostcode.setText(addressDetails[1]);
                //String number = addressDetails[0].
                Pattern streetNumberPattern = Pattern.compile("^\\d+\\S*");
                Matcher streetNumberMatcher = streetNumberPattern.matcher(addressDetails[0].trim());
                if (streetNumberMatcher.find()) {
                    mStreetNumber.setText(streetNumberMatcher.group().trim());
                } else {
                    mStreetNumber.setText("");
                }

                Pattern streetNamePattern = Pattern.compile("^[A-z].*|\\s.*");
                Matcher streetNameMatcher = streetNamePattern.matcher(addressDetails[0].trim());
                if (streetNameMatcher.find()) {
                    mStreet.setText(streetNameMatcher.group().trim());
                } else {
                    mStreet.setText("");
                }

                Pattern suburbPattern = Pattern.compile("^\\D+");
                Matcher suburbMatcher = suburbPattern.matcher(addressDetails[1].trim());
                if (suburbMatcher.find()) {
                    mSuburb.setText(suburbMatcher.group().trim());
                } else {
                    mSuburb.setText("");
                }

                Pattern postcodePattern = Pattern.compile("\\d+$");
                Matcher postcodeMatcher = postcodePattern.matcher(addressDetails[1].trim());
                if (postcodeMatcher.find()) {
                    mPostcode.setText(postcodeMatcher.group().trim());
                } else {
                    mPostcode.setText("");
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    public void handleSubmitAddressButton(View v){
        if(mAddressDbHelper.readAddress(mOrderPk) == null){
            if(saveAddress()) {
                launchPayPal();
            }
        } else if(!mAddressDbHelper.readHasPaymentId(mOrderPk)){
            launchPayPal();
        } else {
            new SendOrderAsyncTask().execute(this);
        }
    }

    private boolean saveAddress(){
        boolean error = false;
        if(mStreetNumber.getText().toString().equals("")){
            mStreetNumber.setError("Pleace enter a street number");
            error = true;
        }
        if(mStreet.getText().toString().equals("")){
            mStreet.setError("Pleace enter a street name");
            error = true;
        }
        if(mSuburb.getText().toString().equals("")){
            mStreet.setError("Pleace enter a suburb");
            error = true;
        }
        if(mPostcode.getText().toString().equals("")){
            mPostcode.setError("Pleace enter a postcode");
            error = true;
        }
        if(mContactNumber.getText().toString().equals("")){
            mContactNumber.setError("Pleace enter a contact number");
            error = true;
        }
        if(!error) {
            Address address;
            long orderFk = mAddressDbHelper.readCurrentOrderPk();
            String unitNumber = mUnitNumber.getText().toString();
            String streetNumber = mStreetNumber.getText().toString();
            String street = mStreet.getText().toString();
            String suburb = mSuburb.getText().toString();
            String postcode = mPostcode.getText().toString();
            String contactNumber = mContactNumber.getText().toString();
            if (unitNumber != null && !unitNumber.equals("")) {
                address = new Address(orderFk, unitNumber, streetNumber, street, suburb, postcode, contactNumber);
            } else {
                address = new Address(orderFk, streetNumber, street, suburb, postcode, contactNumber);
            }
            mAddressDbHelper.insertNewAddress(address);
        }
        return !error;
    }

    private void launchPayPal(){
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(calculateTotal(), "AUD", "Order",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }

    private BigDecimal calculateTotal(){
        long total = 0;
        for(OrderItem orderItem:mAddressDbHelper.readCurrentOrderItems()){
            long price = mAddressDbHelper.readMenuItemByPk(orderItem.getMenuItemFk()).getPrice();
            total += orderItem.getAmount()*price;
        }
        return CurrencyUtil.longCentsToBigDecimal(total);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    Log.i(TAG, confirm.getProofOfPayment().getPaymentId());
                    mAddressDbHelper.updateWithPaymentId(confirm.getProofOfPayment().getPaymentId(), mOrderPk);
                    new SendOrderAsyncTask().execute(this);
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }


    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public class SendOrderAsyncTask extends AsyncTask<Context, Void, Boolean> {
        private static final String TAG = "Order Asnc";
        private Context mContext;
        private OrderApi mOrderApi;
        private OrderDbHelper mOrderDbHelper;

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Context... params) {

            if (mOrderApi == null) {
                OrderApi.Builder builder = new OrderApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://endpointstutorial-1119.appspot.com//_ah/api/");
                mOrderApi = builder.build();
            }

            mContext = params[0];
            mOrderDbHelper = new OrderDbHelper(mContext);
            OrderEntity orderEntity = mAddressDbHelper.readFullOrderEntity(mOrderPk);
            orderEntity.setRegistrationToken(mSharedPreferences.getString(Globals.GCM_TOKEN, "no token"));
            Log.i(TAG, "sending order");
            try {
                OrderReceiptEntity orderReceipt = mOrderApi.putOrder(orderEntity).execute();
                mOrderDbHelper.updateOrderReceived(mOrderDbHelper.readCurrentOrderPk(), orderReceipt);
                //MenuTypesEntity menuTypes = mMenuApi.getMenuTypes().execute();
                //mMenuTypesEntity = menuTypes;
                //Log.i(TAG, "Got menu types: " + menuTypes.getMenuTypes().toString());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.i(TAG, "done sending");
            mProgressDialog.cancel();
            if(success) {
                Intent intent = new Intent(mContext, MonitorOrderActivity.class);
                intent.putExtra(Globals.EXTRA_ORDER_PK, mOrderPk);
                mContext.startActivity(intent);
            } else {
                DialogFragment dialog = new OrderSubmitFailedDialog();
                dialog.show(getSupportFragmentManager(), "SubmitFailedDialog");
            }
            //mDbHelper.insertMenuTypes(mMenuTypesEntity);
            //new GetMenuItemsAsync().execute(mContext);
        }
    }
}
