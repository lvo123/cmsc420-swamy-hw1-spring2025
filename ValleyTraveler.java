
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
    
        Node prevNode = firstValley.prev;
        Node nextNode = firstValley.next;
    
        // Remove the first valley node in O(1)
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
    
        size--;
    
        // Find the next valley efficiently
        firstValley = findNextValley(prevNode, nextNode);
    
        return treasure;
    }
    
    /**
     * Finds the next valley in O(1) whenever possible.
     * If no immediate valley exists, finds the closest one.
     */
    private Node findNextValley(Node prev, Node next) {
        if (prev != null && isValley(prev)) return prev;
        if (next != null && isValley(next)) return next;
    
        // Perform linear scan only if necessary
        Node current = next;
        while (current != null) {
            if (isValley(current)) return current;
            current = current.next;
        }
        return null; // No valley found
    }
    
    /**
     * Finds the next valley in O(1) by checking immediate neighbors or using a stored reference.
     * 
     * @param removed The node that was removed.
     * @return The next valley node or null if none exist.
     */
    private Node findNextValley(Node removed) {
        if (removed == null) return null;
        
        // Check immediate neighbors first
        if (removed.prev != null && isValley(removed.prev)) {
            return removed.prev;
        }
        if (removed.next != null && isValley(removed.next)) {
            return removed.next;
        }
        
        // If no immediate valleys, return the stored `nextValley`
        return removed.next;
    }
    

    /**
     * Creates a new landform at the position where the first valley was just removed.
     * 
     * @param height The height of the new landform.
     */
    public void insert(int height) {
        Node newNode = new Node(height);
    
        if (firstValley == null) {
            // If no valley exists, insert at the head in O(1)
            newNode.next = head;
            if (head != null) {
                head.prev = newNode;
            }
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
        } else {
            // Insert exactly at the removed valley's position in O(1)
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
    
        // Efficiently update `firstValley` in O(1)
        if (isValley(newNode)) {
            firstValley = newNode;
        } else if (newNode.prev != null && isValley(newNode.prev)) {
            firstValley = newNode.prev;
        } else if (newNode.next != null && isValley(newNode.next)) {
            firstValley = newNode.next;
        } else {
            firstValley = newNode.next; // Fallback
            while (firstValley != null && !isValley(firstValley)) {
                firstValley = firstValley.next;
            }
        }
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