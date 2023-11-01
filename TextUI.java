import java.util.Scanner;
public class TextUI {
    /**
	 * chooseIntOption is used when we need the user to enter an integer as input.
	 * @return
	 */
	public int chooseIntOption(int x, int y) {
		Scanner keyboard = new Scanner(System.in);
        int pickedOption = -99;
        while (!keyboard.hasNextInt()){
            keyboard.next();
            System.out.println("Please enter a number: ");
        }
        pickedOption = keyboard.nextInt();

		while (!(pickedOption >= x && pickedOption <= y)){
            System.out.println("please enter a valid choice.");
            while (!keyboard.hasNextInt()){
				keyboard.next();
				System.out.println("Please enter a number: ");
			}
		    pickedOption = keyboard.nextInt();
        }
		
		return pickedOption;
	}

    /**
	 * chooseStringOption is used when we need the user to enter a string as input.
	 * @return
	 */
	public String chooseStringOption() {
		Scanner keyboard = new Scanner(System.in);
		String pickedOption = keyboard.next();
        pickedOption+=keyboard.nextLine();
		return pickedOption;
	}

    public void showObjOptions(){
        System.out.println("You may choose to create one of the following objects:");
        System.out.println("1. primOb: A simple object with only primitives for instance variables.");
        System.out.println("2. placeholder: An object that contains references to other objects.");
        System.out.println("3. placeholder: An object that contains an array of primitives.");
        System.out.println("4. placeholder: An object that contains an array of object references.");
        System.out.println("5. placeholder: An object that uses an instance of one of Java\'s collection classes to refer to several other objects");
        System.out.println("Please enter the number that corresponds to the object you would like to create");
    }

    public void showCreationOptions(){
        System.out.println("You may choose to create this object with default values, or enter your own values:");
        System.out.println("1. Default Values");
        System.out.println("2. Custom Values");
        System.out.println("Please enter the number that corresponds to your choice");
    }

    public void showPrimObVars(){
        System.out.println("A primOb requires 3 values: An Integer, A Double, and A Boolean");
        System.out.println("Please enter these below on one line, separated by spaces.");
    }

    //show custom input instructions for the other 4 object types here

    public void goAgain(){
        System.out.println("Would you like to create another object?");
        System.out.println("Please enter 1 for yes or 2 for no");
    }
}