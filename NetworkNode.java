/**
 * This class represent node (Raspberry,Nintendo) in network tree
 */
public class NetworkNode {
    // store network node name
    private String name;
    // store nintendo or not
    private boolean isNintendo;
    // store broken or not
    private boolean isBroken;
    // store parent node
    private NetworkNode parent;
    // store children nodes
    private NetworkNode[] children;
    // maximum children count
    private final int maxChildren = 9;

    /**
     * Constructor for initializing fields
     * @param name node name
     * @param isNintendo store nintendo or not
     */
    public NetworkNode(String name, boolean isNintendo) {
        this.name = name;
        this.isNintendo = isNintendo;
        if (!isNintendo)
            children = new NetworkNode[maxChildren];
    }

    /**
     * get method for name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set method for name
     * @param name node name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get method for isNintendo
     * @return isNintendo
     */
    public boolean isNintendo() {
        return isNintendo;
    }

    /**
     * set method for isNintendo
     * @param nintendo is nintendo or not
     */
    public void setNintendo(boolean nintendo) {
        isNintendo = nintendo;
    }

    /**
     * get method for isBroken
     * @return isBroken
     */
    public boolean isBroken() {
        return isBroken;
    }

    /**
     * set method for broken
     * @param broken is broken or not
     */
    public void setBroken(boolean broken) {
        isBroken = broken;
    }

    /**
     * get method for parent
     * @return parent
     */
    public NetworkNode getParent() {
        return parent;
    }

    /**
     * set method for parent
     * @param parent parent node
     */
    public void setParent(NetworkNode parent) {
        this.parent = parent;
    }

    /**
     * get method for children
     * @return children
     */
    public NetworkNode[] getChildren() {
        return children;
    }

    /**
     * set method for children
     * @param children children
     */
    public void setChildren(NetworkNode[] children)  {
        this.children = children;
    }

    /**
     * helper method for add children
     */
    public void addChildren(NetworkNode node, int position) throws NodeHoleException {
        if (node==null)
            throw new NullPointerException("Can't add null node");
        if (position >= maxChildren)
            throw new ArrayIndexOutOfBoundsException("Can't add children");

        for (int i = 0; i < position; i++) {
            if (children[i]==null)
                throw new NodeHoleException("ERROR. Hole exception");
        }

        if (children[position] != null) {
            NetworkNode currNode = children[position];
            children[position]=node;
            for (int i = position + 1; i < maxChildren; i++) {
                NetworkNode nxtNode = children[i];
                children[i]=currNode;
                currNode=nxtNode;
            }
        }
        else
        {
            children[position]=node;
        }
    }

    /**
     * helper method for remove children
     * @return removed success or not
     */
    public boolean remove(NetworkNode node)
    {
        if (node == null)
            throw new NullPointerException("Can't remove null node");
        int position = -1;
        for (int i = 0; i < maxChildren; i++) {
            if (children[i] == node) {
                position = i;
                break;
            }
        }
        for (int i = position+1; i < maxChildren; i++) {
            children[i-1]=children[i];
        }
        return true;
    }


    /**
     * used for printing purpose
     * @return string
     */
    public String toString() {
        String broken = isBroken?" ~Fault~":"";
        return name+broken;
    }
}
