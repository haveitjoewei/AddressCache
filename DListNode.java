/**
 *  A DListNode is a node in a DList (doubly-linked list).
 */

import java.net.InetAddress;

public class DListNode {

  /**
   *  item references the item stored in the current node.
   *  prev references the previous node in the DList.
   *  next references the next node in the DList.
   *
   */

  public InetAddress address;
  public Long expiry;
  protected DListNode prev;
  protected DListNode next;

  /**
   *  DListNode() constructor.
   *  @param i the item to store in the node.
   *  @param p the node previous to this node.
   *  @param n the node following this node.
   */
  DListNode(InetAddress i, Long e, DListNode p, DListNode n) {
    address = i;
    expiry = e;
    prev = p;
    next = n;
  }
}
