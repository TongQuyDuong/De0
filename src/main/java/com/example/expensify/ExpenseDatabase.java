package com.example.expensify;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Expense.class},version = 1)
public abstract class ExpenseDatabase extends RoomDatabase {
    public static String NAME = "Expense";

    public abstract ExpenseDAO getDAO();
}
