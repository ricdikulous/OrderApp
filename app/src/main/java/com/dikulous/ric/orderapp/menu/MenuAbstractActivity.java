package com.dikulous.ric.orderapp.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dikulous.ric.orderapp.R;
import com.dikulous.ric.orderapp.db.OrderDbHelper;
import com.dikulous.ric.orderapp.order.OrderActivity;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

/**
 * Created by ric on 31/03/16.
 */
public abstract class MenuAbstractActivity extends AppCompatActivity {

    public MenuItem mActionCart;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ordermenu, menu);
        OrderDbHelper orderDbHelper = new OrderDbHelper(this);
        mActionCart = menu.findItem(R.id.action_cart);
        int badgeCount = orderDbHelper.readNumberOrderItems();
        if(badgeCount > 0) {
            ActionItemBadge.update(this, mActionCart, getResources().getDrawable(R.drawable.ic_shopping_cart_24dp), ActionItemBadge.BadgeStyles.RED, badgeCount);
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.action_cart));
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                // User chose the "Settings" item, show the app settings UI...
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void updateCartCount(){
        int badgeCount = new OrderDbHelper(this).readNumberOrderItems();
        if(badgeCount > 0) {
            ActionItemBadge.update(this, mActionCart, getResources().getDrawable(R.drawable.ic_shopping_cart_24dp), ActionItemBadge.BadgeStyles.RED, badgeCount);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateCartCount();
    }

}
