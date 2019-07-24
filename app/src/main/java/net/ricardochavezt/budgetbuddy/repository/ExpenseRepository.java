package net.ricardochavezt.budgetbuddy.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.ricardochavezt.budgetbuddy.api.ApiFactory;
import net.ricardochavezt.budgetbuddy.api.BudgetBuddyApi;
import net.ricardochavezt.budgetbuddy.api.SaveExpenseRequest;
import net.ricardochavezt.budgetbuddy.api.ExpenseResponse;
import net.ricardochavezt.budgetbuddy.model.Expense;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        api.saveExpense(request).enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
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
            public void onFailure(Call<ExpenseResponse> call, Throwable t) {
                callback.onError(t.getLocalizedMessage());
            }
        });
    }

    public interface SaveExpenseCallback {
        void onSuccess(Expense savedExpense);
        void onError(String errorMessage);
    }

    public LiveData<List<Expense>> getExpenses() {
        MutableLiveData<List<Expense>> expenses = new MutableLiveData<>();
        api.getExpenses().enqueue(new Callback<List<ExpenseResponse>>() {
            @Override
            public void onResponse(Call<List<ExpenseResponse>> call, Response<List<ExpenseResponse>> response) {
                if (response.isSuccessful()) {
                    expenses.setValue(response.body().stream().map(er -> er.toExpense()).collect(Collectors.toList()));
                }
                else {
                    // TODO manejar error del servidor
                    Log.e("Gastos", "error HTTP " + response.code() + ": " + response.message());
                    expenses.setValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<List<ExpenseResponse>> call, Throwable t) {
                // TODO manejar el error
                Log.e("Gastos", "error: " + t.getLocalizedMessage(), t);
                expenses.setValue(Collections.emptyList());
            }
        });
        return expenses;
    }
}
