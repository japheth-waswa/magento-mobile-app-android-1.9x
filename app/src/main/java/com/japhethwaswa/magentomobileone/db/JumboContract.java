package com.japhethwaswa.magentomobileone.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class JumboContract {
    public static final String CONTENT_AUTHORITY = "com.japhethwaswa.magentomobileone.db.jumboprovider";
    public static final String PATH_PAGER = "pager";
    public static final String PATH_MAIN = "main";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_PRODUCT = "product";
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
    public static final class ProductEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PRODUCT);
        //Table name
        public static final String TABLE_NAME = "product";
        //column field names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ENTITY_ID = "entity_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ENTITY_TYPE = "entity_type";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_MODIFICATION_TIME = "modification_time";
        public static final String COLUMN_IN_STOCK = "in_stock";
        public static final String COLUMN_IS_SALABLE = "is_salable";
        public static final String COLUMN_HAS_GALLERY = "has_gallery";
        public static final String COLUMN_HAS_OPTIONS = "has_options";
        public static final String COLUMN_RATING_SUMMARY = "rating_summary";
        public static final String COLUMN_REVIEWS_COUNT = "reviews_count";
        public static final String COLUMN_PRICE_REGULAR = "price_regular";
        public static final String COLUMN_PRICE_SPECIAL = "price_special";
        public static final String COLUMN_CATEGORY_IDS = "category_ids";
    }

}
