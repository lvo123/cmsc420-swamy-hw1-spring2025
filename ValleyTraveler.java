public class ValleyTraveler {

    private static class Node {
        int height;
        Node prev;
        Node next;

        Node(int height) {
            this.height = height;
            this.prev = null;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private Node firstValley;
    private double totalTreasure;
    private double firstValleyTreasure;
    private int size;
    private int totalHeightSum;

    // Additional variables for O(1) treasure calculation
    private int sumUpToFirstValley;
    private int countUpToFirstValley;

    /**
     * Constructor to initialize the magical map with the given landscape of Numerica.
     * 
     * @param landscape An array of distinct integers representing the landscape.
     */
    public ValleyTraveler(int[] landscape) {
        if (landscape == null || landscape.length == 0) {
            throw new IllegalArgumentException("Landscape cannot be null or empty.");
        }

        head = new Node(landscape[0]);
        totalHeightSum = landscape[0];
        size = 1;

        Node current = head;
        for (int i = 1; i < landscape.length; i++) {
            Node newNode = new Node(landscape[i]);
            current.next = newNode;
            newNode.prev = current;
            current = newNode;

            totalHeightSum += landscape[i];
            size++;
        }
        tail = current;

        firstValley = findFirstValley();
        updateSumAndCount(); // Initialize sum and count up to first valley
        firstValleyTreasure = calculateTreasure();
    }

    /**
     * Checks if the entire landscape is excavated (i.e., there are no landforms left).
     * 
     * @return true if the landscape is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Locates the first valley point in the landscape of Numerica.
     * 
     * @return The treasure associated with the first valley point.
     */
    public double getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("Landscape is empty.");
        }
        return firstValleyTreasure;
    }

    /**
     * Excavates the first valley point, removing it from the landscape of Numerica.
     * 
     * @return The treasure collected from the excavated valley point.
     */
    public double remove() {
        if (isEmpty()) {
            throw new IllegalStateException("Landscape is empty.");
        }

        double treasure = firstValleyTreasure;
        totalTreasure += treasure;

        Node prevNode = firstValley.prev;
        Node nextNode = firstValley.next;

        // Remove the first valley node
        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            head = nextNode;
        }
        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            tail = prevNode;
        }

        totalHeightSum -= firstValley.height;
        size--;

        // Update the first valley
        if (prevNode != null && isValley(prevNode)) {
            firstValley = prevNode;
        } else if (nextNode != null && isValley(nextNode)) {
            firstValley = nextNode;
        } else {
            firstValley = findFirstValley();
        }

        // Update sum and count up to the new first valley
        updateSumAndCount();

        // Recalculate the treasure
        firstValleyTreasure = calculateTreasure();

        return treasure;
    }

    /**
     * Creates a new landform at the position where the first valley was just removed.
     * 
     * @param height The height of the new landform.
     */
    public void insert(int height) {
        Node newNode = new Node(height);

        if (firstValley == null) {
            newNode.next = head;
            if (head != null) {
                head.prev = newNode;
            }
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
        } else {
            newNode.next = firstValley;
            newNode.prev = firstValley.prev;
            if (firstValley.prev != null) {
                firstValley.prev.next = newNode;
            } else {
                head = newNode;
            }
            firstValley.prev = newNode;
        }

        totalHeightSum += height;
        size++;

        // Check if the new node is a valley
        if (isValley(newNode)) {
            firstValley = newNode;
        } else if (newNode.prev != null && isValley(newNode.prev)) {
            firstValley = newNode.prev;
        } else if (newNode.next != null && isValley(newNode.next)) {
            firstValley = newNode.next;
        } else {
            firstValley = findFirstValley();
        }

        // Update sum and count up to the new first valley
        updateSumAndCount();

        // Recalculate the treasure
        firstValleyTreasure = calculateTreasure();
    }

    /**
     * Returns the current total treasure collected through successive remove operations.
     * 
     * @return The total treasure collected.
     */
    public double getTotalTreasure() {
        return totalTreasure;
    }

    /**
     * Finds the first valley node in the landscape.
     * 
     * @return The first valley node, or null if no valley exists.
     */
    private Node findFirstValley() {
        Node current = head;
        while (current != null) {
            if (isValley(current)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Checks if a given node is a valley.
     * 
     * @param node The node to check.
     * @return true if the node is a valley, false otherwise.
     */
    private boolean isValley(Node node) {
        if (node == null) return false;
        boolean isLeftValley = node.prev == null || node.height < node.prev.height;
        boolean isRightValley = node.next == null || node.height < node.next.height;
        return isLeftValley && isRightValley;
    }

    /**
     * Updates the sum and count of nodes up to the first valley.
     */
    private void updateSumAndCount() {
        sumUpToFirstValley = 0;
        countUpToFirstValley = 0;
        if (firstValley != null) {
            Node current = head;
            while (current != firstValley.next) {
                sumUpToFirstValley += current.height;
                countUpToFirstValley++;
                current = current.next;
            }
        }
    }

    /**
     * Calculates the treasure for the first valley in O(1) time.
     * 
     * @return The treasure value.
     */
    private double calculateTreasure() {
        if (firstValley == null || countUpToFirstValley == 0) {
            return 0.0;
        }
        return (double) sumUpToFirstValley / countUpToFirstValley;
    }
}