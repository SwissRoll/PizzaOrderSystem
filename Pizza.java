package application;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * This class describes a single immutable Pizza object and was designed to be used as a part of
 * a pizza ordering system.
 * It receives String inputs for four parameters of a pizza:
 * <p><code>- size	</code>	(small, medium, or large)
 * <p><code>- cheese	</code>	(single, double, or triple)
 * <p><code>- mushrooms	</code>	(none, single, or double)
 * <p><code>- pepperoni	</code>	(none, single, or double)
 * <p>Exceptions will be thrown until a valid input is given for each parameter. The only restriction
 * to combinations of valid inputs of the above parameters is that a pizza cannot have mushrooms
 * (single/double) without also having pepperoni (single/double). This is a matter of company policy,
 * unfortunately.
 * @author Declan Rowett
 * @version 1.3
 *
 */

public class Pizza implements Serializable {

	private static final long serialVersionUID = -8581376484799111291L;
	private String size; // small/medium/large
	private String cheese; // single/double/triple
	private String mushrooms; // none/single/double
	private String pepperoni; // none/single/double
	private double cost;

	/**
	 * Default constructor. Creates a small pizza with single cheese, single pepperoni, and no mushrooms.
	 */
	public Pizza() {
		size = "small";
		cheese = "single";
		mushrooms = "none";
		pepperoni = "single";
	}

	/**
	 * Full parameter constructor. Note: can't have mushrooms without also having pepperoni.
	 * @param size The size of the pizza, either small, medium, or large.
	 * @param cheese The amount of cheese, either single, double, or triple.
	 * @param mushrooms The amount of mushrooms, either none, single, or double.
	 * @param pepperoni The amount of pepperoni, either none, single, or double.
	 * @throws IllegalPizza If arguments not legal.
	 */
	public Pizza(String size, String cheese, String mushrooms, String pepperoni) throws IllegalPizza {
		setSize(size);
		setCheese(cheese);
		setPepMush(mushrooms, pepperoni);
	}

	// Sets the size of the pizza to exactly either small, medium, or large.
	private void setSize(String size) throws IllegalPizza {
		if (size == null)
			throw new IllegalPizza("Size error: " + size);
		if (size.equalsIgnoreCase("small") || size.equalsIgnoreCase("medium") || size.equalsIgnoreCase("large"))
			this.size = size.toLowerCase();
		else
			throw new IllegalPizza("Illegal size: " + size);
	}

	// Sets the quantity of cheese to exactly equal single, double, or triple.
	private void setCheese(String cheese) throws IllegalPizza {
		if (cheese == null)
			throw new IllegalPizza("Cheese error: " + cheese);
		if (cheese.equalsIgnoreCase("single") || cheese.equalsIgnoreCase("double") || cheese.equalsIgnoreCase("triple"))
			this.cheese = cheese.toLowerCase();
		else
			throw new IllegalPizza("Illegal cheese quantity: " + cheese);
	}

	// Sets pepperoni and mushroom quantities. Together in a setter to comply with company policy
	// of no Pizza with mushrooms without pepperoni. Both can be either exactly none, single, or double.
	private void setPepMush(String mushrooms, String pepperoni) throws IllegalPizza {
		if (mushrooms == null)
			throw new IllegalPizza("Mushrooms error: " + mushrooms);
		if (pepperoni == null)
			throw new IllegalPizza("Pepperoni error: " + pepperoni);
		if (pepperoni.equalsIgnoreCase("none") && (mushrooms.equalsIgnoreCase("single") || mushrooms.equalsIgnoreCase("double")))
			throw new IllegalPizza("Can't have mushrooms without pepperoni. Company policy!");
		if (pepperoni.equalsIgnoreCase("none") || pepperoni.equalsIgnoreCase("single") || pepperoni.equalsIgnoreCase("double"))
			this.pepperoni = pepperoni.toLowerCase();
		else
			throw new IllegalPizza("Illegal pepperoni quantity: " + pepperoni);
		if (mushrooms.equalsIgnoreCase("none") || mushrooms.equalsIgnoreCase("single") || mushrooms.equalsIgnoreCase("double"))
			this.mushrooms = mushrooms.toLowerCase();
		else
			throw new IllegalPizza("Illegal mushroom quantity: " + mushrooms);
	}

	/**
	 * Returns the cost of the pizza based on the type and number of toppings. Extra cheese is considered a topping.
	 * @return Total cost of the pizza created.
	 */
	public double getCost() {
		if (size.equalsIgnoreCase("small")) {
			cost = 7.00 + priceAdjustment();
		}
		else if (size.equalsIgnoreCase("medium")) {
			cost = 9.00 + priceAdjustment();
		}
		else if (size.equalsIgnoreCase("large")) {
			cost = 11.00 + priceAdjustment();
		}
		return cost;
	}

	// Method to account for price adjustments due to extra toppings.
	private double priceAdjustment() {
		double toAdd = 0;
		if (cheese.equalsIgnoreCase("double"))
			toAdd += 1.50;
		else if (cheese.equalsIgnoreCase("triple"))
			toAdd += 3.00;
		if (mushrooms.equalsIgnoreCase("single"))
			toAdd += 1.50;
		else if (mushrooms.equalsIgnoreCase("double"))
			toAdd += 3.00;
		if (pepperoni.equalsIgnoreCase("single"))
			toAdd += 1.50;
		else if (pepperoni.equalsIgnoreCase("double"))
			toAdd += 3.00;
		return toAdd;
	}

	/**
	 * A String representation of the current object. Formatted as:<p>
	 * <code>size</code> pizza, <code>quantity</code> cheese, <code>quantity</code>
	 * mushrooms, <code>quantity</code> pepperoni. Cost: $<code>cost</code> each.
	 * @return A String representation of the contents of the object containing the values of
	 * all the attributes and the cost.
	 */
	@Override
	public String toString() {
		if (mushrooms.equalsIgnoreCase("none"))
			mushrooms = "no";
		if (pepperoni.equalsIgnoreCase("none"))
			pepperoni = "no";
		DecimalFormat df = new DecimalFormat("#.00");
		String price;
		price = df.format(getCost());
		String s = size + " pizza, " + cheese + " cheese, " + mushrooms +
				" mushrooms, " + pepperoni + " pepperoni. Cost: $" + price + " each.";
		return s;
	}

	/**
	 * Determines if two Pizza objects are equal to each other, where basis for equality is
	 * all attributes being identical.
	 * @param pizza Pizza object described by input parameters.
	 */
	public boolean equals(Object pizza) {
		if (pizza instanceof Pizza) {
			Pizza otherP = (Pizza)pizza;
			return size.equals(otherP.size) && cheese.equals(otherP.cheese) &&
					pepperoni.equals(otherP.pepperoni) && mushrooms.equals(otherP.mushrooms);
		}
		return false;
	}

	/**
	 * Creates a deep copy of the created Pizza object.
	 * @return Deep copy of Pizza object.
	 */
	public Pizza clone() {
		Pizza pCopy = null;
		try {
			pCopy = new Pizza(size, cheese, mushrooms, pepperoni);
		} catch (IllegalPizza e) {
			return null; // will never reach here
		}
		return pCopy;
	}

}
