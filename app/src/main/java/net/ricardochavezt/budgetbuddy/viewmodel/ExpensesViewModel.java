package net.ricardochavezt.budgetbuddy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import net.ricardochavezt.budgetbuddy.model.Expense;
import net.ricardochavezt.budgetbuddy.repository.ExpenseRepository;

import java.util.List;

public class ExpensesViewModel extends ViewModel {
    private ExpenseRepository expenseRepository;
    private LiveData<List<Expense>> expenses;

    public ExpensesViewModel() {
        this.expenseRepository = new ExpenseRepository();
    }

    public LiveData<List<Expense>> getExpenses() {
        if (expenses == null) {
            expenses = expenseRepository.getExpenses();
        }
        return expenses;
    }
}
