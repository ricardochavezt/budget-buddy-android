package net.ricardochavezt.budgetbuddy.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {CategoryEntity.class, ExpenseEntity.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class BudgetBuddyDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();

    private static volatile BudgetBuddyDatabase instance;

    public static BudgetBuddyDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (BudgetBuddyDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            BudgetBuddyDatabase.class,"budget_buddy_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
