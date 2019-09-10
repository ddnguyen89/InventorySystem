/*
 * Program: FXMLAddPartController.java
 * Author: Davis Nguyen
 * Date: 8/19/2019 created
 * Updated: 8/26/2019 added filters for textfields
 *          8/28/2019 added functionality with InventorySystemController
 *          8/30/2019 added functionality with buttons and textfields
 *          9/3/2019 added alert dialogs
 *          9/4/2019 fixed setting id to parts list
 */
package inventorysystem.controller;

import inventorysystem.model.InHouse;
import inventorysystem.model.Inventory;
import inventorysystem.model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FXMLAddPartController implements Initializable {

    //references to view
    @FXML
    private RadioButton inHouseR_BTN, outsourcedR_BTN;
    @FXML
    private Label compMachLabel;
    @FXML
    private TextField nameTF, stockTF, priceTF, minTF, maxTF, compMachTF;
    @FXML
    private Button saveBTN, cancelBTN;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //add listerner to invTF textfield
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
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Inccorect Inv Input");
                            alert.setContentText("Inv Amount: " + stockTF.getText() + "\n\nMust be greater than or equal to"
                                    + "\n\nMin Amount: " + minTF.getText());

                            alert.showAndWait();
                            //clear stockTF textfield
                            stockTF.clear();
                        } else if (Integer.parseInt(maxTF.getText()) < Integer.parseInt(stockTF.getText())) {
                            //alert message for user input error
                            Alert alert = new Alert(AlertType.ERROR);
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

        //add listener to minTF textfield
        minTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //when the focus of textfield is lost
                if (!newValue) {
                    try {
                        //if minTF value is greater than maxTF, clear minTF textfield
                        if (Integer.parseInt(minTF.getText()) > Integer.parseInt(maxTF.getText())) {
                            //alert message for user input error
                            Alert alert = new Alert(AlertType.ERROR);
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

        //add listerner to maxTF textfield
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
                            Alert alert = new Alert(AlertType.ERROR);
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

    //ActionEvent on radiobutton to control label and textfield
    public void radioButtonAction(ActionEvent event) {
        //instantiating label and textfield
        String label = "";
        String textField = "";

        //if 'inhouse' radiobutton is selected, clear textfield, change label and textfield
        if (inHouseR_BTN.isSelected()) {
            compMachTF.clear();
            label = "Machine ID";
            textField = "Mach ID";
        }
        //if 'outsourced' radiobutton is selected, clear textfield, change label and textfield
        if (outsourcedR_BTN.isSelected()) {
            compMachTF.clear();
            label = "Company Name";
            textField = "Comp Nm";
        }

        //set new label and textfield based on selected radiobuttons
        compMachLabel.setText(label);
        compMachTF.setPromptText(textField);
    }

    //KeyEvent used to filter inputs on textfield 'onkeypress'
    public void numberFilter(KeyEvent event) {
        //if radiobutton ishouse, filter companyMachineTF to only allow numbers
        compMachTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (inHouseR_BTN.isSelected()) {
                    if (!newValue.matches("\\d*")) {
                        compMachTF.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            }
        });

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
    public void savePart(ActionEvent event) {
        try {
            if (nameTF.getText().isEmpty() || stockTF.getText().isEmpty() || priceTF.getText().isEmpty()
                    || minTF.getText().isEmpty() || maxTF.getText().isEmpty() || compMachTF.getText().isEmpty()) {
                //alert message if any field in parts is empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Part field empty");
                alert.setContentText("Please make sure all of the part fields are filled");

                alert.showAndWait();
            } else {
                //grab data from textfields
                String name = nameTF.getText();
                Integer inv = Integer.parseInt(stockTF.getText());
                Double price = Double.parseDouble(priceTF.getText());
                Integer min = Integer.parseInt(minTF.getText());
                Integer max = Integer.parseInt(maxTF.getText());
                String compMach = compMachTF.getText();

                //if inHouse button is selected
                if (inHouseR_BTN.isSelected()) {
                    //create new part InHouse
                    InHouse part = new InHouse();

                    //setting autogenerated id
                    //if there is no parts list, set id to 1
                    if (Inventory.getAllParts().isEmpty()) {
                        part.setId(1);
                    } else {
                        //if there is a parts list, search though the parts list's id and grab the largest number
                        //set new part id to largest number + 1
                        int temp;
                        for (int i = 0; i < Inventory.getAllParts().size(); i++) {
                            temp = Inventory.getAllParts().get(0).getId();
                            if (Inventory.getAllParts().get(i).getId() > temp) {
                                temp = Inventory.getAllParts().get(i).getId();
                            }
                            part.setId(temp + 1);
                            //  part.setId(Inventory.getAllParts().get(Inventory.getAllParts().size() - 1).getId() + 1);
                        }
                    }
                    //add data grabed from textfields into part
                    part.setName(name);
                    part.setStock(inv);
                    part.setPrice(price);
                    part.setMin(min);
                    part.setMax(max);
                    part.setmachineId(Integer.parseInt(compMach));
                    //add part to observablelist
                    Inventory.addPart(part);
                }
                //if outsource button is selected
                if (outsourcedR_BTN.isSelected()) {
                    //create new part oursource
                    Outsourced part = new Outsourced();

                    //setting autogenerated id
                    //if there is no parts list, set id to 1
                    if (Inventory.getAllParts().isEmpty()) {
                        part.setId(1);
                    } else {
                        int temp;
                        for (int i = 0; i < Inventory.getAllParts().size(); i++) {
                            temp = Inventory.getAllParts().get(0).getId();
                            if (Inventory.getAllParts().get(i).getId() > temp) {
                                temp = Inventory.getAllParts().get(i).getId();
                            }
                            part.setId(temp + 1);
                            //  part.setId(Inventory.getAllParts().get(Inventory.getAllParts().size() - 1).getId() + 1);
                        }
                    }
                    //add data grabed from textfields into part
                    part.setName(name);
                    part.setStock(inv);
                    part.setPrice(price);
                    part.setMin(min);
                    part.setMax(max);
                    part.setCompanyName(compMach);
                    //add part to observablelist
                    Inventory.addPart(part);
                }
                //load FXMLInventorySystem scene
                FXMLLoader save = new FXMLLoader(getClass().getResource("/resources/view/FXMLInventorySystem.fxml"));
                Stage stage = (Stage) saveBTN.getScene().getWindow();
                Scene scene = new Scene(save.load());

                stage.setScene(scene);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    //exit scene to FXMLInventorySystem
    public void sceneCancel(ActionEvent event) {
        try {
            //alert confirmation for leaving scene
            Alert alert = new Alert(AlertType.CONFIRMATION);
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
