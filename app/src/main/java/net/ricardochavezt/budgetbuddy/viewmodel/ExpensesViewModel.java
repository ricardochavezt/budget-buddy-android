package net.ricardochavezt.budgetbuddy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import net.ricardochavezt.budgetbuddy.model.Expense;
import net.ricardochavezt.budgetbuddy.repository.ExpenseRepository;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class ExpensesViewModel extends ViewModel {

    private DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private NumberFormat formatoMonto = new DecimalFormat("'S/.' 0.00");

    public class ExpenseDisplay {
        private String amount;
        private String categoryText;
        private String madeAt;

        public ExpenseDisplay(Expense expense) {
            this.amount = formatoMonto.format(expense.getAmount());
            if (expense.getComment() == null || expense.getComment().isEmpty()) {
                this.categoryText = String.format("Categoría %d (%s)", expense.getCategory().getId(), expense.getComment());
            }
            else {
                this.categoryText = String.format("Categoría %d", expense.getCategory().getId());
            }
            this.madeAt = formatoFecha.format(expense.getMadeAt());
        }

        public String getAmount() {
            return amount;
        }

        public String getCategoryText() {
            return categoryText;
        }

        public String getMadeAt() {
            return madeAt;
        }
    }

    private ExpenseRepository expenseRepository;
    private LiveData<List<ExpenseDisplay>> expenses;

    public ExpensesViewModel() {
        this.expenseRepository = new ExpenseRepository();
    }

    public LiveData<List<ExpenseDisplay>> getExpenses() {
        if (expenses == null) {
            expenses = Transformations.map(expenseRepository.getExpenses(), expenses -> expenses.stream()
                    .map(e -> new ExpenseDisplay(e)).collect(Collectors.toList()));
        }
        return expenses;
    }
}
