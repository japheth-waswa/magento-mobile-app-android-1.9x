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

public class NavMenuManager extends ContextWrapper {
    private Cursor cursor;
    private NavigationView navView;
    private Menu menu;
    private SubMenu subMenu;

    public NavMenuManager(Context base) {
        super(base);
    }

    public void updateMenu(NavigationView navView,int activeCategory) {
        //get categories cursor and do your magic here
        this.navView = navView;
        setMenuCategory(activeCategory);

    }

    private void setMenuCategory(final int activeCategory) {

        String[] projection = {
                JumboContract.CategoryEntry.COLUMN_LABEL,
                JumboContract.CategoryEntry.COLUMN_ENTITY_ID,
                JumboContract.CategoryEntry.COLUMN_CONTENT_TYPE,
                JumboContract.CategoryEntry.COLUMN_PARENT_ID,
                JumboContract.CategoryEntry.COLUMN_MY_PARENT_ID,
                JumboContract.CategoryEntry.COLUMN_ICON,
                JumboContract.CategoryEntry.COLUMN_MODIFICATION_TIME
        };

        String selection = JumboContract.CategoryEntry.COLUMN_MY_PARENT_ID + "=?";
        String[] selectionArgs = {"0"};

        JumboQueryHandler handler = new JumboQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                updateMenuDeal(cursor, navView,activeCategory);
                cursor.close();
            }

        };

        handler.startQuery(17, null, JumboContract.CategoryEntry.CONTENT_URI, projection, selection, selectionArgs,null);

    }

    public void updateMenuDeal(Cursor cursor, NavigationView navView,int activeCategory) {
        this.cursor = cursor;
        this.navView = navView;

        //TODO initiate background job to get 500 items in each category(should be done by the home activity only the rest should fetch real time)

        if (cursor.getCount() > 0 ) {
            //get the current menu to append data
            menu = this.navView.getMenu();

            //clear the menu
            menu.clear();

            //place submenu
            subMenu = menu.addSubMenu(78, Menu.NONE, Menu.NONE, "SHOP BY CATEGORIES");

            while (cursor.moveToNext()) {
                String label = cursor.getString(cursor.getColumnIndex(JumboContract.CategoryEntry.COLUMN_LABEL)).toUpperCase();
                int entityId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(JumboContract.CategoryEntry.COLUMN_ENTITY_ID)));
                //add submenu
                if(entityId == activeCategory){
                    subMenu.add(78, entityId, entityId, label).setChecked(true);
                }else{
                    subMenu.add(78, entityId, entityId, label);
                }

            }
            cursor.close();

            /**for (int i = 1; i <= 3; i++) {
                // menu.add("runtime item "+ i);
                subMenu.add(78, i, i, "runtime item " + i).setIcon(R.drawable.ic_cart);
            }**/
            //make submenu items checkable
            subMenu.setGroupCheckable(78, true, true);

            //place submenu
            /** subMenu = menu.addSubMenu(178,Menu.NONE,Menu.NONE,"jean");
             for (int i = 11; i <= 13; i++) {
             // menu.add("runtime item "+ i);
             subMenu.add(178, i, i, "runtime item again " + i).setIcon(R.drawable.ic_search_store);
             }
             //make submenu items checkable
             subMenu.setGroupCheckable(178,true,true);**/


            for (int i = 0, count = this.navView.getChildCount(); i < count; i++) {
                final View child = this.navView.getChildAt(i);
                if (child != null && child instanceof ListView) {
                    final ListView menuView = (ListView) child;
                    final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                    final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                    wrapped.notifyDataSetChanged();
                }
            }

        }


    }


}
