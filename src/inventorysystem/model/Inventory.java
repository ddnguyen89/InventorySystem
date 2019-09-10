/*
 * Program: Part.java
 * Author: Davis Nguyen
 * Date: 8/24/2019
 * Updated: 8/26/2019 correcting methods
 *
 */
package inventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    //instantiating variables
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //default constructor
    public Inventory() {
    }

    //setters and getters
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partId) {
        return allParts.get(partId);
    }

    public static Product lookupProduct(int productId) {
        return allProducts.get(productId);
    }

    public static ObservableList<Part> lookupPart(String partName) {
        //create a temporary observablelist part
        ObservableList<Part> temp = FXCollections.observableArrayList();
        
        //search through all indexes of allParts
        for (int i = 0; i < allParts.size(); i++) {
            if (partName.equalsIgnoreCase(allParts.get(i).getName())) {
                //if partName matches any indexes, set temp to that index
                temp.setAll(allParts.get(i));
            }
        }
        //return observablelist temp
        return temp;
    }

    public static ObservableList<Product> lookupProduct(String productName) {
        //create a temporary observablelist product
        ObservableList<Product> temp = FXCollections.observableArrayList();
        
        for (int i = 0; i < allProducts.size(); i++) {
            if (productName.equalsIgnoreCase(allProducts.get(i).getName())) {
                //if productName matches any indexes, set temp to that index
                temp.setAll(allProducts.get(i));
            }
        }
        //return observablelist temp
        return temp;
    }

    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    public static void deletePart(Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).equals(selectedPart)) {
                allParts.remove(i);
            }
        }
        allParts.remove(selectedPart);
    }

    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
