package net.ricardochavezt.budgetbuddy.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryEntity category);

    @Query("SELECT * FROM category ORDER BY id")
    LiveData<List<CategoryEntity>> getAllCategories();
}
