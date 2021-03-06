package ule.edi.tree;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;





public class BinarySearchTreeTests {

   
	/*
	* 10
	* |  5
	* |  |  2
	* |  |  |  ∅
	* |  |  |  ∅
	* |  |  ∅
	* |  20
	* |  |  15
	* |  |  |  ∅
	* |  |  |  ∅
	* |  |  30
	* |  |  |  ∅
	* |  |  |  ∅
    */	
	private BinarySearchTreeImpl<Integer> ejemplo = null;
	
	
	/*
	* 10
	* |  5
	* |  |  2
	* |  |  |  ∅
	* |  |  |  ∅
	* |  |  ∅
	* |  20
	* |  |  15
	* |  |  |  12
	* |  |  |  |  ∅
	* |  |  |  |  ∅
	* |  |  ∅
  */
	private BinarySearchTreeImpl<Integer> other=null;
	
	@Before
	public void setupBSTs() {
		
			
		ejemplo = new BinarySearchTreeImpl<Integer>();
		ejemplo.insert(10, 20, 5, 2, 15, 30);
		Assert.assertEquals(ejemplo.toString(), "{10, {5, {2, ∅, ∅}, ∅}, {20, {15, ∅, ∅}, {30, ∅, ∅}}}");
		
		
		other =new BinarySearchTreeImpl<Integer>();
		other.insert(10, 20, 5, 2, 15, 12);
		Assert.assertEquals(other.toString(), "{10, {5, {2, ∅, ∅}, ∅}, {20, {15, {12, ∅, ∅}, ∅}, ∅}}");
		
	    	}

	@Test
	public void testRemoveHoja() {
		ejemplo.remove(30);
		Assert.assertEquals("{10, {5, {2, ∅, ∅}, ∅}, {20, {15, ∅, ∅}, ∅}}",ejemplo.toString());
	}
	
	@Test
	public void testRemove1Hijo() {
		ejemplo.remove(5);
		Assert.assertEquals("{10, {2, ∅, ∅}, {20, {15, ∅, ∅}, {30, ∅, ∅}}}",ejemplo.toString());
	}
	
	@Test
	public void testRemove2Hijos() {
		ejemplo.remove(10);
		Assert.assertEquals("{15, {5, {2, ∅, ∅}, ∅}, {20, ∅, {30, ∅, ∅}}}",ejemplo.toString());
	}
	
		@Test
		public void testTagDecendentsEjemplo() {
			ejemplo.tagDecendents();
			ejemplo.filterTags("decendents");
			Assert.assertEquals("{10 [(decendents, 5)], {5 [(decendents, 1)], {2 [(decendents, 0)], ∅, ∅}, ∅}, {20 [(decendents, 2)], {15 [(decendents, 0)], ∅, ∅}, {30 [(decendents, 0)], ∅, ∅}}}",ejemplo.toString());
		}
		
		@Test
		public void testTagHeightEjemplo() {
			other.tagHeight();
			other.filterTags("height");
			Assert.assertEquals("{10 [(height, 1)], {5 [(height, 2)], {2 [(height, 3)], ∅, ∅}, ∅}, {20 [(height, 2)], {15 [(height, 3)], {12 [(height, 4)], ∅, ∅}, ∅}, ∅}}",other.toString());
		}
		
		
		@Test
		public void testTagOnlySonEjemplo() {
		
		Assert.assertEquals(other.toString(), "{10, {5, {2, ∅, ∅}, ∅}, {20, {15, {12, ∅, ∅}, ∅}, ∅}}");
		Assert.assertEquals(3,other.tagOnlySonInorder());
		other.filterTags("onlySon");
		Assert.assertEquals("{10, {5, {2 [(onlySon, 1)], ∅, ∅}, ∅}, {20, {15 [(onlySon, 3)], {12 [(onlySon, 2)], ∅, ∅}, ∅}, ∅}}",other.toString());

		}

		@Test
		public void testContains(){
			Assert.assertEquals(true, ejemplo.contains(15));
			Assert.assertEquals(false, ejemplo.contains(13));
		}

		@Test(expected = IllegalArgumentException.class)
		public void testInsertCollectionNull(){
			LinkedList<Integer> lista = new LinkedList<Integer>();
			lista.add(3);
			lista.add(null);
			lista.add(6);
			ejemplo.insert(lista);
		}
		@Test
		public void testInsertCollection(){
			LinkedList<Integer> lista = new LinkedList<Integer>();
			lista.add(3);
			lista.add(5);
			lista.add(6);
			ejemplo.insert(lista);
			Assert.assertEquals(ejemplo.contains(5), true);
		}


		@Test(expected = IllegalArgumentException.class)
		public void testInsertListOfElements(){
			ejemplo.insert(1,null,3);
		}

		@Test(expected = IllegalArgumentException.class)
		public void testInsertNullElement(){
			ejemplo.insert((Integer) null);
		}

		@Test(expected = IllegalArgumentException.class)
		public void testContainsNullElement(){
			ejemplo.contains(null);
		}

		@Test(expected = IllegalArgumentException.class)
		public void testRemoveNullElement(){
			ejemplo.remove((Integer)null);
		}

		@Test(expected = NoSuchElementException.class)
		public void testRemoveNoElement(){
			ejemplo.remove((Integer) 1);
		}

		@Test(expected = IllegalArgumentException.class)
		public void testRemoveNullListElements(){
			ejemplo.insert(1, 2);
			ejemplo.remove(1, null, 2);
		}

		@Test(expected = NoSuchElementException.class)
		public void testRemoveNoListElements(){
			ejemplo.insert(1,2);
			ejemplo.remove(1,2,3);
		}

		@Test
		public void testRemoveListElements(){
			ejemplo.insert(1,2,3);
			ejemplo.remove(1,2,3);
		}

		@Test
		public void testIteratorWidth(){
			BinarySearchTreeImpl<Integer> emptyTree = new BinarySearchTreeImpl<Integer>();
			emptyTree.insert(10, 5, 12, 6, 11);
			Iterator<Integer> iter = emptyTree.iteratorWidth();
			Assert.assertEquals(iter.next().toString(), "10");
			Assert.assertEquals(iter.next().toString(), "5");
			Assert.assertEquals(iter.next().toString(), "12");

		}

		@Test
		public void testTagDecendentsLeftEmpty(){
			BinarySearchTreeImpl<Integer> emptyTree = new BinarySearchTreeImpl<Integer>();
			emptyTree.insert(10, 12);
			emptyTree.tagDecendents();
			other.filterTags("decendents");
			Assert.assertEquals("{10 [(decendents, 1)], ∅, {12 [(decendents, 0)], ∅, ∅}}",emptyTree.toString());

		}

	
	}


