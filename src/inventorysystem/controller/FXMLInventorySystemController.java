/*
 * Program: FXMLAddPartController.java
 * Author: Davis Nguyen
 * Date: 8/26/2019 created, functionality to redirect user to addPart, modifyPart, addProduct, and modifyProduct
 * Updated: 8/28/2019 added more functionality with all buttons and tableview
 *          9/3/2019 added alert dialogs
 *          9/4/2019 created custom cell factory to display tableview column as currency
 *          9/9/2019 fixed search function to allow user to delete item
 *
 */
package inventorysystem.controller;

import static inventorysystem.model.Inventory.getAllParts;
import static inventorysystem.model.Inventory.getAllProducts;
import static inventorysystem.model.Inventory.lookupPart;
import static inventorysystem.model.Inventory.lookupProduct;
import inventorysystem.model.Part;
import inventorysystem.model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FXMLInventorySystemController implements Initializable {

//references to view
    @FXML
    private Button partAddBTN, partModifyBTN, partDeleteBTN, partSearchBTN,
            productAddBTN, productModifyBTN, productDeleteBTN, productSearchBTN;
    @FXML
    private TextField partSearchTF, productSearchTF;

    @FXML
    private TableView<Part> partsTV;
    @FXML
    private TableColumn<Part, Integer> partId;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partStock;
    @FXML
    private TableColumn<Part, Double> partPrice;

    @FXML
    private TableView<Product> productsTV;
    @FXML
    private TableColumn<Product, Integer> productId;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Integer> productStock;
    @FXML
    private TableColumn<Product, Double> productPrice;

    //custom cellfactory to display partPrice as currency
    Callback<TableColumn<Part, Double>, TableCell<Part, Double>> partCurrency
            = (TableColumn<Part, Double> col) -> new TableCell<Part, Double>() {
        @Override
        public void updateItem(Double value, boolean empty) {
            super.updateItem(value, empty);
            if (value == null) {
                setText(null);
            } else {
                setText(String.format("$%.2f", value));
            }
        }
    };

    //custom cellfactory to display productPrice as currency
    Callback<TableColumn<Product, Double>, TableCell<Product, Double>> productCurrency
            = (TableColumn<Product, Double> col) -> new TableCell<Product, Double>() {
        @Override
        public void updateItem(Double value, boolean empty) {
            super.updateItem(value, empty);
            if (value == null) {
                setText(null);
            } else {
                setText(String.format("$%.2f", value));
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setting cell values for tableview partsTV
        partId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        partName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        partPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        partPrice.setCellFactory(partCurrency);

        partsTV.setItems(getAllParts());

        //setting cell values for tableview productsTV
        productId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        productName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        productStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        productPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        productPrice.setCellFactory(productCurrency);

        productsTV.setItems(getAllProducts());

        //adding listerner to partSearchTF to see if textfield is empty
        partSearchTF.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (partSearchTF.getText().isEmpty()) {
                    //if textfield is empty, set partsTV to observablelist parts
                    partsTV.setItems(getAllParts());
                }
            } catch (Exception e) {
            }
        });
        //adding listerner to productSearchTF to see if textfield is empty
        productSearchTF.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (productSearchTF.getText().isEmpty()) {
                    //if textfield is empty, set productsTV to observablelist product
                    productsTV.setItems(getAllProducts());
                }
            } catch (Exception e) {
            }
        });
    }

    //method used to search for parts
    public void searchPart(ActionEvent event) {
        //get and store partSearchTF
        String partSearch = partSearchTF.getText();

        //create temporary parts list to display, prevents original parts list from being changed
        ObservableList<Part> tempParts = FXCollections.observableArrayList();

        //check to see if partSearch is a digit, refering to parts ID
        if (Character.isDigit(partSearch.charAt(0))) {
            for (int i = 0; i < getAllParts().size(); i++) {
                if (partSearch.contains(String.valueOf(lookupPart(i).getId()))) {
                    //if there is a match, display results
                    tempParts.add(getAllParts().get(i));
                }
            }
        } else {
            //call lookupPart that searches part through part name and returns if there's a match
            tempParts = lookupPart(partSearch);
        }
        //display results
        partsTV.setItems(tempParts);
    }

    //changes scene to addpart
    public void sceneAddPart(ActionEvent event) {
        try {
            FXMLLoader addPart = new FXMLLoader(getClass().getResource("/resources/view/FXMLAddPart.fxml"));
            Parent root = addPart.load();

            Stage stage = (Stage) partAddBTN.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //changes scene to modifypart
    public void sceneModifyPart(ActionEvent event) {
        try {
            if (partsTV.getSelectionModel().getSelectedItem() != null) {
                FXMLLoader modifyPart = new FXMLLoader(getClass().getResource("/resources/view/FXMLModifyPart.fxml"));
                Parent root = modifyPart.load();

                //grabbing controller from FXMLModifyPartController to pass partsTV to modifypart scene
                FXMLModifyPartController modifyPartController = modifyPart.getController();
                modifyPartController.setParts(partsTV.getSelectionModel().getSelectedItem());

                Stage stage = (Stage) partModifyBTN.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //method used to delete part from partsTV
    public void deletePart(ActionEvent event) {
        //alert confirmation for deleting part
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Confirmation");
        alert.setContentText("Are you sure you want to delete part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (partsTV.getSelectionModel().getSelectedItem() != null) {

                //create new two new observablelist
                //one for grabbing selected part, the other for grabbing all of the parts
                ObservableList<Part> selected, part;

                part = getAllParts();

                selected = partsTV.getSelectionModel().getSelectedItems();

                //for every instance of selected in part, remove selected part
                for (Part delete : selected) {
                    part.remove(delete);
                }

                //if user searches for part, remove part from search list
                if (!partSearchTF.getText().isEmpty()) {
                    ObservableList<Part> search = partsTV.getItems();

                    for (Part delete : selected) {
                        search.remove(delete);
                    }
                }
            }
        }
    }

    //method used to search for products
    public void searchProduct(ActionEvent event) {
        //get and store productSearchTF
        String productSearch = productSearchTF.getText();

        //create temporary products list to display, prevents original products list from being changed
        ObservableList<Product> tempProducts = FXCollections.observableArrayList();

        if (Character.isDigit(productSearch.charAt(0))) {
            for (int i = 0; i < getAllProducts().size(); i++) {
                if (productSearch.contains(String.valueOf(lookupProduct(i).getId()))) {
                    //if there is a match, display results
                    tempProducts.add(getAllProducts().get(i));
                }
            }
        } else {
            //call lookupProduct that searches product through product name and returns if there's a match
            tempProducts = lookupProduct(productSearch);
        }
        //display results
        productsTV.setItems(tempProducts);
    }

    //changes scene to addproduct
    public void sceneAddProduct(ActionEvent event) {
        try {
            if (partsTV.getItems().isEmpty()) {
                //alert user if a part has not been created and needs to be created to continue
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Empty Part(s)");
                alert.setContentText("Must have at least one part to continue");

                alert.showAndWait();
            } else {
                FXMLLoader addProduct = new FXMLLoader(getClass().getResource("/resources/view/FXMLAddProduct.fxml"));
                Parent root = addProduct.load();

                Stage stage = (Stage) productAddBTN.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //changes scene to modifyproduct
    public void sceneModifyProduct(ActionEvent event) {
        try {
            if (productsTV.getSelectionModel().getSelectedItem() != null) {
                FXMLLoader modifyProduct = new FXMLLoader(getClass().getResource("/resources/view/FXMLModifyProduct.fxml"));
                Parent root = modifyProduct.load();

                //grabbing controller from FXMLModifyProductController to pass productsTV to modifyproduct scene
                FXMLModifyProductController modifyProductController = modifyProduct.getController();
                modifyProductController.setProduct(productsTV.getSelectionModel().getSelectedItem());

                Stage stage = (Stage) partModifyBTN.getScene().getWindow();
                stage.setScene(new Scene(root));
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //method used to delete product from productsTV
    public void deleteProduct(ActionEvent event) {
        //alert confirmation for deleting product
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Confirmation");
        alert.setContentText("Are you sure you want to delete product?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (productsTV.getSelectionModel().getSelectedItem() != null) {
                //create new two new observablelist
                //one for grabbing selected product, the other for grabbing all of the products
                ObservableList<Product> selected, product;

                product = getAllProducts();

                selected = productsTV.getSelectionModel().getSelectedItems();

                //for every instance of selected in product, remove selected product
                for (Product stuff : selected) {
                    product.remove(stuff);
                }

                //if user searches for product, remove product from search list
                if (!productSearchTF.getText().isEmpty()) {
                    ObservableList<Product> search = productsTV.getItems();

                    for (Product delete : selected) {
                        search.remove(delete);
                    }
                }
            }
        }
    }

    //exit program
    public void exit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
