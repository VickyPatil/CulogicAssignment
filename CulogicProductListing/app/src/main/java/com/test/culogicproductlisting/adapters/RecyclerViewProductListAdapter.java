package com.test.culogicproductlisting.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.culogicproductlisting.R;
import com.test.culogicproductlisting.activities.HomeActivity;
import com.test.culogicproductlisting.customviews.ProductGalleryDialog;
import com.test.culogicproductlisting.models.Product;

import java.util.ArrayList;

/**
 * Created by vikas on 28-10-2017.
 */

public class RecyclerViewProductListAdapter extends RecyclerView.Adapter<RecyclerViewProductListAdapter.ViewHolder> {
    private static final String TAG = RecyclerViewProductListAdapter.class.getSimpleName();
    private HomeActivity context;
    private ArrayList<Product> productArrayList;

    public void setProductArrayList(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }

    public RecyclerViewProductListAdapter(HomeActivity context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_rv_product_list, parent, false);
        return new RecyclerViewProductListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Product product = productArrayList.get(position);
        holder.tvProductName.setText(product.getProductName());
        holder.tvProductPrice.setText(context.getString(R.string.label_price)+" "+String.valueOf(product.getPrice()));
        holder.tvVendorName.setText(product.getVendorName());
        holder.tvVendorAddress.setText(product.getVendorAddress());
        showImageFromUrl(product.getProductImg(),holder.ivProduct);

        holder.tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.addProductToCart(product);
            }
        });

        holder.rlImageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductGalleryDialog productGalleryDialog = new ProductGalleryDialog(context,"Product gallery",product.getProductGallery());
                productGalleryDialog.setCancelable(true);
                productGalleryDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlImageContainer;
        TextView tvProductName,tvProductPrice,tvVendorName,tvVendorAddress,tvAddToCart;
        ImageView ivProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
            tvVendorAddress = (TextView) itemView.findViewById(R.id.tvVendorAddress);
            tvVendorName = (TextView) itemView.findViewById(R.id.tvVendorName);
            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
            rlImageContainer = (RelativeLayout) itemView.findViewById(R.id.rlImageContainer);
            tvAddToCart = (TextView) itemView.findViewById(R.id.tvAddToCart);
        }
    }


    private void showImageFromUrl(String url, ImageView imageView) {
        Log.d(TAG, "showImageFromUrl: " + url);
        if (!url.isEmpty())
            Picasso.with(context).load(url).noFade().placeholder(R.drawable.ic_placholder_logo).into(imageView);
    }
}
