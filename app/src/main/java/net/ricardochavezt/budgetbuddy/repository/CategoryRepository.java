package net.ricardochavezt.budgetbuddy.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import net.ricardochavezt.budgetbuddy.api.ApiFactory;
import net.ricardochavezt.budgetbuddy.api.BudgetBuddyApi;
import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.persistence.BudgetBuddyDatabase;
import net.ricardochavezt.budgetbuddy.persistence.CategoryDao;
import net.ricardochavezt.budgetbuddy.persistence.CategoryEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private BudgetBuddyApi api;
    private CategoryDao categoryDao;

    public CategoryRepository(Context context) {
        this.api = ApiFactory.createClient(BudgetBuddyApi.class);
        this.categoryDao = BudgetBuddyDatabase.getInstance(context).categoryDao();
    }

    public LiveData<List<Category>> getCategories() {
        return Transformations.map(categoryDao.getAllCategories(), entities -> {
            if (entities.isEmpty()) {
                // the LiveData returned by the DAO will be updated when we receive
                // the API response
                fetchCategoriesOnline();
                return Collections.emptyList();
            }
            return entities.stream()
                    .map(CategoryEntity::toCategory)
                    .collect(Collectors.toList());
        });
    }

    private void fetchCategoriesOnline() {
        api.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                InsertCategoryTask insertCategoryTask = new InsertCategoryTask(categoryDao);
                List<Category> categoryList = response.body();
                insertCategoryTask.execute(categoryList.toArray(new Category[]{}));
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // TODO manejar el error
                Log.e("Categorias", "error: " + t.getLocalizedMessage(), t);
                //categories.setValue(Collections.emptyList());
            }
        });
    }

    private static class InsertCategoryTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        InsertCategoryTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            Arrays.stream(categories)
                    .map(CategoryEntity::fromCategory)
                    .forEach(categoryEntity -> categoryDao.insert(categoryEntity));
            return null;
        }
    }
}
