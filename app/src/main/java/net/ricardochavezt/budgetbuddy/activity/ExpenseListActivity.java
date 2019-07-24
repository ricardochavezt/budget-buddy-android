package net.ricardochavezt.budgetbuddy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import net.ricardochavezt.budgetbuddy.R;
import net.ricardochavezt.budgetbuddy.adapter.ExpenseListAdapter;
import net.ricardochavezt.budgetbuddy.model.Expense;
import net.ricardochavezt.budgetbuddy.viewmodel.ExpensesViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseListActivity extends AppCompatActivity {

    @BindView(R.id.rvExpenses)
    RecyclerView rvExpenses;

    private ExpensesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        ButterKnife.bind(this);
        final ExpenseListAdapter expenseListAdapter = new ExpenseListAdapter(this);
        rvExpenses.setAdapter(expenseListAdapter);
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(ExpensesViewModel.class);
        viewModel.getExpenses().observe(this, expenses -> {
            expenseListAdapter.setExpenses(expenses);
        });
    }
}
