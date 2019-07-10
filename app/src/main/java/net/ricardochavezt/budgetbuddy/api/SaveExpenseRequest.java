package net.ricardochavezt.budgetbuddy.api;

import java.util.Date;

public class SaveExpenseRequest {
    private String amount;
    private int categoryId;
    private String comment;
    private Date madeAt;

    public SaveExpenseRequest() { }

    public SaveExpenseRequest(String amount, int categoryId, String comment, Date madeAt) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.comment = comment;
        this.madeAt = madeAt;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getMadeAt() {
        return madeAt;
    }

    public void setMadeAt(Date madeAt) {
        this.madeAt = madeAt;
    }
}
