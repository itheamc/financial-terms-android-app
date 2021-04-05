package com.itheamc.financialterms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.financialterms.R;
import com.itheamc.financialterms.interfaces.TermsInterface;
import com.itheamc.financialterms.models.Terms;

import static com.itheamc.financialterms.models.Terms.termsItemCallback;

public class TermsAdapter extends ListAdapter<Terms, TermsAdapter.TermsViewHolder> {
    private TermsInterface termsInterface;

    public TermsAdapter(TermsInterface termsInterface) {
        super(termsItemCallback);
        this.termsInterface = termsInterface;
    }

    @NonNull
    @Override
    public TermsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.terms_view, parent, false);
        return new TermsViewHolder(view, termsInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TermsViewHolder holder, int position) {
        Terms financial_term = getItem(position);
        holder.postTitle.setText(financial_term.get_title());
        holder.postDesc.setText(financial_term.get_body());
    }


    public static class TermsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView postTitle;
        private final TextView postDesc;
        private final TermsInterface termsInterface;

        public TermsViewHolder(@NonNull View itemView, TermsInterface termsInterface) {
            super(itemView);
            this.postTitle = itemView.findViewById(R.id.post_title);
            this.postDesc = itemView.findViewById(R.id.post_desc);
            this.termsInterface = termsInterface;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            termsInterface.handleClick(new View[]{postTitle, postDesc}, getAdapterPosition());
        }
    }
}
