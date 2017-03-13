package com.japhethwaswa.magentomobileone.nav;

import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.japhethwaswa.magentomobileone.R;

public class NavMenuManager {

    public static void updateMenu(NavigationView navView){
        //get categories cursor and do your magic here

        //get the current menu to append data
        Menu menu = navView.getMenu();
        menu.clear();
        SubMenu subMenu;
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
