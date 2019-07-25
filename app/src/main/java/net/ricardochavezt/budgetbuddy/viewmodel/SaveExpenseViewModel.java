package net.ricardochavezt.budgetbuddy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.ricardochavezt.budgetbuddy.model.Expense;
import net.ricardochavezt.budgetbuddy.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveExpenseViewModel extends AndroidViewModel {

    public SaveExpenseViewModel(@NonNull Application application) {
        super(application);
    }

    public class SaveExpenseResponse {
        private boolean amountError;
        private String amountErrorMessage;
        private boolean categoryError;
        private boolean saveOK;
        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean isSaveOK() {
            return saveOK;
        }

        public boolean isCategoryError() {
            return categoryError;
        }

        public String getAmountErrorMessage() {
            return amountErrorMessage;
        }

        public boolean isAmountError() {
            return amountError;
        }
    }

    public static final DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    public LiveData<SaveExpenseResponse> saveExpense(String amount, int categoryId, String comment, Date madeAt) {
        MutableLiveData<SaveExpenseResponse> saveExpenseResponse = new MutableLiveData<>();
        SaveExpenseResponse response = new SaveExpenseResponse();
        if (amount == null || amount.trim().isEmpty()) {
            response.amountError = true;
            response.amountErrorMessage = "Debe ingresar un monto";
        }
        else {
            try {
                new BigDecimal(amount);
            } catch (NumberFormatException e) {
                response.amountError = true;
                response.amountErrorMessage = "El monto ingresado no es válido";
            }
        }
        if (categoryId < 0) {
            response.categoryError = true;
        }
        if (response.amountError ||response.categoryError) {
            saveExpenseResponse.setValue(response);
        }
        else {
            ExpenseRepository expenseRepository = new ExpenseRepository(getApplication());
            expenseRepository.saveExpense(amount, categoryId, comment, madeAt, new ExpenseRepository.SaveExpenseCallback() {
                @Override
                public void onSuccess(Expense savedExpense) {
                    response.saveOK = true;
                    saveExpenseResponse.setValue(response);
                }

                @Override
                public void onError(String errorMessage) {
                    response.errorMessage = errorMessage;
                    saveExpenseResponse.setValue(response);
                }
            });
        }
        return saveExpenseResponse;
    }
}
