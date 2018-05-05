package com.sapient.hackathon;
import org.junit.Test;

import com.sapient.hackathon.engine.GridOperator;

/**
 * @author mkumar
 *
 */
public class GridOperatorTest {

	/**
	 * @param args
	 */

//	@Test
	public void testGetLineCapacity() {
		System.out.println("testGetLineCapacity");
		GridOperator gridOperator = new GridOperator();
		System.out.println(gridOperator.getLineCapacity("TestLine1"));
	}

//	@Test
	public void testGetLineGeneration() {
		System.out.println("testGetLineGeneration");
		GridOperator gridOperator = new GridOperator();
		System.out.println(gridOperator.getLineGeneration("TestLine1"));
	}
	
	//@Test
	public void testGetPoolPrice() {
		System.out.println("testGetPoolPrice");
		GridOperator gridOperator = new GridOperator();
		System.out.println(gridOperator.getCurrentPoolPriceFromRPA());
	}
	
	@Test
	public void testGetStatusofAllLines() {
		System.out.println("testGetStatusofAllLines");
		GridOperator gridOperator = new GridOperator();
		System.out.println(gridOperator.getStatusofAllLines());
	}
	
	@Test
	public void testapplyConstraint() {
		System.out.println("applyConstraint");
		GridOperator gridOperator = new GridOperator();
		System.out.println(gridOperator.applyConstraint());
	}
}
