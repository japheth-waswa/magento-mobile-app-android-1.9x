package com.japhethwaswa.magentomobileone.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class JumboContract {
    public static final String CONTENT_AUTHORITY = "com.japhethwaswa.magentomobileone.db.jumboprovider";
    public static final String PATH_PAGER = "pager";
    public static final String PATH_MAIN = "main";
    public static final String PATH_CATEGORY = "category";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class PagerEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PAGER);

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
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_MAIN);
        //Table name
        public static final String TABLE_NAME = "main";
        //column field names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_KEY_HOME= "key_home";
        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_SECTION = "section";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }

    public static final class CategoryEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_CATEGORY);
        //Table name
        public static final String TABLE_NAME = "category";
        //column field names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ENTITY_ID = "entity_id";
        public static final String COLUMN_CONTENT_TYPE = "content_type";
        public static final String COLUMN_PARENT_ID = "parent_id";
        public static final String COLUMN_LABEL = "label";
        public static final String COLUMN_MY_PARENT_ID = "my_parent_id";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_MODIFICATION_TIME = "modification_time";
    }

}
