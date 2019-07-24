package net.ricardochavezt.budgetbuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ricardochavezt.budgetbuddy.R;
import net.ricardochavezt.budgetbuddy.model.Expense;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseListAdapter extends RecyclerView.Adapter {
    // TODO refactor this
    private DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private NumberFormat formatoMonto = NumberFormat.getCurrencyInstance();

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
    private List<Expense> expenses;

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
        Expense expense = expenses.get(position);
        expenseViewHolder.tvCategoria.setText(String.format("Categoria %d - %s",
                expense.getCategory().getId(), expense.getComment()));
        expenseViewHolder.tvMonto.setText(formatoMonto.format(expense.getAmount()));
        expenseViewHolder.tvFecha.setText(formatoFecha.format(expense.getMadeAt()));
    }

    @Override
    public int getItemCount() {
        if (expenses == null) return 0;
        return expenses.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }
}
