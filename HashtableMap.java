// == CS400 Spring 2024 File Header Information ==
// Name: Noah Berg
// Email: naberg3@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: N/A
import java.util.NoSuchElementException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
import java.util.LinkedList;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType>{
  
  private int capacity; // capacity of hashtable
  private int size;
  //hashtable (array of linked lists for chaining)
  protected LinkedList<Pair>[] table = null; 
  
  /**
   * Class to represent key-value pairs
   */
  protected class Pair {

    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
        this.key = key;
        this.value = value;
    }
  }
  
  // constructors
  public HashtableMap(int capacity) {
    this.capacity = capacity;
    // sets the table object to an array of linked lists
    this.table = (LinkedList<Pair>[])(new LinkedList[capacity]);
    // initializes the linked lists
    for (int i = 0; i < capacity;i++)
      table[i] = new LinkedList<Pair>();
  }
  //with default capacity = 64
  public HashtableMap() {
    this.capacity = 64;
    
    // sets the table object to an array of linked lists
    this.table = (LinkedList<Pair>[])(new LinkedList[capacity]);
    // initializes the linked lists
    for (int i = 0; i < capacity;i++)
      table[i] = new LinkedList<Pair>();    
  }
  
  /**
   * Helper method to expand the size of the array
   */
  private void expand() {
    Integer index; 
    // makes a new Array for table and updates capacity
    LinkedList<Pair>[] newTable = (LinkedList<Pair>[])(new LinkedList[this.capacity*2]);
    this.capacity = capacity*2;
    
    // initializes the linked lists
    for (int i = 0; i < capacity;i++)
      newTable[i] = new LinkedList<Pair>();
   
    // copies the values from table into the new Array with their updated hashes
    for (LinkedList<Pair> list : table) {     
      for (Pair p : list) {
        index  = Math.abs(p.key.hashCode())%capacity; // gets the index for the new key,value pair
        
        newTable[index].add(p); // adds the new pair to the correct linked list (chaining)
      }   
    }
    this.table = newTable; // updates the table object   
  }
  
  /**
   * Adds a new key,value pair/mapping to this collection.
   * @param key the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException if key is null
   */
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException{
    Integer index; 
    Pair toAdd;
    
    // verifies that the value is not null
    if (value == null)
      throw new NullPointerException("Value cannot be null");
    
    // verifies that the key is not in the table
    if (containsKey(key))
      throw new IllegalArgumentException("Table already contains that key");
    
    // gets the index to add at and the pair to add
    index  = Math.abs(key.hashCode())%capacity; // gets the index for the new key,value pair
    toAdd = new Pair(key,value); // creates the new pair object
      
    size++; // increment size
    table[index].add(toAdd); // adds the new pair to the correct linked list (chaining)  
    
    // check if the capacity should be increased
    if (Double.valueOf(this.size) / Double.valueOf(this.capacity) >= 0.8)
      expand();
  }

  /**
   * Checks whether a key maps to a value in this collection.
   * @param key the key to check
   * @return true if the key maps to a value, and false is the
   *         key doesn't map to a value
   */
  @Override
  public boolean containsKey(KeyType key) {
    // uses the formula to get the index of the linked lists
    int index = Math.abs(key.hashCode())%capacity;
    boolean contains = false;
    
    // searches through linked list to check if the key is present
    for (Pair p : table[index])
      if (p.key.equals(key))
        contains = true;
    
    return contains;
  }

  /**
   * Retrieves the specific value that a key maps to.
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException{
    // uses the formula to get the index of the linked lists
    int index = Math.abs(key.hashCode())%capacity;
    
    ValueType value = null;
    
    // searches through linked list to check if the key is present
    for (Pair p : table[index]) {
      //System.out.println(p.value);
      if (p.key.equals(key))
        value = p.value;
    }
    
    // if no value is found, throw exception
    if (value == null)
      throw new NoSuchElementException("This key is not stored in this collection");
    
    return value;
  }

  /**
   * Remove the mapping for a key from this collection.
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this
   *         collection
   */
  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException{
    // uses the formula to get the index of the linked list
    int index = Math.abs(key.hashCode())%capacity;
    // will hold the value in the removed pair
    ValueType removed = null;
    
    // look through every pair in the linked list
    for (int i = 0; i < table[index].size();i++) {
      if (table[index].get(i).key.equals(key))
        removed = table[index].remove(i).value;
    }
    
    // handles if there is no value of that key
    if (removed == null)
      throw new NoSuchElementException("That key is not used in this collection");
    
    size--; // decrement size if a value was removed
    return removed; 
  }

  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    // uses a for look to use the .clear() method on every linked list
    for (LinkedList<Pair> list : table) 
      list.clear();
    
    size = 0; // sets the size to 0
  }

  /**
   * Retrieves the number of keys stored in this collection.
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    return this.size;
  }

  /**
   * Retrieves this collection's capacity.
   * @return the size of te underlying array for this collection
   */
  @Override
  public int getCapacity() {
    return this.capacity;
  }
  
  // TESTS
  
  /*
   * Tests the put method
   */
