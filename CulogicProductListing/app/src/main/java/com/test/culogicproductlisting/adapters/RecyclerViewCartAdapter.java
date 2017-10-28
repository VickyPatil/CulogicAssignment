package com.test.culogicproductlisting.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidapp.culogicassignment.R;
import com.androidapp.culogicassignment.fragments.CartFragment;
import com.androidapp.culogicassignment.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by vikas on 28-10-2017.
 */

public class RecyclerViewCartAdapter extends RecyclerView.Adapter<RecyclerViewCartAdapter.ViewHolder> {
    private static final String TAG = RecyclerViewProductListAdapter.class.getSimpleName();
    private CartFragment context;
    private ArrayList<Product> productArrayList;

    public void setProductArrayList(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }

    public RecyclerViewCartAdapter(CartFragment context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @Override
    public RecyclerViewCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_rv_cart_products, parent, false);
        return new RecyclerViewCartAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewCartAdapter.ViewHolder holder, final int position) {
        final Product product = productArrayList.get(position);
        holder.tvProductName.setText(product.getProductName());
        holder.tvProductPrice.setText("Price :\n"+String.valueOf(product.getPrice()));
        holder.tvVendorName.setText(product.getVendorName());
        holder.tvVendorAddress.setText(product.getVendorAddress());
        showImageFromUrl(product.getProductImg(),holder.ivProduct);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProductFromCart(position);
                context.updateTotalPrice(productArrayList);
            }
        });

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placePhoneCall(product.getPhoneNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlImageContainer;
        TextView tvProductName,tvProductPrice,tvVendorName,tvVendorAddress;
        ImageView ivProduct;
        Button btnCall,btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
            tvVendorAddress = (TextView) itemView.findViewById(R.id.tvVendorAddress);
            tvVendorName = (TextView) itemView.findViewById(R.id.tvVendorName);
            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
            rlImageContainer = (RelativeLayout) itemView.findViewById(R.id.rlImageContainer);
            btnCall = (Button) itemView.findViewById(R.id.btnCall);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }

    private void showImageFromUrl(String url, ImageView imageView) {
        if (!url.isEmpty())
            Picasso.with(context.getActivity()).load(url).noFade().placeholder(R.drawable.ic_placholder_logo).into(imageView);
    }

    private void placePhoneCall(String phoneNumber){
        if(phoneNumber!=null && !phoneNumber.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.getActivity().startActivity(intent);
        }
    }

    private void removeProductFromCart(int position){
        productArrayList.remove(position);
        this.notifyItemRemoved(position);
    }
}

