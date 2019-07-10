package net.ricardochavezt.budgetbuddy.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import net.ricardochavezt.budgetbuddy.R;
import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.viewmodel.CategoriesViewModel;
import net.ricardochavezt.budgetbuddy.viewmodel.SaveExpenseViewModel;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etMonto)
    EditText etMonto;
    @BindView(R.id.spCategoria)
    Spinner spCategoria;
    @BindView(R.id.etComentario)
    EditText etComentario;
    @BindView(R.id.tilMonto)
    TextInputLayout tilMonto;

    private SaveExpenseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.prompts_categoria, android.R.layout.simple_dropdown_item_1line);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(spinnerAdapter);

        CategoriesViewModel categoriesViewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);
        categoriesViewModel.getCategories().observe(this, categories -> {
            ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_dropdown_item_1line, categories);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spCategoria.setAdapter(categoryAdapter);
        });

        viewModel = ViewModelProviders.of(this).get(SaveExpenseViewModel.class);
    }

    @OnClick(R.id.btnRegistrar)
    public void btnRegistrarClick() {
        // TODO clear on edit
        tilMonto.setError(null);

        Category selectedCategory = (Category) spCategoria.getSelectedItem();
        int categoryId = selectedCategory != null ? selectedCategory.getId() : -1;
        viewModel.saveExpense(etMonto.getText().toString(), categoryId, etComentario.getText().toString(), new Date())
                .observe(this, saveExpenseResponse -> {
                    if (!saveExpenseResponse.isSaveOK()) {
                        if (saveExpenseResponse.isAmountError()) {
                            tilMonto.setError(saveExpenseResponse.getAmountErrorMessage());
                        }
                        if (saveExpenseResponse.isCategoryError()) {
                            Toast.makeText(this, R.string.error_category_not_selected, Toast.LENGTH_SHORT)
                                    .show();
                        }
                        Snackbar.make(tilMonto, R.string.error_saving_expense, Snackbar.LENGTH_SHORT).setAction("Ver mensaje", view -> {
                            new AlertDialog.Builder(this)
                                    .setTitle(R.string.error_message_title)
                                    .setMessage(saveExpenseResponse.getErrorMessage())
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                        }).show();
                    }
                    else {
                        Snackbar.make(tilMonto, R.string.saving_expense_ok, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
