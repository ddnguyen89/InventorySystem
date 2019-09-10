/*
 * Program: FXMLModifyProductController.java
 * Author: Davis Nguyen
 * Date: 8/22/2019 created 
 * Updated: 8/30/2019 added basic functionality to textfields and buttons
 *          9/3/2019 added alert dialogs, fixed adding parts to products
 *          9/4/2019 fixed setting id to products list, created custom cell factory to display tableview column as currency
 *          9/9/2019 added search function
 *
 */
package inventorysystem.controller;

import inventorysystem.model.Inventory;
import static inventorysystem.model.Inventory.getAllParts;
import static inventorysystem.model.Inventory.lookupPart;
import inventorysystem.model.Part;
import inventorysystem.model.Product;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FXMLModifyProductController implements Initializable {

    //references to view
    @FXML
    private Button partSearchBTN, partAddBTN, partDeleteBTN, saveBTN, cancelBTN;

    @FXML
    private TextField searchTF, idTF, nameTF, stockTF, priceTF, minTF, maxTF;

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
    private TableView<Part> addedPartsTV;
    @FXML
    private TableColumn<Part, Integer> addedPartId;
    @FXML
    private TableColumn<Part, String> addedPartName;
    @FXML
    private TableColumn<Part, Integer> addedPartStock;
    @FXML
    private TableColumn<Part, Double> addedPartPrice;

    //creating product
    private Product modifyProduct;

    //formating double text to currency for alert dialogs
    NumberFormat nf = NumberFormat.getCurrencyInstance();

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setting cell values for tableview partsTV
        partId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        partName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        partPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        partPrice.setCellFactory(partCurrency);

        partsTV.setItems(getAllParts());

        //setting cell values for tableview addedPartsTV
        addedPartId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        addedPartName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addedPartStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        addedPartPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        addedPartPrice.setCellFactory(partCurrency);
        
        //adding listerner to SearchTF to see if textfield is empty
        searchTF.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (searchTF.getText().isEmpty()) {
                    //if textfield is empty, set partsTV to observablelist part
                    partsTV.setItems(getAllParts());
                }
            } catch (Exception e) {
            }
        });

        //add focusproperty to invTF textfield
        stockTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //when the focus of textfield is lost
                if (!newValue) {
                    //if there is no values in minTF or maxTF textfield, clear invTF textfield
                    if (minTF.getText().isEmpty() || maxTF.getText().isEmpty()) {
                        //clear stockTF textfield
                        stockTF.clear();
                    }
                    try {
                        //if invTF value is not inbetween minTF and maxTF, clear invTF textfield
                        if (Integer.parseInt(minTF.getText()) > Integer.parseInt(stockTF.getText())) {
                            //alert message for user input error
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Inccorect Inv Input");
                            alert.setContentText("Inv Amount: " + stockTF.getText() + "\n\nMust be greater than or equal to"
                                    + "\n\nMin Amount: " + minTF.getText());

                            alert.showAndWait();
                            //clear stockTF textfield
                            stockTF.clear();
                        } else if (Integer.parseInt(maxTF.getText()) < Integer.parseInt(stockTF.getText())) {
                            //alert message for user input error
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Incorrect Inv Input");
                            alert.setContentText("Inv Amount: " + stockTF.getText() + "\n\nMust be lower than or equal to"
                                    + "\n\nMax Amount: " + maxTF.getText());

                            alert.showAndWait();
                            //clear stockTF textfield
                            stockTF.clear();
                        }
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
            }
        });

        //add focusproperty to minTF textfield
        minTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //when the focus of textfield is lost
                if (!newValue) {
                    try {
                        //if minTF value is greater than maxTF, clear minTF textfield
                        if (Integer.parseInt(minTF.getText()) > Integer.parseInt(maxTF.getText())) {
                            //alert message for user input error
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Incorrect Min Input");
                            alert.setContentText("Min Amount: " + minTF.getText() + "\n\nMust be lower than or equal to"
                                    + "\n\nMax Amount: " + maxTF.getText());

                            alert.showAndWait();
                            //clear minTF textfield
                            minTF.clear();
                        }
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
            }
        });

        //add focusproperty to maxTF textfield
        maxTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                }
                //when the focus of textfield is lost
                if (!newValue) {
                    try {
                        //if maxTF value is less than minTF, clear maxTF textfield
                        if (Integer.parseInt(maxTF.getText()) < Integer.parseInt(minTF.getText())) {
                            //alert message for user input error
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Incorrect Max Input");
                            alert.setContentText("Max Amount: " + maxTF.getText() + "\n\nMust be greater than or equal to"
                                    + "\n\nMin Amount: " + minTF.getText());

                            alert.showAndWait();
                            //clear maxTF textfield
                            maxTF.clear();
                        }
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
            }
        });
    }
    
    //method used to search for parts
    public void searchPart(ActionEvent event) {
        //get and store partSearchTF
        String partSearch = searchTF.getText();

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

    //method used to pass tableview from InventorySystemController to ModifyProductController
    public void setProduct(Product value) {
        //set modifyProduct as new product value
        modifyProduct = value;

        //set the values of modifyProduct into textfield
        idTF.setText(String.valueOf(modifyProduct.getId()));
        nameTF.setText(modifyProduct.getName());
        stockTF.setText(String.valueOf(modifyProduct.getStock()));
        priceTF.setText(String.valueOf(modifyProduct.getPrice()));
        maxTF.setText(String.valueOf(modifyProduct.getMax()));
        minTF.setText(String.valueOf(modifyProduct.getMin()));

        addedPartsTV.setItems(modifyProduct.getAllAssociatedParts());
    }

    //method used to add part from parts list to associated product list   
    public void addPart(ActionEvent event) {
        if (partsTV.getSelectionModel().getSelectedItem() != null) {
            //setting cell values for tableview addedPartsTV
            addedPartId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            addedPartName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            addedPartStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
            addedPartPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

            addedPartsTV.getItems().add(partsTV.getSelectionModel().getSelectedItem());
        }
    }

    //method used to delete part from addedPartsTV
    public void deletePart(ActionEvent event) {
        //alert confirmation for deleting part
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Confirmation");
        alert.setContentText("Are you sure you want to delete part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (addedPartsTV.getSelectionModel().getSelectedItem() != null) {
                //create new two new observablelist
                //one for grabbing selected part, the other for grabbing all of the parts
                ObservableList<Part> selected, part;

                part = addedPartsTV.getItems();

                selected = addedPartsTV.getSelectionModel().getSelectedItems();

                //for every instance of the selected part in all the parts, remove the part
                for (Part stuff : selected) {
                    part.remove(stuff);
                }
            }
        }
    }

    //KeyEvent used to filter inputs on textfield 'onkeypress'
    public void numberFilter(KeyEvent event) {
        //filter invTF to only allow numbers
        stockTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*") || newValue.matches("0")) {
                    stockTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //filter maxTF to only allow numbers
        maxTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*") || newValue.matches("0")) {
                    maxTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //filter minTF to only allow numbers
        minTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    minTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //filter priceTF to only allow doubles
        priceTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                    priceTF.setText(oldValue);
                }
            }
        });
    }

    //when save button is clicked
    public void saveProduct(ActionEvent event) {
        try {
            if (nameTF.getText().isEmpty() || stockTF.getText().isEmpty() || priceTF.getText().isEmpty()
                    || minTF.getText().isEmpty() || maxTF.getText().isEmpty()) {
                //alert message if any field in products is empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Product field empty");
                alert.setContentText("Please make sure all of the product fields are filled");

                alert.showAndWait();
            } else if (addedPartsTV.getItems().isEmpty()) {
                //alert message if addedPartsTV is empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Part field empty");
                alert.setContentText("Please make sure all there is at least one part added");

                alert.showAndWait();
            } else {
                //creating and instantiating total parts cost
                double partsCost = 0.0;

                //grab price from each item in addedPartsTV and add price to partsCost
                for (int i = 0; i < addedPartsTV.getItems().size(); i++) {
                    partsCost += addedPartsTV.getItems().get(i).getPrice();
                }
                //if product price is less than total parts cost, alert user
                if (Double.parseDouble(priceTF.getText()) < partsCost) {
                    //alert message if product cost is less than total parts cost
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Product cost");
                    alert.setContentText("Product Cost: " + nf.format(Double.parseDouble(priceTF.getText())) + "\n\nMust be greater than"
                            + "\n\nTotal Parts Cost: " + nf.format(partsCost));
                    alert.showAndWait();
                } else {
                    //delete current product
                    Inventory.deleteProduct(modifyProduct);

                    //grab data from textfields
                    Integer id = Integer.parseInt(idTF.getText());
                    String name = nameTF.getText();
                    Integer inv = Integer.parseInt(stockTF.getText());
                    Double price = Double.parseDouble(priceTF.getText());
                    Integer min = Integer.parseInt(minTF.getText());
                    Integer max = Integer.parseInt(maxTF.getText());

                    //create new product
                    Product newProduct = new Product();
                    //add data grabed from textfields into new product
                    newProduct.setId(id);
                    newProduct.setName(name);
                    newProduct.setStock(inv);
                    newProduct.setPrice(price);
                    newProduct.setMin(min);
                    newProduct.setMax(max);

                    //clear all parts associated with the new product
                    newProduct.getAllAssociatedParts().clear();

                    //for every instance of parts in addedPartsTV, add the parts to new product
                    for (Part adding : addedPartsTV.getItems()) {
                        newProduct.addAssociatedPart(adding);
                    }
                    //add product to observablelist
                    Inventory.addProduct(newProduct);

                    //load FXMLInventorySystem scene
                    FXMLLoader save = new FXMLLoader(getClass().getResource("/resources/view/FXMLInventorySystem.fxml"));
                    Stage stage = (Stage) saveBTN.getScene().getWindow();
                    Scene scene = new Scene(save.load());

                    stage.setScene(scene);
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

//exit scene to FXMLInventorySystem
    public void sceneCancel(ActionEvent event) {
        try {
            //alert confirmation for leaving scene
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Cancel Confirmation");
            alert.setContentText("Are you sure you want to cancel?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                FXMLLoader cancel = new FXMLLoader(getClass().getResource("/resources/view/FXMLInventorySystem.fxml"));
                Stage stage = (Stage) cancelBTN.getScene().getWindow();
                Scene scene = new Scene(cancel.load());

                stage.setScene(scene);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
