/**
 *  A DList is a mutable doubly-linked list ADT.  Its implementation is
 *  circularly-linked and employs a sentinel (dummy) node at the head
 *  of the list.
 */
import java.net.InetAddress;

public class DList {

  /**
   *  head references the sentinel node.
   *  size is the number of items in the list.  (The sentinel node does not
   *       store an item.)
   */

  protected DListNode head;
  protected int size;

  /**
   *  newNode() calls the DListNode constructor.  
   *  @param item the item to store in the node.
   *  @param prev the node previous to this node.
   *  @param next the node following this node.
   */
  protected DListNode newNode(InetAddress item, Long expiry, DListNode prev, DListNode next) {
    return new DListNode(item, expiry, prev, next);
  }

  /**
   *  DList() constructor for an empty DList.
   */
  public DList() {
    head = newNode(null, null, null, null);
    head.next = head;
    head.prev = head;
    size = 0;
  }

  /**
   *  isEmpty() returns true if this DList is empty, false otherwise.
   *  @return true if this DList is empty, false otherwise. 
   *  Performance:  runs in O(1) time.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /** 
   *  length() returns the length of this DList. 
   *  @return the length of this DList.
   *  Performance:  runs in O(1) time.
   */
  public int length() {
    return size;
  }

  /**
   *  insertFront() inserts an item at the front of this DList.
   *  @param item is the item to be inserted.
   *  Performance:  runs in O(1) time.
   */
  public void insertFront(InetAddress item, Long expiry) {
    DListNode node = newNode(item, expiry, head, head.next);
    head.next.prev = node;
    head.next = node;
    size++;
  }

  /**
   *  insertBack() inserts an item at the back of this DList.
   *  @param item is the item to be inserted.
   *  Performance:  runs in O(1) time.
   */
  public void insertBack(InetAddress item, Long expiry) {
    DListNode node = newNode(item, expiry, head.prev, head);
    head.prev.next = node; 
    head.prev = node;
    size++;
  }

  /**
   *  front() returns the node at the front of this DList.  If the DList is
   *  empty, return null.
   *
   *  @return the node at the front of this DList.
   *  Performance:  runs in O(1) time.
   */
  public DListNode front() {
    if (size == 0){
      return null;
    } else{
      return head.next;
    }
  }

  /**
   *  back() returns the node at the back of this DList.  If the DList is
   *  empty, return null.
   *
   *  @return the node at the back of this DList.
   *  Performance:  runs in O(1) time.
   */
  public DListNode back() {
    if (size == 0){
      return null;
    } else{
      return head.prev;
    }
  }

  /**
   *  next() returns the node following "node" in this DList.  If "node" is
   *  null, or "node" is the last node in this DList, return null.
   *
   *  @param node the node whose successor is sought.
   *  @return the node following "node".
   *  Performance:  runs in O(1) time.
   */
  public DListNode next(DListNode node) {
    if (node == null || node.next == head){
      return null;
    } else{
      return node.next;
    }
  }

  /**
   *  prev() returns the node prior to "node" in this DList.  If "node" is
   *  null, or "node" is the first node in this DList, return null.
   *
   *  @param node the node whose predecessor is sought.
   *  @return the node prior to "node".
   *  Performance:  runs in O(1) time.
   */
  public DListNode prev(DListNode node) {
    if (node == null || node.prev == head){
      return null;
    } else {
      return node.prev;
    }
  }

  /**
   *  insertAfter() inserts an item in this DList immediately following "node".
   *  If "node" is null, do nothing.
   *  @param item the item to be inserted.
   *  @param node the node to insert the item after.
   *  Performance:  runs in O(1) time.
   */
  public void insertAfter(InetAddress item, Long expiry, DListNode node) {
    if (node != null) {
      DListNode insert = newNode(item, expiry, node, node.next);
      node.next.prev = insert;
      node.next = insert;
      size++;
    }
  }

  /**
   *  insertBefore() inserts an item in this DList immediately before "node".
   *  If "node" is null, do nothing.
   *  @param item the item to be inserted.
   *  @param node the node to insert the item before.
   *  Performance:  runs in O(1) time.
   */
  public void insertBefore(InetAddress item, Long expiry, DListNode node) {
    if (node != null) {
      DListNode insert = newNode(item, expiry, node.prev, node);
      node.prev.next = insert;
      node.prev = insert;
      size++;
    } 
  }

  /**
   *  remove() removes "node" from this DList.  If "node" is null, do nothing.
   *  Performance:  runs in O(1) time.
   */
  public void remove(DListNode node) {
    if (node != null) {
      node.prev.next = node.next;
      node.next.prev = node.prev;
      size--;
    } 
  }

  /**
   *  pop() removes first item from this DList like a stack. 
   *  Performance:  runs in O(1) time.
   */
  public DListNode pop(){
    DListNode top = this.front();
    this.remove(top);
    size--;
    return top;
  }

  /**
   *  push() inserts a "node" into the front of the DList like a stack.
   *  Performance:  runs in O(1) time.
   */
  public DListNode push(InetAddress item, Long expiry) {
    DListNode node = newNode(item, expiry, head, head.next);
    head.next.prev = node;
    head.next = node;
    size++;
    return node;
  }

  /**
  *  Same as front()
  */
  public DListNode peek(){
    return this.front();
  }

  /**
   *  toString() returns a String representation of this DList
   *
   *  @return a String representation of this DList.
   *  Performance:  runs in O(n) time, where n is the length of the list.
   */
  public String toString() {
    String result = "[  ";
    DListNode current = head.next;
    while (current != head) {
      result = result + current.address + "  ";
      current = current.next;
    }
    return result + "]";
  }
}
