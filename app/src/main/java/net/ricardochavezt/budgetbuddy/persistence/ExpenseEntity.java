package net.ricardochavezt.budgetbuddy.persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.model.Expense;

import java.math.BigDecimal;
import java.util.Date;

@Entity(tableName = "expense",
        foreignKeys = @ForeignKey(entity = CategoryEntity.class,
                parentColumns = "id", childColumns = "categoryId"))
public class ExpenseEntity {
    @PrimaryKey
    @NonNull
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
        expense.setId(getId());
        expense.setAmount(new BigDecimal(getAmount()));
        expense.setMadeAt(getMadeAt());
        expense.setComment(getComment());
        expense.setCategory(new Category());
        expense.getCategory().setId(getCategoryId());
        return expense;
    }
}
