/*
 * Program: Product.java
 * Author: Davis Nguyen
 * Date: 8/24/2019 created
 *
 */
package inventorysystem.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    //instantiating variables
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty stock;
    private SimpleIntegerProperty min;
    private SimpleIntegerProperty max;

    //default constructor
    public Product() {
    }

    //constructor with parameters
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.min = new SimpleIntegerProperty(min);
        this.max = new SimpleIntegerProperty(max);
    }

    //setters and getters
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }

    public void deleteAssociatedPart(Part associatedPart) {
        associatedParts.remove(associatedPart);
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setPrice(double price) {
        this.price = new SimpleDoubleProperty(price);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setStock(int stock) {
        this.stock = new SimpleIntegerProperty(stock);
    }

    public int getStock() {
        return stock.get();
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public void setMin(int min) {
        this.min = new SimpleIntegerProperty(min);
    }

    public int getMin() {
        return min.get();
    }

    public void setMax(int max) {
        this.max = new SimpleIntegerProperty(max);
    }

    public int getMax() {
        return max.get();
    }
}
