package net.ricardochavezt.budgetbuddy.api;

import net.ricardochavezt.budgetbuddy.model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BudgetBuddyApi {

    @GET("categories")
    Call<List<Category>> getCategories();

    @POST("expenses")
    Call<ExpenseResponse> saveExpense(@Body SaveExpenseRequest saveExpenseRequest);

    @GET("expenses")
    Call<List<ExpenseResponse>> getExpenses();
}
