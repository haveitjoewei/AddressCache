import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.net.InetAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.Assert.*;

public class MyCacheTest {

	private MyCache<Object> cache;

	@Before
	public void setup() {
		this.cache = new MyCache<Object>();
	}

	@Test
	public void RemoveByName() throws InterruptedException {
		try{
			System.out.println("Running remove by name test...");
			// Inserts a bunch of random InetAddresses
			long start = System.currentTimeMillis();
			for (int i = 0; i < 255; i++) {
				this.cache.offer(InetAddress.getByName(i+"."+i+"."+i+"."+i));
				assertTrue(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
			}
			long end = System.currentTimeMillis() - start;
			System.out.println("Offering took: " + end + "ms");

			// Removes a bunch of random InetAddresses by name
			start = System.currentTimeMillis();
			for (int i = 0; i < 255; i++) {
				assertTrue(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
				this.cache.remove(InetAddress.getByName(i+"."+i+"."+i+"."+i));
				assertFalse(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
			}
			assertTrue(this.cache.isEmpty());
			end = System.currentTimeMillis() - start;
			System.out.println("Removing by name took: " + end + "ms" + "\n");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Test
	public void Remove() throws InterruptedException {
		try{
			System.out.println("Running remove test...");
			// Inserts a bunch of random InetAddresses
			long start = System.currentTimeMillis();
			for (int i = 0; i < 255; i++) {
				this.cache.offer(InetAddress.getByName(i+"."+i+"."+i+"."+i));
				assertTrue(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
			}
			long end = System.currentTimeMillis() - start;
			System.out.println("Offering took: " + end + "ms");

			// Removes a bunch of InetAddresses by most recently added
			start = System.currentTimeMillis();
			for (int i = 254; i >= 0; i--) {
				assertTrue(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
				this.cache.remove();
				assertFalse(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
			}
			assertTrue(this.cache.isEmpty());
			end = System.currentTimeMillis() - start;
			System.out.println("Removing took: " + end + "ms" + "\n");
		} catch (Exception e) {
			// System.out.println(e);
		}
	}

	@Test
	public void Take() throws InterruptedException {
		try{
			System.out.println("Running take test...");
			// Inserts a bunch of random InetAddresses
			long start = System.currentTimeMillis();
			for (int i = 0; i < 255; i++) {
				this.cache.offer(InetAddress.getByName(i+"."+i+"."+i+"."+i));
				assertTrue(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
			}
			long end = System.currentTimeMillis() - start;
			System.out.println("Offering took: " + end + "ms");

			// Takes a bunch of InetAddresses by most recently added
			start = System.currentTimeMillis();
			for (int i = 254; i >= 0 ; i--) {
				assertTrue(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
				this.cache.take();
				assertFalse(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
			}
			assertTrue(this.cache.isEmpty());
			end = System.currentTimeMillis() - start;
			System.out.println("Taking took: " + end + "ms" + "\n");
		} catch (Exception e) {
			// System.out.println(e);
		}
	}

	@Test
	public void Close() throws InterruptedException {
		try{
			System.out.println("Running close test...");
			// Inserts a bunch of random InetAddresses
			long start = System.currentTimeMillis();
			for (int i = 0; i < 255; i++) {
				this.cache.offer(InetAddress.getByName(i+"."+i+"."+i+"."+i));
				assertTrue(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
			}
			long end = System.currentTimeMillis() - start;
			System.out.println("Offering took: " + end + "ms");

			// Closes Cache
			start = System.currentTimeMillis();
			this.cache.close();
			assertTrue(this.cache.isEmpty());
			end = System.currentTimeMillis() - start;
			System.out.println("Closing took: " + end + "ms" + "\n");
		} catch (Exception e) {
			// System.out.println(e);
		}
	}

	@Test
	public void Size() throws InterruptedException {
		try{
			System.out.println("Running size test...");
			// Inserts a bunch of random InetAddresses and asserts item count is correct
			long start = System.currentTimeMillis();
			for (int i = 0; i < 255; i++) {
				this.cache.offer(InetAddress.getByName(i+"."+i+"."+i+"."+i));
				assertTrue(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
			}
			assertEquals(this.cache.size(), 255);
			long end = System.currentTimeMillis() - start;
			System.out.println("Offering took: " + end + "ms");

			// Closes Cache and Counts number of items in cache
			start = System.currentTimeMillis();
			this.cache.close();
			assertEquals(this.cache.size(), 0);
			end = System.currentTimeMillis() - start;
			System.out.println("Closing took: " + end + "ms" + "\n");
		} catch (Exception e) {
			// System.out.println(e);
		}
	}

	@Test
	public void expire() throws InterruptedException {
		try{
			for (int i = 0; i < 5; i++) {
				this.cache.offer(InetAddress.getByName(i+"."+i+"."+i+"."+i));
				assertTrue(this.cache.contains(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
				// assertNull(this.cache.remove(InetAddress.getByName(i+"."+i+"."+i+"."+i)));
			}
			// wait until this object is expired
			assertEquals(this.cache.size(), 5);
			Thread.sleep(this.cache.getExpire() * 5010);
			System.out.println(this.cache.objects);
			assertTrue(this.cache.isEmpty());

			// check if a default time works
			assertTrue("Expiration time should be greater than or equal to 5", this.cache.getExpire() >= 5);
		} catch (Exception e) {

		}
	}
	
}