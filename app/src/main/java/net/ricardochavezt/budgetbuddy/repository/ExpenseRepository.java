package net.ricardochavezt.budgetbuddy.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import net.ricardochavezt.budgetbuddy.api.ApiFactory;
import net.ricardochavezt.budgetbuddy.api.BudgetBuddyApi;
import net.ricardochavezt.budgetbuddy.api.SaveExpenseRequest;
import net.ricardochavezt.budgetbuddy.api.ExpenseResponse;
import net.ricardochavezt.budgetbuddy.model.Expense;
import net.ricardochavezt.budgetbuddy.persistence.BudgetBuddyDatabase;
import net.ricardochavezt.budgetbuddy.persistence.ExpenseDao;
import net.ricardochavezt.budgetbuddy.persistence.ExpenseEntity;
import net.ricardochavezt.budgetbuddy.persistence.ExpenseWithCategoryEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseRepository {
    private BudgetBuddyApi api;
    private ExpenseDao expenseDao;

    public ExpenseRepository(Context context) {
        this.api = ApiFactory.createClient(BudgetBuddyApi.class);
        this.expenseDao = BudgetBuddyDatabase.getInstance(context).expenseDao();
    }

    public void saveExpense(String amount, int categoryId, String comment, Date madeAt, SaveExpenseCallback callback) {
        SaveExpenseRequest request = new SaveExpenseRequest(amount, categoryId, comment, madeAt);
        api.saveExpense(request).enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                if (response.isSuccessful()) {
                    ExpenseResponse savedExpense = response.body();
                    InsertExpenseTask insertExpenseTask = new InsertExpenseTask(expenseDao);
                    insertExpenseTask.execute(mapResponseToEntity(savedExpense));
                    callback.onSuccess(savedExpense.toExpense());
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
        return Transformations.map(expenseDao.getAllExpensesWithCategories(), entities -> {
            if (entities.isEmpty()) {
                fetchExpensesOnline();
                return Collections.emptyList();
            }
            return entities.stream()
                    .map(ExpenseWithCategoryEntity::toExpense)
                    .collect(Collectors.toList());
        });
    }

    public void fetchExpensesOnline() {
        api.getExpenses().enqueue(new Callback<List<ExpenseResponse>>() {
            @Override
            public void onResponse(Call<List<ExpenseResponse>> call, Response<List<ExpenseResponse>> response) {
                if (response.isSuccessful()) {
                    InsertExpenseTask insertExpenseTask = new InsertExpenseTask(expenseDao);
                    List<ExpenseEntity> expenseEntityList = response.body().stream()
                            .map(ExpenseRepository.this::mapResponseToEntity)
                            .collect(Collectors.toList());
                    insertExpenseTask.execute(expenseEntityList.toArray(new ExpenseEntity[]{}));
                }
                else {
                    // TODO manejar error del servidor
                    Log.e("Gastos", "error HTTP " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ExpenseResponse>> call, Throwable t) {
                // TODO manejar el error
                Log.e("Gastos", "error: " + t.getLocalizedMessage(), t);
            }
        });
    }

    private ExpenseEntity mapResponseToEntity(ExpenseResponse expenseResponse) {
        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setId(expenseResponse.getId());
        expenseEntity.setAmount(expenseResponse.getAmount());
        expenseEntity.setCategoryId(expenseResponse.getCategoryId());
        expenseEntity.setMadeAt(expenseResponse.getMadeAt());
        expenseEntity.setComment(expenseResponse.getComment());
        return expenseEntity;
    }

    private static class InsertExpenseTask extends AsyncTask<ExpenseEntity, Void, Void> {
        private ExpenseDao expenseDao;

        InsertExpenseTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(ExpenseEntity... expenseEntities) {
            Arrays.stream(expenseEntities)
                    .forEach(expenseEntity -> expenseDao.insert(expenseEntity));
            return null;
        }
    }
}
