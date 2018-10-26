package application;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLController {

	@FXML
	private AnchorPane backgroundAnchor;

	@FXML
	private ChoiceBox<String> choiceBoxSize = new ChoiceBox<>();

	@FXML
	private ChoiceBox<String> choiceBoxCheese = new ChoiceBox<>();

	@FXML
	private ChoiceBox<String> choiceBoxPepperoni = new ChoiceBox<>();

	@FXML
	private ChoiceBox<String> choiceBoxMushrooms = new ChoiceBox<>();

	@FXML
	private TextField pizzaNumber;

	@FXML
	private Button orderButton;

	@FXML
	private Button exitButton;

	@FXML
	private TextArea orderOutput;

	@FXML
	private Label description;

	@FXML
	private Label price;

	@FXML
	private Label totalPrice;

	@FXML
	private Button clearOrders;

	private double cumulativeCost;

	private static ArrayList<String> orders = new ArrayList<>();

	private ObservableList<String> choiceListSize = FXCollections.observableArrayList(
			"Small", "Medium", "Large");

	private ObservableList<String> choiceListCheese = FXCollections.observableArrayList(
			"Single", "Double", "Triple");

	private ObservableList<String> choiceListPepperoni = FXCollections.observableArrayList(
			"None", "Single", "Double");

	private ObservableList<String> choiceListMushrooms = FXCollections.observableArrayList(
			"None", "Single", "Double");

	private ObservableList<String> choiceListMushroomsRestricted = FXCollections.observableArrayList(
			"None");

	@FXML
	void clearOrdersAction(ActionEvent event) {
		orderOutput.clear();
		orders.removeAll(orders);
		cumulativeCost = 0.00;
	}

	@FXML
	private void closeButtonAction(){
		Stage stage = (Stage) exitButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	void placeOrder(ActionEvent event) {
		String orderSize = pizzaNumber.getText();
		Pizza pizza;
		try {
			pizza = new Pizza(choiceBoxSize.getValue(), choiceBoxCheese.getValue(),
					choiceBoxMushrooms.getValue(), choiceBoxPepperoni.getValue());
			DecimalFormat df = new DecimalFormat("#.00");
			price.setText("$"  + df.format(pizza.getCost()));
			// Create LineItem so can invoke getCost() method that takes into account the discount
			LineItem order = new LineItem(Integer.parseInt(orderSize), pizza);
			orders.add(order.toString());
			cumulativeCost += order.getCost();
			printOrders(orders);
		} catch (IllegalPizza e) {
			e.printStackTrace();
		}
	}

	private void displayCost() {
		String orderSize = pizzaNumber.getText();
		if (orderSize.equalsIgnoreCase("")) // Set to 1 when there is no input
			orderSize = "1";
		Pizza pizza;
		try {
			pizza = new Pizza(choiceBoxSize.getValue(), choiceBoxCheese.getValue(),
					choiceBoxMushrooms.getValue(), choiceBoxPepperoni.getValue());
			DecimalFormat df = new DecimalFormat("#.00");
			price.setText("$"  + df.format(pizza.getCost()));
			// Create LineItem so can invoke getCost() method that takes into account the discount
			LineItem order = new LineItem(Integer.parseInt(orderSize), pizza);
			totalPrice.setText("$" + df.format(order.getCost()));
		} catch (IllegalPizza e) {
			e.printStackTrace();
		}
	}

	void printOrders(ArrayList<String> orders) {
		Iterator<String> it = orders.iterator();
		orderOutput.clear();
		String singleOrder;
		while (it.hasNext()) {
			singleOrder = it.next();
			orderOutput.appendText(singleOrder + "\n");
		}
		DecimalFormat df = new DecimalFormat("#.00");
		orderOutput.appendText("Total cost: $" + df.format(cumulativeCost));
	}

	@FXML
	void initialize() {
		// Initialize and listener for size
		choiceBoxSize.setItems(choiceListSize);
		choiceBoxSize.setValue("Small");
		choiceBoxSize.valueProperty().addListener((observableValue, oldVal, newVal) ->
		{
			displayCost(); // So cost updates with a change in size
		});

		// Initialize and listener for cheese
		choiceBoxCheese.setItems(choiceListCheese);
		choiceBoxCheese.setValue("Single");
		choiceBoxCheese.valueProperty().addListener((observableValue, oldVal, newVal) ->
		{
			displayCost(); // So cost updates with a change in cheese quantity
		});

		//Initialize and listener for pepperoni
		choiceBoxPepperoni.setItems(choiceListPepperoni);
		choiceBoxPepperoni.setValue("Single");
		choiceBoxPepperoni.valueProperty().addListener((observableValue, oldVal, newVal) ->
		{
			switch (newVal) {
			case "None" :
				choiceBoxMushrooms.setItems(choiceListMushroomsRestricted);
				choiceBoxMushrooms.setValue("None");
				displayCost();
				break;
			case "Single" :
				// Makes sure value of mushroom choiceBox is "None" if switching from restricted list
				if (choiceBoxMushrooms.getValue().equals("None")){
					choiceBoxMushrooms.setItems(choiceListMushrooms);
					choiceBoxMushrooms.setValue("None");
				}
				else
					choiceBoxMushrooms.setItems(choiceListMushrooms);
				displayCost();
				break;
			case "Double" :
				// Makes sure value of mushroom choiceBox is "None" if switching from restricted list
				if (choiceBoxMushrooms.getValue().equals("None")){
					choiceBoxMushrooms.setItems(choiceListMushrooms);
					choiceBoxMushrooms.setValue("None");
				}
				else
					choiceBoxMushrooms.setItems(choiceListMushrooms);
				displayCost();
			}
		});

		// Initialize and listener for mushrooms
		choiceBoxMushrooms.setItems(choiceListMushrooms);
		choiceBoxMushrooms.setValue("None");
		choiceBoxMushrooms.valueProperty().addListener((observableValue, oldVal, newVal) ->
		{
			// added to deal with an annoying error that passed mushrooms as null when selecting "None" for pepperoni
			if (newVal == null)
				choiceBoxMushrooms.setValue("None");
			displayCost(); // So cost updates with a change in mushroom quantity
		});

		displayCost(); // Displays cost of default pizza

		// Restrict "How many?" text box to only integers from 1-100, reverting to the previous
		// valid entry if an illegal entry is typed
		pizzaNumber.textProperty().addListener((observableValue, oldText, newText) ->
		{
			if (newText != null && !newText.isEmpty()) {
				try {
					int aVal = Integer.parseInt(newText);
					if (aVal < 1)
						throw new NumberFormatException();
					else if (aVal > 100)
						((StringProperty)observableValue).setValue("100");
					displayCost();
				} catch (NumberFormatException e) {
					((StringProperty)observableValue).setValue(oldText);
				}
			}
		});

		pizzaNumber.textProperty().addListener((observedValue, oldText, newText) -> 
		{
			if (newText == null || newText.equalsIgnoreCase("")) {
				displayCost();
				orderButton.setDisable(true);
			}
			else // As long as there is a value, it is valid and order button is enabled
				orderButton.setDisable(false);
		});
	}
}
