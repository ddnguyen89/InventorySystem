/*
 * Program: Outsourced.java
 * Author: Davis Nguyen
 * Date: 8/24/2019 created
 *
 */
package inventorysystem.model;

import javafx.beans.property.SimpleStringProperty;

public class Outsourced extends Part {

    //instantiating variable
    private SimpleStringProperty companyName;

    //default constructor
    public Outsourced() {
    }

    //constructor with parameters
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = new SimpleStringProperty(companyName);
    }

    //setters and getters
    public void setCompanyName(String companyName) {
        this.companyName = new SimpleStringProperty(companyName);
    }

    public String getCompanyName() {
        return companyName.get();
    }

}
