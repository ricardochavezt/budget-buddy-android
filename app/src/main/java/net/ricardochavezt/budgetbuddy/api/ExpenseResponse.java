package net.ricardochavezt.budgetbuddy.api;

import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.model.Expense;

import java.math.BigDecimal;
import java.util.Date;

public class ExpenseResponse {
    private int id;
    private String amount;
    private int categoryId;
    private Date madeAt;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Date getMadeAt() {
        return madeAt;
    }

    public void setMadeAt(Date madeAt) {
        this.madeAt = madeAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Expense toExpense() {
        Expense expense = new Expense();
        expense.setId(id);
        expense.setAmount(new BigDecimal(amount));
        expense.setCategory(new Category());
        expense.getCategory().setId(categoryId);
        expense.setComment(comment);
        expense.setMadeAt(madeAt);
        return expense;
    }
}
