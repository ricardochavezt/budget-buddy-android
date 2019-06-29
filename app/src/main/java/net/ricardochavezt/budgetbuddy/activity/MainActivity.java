package net.ricardochavezt.budgetbuddy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.ricardochavezt.budgetbuddy.R;
import net.ricardochavezt.budgetbuddy.model.Category;
import net.ricardochavezt.budgetbuddy.viewmodel.CategoriesViewModel;

import java.util.List;

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
    }

    @OnClick(R.id.btnRegistrar)
    public void btnRegistrarClick() {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
    }
}
