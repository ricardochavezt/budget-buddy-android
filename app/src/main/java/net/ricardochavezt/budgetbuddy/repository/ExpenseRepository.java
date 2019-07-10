package net.ricardochavezt.budgetbuddy.repository;

import net.ricardochavezt.budgetbuddy.api.ApiFactory;
import net.ricardochavezt.budgetbuddy.api.BudgetBuddyApi;
import net.ricardochavezt.budgetbuddy.api.SaveExpenseRequest;
import net.ricardochavezt.budgetbuddy.api.SaveExpenseResponse;
import net.ricardochavezt.budgetbuddy.model.Expense;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseRepository {
    private BudgetBuddyApi api;

    public ExpenseRepository() {
        this.api = ApiFactory.createClient(BudgetBuddyApi.class);
    }

    public void saveExpense(String amount, int categoryId, String comment, Date madeAt, SaveExpenseCallback callback) {
        SaveExpenseRequest request = new SaveExpenseRequest(amount, categoryId, comment, madeAt);
        api.saveExpense(request).enqueue(new Callback<SaveExpenseResponse>() {
            @Override
            public void onResponse(Call<SaveExpenseResponse> call, Response<SaveExpenseResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().toExpense());
                }
                else {
                    String errorMessage;
                    try {
                        errorMessage = response.errorBody().string();
                    } catch (IOException e) {
                        errorMessage = response.message();
                    }
                    callback.onError(String.format("HTTP %d: %s", response.code(), errorMessage));
                }
            }

            @Override
            public void onFailure(Call<SaveExpenseResponse> call, Throwable t) {
                callback.onError(t.getLocalizedMessage());
            }
        });
    }

    public interface SaveExpenseCallback {
        void onSuccess(Expense savedExpense);
        void onError(String errorMessage);
    }
}
