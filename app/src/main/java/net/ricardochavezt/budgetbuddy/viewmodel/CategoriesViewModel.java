package net.ricardochavezt.budgetbuddy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.repository.CategoryRepository;

import java.util.List;

public class CategoriesViewModel extends AndroidViewModel {
    private LiveData<List<Category>> categories;

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Category>> getCategories() {
        if (categories == null) {
            CategoryRepository categoryRepository = new CategoryRepository(getApplication());
            categories = categoryRepository.getCategories();
        }
        return categories;
    }

}
