package com.japhethwaswa.magentomobileone.nav;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;

public class NavMenuManager extends ContextWrapper{
    private NavigationView navView;
    private Menu menu;
    private SubMenu subMenu;

    public NavMenuManager(Context base) {
        super(base);
    }

    public void updateMenu(NavigationView navView){
        //get categories cursor and do your magic here
        this.navView =  navView;
        getCategories();

    }

    private void getCategories(){

        String[] projection = {
                JumboContract.MainEntry.COLUMN_CATEGORY_ID,
                JumboContract.MainEntry.COLUMN_PRODUCT_ID,
                JumboContract.MainEntry.COLUMN_UPDATED_AT,
                JumboContract.MainEntry.COLUMN_KEY_HOME,
                JumboContract.MainEntry.COLUMN_IMAGE_URL,
                JumboContract.MainEntry.COLUMN_SECTION
        };

        String selection = JumboContract.MainEntry.COLUMN_SECTION + "=?";
        String[] selectionArgs = {"1"};

        JumboQueryHandler handler = new JumboQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                updateMenuDeal(cursor);
                cursor.close();
            }

        };

        handler.startQuery(17, null, JumboContract.MainEntry.CONTENT_URI, projection, null, null, JumboContract.MainEntry.COLUMN_KEY_HOME);

    }

    private void updateMenuDeal(Cursor cursor) {
        //TODO loop through the category cursor updating the menu
        //TODO take care of the parent who will be the main sub menu.
        //TODO initiate background job to get 5 items in each category(should be done by the home activity only the rest should fetch real time)

        //get the current menu to append data
         menu = navView.getMenu();

        //clear the menu
        menu.clear();

        //place submenu
        subMenu = menu.addSubMenu(78,Menu.NONE,Menu.NONE,"jean");

        for (int i = 1; i <= 3; i++) {
            // menu.add("runtime item "+ i);
            subMenu.add(78, i, i, "runtime item " + i).setIcon(R.drawable.ic_cart);
        }
        //make submenu items checkable
        subMenu.setGroupCheckable(78,true,true);

        //place submenu
        subMenu = menu.addSubMenu(178,Menu.NONE,Menu.NONE,"jean");
        for (int i = 11; i <= 13; i++) {
            // menu.add("runtime item "+ i);
            subMenu.add(178, i, i, "runtime item again " + i).setIcon(R.drawable.ic_search_store);
        }
        //make submenu items checkable
        subMenu.setGroupCheckable(178,true,true);


        for (int i = 0, count = navView.getChildCount(); i < count; i++) {
            final View child = navView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }


    }


}
