package net.ricardochavezt.budgetbuddy.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.ricardochavezt.budgetbuddy.api.ApiFactory;
import net.ricardochavezt.budgetbuddy.api.BudgetBuddyApi;
import net.ricardochavezt.budgetbuddy.model.Category;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private BudgetBuddyApi api;

    public CategoryRepository() {
        this.api = ApiFactory.createClient(BudgetBuddyApi.class);
    }

    public LiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> categories = new MutableLiveData<>();
        api.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categories.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // TODO manejar el error
                Log.e("getCategorias", "error: " + t.getLocalizedMessage(), t);
                categories.setValue(Collections.emptyList());
            }
        });
        return categories;
    }
}
