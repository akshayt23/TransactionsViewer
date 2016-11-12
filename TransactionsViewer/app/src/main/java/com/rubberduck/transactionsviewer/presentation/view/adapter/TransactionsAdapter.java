package com.rubberduck.transactionsviewer.presentation.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubberduck.transactionsviewer.R;
import com.rubberduck.transactionsviewer.presentation.model.TransactionViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private List<TransactionViewModel> transactions;

    public TransactionsAdapter(List<TransactionViewModel> transactions) {
        this.transactions = transactions;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        holder.bindView(transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void refreshTransactions(List<TransactionViewModel> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.transaction_card)
        CardView transactionCard;

        @BindView(R.id.original_amount_text_view)
        TextView originalAmountTextView;

        @BindView(R.id.amount_in_gbp_text_view)
        TextView amountInGbpTextView;

        TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(TransactionViewModel transaction) {
            originalAmountTextView.setText(transaction.getOriginalAmount());
            amountInGbpTextView.setText(transaction.getConvertedAmount());
        }
    }
}
