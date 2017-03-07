package com.japhethwaswa.magentomobileone.db;

import android.provider.BaseColumns;

public final class JumboContract {
    public static final class PagerEntry implements BaseColumns{
        //Table name
        public static final String TABLE_NAME = "pager";
        //column field names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BRIEF_DESCRIPTION = "brief_description";
        public static final String COLUMN_IMAGE_URL_LOCAL = "image_url_local";
        public static final String COLUMN_IMAGE_URL_REMOTE = "image_url_remote";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static final class MainEntry implements BaseColumns{
        //Table name
        public static final String TABLE_NAME = "main";
        //column field names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_KEY_HOME= "key_home";
        public static final String COLUMN_SECTION = "section";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }
}
