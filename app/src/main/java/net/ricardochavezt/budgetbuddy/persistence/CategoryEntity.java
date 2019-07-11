package net.ricardochavezt.budgetbuddy.persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import net.ricardochavezt.budgetbuddy.model.Category;

@Entity(tableName = "category")
public class CategoryEntity {
    @PrimaryKey
    @NonNull
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category toCategory() {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    public static CategoryEntity fromCategory(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(category.getId());
        categoryEntity.setName(category.getName());
        return categoryEntity;
    }
}
