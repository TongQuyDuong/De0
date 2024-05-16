package com.example.expensify;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextDate;
    EditText editTextExpenseName;
    EditText editTextExpenseAmount;
    EditText editTextLocation;
    Spinner spinnerExpenseCategory;
    SwitchCompat switchExpensePaid;
    Button buttonAddExpense;
    Button buttonView;
    ImageButton pickDateButton;

    ExpenseDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Ánh xạ các thành phần trong layout
        editTextDate = findViewById(R.id.editTextDate);
        editTextExpenseName = findViewById(R.id.editTextExpenseName);
        editTextExpenseAmount = findViewById(R.id.editTextExpenseAmount);
        editTextLocation = findViewById(R.id.editTextLocation);
        spinnerExpenseCategory = findViewById(R.id.spinnerExpenseCategory);
        switchExpensePaid = findViewById(R.id.switchExpensePaid);
        buttonAddExpense = findViewById(R.id.buttonAddExpense);
        pickDateButton = findViewById(R.id.pickDate);
        buttonView = findViewById(R.id.buttonView);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    editTextLocation.setText( location.getLatitude() + " " + location.getLongitude());
                }
            });
        }

        db = Room.databaseBuilder(getApplicationContext(), ExpenseDatabase.class, ExpenseDatabase.NAME).allowMainThreadQueries().build().getDAO();



        // Đặt sự kiện click cho nút "Thêm"
        buttonAddExpense.setOnClickListener(view -> {
            addExpense();
        });

        pickDateButton.setOnClickListener(view -> {
            // on below line we are getting
            // the instance of our calendar.
            final Calendar c = Calendar.getInstance();

            // on below line we are getting
            // our day, month and year.
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // on below line we are creating a variable for date picker dialog.
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    // on below line we are passing context.
                    MainActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            editTextDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        buttonView.setOnClickListener(view -> {
            Expense result = db.getAllData().get(0);

            Toast.makeText(MainActivity.this, String.valueOf(result.amount) , Toast.LENGTH_SHORT).show();

        });
    }

    private void addExpense() {
        if (checkEmptyFields()) {
            Toast.makeText(MainActivity.this, "Hay dien het cac thong tin", Toast.LENGTH_SHORT).show();
        } else {
            String date = editTextDate.getText().toString();
            String expenseName = editTextExpenseName.getText().toString();
            long expenseAmount = Long.parseLong(editTextExpenseAmount.getText().toString());
            String expenseCategory = spinnerExpenseCategory.getSelectedItem().toString();
            String location = editTextLocation.getText().toString();
            boolean expensePaid = switchExpensePaid.isChecked();


            db.addExpense(new Expense(expenseName,expenseAmount,date,location,expenseCategory,expensePaid));

            Toast.makeText(MainActivity.this, "Đã thêm khoản chi phí: " + expenseName, Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean checkEmptyFields() {
        EditText[] fields = new EditText[]{editTextLocation,editTextDate,editTextExpenseAmount,editTextExpenseName};

        for (EditText field : fields) {
             if (field.getText().toString().trim().length() == 0) {
                 return  true;
             }
        }
        return false;
    }

}
