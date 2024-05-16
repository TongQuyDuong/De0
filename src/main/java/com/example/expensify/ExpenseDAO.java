package com.example.expensify;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;

@Dao
interface ExpenseDAO {
    @Query("SELECT * FROM Expense")
    List<Expense> getAllData();

    @Insert
    void addExpense(Expense expense);

}
