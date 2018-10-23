package com.example.amira.bakingapp.models;

public class Ingredient {
    private String Name;
    private double Quantity;
    private String Measure;

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setMeasure(String measure) {
        Measure = measure;
    }

    public String getMeasure() {
        return Measure;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    public double getQuantity() {
        return Quantity;
    }
}
