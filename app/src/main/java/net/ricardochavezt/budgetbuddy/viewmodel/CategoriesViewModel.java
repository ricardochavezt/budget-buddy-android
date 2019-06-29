package net.ricardochavezt.budgetbuddy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.repository.CategoryRepository;

import java.util.List;

public class CategoriesViewModel extends ViewModel {
    private LiveData<List<Category>> categories;

    public LiveData<List<Category>> getCategories() {
        if (categories == null) {
            CategoryRepository categoryRepository = new CategoryRepository();
            categories = categoryRepository.getCategories();
        }
        return categories;
    }

}
