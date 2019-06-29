package net.ricardochavezt.budgetbuddy.api;

import net.ricardochavezt.budgetbuddy.model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BudgetBuddyApi {

    @GET("categories")
    Call<List<Category>> getCategories();
}
