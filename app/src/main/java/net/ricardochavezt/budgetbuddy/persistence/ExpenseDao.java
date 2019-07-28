package net.ricardochavezt.budgetbuddy.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExpenseEntity expense);

    @Query("SELECT * FROM expense ORDER BY madeAt DESC, id DESC")
    LiveData<List<ExpenseEntity>> getAllExpenses();

    @Query("SELECT * FROM expense_category ORDER BY madeAt DESC, id DESC")
    LiveData<List<ExpenseWithCategoryEntity>> getAllExpensesWithCategories();
}
