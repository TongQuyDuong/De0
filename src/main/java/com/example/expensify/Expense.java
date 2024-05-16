package com.example.expensify;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Expense {
    @PrimaryKey(autoGenerate = true)
    int id = 0;
    String name;
    float amount;
    String createdDate;
    String location;
    String category;
    boolean isPaid;

    public Expense() {
    }

    public Expense(String name, float amount, String createdDate, String location, String category, boolean isPaid) {
        this.name = name;
        this.amount = amount;
        this.createdDate = createdDate;
        this.location = location;
        this.category = category;
        this.isPaid = isPaid;
    }

}
