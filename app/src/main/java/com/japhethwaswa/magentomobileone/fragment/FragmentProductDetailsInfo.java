package com.japhethwaswa.magentomobileone.fragment;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.app.ProductDetailActivity;
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailImagesBinding;
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailInfoBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;
import com.japhethwaswa.magentomobileone.job.RetrieveProductGallery;
import com.japhethwaswa.magentomobileone.job.RetrieveProductOptionsReviews;
import com.japhethwaswa.magentomobileone.model.Product;

public class FragmentProductDetailsInfo extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private FragmentProductDetailInfoBinding fragmentProductDetailInfoBinding;
    private int entityId;
    private static final int URL_LOADER = 13;
    public ProductDetailActivity productDetailActivity;
    private Cursor cursor;
    private Product product;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //StrictMode
        StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(vmPolicy);
        /**==============**/

        fragmentProductDetailInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail_info, container, false);

        //get activity context
        productDetailActivity = (ProductDetailActivity) getActivity();

        if (savedInstanceState != null) {
            entityId = savedInstanceState.getInt("entityId");
            //get product detail from db
            getProductDetailsFromDb();
            //restart loader
            //getSupportLoaderManager().restartLoader(URL_LOADER,null,this);
        }

        product = new Product();
        //todo hide spinner that will contain product options
        //todo add spinners dynamically at runtime


        return fragmentProductDetailInfoBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("entityId", entityId);
    }

    public void receiveEntityIdentifier(int entityId) {
        this.entityId = entityId;

        //start bg job to fetch both product options,reviews,related products
        productDetailActivity.jobManager.addJobInBackground(new RetrieveProductOptionsReviews(String.valueOf(this.entityId)));


        //fetch product data in database
        getProductDetailsFromDb();

        //todo initialize loader here to get the necessary data
        //initialize loader
        //getSupportLoaderManager().initLoader(URL_LOADER, null, this);
    }

    private void getProductDetailsFromDb() {

        //double number = 22.5;
        /** double number = ((double)(regularPrice-specialPrice)*100);
         int rounded = (int)Math.round(number);
         Log.e("jeff-waswa-num",String.valueOf(number));
         Log.e("jeff-waswa-round",String.valueOf(rounded));**/

        JumboQueryHandler handler = new JumboQueryHandler(getActivity().getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                //set data to the variable in the layout view
                while (cursor.moveToNext()) {
                    String regularPrice = cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_PRICE_REGULAR));
                    String specialPrice = cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_PRICE_SPECIAL));
                    product.setName(cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_NAME)).toUpperCase());
                    product.setPrice_regular(regularPrice);
                    product.setPrice_special(specialPrice);
                    product.setPrice_special(cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_PRICE_SPECIAL)));
                    if (specialPrice != null && !specialPrice.isEmpty()) {

                        /**remove all non numeric characters**/
                        regularPrice = regularPrice + "&*%$";
                        String regularPriceFormatted = regularPrice.replaceAll("[^\\d.]", "");
                        String specialPriceFormatted = specialPrice.replaceAll("[^\\d.]", "");
                        double regularPriceFormattedNum = (Double.valueOf(regularPriceFormatted));
                        double specialPriceFormattedNum = (Double.valueOf(specialPriceFormatted));

                        double number = (((regularPriceFormattedNum - specialPriceFormattedNum) / regularPriceFormattedNum) * 100);
                        int roundedDiscount = (int) Math.round(number);
                        product.setDiscount_percentage(String.valueOf(roundedDiscount) + "% OFF");
                    }

                }
                fragmentProductDetailInfoBinding.setProduct(product);
                cursor.close();
            }
        };

        String[] projection = {
                JumboContract.ProductEntry.COLUMN_ICON,
                JumboContract.ProductEntry.COLUMN_NAME,
                JumboContract.ProductEntry.COLUMN_ENTITY_ID,
                JumboContract.ProductEntry.COLUMN_PRICE_REGULAR,
                JumboContract.ProductEntry.COLUMN_PRICE_SPECIAL,
                JumboContract.ProductEntry.COLUMN_ENTITY_TYPE
        };

        String selection = JumboContract.ProductEntry.COLUMN_ENTITY_ID + "=?";
        String[] selectionArgs = {String.valueOf(this.entityId)};

        handler.startQuery(URL_LOADER, null, JumboContract.ProductEntry.CONTENT_URI, projection, selection, selectionArgs, null);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //todo fetch product options from the database
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //todo update product options
        //todo fetch product reviews from the db
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        //todo reset loader to null
    }
}
