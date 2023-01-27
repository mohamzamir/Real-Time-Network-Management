import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class for this application
 */
public class NintendoNetwork {
    // Store tree
    private static NetworkTree tree;
    // Store cut node
    private static NetworkNode cutNode=null;
    // Store scanner object
    private static Scanner sc;

    /**
     * main method for this program
     */
    public static void main(String[] args) {
        tree=new NetworkTree();
        sc=new Scanner(System.in);
        boolean isQuit=false;
        printMenu();
        while (!isQuit)
        {

            System.out.print("Please select an option: ");
            String option = sc.nextLine();
            switch (option.toUpperCase()) {
                case "L":
                    loadFromFile();
                    break;
                case "P":
                    printTree();
                    break;
                case "C":
                    moveCursorToChild();
                    break;
                case "R":
                    moveCursorToRoot();
                    break;
                case "U":
                    tree.cursorToParent();
                    break;
                case "A":
                    addChild();
                    break;
                case "X":
                    cutNode = tree.cutCursor();
                    break;
                case "V":
                    pasteCursor();
                    break;
                case "S":
                    saveFile();
                    break;
                case "M":
                    minimalSubtree();
                    break;
                case "B":
                    tree.changeCursorBrokenStatus();
                    break;
                case "Q":
                    isQuit=true;
                    System.out.println("Make like a tree and leave!");
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }
    }

    /**
     * method for user input 'M'
     */
    private static void minimalSubtree() {
        tree.cursorToMinimalBrokenSubtree();
    }

    /**
     * method for user input 'S'
     */
    private static void saveFile() {
        System.out.print("Please enter a filename: ");
        String fileName=sc.nextLine();
        NetworkTree.writeToFile(tree,fileName);
    }

    /**
     * method for user input 'V'
     */
    private static void pasteCursor() {
        if (cutNode==null)
        {
            System.out.println("Sorry there no node available for paste");
            return;
        }
        System.out.print("Please enter an index: ");
        int index = getIntegerInput();
        try {
            tree.addChild(index, cutNode);
            System.out.println(cutNode.getName()+" pasted as child of "+cutNode.getParent().getName());
            cutNode=null;
        } catch (NodeHoleException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * print menu
     */
    private static void printMenu()
    {
        System.out.println("L) Load from file");
        System.out.println("P) Print tree");
        System.out.println("C) Move cursor to a child node");
        System.out.println("R) Move cursor to root");
        System.out.println("U) Move cursor up to parent");
        System.out.println("A) Add a child");
        System.out.println("X) Remove/Cut Cursor and its subtree");
        System.out.println("V) Paste Cursor and its subtree");
        System.out.println("S) Save to file");
        System.out.println("M) Cursor to root of minimal subtree containing all faults");
        System.out.println("B) Mark cursor as broken/fixed");
        System.out.println("Q) Quit");
    }

    /**
     * method for user input 'A'
     */
    private static void addChild() {
        int index=1;
        if (tree!=null&&!tree.isEmpty()) {
            System.out.print("Please enter an index: ");
            index = getIntegerInput();
        }
        System.out.print("Please enter device name: ");
        String name = sc.nextLine();
        System.out.print("Is this Nintendo (y/n): ");
        String isNintendoTxt = sc.nextLine();
        boolean isNintendo=false;
        if (isNintendoTxt.equalsIgnoreCase("y"))
            isNintendo=true;
        NetworkNode node=new NetworkNode(name,isNintendo);
        try {
            tree.addChild(index,node);
            System.out.println((node.isNintendo()?"Nintendo":"Raspberry")+" added.");
        } catch (NodeHoleException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method for user input 'L'
     */
    private static void loadFromFile()
    {
        System.out.print("Please enter filename: ");
        String fileName = sc.nextLine();
        tree = NetworkTree.readFromFile(fileName);

    }

    /**
     * method for user input 'R'
     */
    private static void moveCursorToRoot() {
        tree.cursorToRoot();
    }

    /**
     * method for user input 'C'
     */
    private static void moveCursorToChild() {
        System.out.print("Please enter an index: ");
        int index = getIntegerInput();
        tree.cursorToChild(index);
    }

    /**
     * method for user input 'P'
     */
    private static void printTree() {
        if (tree.isEmpty())
        {
            System.out.println("Tree is empty");
        }
        else {
            tree.printTree();
        }
    }

    /**
     * get integer input from user
     */
    private static int getIntegerInput()
    {
        while (true)
        {
            try
            {
                int n = sc.nextInt();
                sc.nextLine();
                return n;
            }
            catch (InputMismatchException e)
            {
                sc.nextLine();
                System.out.println("Invalid input please re enter");
            }
        }
    }
}
