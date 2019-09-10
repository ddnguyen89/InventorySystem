/*
 * Program: FXMLModifyPartController.java
 * Author: Davis Nguyen
 * Date: 8/21/2019 created
 * Updated: 8/26/2019 added filters for textfields
 *          8/29/2019 added functionality with InventorySystemController
 *          9/3/2019 added alert dialogs
 *          9/4/2019 fixed setting id to parts list
 *
 */
package inventorysystem.controller;

import inventorysystem.model.InHouse;
import inventorysystem.model.Inventory;
import inventorysystem.model.Outsourced;
import inventorysystem.model.Part;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FXMLModifyPartController implements Initializable {

    //references to view
    @FXML
    private Button saveBTN, cancelBTN;
    @FXML
    private Label compMachLabel;
    @FXML
    private RadioButton inHouseR_BTN, outsourcedR_BTN;
    @FXML
    private TextField idTF, nameTF, stockTF, priceTF, maxTF, minTF, compMachTF;

    //instantiating Part modifyPart
    private Part modifyPart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                            //clear minTF textfield
                            maxTF.clear();
                        }
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
            }
        });
    }

    //method used to pass tableview from InventorySystemController to ModifyPartController
    public void setParts(Part value) {
        //set modifyPart as new part value
        modifyPart = value;

        //check to see instanceof the subtype of outsourced from modifyPart
        if (modifyPart instanceof Outsourced) {
            //set outsourced radio button to true and change label
            outsourcedR_BTN.setSelected(true);
            compMachLabel.setText("Company Name");

            //create new Part from modifyPart
            Outsourced part = (Outsourced) modifyPart;

            //set the values of part into textfield
            idTF.setText(String.valueOf(part.getId()));
            nameTF.setText(part.getName());
            stockTF.setText(String.valueOf(part.getStock()));
            priceTF.setText(String.valueOf(part.getPrice()));
            maxTF.setText(String.valueOf(part.getMax()));
            minTF.setText(String.valueOf(part.getMin()));
            compMachTF.setText(part.getCompanyName());
        } else {
            //set outsourced radio button to true and change label
            inHouseR_BTN.setSelected(true);
            compMachLabel.setText("Machine ID");

            //create new Part from modifyPart
            InHouse part = (InHouse) modifyPart;

            //set the values of part into textfield
            idTF.setText(String.valueOf(part.getId()));
            nameTF.setText(part.getName());
            stockTF.setText(String.valueOf(part.getStock()));
            priceTF.setText(String.valueOf(part.getPrice()));
            maxTF.setText(String.valueOf(part.getMax()));
            minTF.setText(String.valueOf(part.getMin()));
            compMachTF.setText(String.valueOf(part.getmachineId()));
        }
    }

    //ActionEvent on radiobutton to control label and textfield
    public void radioButtonAction(ActionEvent event) {
        //instantiating label and textfield
        String label = "";
        String textField = "";

        //if 'inhouse' radiobutton is selected, change label and textfield
        if (inHouseR_BTN.isSelected()) {
            compMachTF.clear();
            label = "Machine ID";
            textField = "Mach ID";
        }
        //if 'outsourced' radiobutton is selected, change label and textfield
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
                Inventory.deletePart(modifyPart);

                //grab data from textfields
                Integer id = Integer.parseInt(idTF.getText());
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
                    //add data grabed from textfields into part
                    part.setId(id);
                    part.setName(name);
                    part.setStock(inv);
                    part.setPrice(price);
                    part.setMin(min);
                    part.setMax(max);
                    part.setmachineId(Integer.parseInt(compMach));
                    //update observablelist with part
                    Inventory.addPart(part);
                }
                //if outsource button is selected
                if (outsourcedR_BTN.isSelected()) {
                    //create new part oursource
                    Outsourced part = new Outsourced();
                    //add data grabed from textfields into part
                    part.setId(id);
                    part.setName(name);
                    part.setStock(inv);
                    part.setPrice(price);
                    part.setMin(min);
                    part.setMax(max);
                    part.setCompanyName(compMach);
                    //update observablelist with part
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
