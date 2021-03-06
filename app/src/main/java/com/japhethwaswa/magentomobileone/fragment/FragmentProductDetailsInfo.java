package com.japhethwaswa.magentomobileone.fragment;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.app.ProductDetailActivity;
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailImagesBinding;
import com.japhethwaswa.magentomobileone.databinding.FragmentProductDetailInfoBinding;
import com.japhethwaswa.magentomobileone.db.DatabaseHelper;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.db.JumboQueryHandler;
import com.japhethwaswa.magentomobileone.job.RetrieveProductGallery;
import com.japhethwaswa.magentomobileone.job.RetrieveProductOptionsReviews;
import com.japhethwaswa.magentomobileone.job.SubmitCartItem;
import com.japhethwaswa.magentomobileone.model.Product;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
//todo check network and internet connectivity in each activity
public class FragmentProductDetailsInfo extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private FragmentProductDetailInfoBinding fragmentProductDetailInfoBinding;
    private int entityId;
    private static final int URL_LOADER = 13;
    public ProductDetailActivity productDetailActivity;
    private Cursor cursor;
    private Product product;
    private ArrayList<String> prodOpsHm;
    private HashMap<Integer,String> prodOpsHashMap;
    private ArrayList<String> prodOpsHmLittle;
    private HashMap<Integer,String> prodOpsHashMapLittle;
    private int prodOptionsMainPosition = -1;
    private int prodOptionsMainPositionLittle = -1;
    String productHasOptions = "-1";

    private String prodOptionsMainParentCode =  "-1";
    String prodOptionsMainChildCode =  "-1";
    private String prodOptionsMainParentCodeLittle =  "-1";
    String prodOptionsLittleChildCode =  "-1";
    SQLiteDatabase db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //StrictMode
         StrictMode.VmPolicy vmPolicy = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(vmPolicy);
        /** ==============**/

        fragmentProductDetailInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail_info, container, false);

        //get activity context
        productDetailActivity = (ProductDetailActivity) getActivity();

        product = new Product();

        //hide top values spinner
        fragmentProductDetailInfoBinding.productOptions.setVisibility(View.INVISIBLE);
        //hide little values spinner
        fragmentProductDetailInfoBinding.productOptionsLittle.setVisibility(View.INVISIBLE);

        if (savedInstanceState != null) {
            entityId = savedInstanceState.getInt("entityId");
            prodOptionsMainPosition = savedInstanceState.getInt("prodOptionsMainPosition");
            prodOptionsMainPositionLittle = savedInstanceState.getInt("prodOptionsMainPositionLittle");
            //get product detail from db
            getProductDetailsFromDb();
            //restart loader
            getActivity().getSupportLoaderManager().restartLoader(URL_LOADER,null,this);
        }

        //set on click listener
        fragmentProductDetailInfoBinding.productOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //store the position
                prodOptionsMainPosition = position;

                String prodMainOpsCode = prodOpsHashMap.get(position);
                prodOptionsMainChildCode = prodMainOpsCode;


                //fetch sub-children data
                setSubChildrenWithSpinner(prodMainOpsCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //set on click listener
        fragmentProductDetailInfoBinding.productOptionsLittle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //store the position
                prodOptionsMainPositionLittle = position;

                String prodMainOpsLittleCode = prodOpsHashMapLittle.get(position);
                prodOptionsLittleChildCode = prodMainOpsLittleCode;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return fragmentProductDetailInfoBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("entityId", entityId);
        outState.putInt("prodOptionsMainPosition", prodOptionsMainPosition);
        outState.putInt("prodOptionsMainPositionLittle", prodOptionsMainPositionLittle);
    }

    public void receiveEntityIdentifier(int entityId) {
        this.entityId = entityId;

        //start bg job to fetch both product options,reviews,related products
        productDetailActivity.jobManager.addJobInBackground(new RetrieveProductOptionsReviews(String.valueOf(this.entityId)));


        //fetch product data in database
        getProductDetailsFromDb();

        //initialize loader
        getActivity().getSupportLoaderManager().initLoader(URL_LOADER, null, this);
    }

    private void getProductDetailsFromDb() {

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
                    product.setShort_description(cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_SHORT_DESCRIPTION)));
                    productHasOptions = cursor.getString(cursor.getColumnIndex(JumboContract.ProductEntry.COLUMN_HAS_OPTIONS));
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
                JumboContract.ProductEntry.COLUMN_SHORT_DESCRIPTION,
                JumboContract.ProductEntry.COLUMN_ENTITY_ID,
                JumboContract.ProductEntry.COLUMN_PRICE_REGULAR,
                JumboContract.ProductEntry.COLUMN_PRICE_SPECIAL,
                JumboContract.ProductEntry.COLUMN_ENTITY_TYPE,
                JumboContract.ProductEntry.COLUMN_HAS_OPTIONS
        };

        String selection = JumboContract.ProductEntry.COLUMN_ENTITY_ID + "=?";
        String[] selectionArgs = {String.valueOf(this.entityId)};

        handler.startQuery(URL_LOADER, null, JumboContract.ProductEntry.CONTENT_URI, projection, selection, selectionArgs, null);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //fetch product options from the database
        String[] projection = {
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_CODE,
                JumboContract.ProductOptionsEntry.COLUMN_IS_PARENT,
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_LABEL,
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_REQUIRED,
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_TYPE
        };

        String selection = JumboContract.ProductOptionsEntry.COLUMN_ENTITY_ID + "=? and " + JumboContract.ProductOptionsEntry.COLUMN_IS_PARENT + "=? ";
        String[] selectionArgs = {String.valueOf(entityId), "1"};

        return new CursorLoader(getContext(), JumboContract.ProductOptionsEntry.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //get parent children
        getParentTopChildren(data);
        //todo fetch product reviews from the db
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        //reset loader to null
    }

    //GET TOP CHILDREN
    private void getParentTopChildren(Cursor cursor) {
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {


                JumboQueryHandler handler = new JumboQueryHandler(getActivity().getContentResolver()){
                    @Override
                    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {



                        if(cursor != null && cursor.getCount() >0){

                            //initialize variables
                            prodOpsHm=null;
                            prodOpsHm = new ArrayList<>();
                            prodOpsHashMap=null;
                            prodOpsHashMap= new HashMap<>();

                            //set spinner visible
                            fragmentProductDetailInfoBinding.productOptions.setVisibility(View.VISIBLE);

                            int i=0;
                            while(cursor.moveToNext()){
                                prodOpsHm.add(i,cursor.getString(cursor.getColumnIndex(JumboContract.ProductOptionsEntry.COLUMN_CHILD_LABEL)));
                                prodOpsHashMap.put(i,cursor.getString(cursor.getColumnIndex(JumboContract.ProductOptionsEntry.COLUMN_CHILD_CODE)));
                               prodOptionsMainParentCode = cursor.getString(cursor.getColumnIndex(JumboContract.ProductOptionsEntry.COLUMN_PARENT_CODE));
                                i++;
                            }

                            //set adapter to the spinner
                            ArrayAdapter<String> prodOpsHmAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,prodOpsHm);
                            prodOpsHmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            fragmentProductDetailInfoBinding.productOptions.setAdapter(prodOpsHmAdapter);

                            //set current item if screen rotated
                            if(prodOptionsMainPosition != -1){
                                fragmentProductDetailInfoBinding.productOptions.setSelection(prodOptionsMainPosition);
                            }

                            //set label for this parent in UI
                            Cursor parentCursor = getSpecificParentCursor(prodOptionsMainParentCode);
                            if(parentCursor != null && parentCursor.getCount() >0){
                                parentCursor.moveToFirst();
                                String parentMainLabel = parentCursor.getString(cursor.getColumnIndex(JumboContract.ProductOptionsEntry.COLUMN_PARENT_LABEL));
                                fragmentProductDetailInfoBinding.productOptionsLabel.setText(parentMainLabel.toUpperCase());

                            }

                            parentCursor.close();
                            db.close();

                        }
                        cursor.close();
                    }
                };

                String[] projection = {
                        JumboContract.ProductOptionsEntry.COLUMN_PARENT_CODE,
                        JumboContract.ProductOptionsEntry.COLUMN_IS_PARENT,
                        JumboContract.ProductOptionsEntry.COLUMN_PARENT_LABEL,
                        JumboContract.ProductOptionsEntry.COLUMN_PARENT_REQUIRED,
                        JumboContract.ProductOptionsEntry.COLUMN_PARENT_TYPE,
                        JumboContract.ProductOptionsEntry.COLUMN_CHILD_CODE,
                        JumboContract.ProductOptionsEntry.COLUMN_CHILD_LABEL,
                        JumboContract.ProductOptionsEntry.COLUMN_CHILD_TO_CODE
                };

                String localParentCode = cursor.getString(cursor.getColumnIndex(JumboContract.ProductOptionsEntry.COLUMN_PARENT_CODE));
                String selection = JumboContract.ProductOptionsEntry.COLUMN_PARENT_CODE + "=? AND "
                        + JumboContract.ProductOptionsEntry.COLUMN_IS_PARENT + "=? AND " + JumboContract.ProductOptionsEntry.COLUMN_CHILD_TO_CODE + " IS NULL";
                String[] selectionArgs = {localParentCode, "0"};

                handler.startQuery(23,null,JumboContract.ProductOptionsEntry.CONTENT_URI,projection,selection,selectionArgs,null);

            }

        }

    }



    //GET LITTLE CHILDREN
    private void setSubChildrenWithSpinner(String prodMainOpsCode) {


        JumboQueryHandler handler = new JumboQueryHandler(getActivity().getContentResolver()){
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {



                if(cursor != null && cursor.getCount() >0){

                    //initialize variables
                   prodOpsHmLittle=null;
                    prodOpsHmLittle = new ArrayList<>();
                    prodOpsHashMapLittle=null;
                    prodOpsHashMapLittle= new HashMap<>();

                    //set spinner visible
                    fragmentProductDetailInfoBinding.productOptionsLittle.setVisibility(View.VISIBLE);

                    int i=0;
                    while(cursor.moveToNext()){
                        prodOpsHmLittle.add(i,cursor.getString(cursor.getColumnIndex(JumboContract.ProductOptionsEntry.COLUMN_CHILD_LABEL)));
                        prodOpsHashMapLittle.put(i,cursor.getString(cursor.getColumnIndex(JumboContract.ProductOptionsEntry.COLUMN_CHILD_CODE)));
                        prodOptionsMainParentCodeLittle = cursor.getString(cursor.getColumnIndex(JumboContract.ProductOptionsEntry.COLUMN_PARENT_CODE));
                        i++;
                    }

                    //set adapter to the spinner
                    ArrayAdapter<String> prodOpsHmAdapterLittle = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,prodOpsHmLittle);
                    prodOpsHmAdapterLittle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fragmentProductDetailInfoBinding.productOptionsLittle.setAdapter(prodOpsHmAdapterLittle);

                    //set current item if screen rotated
                    if(prodOptionsMainPositionLittle != -1){
                        fragmentProductDetailInfoBinding.productOptionsLittle.setSelection(prodOptionsMainPositionLittle);
                    }

                    //set label for this parent in UI
                    Cursor parentCursor = getSpecificParentCursor(prodOptionsMainParentCodeLittle);
                    if(parentCursor != null && parentCursor.getCount() >0){
                        parentCursor.moveToFirst();
                        String parentMainLabel = parentCursor.getString(cursor.getColumnIndex(JumboContract.ProductOptionsEntry.COLUMN_PARENT_LABEL));
                        fragmentProductDetailInfoBinding.productOptionsLabelLittle.setText(parentMainLabel.toUpperCase());

                    }

                    parentCursor.close();
                    db.close();

                }
                cursor.close();
            }
        };

        String[] projection = {
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_CODE,
                JumboContract.ProductOptionsEntry.COLUMN_IS_PARENT,
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_LABEL,
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_REQUIRED,
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_TYPE,
                JumboContract.ProductOptionsEntry.COLUMN_CHILD_CODE,
                JumboContract.ProductOptionsEntry.COLUMN_CHILD_LABEL,
                JumboContract.ProductOptionsEntry.COLUMN_CHILD_TO_CODE
        };

        String selection = JumboContract.ProductOptionsEntry.COLUMN_ENTITY_ID + "=? AND "
                + JumboContract.ProductOptionsEntry.COLUMN_IS_PARENT + "=? AND " + JumboContract.ProductOptionsEntry.COLUMN_CHILD_TO_CODE + "=?";
        String[] selectionArgs = {String.valueOf(entityId), "0",prodMainOpsCode};

        handler.startQuery(23,null,JumboContract.ProductOptionsEntry.CONTENT_URI,projection,selection,selectionArgs,null);
    }


    //GET SPECIFIC PARENT CURSOR
    private Cursor getSpecificParentCursor(String prodOptionsMainParentCode) {

        DatabaseHelper helper = new DatabaseHelper(getContext());
        db = helper.getReadableDatabase();
        Cursor cursor;
        String[] projection = {
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_CODE,
                JumboContract.ProductOptionsEntry.COLUMN_IS_PARENT,
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_LABEL,
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_REQUIRED,
                JumboContract.ProductOptionsEntry.COLUMN_PARENT_TYPE
        };

        String selection = JumboContract.ProductOptionsEntry.COLUMN_PARENT_CODE + "=? AND "
                + JumboContract.ProductOptionsEntry.COLUMN_IS_PARENT + "=?";
        String[] selectionArgs = {prodOptionsMainParentCode, "1"};

        cursor = db.query(JumboContract.ProductOptionsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        return cursor;
    }

    public void addItemToCart() {
        //get the options that the client chose and show them ie adding item to cart or error

        if(productHasOptions.equalsIgnoreCase("1") && prodOptionsMainParentCode.equalsIgnoreCase("-1")){
            Toasty.error(getActivity(),"Please wait options loading or select options",Toast.LENGTH_SHORT,true).show();
        }

        if(productHasOptions.equalsIgnoreCase("1") && !prodOptionsMainParentCode.equalsIgnoreCase("-1") || productHasOptions.equalsIgnoreCase("0")){
            Toasty.info(getActivity(),"Adding to Bag Please Continue Shopping", Toast.LENGTH_SHORT,true).show();
            nowAddToCart();
        }

    }

    private void nowAddToCart() {
        //start bg job to insert items in online cart and update local repository
        //build the hashMap
        HashMap<String,String> optionsKeyValue = new HashMap<>();
        String itemQuantity = "1";
        String currentEntityId = String.valueOf(entityId);
        if(!prodOptionsMainParentCode.equalsIgnoreCase("-1")){
            optionsKeyValue.put(prodOptionsMainParentCode,prodOptionsMainChildCode);
        }
        if(!prodOptionsMainParentCodeLittle.equalsIgnoreCase("-1")){
            optionsKeyValue.put(prodOptionsMainParentCodeLittle,prodOptionsLittleChildCode);
        }

        productDetailActivity.jobManager.addJobInBackground(new SubmitCartItem(currentEntityId,itemQuantity,optionsKeyValue));
    }
}
