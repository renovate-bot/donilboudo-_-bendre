package com.admedia.bendre.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.activities.PaymentActivity;
import com.admedia.bendre.model.woocommerce.Product;
import com.admedia.bendre.util.MessageUtil;
import com.admedia.bendre.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsViewAdapter extends RecyclerView.Adapter<ProductsViewAdapter.ViewHolder> {
    private final List<Product> mValues;
    private final Activity activity;
    private final View.OnClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductsViewAdapter(Activity activity, List<Product> items, View.OnClickListener listener) {
        this.mValues = items;
        this.mListener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getName());
        String price = holder.mItem.getRegularPrice() + " " + activity.getString(R.string.fcfa);
        holder.mPrice.setText(price);

        if (mValues.get(position).getImages() != null)
        {
            Picasso.get()
                    .load(holder.mItem.getImages().get(0).getSrc())
                    .placeholder(R.drawable.bendre)
                    .into(holder.mImage);
        }

        holder.btnBuy.setOnClickListener(v -> {
            if (NetworkUtil.isOnline(activity))
            {
                Intent intent = new Intent(activity.getApplicationContext(), PaymentActivity.class);
                intent.putExtra("SELECTED_PRODUCT", holder.mItem);
                activity.startActivity(intent);
            }
            else
            {
                MessageUtil.getInstance().ToastMessage(activity.getApplicationContext(), activity.getString(R.string.no_internet_connexion));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTitle;
        final ImageView mImage;
        final TextView mPrice;
        final Button btnBuy;
        Product mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = view.findViewById(R.id.product_title);
            mImage = view.findViewById(R.id.product_image);
            mPrice = view.findViewById(R.id.product_price);
            btnBuy = view.findViewById(R.id.btn_buy);
        }
    }
}
