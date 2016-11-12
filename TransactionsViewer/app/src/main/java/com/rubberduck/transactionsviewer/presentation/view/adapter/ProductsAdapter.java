package com.rubberduck.transactionsviewer.presentation.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubberduck.transactionsviewer.R;
import com.rubberduck.transactionsviewer.presentation.model.ProductViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private List<ProductViewModel> products;
    private final OnProductSelectedListener onProductSelectedListener;

    public ProductsAdapter(List<ProductViewModel> products,
                           OnProductSelectedListener onProductSelectedListener) {
        this.products = products;
        this.onProductSelectedListener = onProductSelectedListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bindView(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void refresh(List<ProductViewModel> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_card)
        CardView productCard;

        @BindView(R.id.product_name_text_view)
        TextView productNameTextView;

        @BindView(R.id.transactions_count_text_view)
        TextView transactionsCountTextView;

        ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(ProductViewModel product) {
            productNameTextView.setText(product.getSku());
            transactionsCountTextView.setText(product.getTransactionsCount());
        }

        @OnClick(R.id.product_card)
        void onProductClicked(View view) {
            int itemClickedPosition = getAdapterPosition();

            if (itemClickedPosition != RecyclerView.NO_POSITION) {
                onProductSelectedListener.onProductSelected(products.get(itemClickedPosition));
            }
        }
    }

    public interface OnProductSelectedListener {

        void onProductSelected(ProductViewModel product);
    }
}
