package net.ricardochavezt.budgetbuddy.persistence;

import androidx.room.DatabaseView;

import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.model.Expense;

import java.math.BigDecimal;
import java.util.Date;

@DatabaseView(value = "SELECT e.id, e.amount, c.id as categoryId, c.name as categoryName, e.madeAt, e.comment" +
        " FROM expense e JOIN category c ON e.categoryId = c.id",
        viewName = "expense_category")
public class ExpenseWithCategoryEntity {
    private int id;
    private String amount;
    private int categoryId;
    private String categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
        expense.setId(getId());
        expense.setAmount(new BigDecimal(getAmount()));
        expense.setCategory(new Category());
        expense.getCategory().setId(getCategoryId());
        expense.getCategory().setName(getCategoryName());
        expense.setComment(getComment());
        expense.setMadeAt(getMadeAt());
        return expense;
    }
}
