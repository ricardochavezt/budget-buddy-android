package net.ricardochavezt.budgetbuddy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.ricardochavezt.budgetbuddy.model.Expense;
import net.ricardochavezt.budgetbuddy.repository.ExpenseRepository;
import net.ricardochavezt.budgetbuddy.util.Result;
import net.ricardochavezt.budgetbuddy.util.ValidationError;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveExpenseViewModel extends AndroidViewModel {
    public enum Fields {
        AMOUNT, CATEGORY
    }

    public class SaveExpenseResponse {
        private Result result;
        private ValidationError validationError;
        private Fields errorField;
        private String serverErrorMessage;

        public SaveExpenseResponse(Result result) {
            this.result = result;
        }

        public SaveExpenseResponse(Result result, Fields errorField, ValidationError validationError) {
            this.result = result;
            this.validationError = validationError;
            this.errorField = errorField;
        }

        public SaveExpenseResponse(Result result, String serverErrorMessage) {
            this.result = result;
            this.serverErrorMessage = serverErrorMessage;
        }

        public Result getResult() {
            return result;
        }

        public ValidationError getValidationError() {
            return validationError;
        }

        public Fields getErrorField() {
            return errorField;
        }

        public String getServerErrorMessage() {
            return serverErrorMessage;
        }
    }

    private String amount;
    private int categoryId = -1;
    private String comment;
    private Date madeAt;
    private MutableLiveData<String> madeAtDisplay;

    public SaveExpenseViewModel(@NonNull Application application) {
        super(application);
        madeAtDisplay = new MutableLiveData<>();
    }

    public static final DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getMadeAt() {
        return madeAt;
    }

    public void setMadeAt(Date madeAt) {
        this.madeAt = madeAt;
        madeAtDisplay.setValue(formatoFecha.format(this.madeAt));
    }

    public LiveData<String> displayMadeAt() {
        return madeAtDisplay;
    }

    public LiveData<SaveExpenseResponse> saveExpense() {
        MutableLiveData<SaveExpenseResponse> saveExpenseResponse = new MutableLiveData<>();
        if (amount == null || amount.trim().isEmpty()) {
            saveExpenseResponse.setValue(new SaveExpenseResponse(Result.INVALID_DATA,
                    Fields.AMOUNT, ValidationError.EMPTY));
        }
        else {
            try {
                new BigDecimal(amount);
            } catch (NumberFormatException e) {
                saveExpenseResponse.setValue(new SaveExpenseResponse(Result.INVALID_DATA,
                        Fields.AMOUNT, ValidationError.INVALID_VALUE));
            }
        }
        if (categoryId < 0) {
            saveExpenseResponse.setValue(new SaveExpenseResponse(Result.INVALID_DATA,
                    Fields.CATEGORY, ValidationError.EMPTY));
        }

        if (saveExpenseResponse.getValue() != null) {
            return saveExpenseResponse;
        }

        saveExpenseResponse.setValue(new SaveExpenseResponse(Result.IN_PROGRESS));
        ExpenseRepository expenseRepository = new ExpenseRepository(getApplication());
        expenseRepository.saveExpense(amount, categoryId, comment, madeAt, new ExpenseRepository.SaveExpenseCallback() {
            @Override
            public void onSuccess(Expense savedExpense) {
                saveExpenseResponse.setValue(new SaveExpenseResponse(Result.OK));
            }

            @Override
            public void onError(String errorMessage) {
                saveExpenseResponse.setValue(new SaveExpenseResponse(Result.ERROR, errorMessage));
            }
        });
        return saveExpenseResponse;
    }
}