/*
  @Test
  public void Test1() {
    // creates testing object
    HashtableMap<Integer,Integer> tester = new HashtableMap<Integer, Integer>(5);
    Assertions.assertEquals(tester.getCapacity(),5);
    tester.put(100, 1);
    tester.put(101, 2);
    tester.put(105, 3);
    tester.put(103, 4);
    tester.put(104, 5);
    
    // verifies that capacity was correctly updated
    Assertions.assertEquals(tester.getCapacity(), 10);
    // verifies that the hashtable contains the correct keys and also the correct values
    Assertions.assertTrue(tester.containsKey(100));
    Assertions.assertTrue(tester.containsKey(101));
    Assertions.assertTrue(tester.containsKey(105));
    Assertions.assertTrue(tester.containsKey(103));
    Assertions.assertTrue(tester.containsKey(104));
    Assertions.assertEquals(tester.get(100), 1);
    Assertions.assertEquals(tester.get(101), 2);
    Assertions.assertEquals(tester.get(105), 3);
    Assertions.assertEquals(tester.get(103), 4);
    Assertions.assertEquals(tester.get(104), 5);
    
    // verifies that adding a duplicate key results in an exception
    try {
      tester.put(100, 29);
      Assertions.fail();
    } catch (IllegalArgumentException e) {}
    
    // verifies that adding a null value results in an exception
    try {
      tester.put(100, null);
      Assertions.fail();
    } catch (NullPointerException e) {}
    
  }
*/
  /**
   * Tests that containsKey method
   */
/*  
@Test
  public void Test2() {
    // creates testing object
    HashtableMap<Integer,Integer> tester = new HashtableMap<Integer, Integer>(5);
    tester.put(100, 1);
    tester.put(101, 2);
    tester.put(105, 3);
    tester.put(103, 4);
    tester.put(104, 5);
    
    // tests keys that it should contain
    Assertions.assertTrue(tester.containsKey(100));
    Assertions.assertTrue(tester.containsKey(101));
    Assertions.assertTrue(tester.containsKey(105));
    Assertions.assertTrue(tester.containsKey(103));
    Assertions.assertTrue(tester.containsKey(104));
    // tests with keys that it should not hve
    Assertions.assertFalse(tester.containsKey(106));
    Assertions.assertFalse(tester.containsKey(107));
    Assertions.assertFalse(tester.containsKey(108));  
  }
*/  
/**
   * Tests the get method
   */
/*  
@Test
  public void Test3() {
    // creates testing object
    HashtableMap<Integer,Integer> tester = new HashtableMap<Integer, Integer>(5);
    tester.put(100, 1);
    tester.put(101, 2);
    tester.put(105, 3);
    tester.put(103, 4);
    tester.put(104, 5);
    
    // verifies that an exception is thrown when the hashtable does not contain a certain key
    try {
      tester.get(106);
      Assertions.fail();
    } catch (NoSuchElementException e) { }
    
    // verifies that the other keys are mapped to the correct values
    Assertions.assertEquals(tester.get(100), 1);
    Assertions.assertEquals(tester.get(101), 2);
    Assertions.assertEquals(tester.get(105), 3);
    Assertions.assertEquals(tester.get(103), 4);
    Assertions.assertEquals(tester.get(104), 5);  
  }
*/  
/**
   * Tests the remove method
   */
/*  
@Test
  public void Test4() {
    // creates testing object
    HashtableMap<Integer,Integer> tester = new HashtableMap<Integer, Integer>(5);
    tester.put(100, 1);
    tester.put(101, 2);
    tester.put(105, 3);
    tester.put(103, 4);
    tester.put(104, 5);
    
    // verifies that removing keys returns their values and that size is correctly updated
    Assertions.assertEquals(tester.remove(104),5);
    Assertions.assertEquals(tester.remove(103),4);
    Assertions.assertEquals(tester.getSize(), 3);
    
    // verifies that attempting to get the removed keys throws an exception
    try {
      tester.get(104);
      Assertions.fail();
    } catch (NoSuchElementException e) { }
  }
*/  
/**
   * Tests clear,getSize, and getCapacity methods
   */
/*  
@Test
  public void Test5() {
    // creates testing object
    HashtableMap<Integer,Integer> tester = new HashtableMap<Integer, Integer>(5);
    tester.put(100, 1);
    tester.put(101, 2);
    tester.put(105, 3);
    tester.put(103, 4);
    tester.put(104, 5);
    
    // verifies the that size and capacity are both correct
    Assertions.assertEquals(tester.getSize(), 5);
    Assertions.assertEquals(tester.getCapacity(), 10);
    // clears the hashtable and verifies that size is now 0
    tester.clear();
    Assertions.assertEquals(tester.getSize(), 0);    
  }
  
*/  
/**
   * Tests that capacity correctly updates at the right loading threshold
   */
/*  
@Test
  public void Test6() {
    // creates testing object
    HashtableMap<Integer,Integer> tester = new HashtableMap<Integer, Integer>(10);
    // inserts 8 values to trigger size increase
    tester.put(100, 1);
    tester.put(101, 2);
    tester.put(102, 3);
    tester.put(103, 4);
    tester.put(104, 5);
    tester.put(105, 6);
    tester.put(106, 7);
    tester.put(107, 8);

    // verifies that size is double to 20
    Assertions.assertEquals(tester.getCapacity(), 20); 
  }
*/
}
