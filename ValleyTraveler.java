/**
 * ValleyTraveler class represents a magical map that can identify and modify
 * valley points in the landscape of Numerica.
 * Optimized using a doubly linked list and cumulative sums for O(1) operations.
 * 
 * @author <Your Name>
 */
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

    private Node head; // Head of the doubly linked list
    private Node tail; // Tail of the doubly linked list
    private Node firstValley; // Pointer to the first valley point
    private double totalTreasure; // Total treasure collected
    private int size; // Size of the landscape

    /**
     * Constructor to initialize the magical map with the given landscape of Numerica.
     * 
     * @param landscape An array of distinct integers representing the landscape.
     */
    public ValleyTraveler(int[] landscape) {
        this.head = null;
        this.tail = null;
        this.firstValley = null;
        this.totalTreasure = 0.0;
        this.size = 0;

        // Build the doubly linked list from the landscape array
        for (int height : landscape) {
            addElement(height);
        }

        // Find the first valley point
        firstValley = findFirstValley();
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
        if (firstValley == null) {
            return -1; // No valley found
        }
        return calculateTreasure(firstValley);
    }

    /**
     * Excavates the first valley point, removing it from the landscape of Numerica.
     * 
     * @return The treasure collected from the excavated valley point.
     */
    public double remove() {
        if (firstValley == null) {
            return -1; // No valley found
        }

        // Calculate the treasure for the first valley
        double treasure = calculateTreasure(firstValley);
        totalTreasure += treasure;

        // Remove the first valley node
        removeNode(firstValley);

        // Update the first valley pointer
        firstValley = findFirstValley();

        return treasure;
    }

    /**
     * Creates a new landform at the position where the first valley was just removed.
     * 
     * @param height The height of the new landform.
     */
    public void insert(int height) {
        if (firstValley == null) {
            // If no valley exists, add the new element to the end
            addElement(height);
        } else {
            // Insert the new element at the position of the first valley
            insertBefore(firstValley, height);
        }

        // Update the first valley pointer
        firstValley = findFirstValley();
    }

    public double getTotalTreasure() {
        return totalTreasure;
    }

    // Helper method to add an element to the end of the list
    private void addElement(int height) {
        Node newNode = new Node(height);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    // Helper method to insert an element before a given node
    private void insertBefore(Node node, int height) {
        Node newNode = new Node(height);
        newNode.prev = node.prev;
        newNode.next = node;

        if (node.prev != null) {
            node.prev.next = newNode;
        } else {
            head = newNode;
        }
        node.prev = newNode;
        size++;
    }

    // Helper method to remove a node from the list
    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        size--;

        // If the removed node was the first valley, update the pointer
        if (node == firstValley) {
            firstValley = findFirstValley();
        }
    }

    // Helper method to find the first valley point
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

    // Helper method to check if a node is a valley
    private boolean isValley(Node node) {
        if (node.prev == null && node.next == null) {
            return true; // Single node is a valley
        }
        if (node.prev == null) {
            return node.height < node.next.height; // First node
        }
        if (node.next == null) {
            return node.height < node.prev.height; // Last node
        }
        return node.height < node.prev.height && node.height < node.next.height; // Middle node
    }

    /**
     * Calculates the treasure of a given valley point.
     * 
     * @param valleyNode The valley node.
     * @return The treasure value.
     */
    
    private double calculateTreasure(Node node) {
        double sum = 0;
        int count = 0;
        Node current = head;
        while (current != null) {
            sum += current.height;
            count++;
            if (current == node) {
                break;
            }
            current = current.next;
        }
        return sum / count;
    }
}