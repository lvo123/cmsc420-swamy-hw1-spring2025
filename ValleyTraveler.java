/**
 * ValleyTraveler class represents a magical map that can identify and modify
 * valley points in the landscape of Numerica.
 * 
 * @author <Your Name>
 */
public class ValleyTraveler {

    // Node class for the doubly linked list
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

    // Instance variables
    private Node head; // Head of the doubly linked list
    private Node tail; // Tail of the doubly linked list
    private Node firstValley; // Pointer to the first valley node
    private double totalTreasure; // Total treasure collected
    private int size; // Size of the landscape

    /**
     * Constructor to initialize the magical map with the given landscape of Numerica.
     * 
     * @param landscape An array of distinct integers representing the landscape.
     */
    public ValleyTraveler(int[] landscape) {
        if (landscape == null || landscape.length == 0) {
            throw new IllegalArgumentException("Landscape cannot be null or empty.");
        }

        // Initialize the doubly linked list
        head = new Node(landscape[0]);
        Node current = head;
        for (int i = 1; i < landscape.length; i++) {
            Node newNode = new Node(landscape[i]);
            current.next = newNode;
            newNode.prev = current;
            current = newNode;
        }
        tail = current;

        // Initialize the first valley
        firstValley = findFirstValley();
        totalTreasure = 0.0;
        size = landscape.length;
    }

    /**
     * Finds the first valley in the landscape.
     * 
     * @return The first valley node.
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
     * Checks if a node is a valley.
     * 
     * @param node The node to check.
     * @return true if the node is a valley, false otherwise.
     */
    private boolean isValley(Node node) {
        if (node == null) return false;
        if (node.prev == null && node.next == null) return true; // Single node
        if (node.prev == null) return node.height < node.next.height; // First node
        if (node.next == null) return node.height < node.prev.height; // Last node
        return node.height < node.prev.height && node.height < node.next.height;
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
        return calculateTreasure(firstValley);
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

        double treasure = calculateTreasure(firstValley);
        totalTreasure += treasure;

        // Remove the first valley node
        if (firstValley.prev != null) {
            firstValley.prev.next = firstValley.next;
        } else {
            head = firstValley.next;
        }
        if (firstValley.next != null) {
            firstValley.next.prev = firstValley.prev;
        } else {
            tail = firstValley.prev;
        }

        size--;
        firstValley = findFirstValley();
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
            // If no valley, insert at the head
            newNode.next = head;
            if (head != null) {
                head.prev = newNode;
            }
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
        } else {
            // Insert before the first valley
            newNode.next = firstValley;
            newNode.prev = firstValley.prev;
            if (firstValley.prev != null) {
                firstValley.prev.next = newNode;
            } else {
                head = newNode;
            }
            firstValley.prev = newNode;
        }
        size++;
        firstValley = findFirstValley();
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
     * Calculates the treasure associated with a valley node.
     * 
     * @param node The valley node.
     * @return The treasure associated with the valley node.
     */
    private double calculateTreasure(Node node) {
        if (node == null) return 0.0;
        int sum = 0;
        int count = 0;
        Node current = head;
        while (current != null) {
            sum += current.height;
            count++;
            if (current == node) break;
            current = current.next;
        }
        return (double) sum / count;
    }
}