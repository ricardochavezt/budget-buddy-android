package net.ricardochavezt.budgetbuddy.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import net.ricardochavezt.budgetbuddy.R;
import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.viewmodel.CategoriesViewModel;
import net.ricardochavezt.budgetbuddy.viewmodel.SaveExpenseViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etMonto)
    EditText etMonto;
    @BindView(R.id.spCategoria)
    Spinner spCategoria;
    @BindView(R.id.etComentario)
    EditText etComentario;
    @BindView(R.id.etFecha)
    EditText etFecha;
    @BindView(R.id.tilMonto)
    TextInputLayout tilMonto;
    @BindView(R.id.progressOverlay)
    View progressOverlay;

    private SaveExpenseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        viewModel.displayMadeAt().observe(this, strFecha -> {
            etFecha.setText(strFecha);
        });
        viewModel.setMadeAt(new Date());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list_expenses:
                Intent listIntent = new Intent(this, ExpenseListActivity.class);
                startActivity(listIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnTextChanged(R.id.etMonto)
    public void etMontoTextChanged(CharSequence text){
        viewModel.setAmount(text.toString());
        tilMonto.setError(null);
    }

    @OnItemSelected(R.id.spCategoria)
    public void spCategoriaSelectionChanged(int position) {
        if (spCategoria.getItemAtPosition(position) instanceof Category) {
            Category selectedCategory = (Category) spCategoria.getItemAtPosition(position);
            viewModel.setCategoryId(selectedCategory.getId());
        }
    }

    @OnTextChanged(R.id.etComentario)
    public void etComentarioTextChanged(CharSequence text) {
        viewModel.setComment(text.toString());
    }

    @OnClick(R.id.etFecha)
    public void etFechaClick() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(viewModel.getMadeAt());

        new DatePickerDialog(this, (datePicker, year, month, date) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, date);
            viewModel.setMadeAt(calendar.getTime());
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @OnClick(R.id.btnRegistrar)
    public void btnRegistrarClick() {
        progressOverlay.setVisibility(View.VISIBLE);

        viewModel.saveExpense().observe(this, saveExpenseResponse -> {
                    progressOverlay.setVisibility(View.GONE);
                    if (!saveExpenseResponse.isSaveOK()) {
                        if (saveExpenseResponse.isAmountError()) {
                            tilMonto.setError(saveExpenseResponse.getAmountErrorMessage());
                        }
                        else if (saveExpenseResponse.isCategoryError()) {
                            Toast.makeText(this, R.string.error_category_not_selected, Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else {
                            hideSoftKeyboard();
                            Snackbar.make(tilMonto, R.string.error_saving_expense, Snackbar.LENGTH_SHORT).setAction("Ver mensaje", view -> {
                                new AlertDialog.Builder(this)
                                        .setTitle(R.string.error_message_title)
                                        .setMessage(saveExpenseResponse.getErrorMessage())
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                            }).show();
                        }
                    }
                    else {
                        hideSoftKeyboard();
                        clearFields();
                        Snackbar.make(tilMonto, R.string.saving_expense_ok, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void hideSoftKeyboard() {
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = getSystemService(InputMethodManager.class);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    private void clearFields() {
        etMonto.setText("");
        etComentario.setText("");
        spCategoria.setSelection(0);
    }
}
