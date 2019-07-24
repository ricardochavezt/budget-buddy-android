package net.ricardochavezt.budgetbuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ricardochavezt.budgetbuddy.R;
import net.ricardochavezt.budgetbuddy.viewmodel.ExpensesViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseListAdapter extends RecyclerView.Adapter {

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFecha)
        TextView tvFecha;
        @BindView(R.id.tvMonto)
        TextView tvMonto;
        @BindView(R.id.tvCategoria)
        TextView tvCategoria;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private final LayoutInflater inflater;
    private List<ExpensesViewModel.ExpenseDisplay> expenses;

    public ExpenseListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.expense_list_item, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExpenseViewHolder expenseViewHolder = (ExpenseViewHolder) holder;
        ExpensesViewModel.ExpenseDisplay expense = expenses.get(position);
        expenseViewHolder.tvCategoria.setText(expense.getCategoryText());
        expenseViewHolder.tvMonto.setText(expense.getAmount());
        expenseViewHolder.tvFecha.setText(expense.getMadeAt());
    }

    @Override
    public int getItemCount() {
        if (expenses == null) return 0;
        return expenses.size();
    }

    public void setExpenses(List<ExpensesViewModel.ExpenseDisplay> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }
}
