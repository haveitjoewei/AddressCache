import java.net.*;
import java.net.UnknownHostException;
import java.net.InetAddress;

import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;


public class MyCache<T> implements AddressCache {
	// Main data structure we will be using to store a map of InetAddress to the time added
	public final Map<InetAddress, DListNode> objects;
	// Stack (Doubly-linked list) is used to keep track of most recently added objects
	private DList orderedObjects;
	private final long initialExpire;
	private final ExecutorService threads;
	private final long runRate;

	public MyCache() {
		this(5);
	}

	// In case we want a different expiration time
	public MyCache(long expiration){
		this.objects = Collections.synchronizedMap(new HashMap<InetAddress, DListNode>());
		this.orderedObjects = new DList();

		this.initialExpire = expiration;
		this.runRate = 5;

		this.threads = Executors.newFixedThreadPool(256);

		// Removes expired InetAddresses from the cache periodically
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
		scheduler.scheduleWithFixedDelay(this.removeExpired(), this.runRate/2, this.runRate, TimeUnit.SECONDS);
	}

	/**
	* Returns a Runnable that removes all expired entries from the cache
	* Runs in O(n) time
	**/
	private final Runnable removeExpired(){
		return new Runnable(){
			public void run(){
				try{
					Iterator it = objects.keySet().iterator();
					while(it.hasNext()){
						if (System.currentTimeMillis() > objects.get(it.next()).expiry) {
							System.out.println("Removing Expired");
							it.remove();
							orderedObjects.remove(objects.get(it.next()));
						}
					}
				} catch (Exception e) {
				}
			}
		};
	}

	/**
	 * Returns the default expiration time for the objects in the cache.
	 */
	public long getExpire() {
		return this.initialExpire;
	}

	/**
     * Adds the given {@link InetAddress} and returns {@code true} on success.
     * Runs in O(1) time
     */
    public boolean offer(InetAddress address){
    	synchronized(this){
	    	try {	
	    		if (this.objects.containsKey(address)){
	    			DListNode node = this.objects.get(address);
	    			this.orderedObjects.remove(node);
	    			node = this.orderedObjects.push(address, System.currentTimeMillis() + this.initialExpire*1000);
	    			this.objects.put(address, node);
	    		} else {
			    	DListNode node = this.orderedObjects.push(address, System.currentTimeMillis() + this.initialExpire*1000);
			    	this.objects.put(address, node);
	    		}
		    	notifyAll();
		    	return true;
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		return false;
	    	}
    	}
    }
    
    /**
     * Returns {@code true} if the given {@link InetAddress} 
     * is in the {@link AddressCache}.
     * Runs in O(1) time. Java treats containsKey() as a get() that throws the retrieved value away
     */
    public boolean contains(InetAddress address){
    	return this.objects.containsKey(address);
    }
    
    /**
     * Removes the given {@link InetAddress} and returns {@code true}
     * on success.
     * Runs in O(1) time.
     */
    public boolean remove(InetAddress address){
    	synchronized(this){
	    	if (this.objects.containsKey(address)){
	    		DListNode node = this.objects.get(address);
	    		this.orderedObjects.remove(node);
	    		this.objects.remove(address);
	    		return true;
	    	} else {
	    		return false;
	    	}
	    }
    }
    
    /**
     * Returns the most recently added {@link InetAddress} and returns 
     * {@code null} if the {@link AddressCache} is empty.
     * Runs in O(1) time 
     */
    public InetAddress peek(){
    	synchronized(this){
	    	if (this.objects.containsKey(this.orderedObjects.peek().address)){
	    		return this.orderedObjects.peek().address;
	    	} else {
	    		return null;
	    	}
    	}
    }
    
    /**
     * Removes and returns the most recently added {@link InetAddress} and  
     * returns {@code null} if the {@link AddressCache} is empty.
     * Runs in O(1) time 
     */
    public InetAddress remove(){
    	synchronized(this){
	    	if (this.objects.containsKey(this.orderedObjects.peek().address)){ 
	    		this.objects.remove(this.orderedObjects.peek().address);
	    		return this.orderedObjects.pop().address;
	    	} else {
	    		return null;
	    	}	
    	}
    }
    
    /**
     * Retrieves and removes the most recently added {@link InetAddress},
     * waiting if necessary until an element becomes available.
     * Runs in O(1) time 
     */
    public InetAddress take() throws InterruptedException {
    	synchronized(this){
    		try{
		    	while(this.objects.isEmpty()){
		    		wait();
		    	}	
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		InetAddress item = this.remove();
    		notifyAll();
    		return item;
    	}
    }
    
    /**
     * Closes the {@link AddressCache} and releases all resources.
     * Java's clear() runs in O(n) time. The commented out code is a suitable O(1) replacement.
     */
    public void close(){
    	this.objects.clear();
    	// this.objects = Collections.synchronizedMap(new HashMap<InetAddress, Long>());
    	this.orderedObjects = new DList();
    };
    
    /**
     * Returns the number of elements in the {@link AddressCache}.
     * Runs in O(1) time
     */
    public int size(){
    	return this.objects.size();
    }
    
    /**
     * Returns {@code true} if the {@link AddressCache} is empty.
     * Runs in O(1) time
     */
    public boolean isEmpty(){
    	return this.objects.isEmpty();
    }
}