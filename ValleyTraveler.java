/**
 * ValleyTraveler class represents a magical map that can identify and modify
 * valley points in the landscape of Numerica.
 * 
 * @author <Your Name>
 */
public class ValleyTraveler {

    // Instance variables to manage the landscape and collected treasures.
    private int[] landscape;
    private int size;
    private double totalTreasure;

    /**
     * Constructor to initialize the magical map with the given landscape of Numerica.
     * 
     * @param landscape An array of distinct integers representing the landscape.
     */
    public ValleyTraveler(int[] landscape) {
        this.landscape = new int[landscape.length];
        System.arraycopy(landscape, 0, this.landscape, 0, landscape.length);
        this.size = landscape.length;
        this.totalTreasure = 0.0;
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
        int index = findFirstValley();
        if (index == -1) {
            return -1; // No valley found
        }
        return calculateTreasure(index);
    }

    /**
     * Excavates the first valley point, removing it from the landscape of Numerica.
     * 
     * @return The treasure collected from the excavated valley point.
     */
    public double remove() {
        int index = findFirstValley();
        if (index == -1) {
            return -1; // No valley found
        }
        double treasure = calculateTreasure(index);
        totalTreasure += treasure;
        removeElement(index);
        return treasure;
    }

    /**
     * Creates a new landform at the position where the first valley was just removed.
     * 
     * @param height The height of the new landform.
     */
    public void insert(int height) {
        int index = findFirstValley();
        if (index == -1) {
            addElement(height);
        } else {
            addElementAtIndex(height, index);
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
     * Helper method to find the index of the first valley point in the landscape.
     * 
     * @return The index of the first valley point, or -1 if no valley is found.
     */
    private int findFirstValley() {
        if (size == 1) {
            return 0;
        }
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                if (landscape[i] < landscape[i + 1]) {
                    return i;
                }
            } else if (i == size - 1) {
                if (landscape[i] < landscape[i - 1]) {
                    return i;
                }
            } else {
                if (landscape[i] < landscape[i - 1] && landscape[i] < landscape[i + 1]) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Helper method to calculate the treasure associated with a valley point at a given index.
     * 
     * @param index The index of the valley point.
     * @return The treasure associated with the valley point.
     */
    private double calculateTreasure(int index) {
        double sum = 0;
        for (int i = 0; i <= index; i++) {
            sum += landscape[i];
        }
        return sum / (index + 1);
    }

    /**
     * Helper method to remove an element at a specific index from the landscape array.
     * 
     * @param index The index of the element to remove.
     */
    private void removeElement(int index) {
        for (int i = index; i < size - 1; i++) {
            landscape[i] = landscape[i + 1];
        }
        size--;
    }

    /**
     * Helper method to add an element at the end of the landscape array.
     * 
     * @param height The height of the new landform.
     */
    private void addElement(int height) {
        if (size >= landscape.length) {
            int[] newLandscape = new int[landscape.length + 1];
            System.arraycopy(landscape, 0, newLandscape, 0, landscape.length);
            landscape = newLandscape;
        }
        landscape[size] = height;
        size++;
    }

    /**
     * Helper method to add an element at a specific index in the landscape array.
     * 
     * @param height The height of the new landform.
     * @param index The index at which to insert the new landform.
     */
    private void addElementAtIndex(int height, int index) {
        if (size >= landscape.length) {
            int[] newLandscape = new int[landscape.length + 1];
            System.arraycopy(landscape, 0, newLandscape, 0, index);
            newLandscape[index] = height;
            System.arraycopy(landscape, index, newLandscape, index + 1, size - index);
            landscape = newLandscape;
        } else {
            for (int i = size; i > index; i--) {
                landscape[i] = landscape[i - 1];
            }
            landscape[index] = height;
        }
        size++;
    }
}