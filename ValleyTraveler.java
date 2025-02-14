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
    private double firstValleyTreasure; // Cached treasure value for O(1) getFirst()
    private int size;

    public ValleyTraveler(int[] landscape) {
        if (landscape == null || landscape.length == 0) {
            throw new IllegalArgumentException("Landscape cannot be null or empty.");
        }

        head = new Node(landscape[0]);
        Node current = head;
        for (int i = 1; i < landscape.length; i++) {
            Node newNode = new Node(landscape[i]);
            current.next = newNode;
            newNode.prev = current;
            current = newNode;
        }
        tail = current;

        firstValley = findFirstValley();
        totalTreasure = 0.0;
        size = landscape.length;

        // Compute the initial treasure for firstValley
        firstValleyTreasure = calculateTreasure(firstValley);
    }

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

    private boolean isValley(Node node) {
        if (node == null) return false;
        if (node.prev == null && node.next == null) return true; // Single node
        if (node.prev == null) return node.height < node.next.height; // First node
        if (node.next == null) return node.height < node.prev.height; // Last node
        return node.height < node.prev.height && node.height < node.next.height;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public double getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("Landscape is empty.");
        }
        return firstValleyTreasure; // O(1) retrieval
    }

    public double remove() {
        if (isEmpty()) {
            throw new IllegalStateException("Landscape is empty.");
        }

        double treasure = firstValleyTreasure; // Use cached value
        totalTreasure += treasure;

        Node prevNode = firstValley.prev;
        Node nextNode = firstValley.next;

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

        // Update firstValley and its cached treasure
        firstValley = findNextValley(prevNode, nextNode);
        firstValleyTreasure = calculateTreasure(firstValley);

        return treasure;
    }

    private Node findNextValley(Node prev, Node next) {
        if (prev != null && isValley(prev)) return prev;
        if (next != null && isValley(next)) return next;

        Node current = next;
        while (current != null) {
            if (isValley(current)) return current;
            current = current.next;
        }
        return null;
    }

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

        size++;

        // Update firstValley if the new node is a valley
        if (isValley(newNode)) {
            firstValley = newNode;
        } else if (newNode.prev != null && isValley(newNode.prev)) {
            firstValley = newNode.prev;
        } else if (newNode.next != null && isValley(newNode.next)) {
            firstValley = newNode.next;
        } else {
            firstValley = findFirstValley(); // Fallback to linear search
        }

        firstValleyTreasure = calculateTreasure(firstValley); // Update cached value
    }

    public double getTotalTreasure() {
        return totalTreasure;
    }

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
