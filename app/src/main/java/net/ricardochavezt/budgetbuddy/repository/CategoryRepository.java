package net.ricardochavezt.budgetbuddy.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.ricardochavezt.budgetbuddy.api.ApiFactory;
import net.ricardochavezt.budgetbuddy.api.BudgetBuddyApi;
import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.persistence.BudgetBuddyDatabase;
import net.ricardochavezt.budgetbuddy.persistence.CategoryDao;
import net.ricardochavezt.budgetbuddy.persistence.CategoryEntity;

import java.util.Arrays;
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
        MutableLiveData<List<Category>> categories = new MutableLiveData<>();
        ListCategoriesTask listCategoriesTask = new ListCategoriesTask(categoryDao, categories);
        listCategoriesTask.execute();

        api.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                InsertCategoryTask insertCategoryTask = new InsertCategoryTask(categoryDao);
                List<Category> categoryList = response.body();
                insertCategoryTask.execute(categoryList.toArray(new Category[]{}));
                try {
                    insertCategoryTask.get();
                    ListCategoriesTask listCategoriesTask = new ListCategoriesTask(categoryDao, categories);
                    listCategoriesTask.execute();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // TODO manejar el error
                Log.e("Categorias", "error: " + t.getLocalizedMessage(), t);
                //categories.setValue(Collections.emptyList());
            }
        });
        return categories;
    }

    private static class ListCategoriesTask extends AsyncTask<Void, Void, List<CategoryEntity>> {
        private CategoryDao categoryDao;
        private MutableLiveData<List<Category>> categoryLiveData;

        ListCategoriesTask(CategoryDao categoryDao, MutableLiveData<List<Category>> categoryLiveData) {
            this.categoryDao = categoryDao;
            this.categoryLiveData = categoryLiveData;
        }

        @Override
        protected List<CategoryEntity> doInBackground(Void... voids) {
            return categoryDao.getAllCategories();
        }

        @Override
        protected void onPostExecute(List<CategoryEntity> categoryEntities) {
            categoryLiveData.setValue(categoryEntities.stream()
                    .map(c -> c.toCategory())
                    .collect(Collectors.toList()));
        }
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
