package application;
import java.io.Serializable;
import java.lang.Math;

/**
 * This class describes a single line item as a part of a pizza order. It receives inputs for a
 * desired combination of pizza size and toppings as well as how many of the described pizza
 * the user wants to add to their order. Exceptions will be thrown until valid inputs are given.
 * The desired number of pizzas is mutable, but the Pizza attribute is not. 
 * 
 * <p>The line item is subject to a bulk discount, where 10 to 20 pizzas, inclusive, gets a 10%
 * discount, and more than 20 pizzas gets a 15% discount.
 * @author Declan Rowett
 * @version 1.2
 */

/* NOTE about the PizzaOrderSystem class:
 * If you try to remove an item when you have no items in your order you will be stuck in an
 * an endless loop since there are no legal inputs. I don't think there's a way to fix this
 * without changing the PizzaOrderSystem class itself. Adding the following to the beginning
 * of case 'r' : in getMenuOperation() will fix it:
 * if (orders.size() == 0) {
 * 	System.out.println("No items in the order!");
 * 	return false;
 * }
*/

public class LineItem implements Serializable, Comparable<LineItem> {

	private static final long serialVersionUID = -6056223886804451173L;
	private int number;
	private Pizza pizza = new Pizza();

	/**
	 * Single parameter constructor. Creates a single pizza.
	 * @param pizza The described pizza.
	 * @throws IllegalPizza If pizza arguments not legal.
	 */
	public LineItem(Pizza pizza) throws IllegalPizza {
		number = 1;
		setPizza(pizza);
	}

	/**
	 * Full parameter constructor. Creates between 1 and 100 of the desired pizza.
	 * @param number Number of pizzas in the line item.
	 * @param pizza The described pizza.
	 * @throws IllegalPizza If arguments not legal.
	 */
	public LineItem(int number, Object pizza) throws IllegalPizza {
		setNumber(number);
		setPizza(pizza);
	}

	// Method to set described pizza, given that all input parameters are legal.
	private void setPizza(Object pizza) throws IllegalPizza {
		if (pizza == null)
			throw new IllegalPizza("Illegal pizza!");
		this.pizza = (Pizza) pizza;
	}

	/**
	 * Sets the number of pizzas in the line item. Only accepts values between
	 * 1 and 100, inclusive.
	 * @param number Number of pizzas in the line item.
	 * @throws IllegalPizza If number of pizzas is below 1 or above 100.
	 */
	public void setNumber(int number) throws IllegalPizza {
		if (number < 1 || number > 100)
			throw new IllegalPizza("Illegal number of pizzas!");
		this.number = number;
	}

	/**
	 * Returns the created immutable Pizza object.
	 * @return Described pizza.
	 */
	public Pizza getPizza() {
		return pizza; // doesn't need to be cloned since Pizza object is immutable
	}

	/**
	 * Returns the number of pizzas in the line item.
	 * @return Number of pizzas in the line item.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Returns the total cost of the line item. Applies bulk discount as follows:
	 * <p>10 to 20 pizzas inclusive = 10% discount
	 * <p>More than 20 pizzas (up to 100) = 15% discount
	 * @return Total cost of line item, including applicable discounts.
	 */
	public double getCost() {
		double cost = pizza.getCost();
		return cost * number * discount();
	}

	// Method to calculate the bulk discount for a line item.
	private double discount() {
		double discount = 1;
		if (number >= 10 && number <= 20)
			discount = 0.90;
		else if (number > 20) // number already is never greater than 100
			discount = 0.85;
		return discount;
	}

	/**
	 * A String representation of the current object. Formatted as:<p>
	 * <code>number</code> <code>size</code> pizza, <code>quantity</code> cheese, 
	 * <code>quantity</code> mushrooms, <code>quantity</code> pepperoni. Cost: $
	 * <code>cost</code> each.
	 * @return A String representation of the contents of the object containing the values of
	 * all the attributes.
	 */
	public String toString() {
		String ifSingleDigit = "";
		if (number < 10)
			ifSingleDigit = " ";
		String s = ifSingleDigit + number + " " + pizza.toString();
		return s;
	}

	/**
	 * Compares line items based solely on their total costs. Returns zero if the cost difference is less than one dollar.
	 */
	@Override
	public int compareTo(LineItem otherPizza) {
		// cost diffence of less than $1.00 is considered equal
		if((Math.abs(otherPizza.getCost() - pizza.getCost() * number)) < 1.00)
			return 0;
		else if (((otherPizza.getCost()) < (pizza.getCost()) * number))
			return -1;
		else
			return 1;
	}

}
