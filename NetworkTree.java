import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This is a tree class
 */
public class NetworkTree {
    // Store root
    private NetworkNode root;
    // Store cursor
    private NetworkNode cursor;

    /**
     * Move cursor to root
     */
    public void cursorToRoot()
    {
        cursor = root;
        System.out.println("Cursor move to "+root.getName());
    }

    /**
     * Cut subtree in cursor
     * @return cut node
     */
    public NetworkNode cutCursor()
    {
        NetworkNode cutNode = cursor;
        NetworkNode parent = cursor.getParent();
        cursor = cursor.getParent();
        if (parent!=null)
        {
            boolean isSuccess = parent.remove(cutNode);

            if (isSuccess)
            {
                System.out.println(cutNode.getName()+" cut, cursor is at "+ cursor.getName());
            }
        }
        return cutNode;
    }

    /**
     * add node to tree
     * @param index node position
     * @param node network node
     * @throws NodeHoleException throw exception
     */
    public void addChild(int index, NetworkNode node) throws NodeHoleException {
        if (root == null) {
            root = node;
        } else {
            node.setParent(cursor);
            cursor.addChildren(node, index - 1);
        }
        cursor = node;
    }

    /**
     * move cursor to child (given index child)
     * @param index child index
     */
    public void cursorToChild(int index) {
        cursor = cursor.getChildren()[index-1];
        System.out.println("Cursor move to "+cursor.getName());
    }

    /**
     * move cursor to it's parent
     */
    public void cursorToParent()
    {
        cursor = cursor.getParent();
        System.out.println("Cursor moved to "+ cursor.getName());
    }

    /**
     * Build network tree from text file
     * @param filename text file name
     * @return network tree
     */
    public static NetworkTree readFromFile(String filename) {
        NetworkTree tree = new NetworkTree();
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            int index = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (index == 0)
                    tree.root = new NetworkNode(data, false);
                else {
                    tree.cursor = tree.root;
                    int currentIndex = 0;
                    int prevIndex = Integer.parseInt(String.valueOf(data.charAt(0)));
                    boolean isNintendo = false;
                    String name = "";
                    for (int i = 1; i < data.length(); i++) {
                        char c = data.charAt(i);
                        if (Character.isDigit(c)) {
                            currentIndex = Integer.parseInt(String.valueOf(c));
                            tree.cursor = tree.cursor.getChildren()[prevIndex-1];

                            prevIndex = currentIndex;
                        } else if (c == '-') {
                            isNintendo = true;
                        } else {
                            name += c;
                        }
                    }
                    if (currentIndex == 0) {
                        currentIndex = prevIndex;
                    }
                    NetworkNode node = new NetworkNode(name, isNintendo);
                    tree.addChild(currentIndex,node);
                }
                index++;
            }
            tree.cursor = tree.root;
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(filename + " not found.");
        } catch (NodeHoleException e) {
            System.out.println(e.getMessage());
        }
        return tree;
    }

    /**
     * write given tree to text file
     * @param tree network tree
     * @param filename file name
     */
    public static void writeToFile(NetworkTree tree, String filename)
    {
        String s = tree.writeHelper(tree.root, "", "");
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(s);
            myWriter.close();
            System.out.println("File saved.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    /**
     * Cut minimal subtree which include all broken node
     */
    public void cursorToMinimalBrokenSubtree()
    {
        NetworkNode arr[]=new NetworkNode[9];
        minimalSubTreeHelper(root,arr);
        int total=0;
        for (int i = 0; i < arr.length; i++) {
            NetworkNode n = arr[i];
            if (n==null)
                break;

            total++;
        }
        if (total==0) {
            System.out.println("Empty broken nodes");
            return;
        }
        NetworkNode parent=arr[0];
        boolean isFound;
        while (parent != null)
        {
            isFound=true;
            for (int i = 1; i < total; i++) {
                if (!checkValidParentOrNot(arr[i],parent)) {
                    isFound = false;
                }
            }
            if (isFound)
                break;
            parent=parent.getParent();
        }
        if (parent==null)
            parent=root;
        cursor=parent;
        System.out.println("Cursor move to "+cursor.getName());
    }


    /**
     * Helper method for broken minimal sub tree
     * @param node current node
     * @param parent parent node
     * @return if current node parent is given parent node then return true else return false
     */
    private boolean checkValidParentOrNot(NetworkNode node, NetworkNode parent)
    {
        if (parent==node)
            return true;

        if (parent.getChildren()!=null)
        {
            for (int i = 0; i < parent.getChildren().length; i++) {
                NetworkNode child = parent.getChildren()[i];
                if (child==null)
                    continue;
                if (node==child)
                    return true;
                checkValidParentOrNot(node,child);
            }
        }
        return false;
    }


    /**
     * another helper method for broken minimal subtree
     * @param node current node
     * @param arr contain all parent node for broken node
     */
    private void minimalSubTreeHelper(NetworkNode node, NetworkNode[] arr) {
        if (node.getChildren() != null) {
            boolean broken = checkBrokenOrNot(node);
            if (broken) {
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i]==null)
                    {
                        arr[i] = node;
                        break;
                    }
                }

            }
            for (int i = 0; i < node.getChildren().length; i++) {
                NetworkNode child = node.getChildren()[i];
                if (child == null)
                    continue;
                minimalSubTreeHelper(child,arr);
            }
        }
    }

    /**
     * another helper method for broken minimal subtree
     * @param node current node
     * @return if any child node is broken then return true else return false
     */
    private boolean checkBrokenOrNot(NetworkNode node) {

        if (node.getChildren()!=null) {
            for (int i = 0; i < node.getChildren().length; i++) {
                NetworkNode child = node.getChildren()[i];
                if (child == null)
                    continue;
                if (child.isBroken()) {
                    return true;

                }
            }
        }

        return false;
    }

    /**
     * change broken status for cursor node
     */
    public void changeCursorBrokenStatus() {
        cursor.setBroken(!cursor.isBroken());
        if (cursor.isBroken())
            System.out.println(cursor.getName() + " marked as broken.");
        else
            System.out.println(cursor.getName() + " marked as not broken.");
    }

    /**
     * helper method for write file
     * @param node node
     * @param text output text
     * @param parent parent text
     * @return return string
     */
    private String writeHelper(NetworkNode node,String text,String parent)
    {
        if (node == root) {
            text+=node.getName()+"\n";
        }
        if (node.getChildren() != null)
            for (int i = 0; i < node.getChildren().length; i++) {
                NetworkNode child = node.getChildren()[i];
                if (child != null)
                {
                    String newPaent=parent+(i+1);
                    text+=newPaent+(child.isNintendo()?"-":"")+child.getName()+"\n";
                    text=writeHelper(child,text,newPaent);
                }
            }

        return text;
    }

    /**
     * print tree
     */
    public void printTree() {
        printHelperTree(root, 0);
    }

    /**
     * helper method for printing
     * @param node current node
     * @param tab tab index
     */
    private void printHelperTree(NetworkNode node, int tab) {
        for (int i = 0; i < tab; i++) {
            System.out.print("  ");
        }
        String mark="+";
        if (node.isNintendo())
            mark = "-";
        if (node == cursor)
            mark = "->";

        System.out.println(mark + node);

        if (node.getChildren() != null)
            for (int i = 0; i < node.getChildren().length; i++) {
                NetworkNode child = node.getChildren()[i];
                if (child != null)
                    printHelperTree(child, tab+1);
            }
    }


    /**
     * check tree is empty or not
     * @return return true is root is null else return false
     */
    public boolean isEmpty()
    {
        return root==null;
    }

}
