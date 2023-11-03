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
        System.out.println("2. refOb2: An object that contains references to a primOb and its parent, refOb1 (which has a reference to its child refOb2 and its own primOb)");
        System.out.println("3. <type>ArrayOb: An object that contains a primitive array, you can choose from several types.");
        System.out.println("4. ObjArrayOb: An object that contains an array of object references.");
        System.out.println("5. CollectionField: An object that uses an instance of one of Java\'s collection classes to refer to several other objects");
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

    public void showRefObVars(){
        System.out.println("You will need to create two primObs, one for refOb2 and one for refOb1");
    }

    public void showPrimArrayOptions(){
        System.out.println("You may choose one from the following types: ");
        System.out.println("1. Boolean");
        System.out.println("2. Integer");
        System.out.println("3. Short");
        System.out.println("4. Double");
        System.out.println("5. Character");
        System.out.println("Please enter the number that corresponds to the object you would like to create");
    }

    public void primArrayPrompt(){
        System.out.println("Please enter the elements of the array, separated by spaces");
    }

    //show custom input instructions for the other 4 object types here
}