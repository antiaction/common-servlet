/*
 * Created on 20/03/2013
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.mayhem.mailmanager;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestPathMap {

	@Test
	public void test_pathmap() {
		PathMap<Integer> pathMap;

		PathMap<Integer> domainPathMap;
		PathMap<Integer> domainNumericPathMap;
		PathMap<Integer> accountPathMap;
		PathMap<Integer> accountNumericPathMap;

		/*
		 * add() path.
		 */

		pathMap = new PathMap<Integer>();

		Assert.assertEquals( 0, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertNull( pathMap.action );

		pathMap.add( null, -1 );

		Assert.assertEquals( 0, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertNull( pathMap.action );

		pathMap.add( "", 0 );

		Assert.assertEquals( 0, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertEquals( new Integer( 0 ), pathMap.action );

		pathMap.add( "domain/list/", 1 );

		Assert.assertEquals( 1, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertEquals( new Integer( 0 ), pathMap.action );

		domainPathMap = pathMap.pathMap.get( "domain" );
		Assert.assertNotNull( domainPathMap );

		Assert.assertEquals( 1, domainPathMap.pathMap.size() );
		Assert.assertNull( domainPathMap.numeric );
		Assert.assertNull( domainPathMap.action );

		pathMap.add( "domain/add/", 2 );

		Assert.assertEquals( 2, domainPathMap.pathMap.size() );
		Assert.assertNull( domainPathMap.numeric );
		Assert.assertNull( domainPathMap.action );

		pathMap.add( "domain/<numeric>/edit/", 3 );
		pathMap.add( "domain/<numeric>/account/list/", 4 );
		pathMap.add( "domain/<numeric>/account/add/", 5 );
		pathMap.add( "domain/<numeric>/account/<numeric>/edit/", 6 );

		Assert.assertEquals( 2, domainPathMap.pathMap.size() );
		Assert.assertNotNull( domainPathMap.numeric );
		Assert.assertNull( domainPathMap.action );

		domainNumericPathMap = domainPathMap.numeric;
		Assert.assertNotNull( domainNumericPathMap );

		Assert.assertEquals( 2, domainNumericPathMap.pathMap.size() );
		Assert.assertNull( domainNumericPathMap.numeric );
		Assert.assertNull( domainNumericPathMap.action );

		accountPathMap = domainNumericPathMap.pathMap.get( "account" );
		Assert.assertNotNull( accountPathMap );

		Assert.assertEquals( 2, accountPathMap.pathMap.size() );
		Assert.assertNotNull( accountPathMap.numeric );
		Assert.assertNull( accountPathMap.action );

		accountNumericPathMap = accountPathMap.numeric;
		Assert.assertNotNull( accountNumericPathMap );

		Assert.assertEquals( 1, accountNumericPathMap.pathMap.size() );
		Assert.assertNull( accountNumericPathMap.numeric );
		Assert.assertNull( accountNumericPathMap.action );

		Assert.assertEquals( 1, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertEquals( new Integer( 0 ), pathMap.action );

		/*
		 * add() /path.
		 */

		pathMap = new PathMap<Integer>();

		Assert.assertEquals( 0, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertNull( pathMap.action );

		pathMap.add( null, -1 );

		Assert.assertEquals( 0, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertNull( pathMap.action );

		pathMap.add( "/", 0 );

		Assert.assertEquals( 0, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertEquals( new Integer( 0 ), pathMap.action );

		pathMap.add( "/domain/list/", 1 );

		Assert.assertEquals( 1, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertEquals( new Integer( 0 ), pathMap.action );

		domainPathMap = pathMap.pathMap.get( "domain" );
		Assert.assertNotNull( domainPathMap );

		Assert.assertEquals( 1, domainPathMap.pathMap.size() );
		Assert.assertNull( domainPathMap.numeric );
		Assert.assertNull( domainPathMap.action );

		pathMap.add( "/domain/add/", 2 );

		Assert.assertEquals( 2, domainPathMap.pathMap.size() );
		Assert.assertNull( domainPathMap.numeric );
		Assert.assertNull( domainPathMap.action );

		pathMap.add( "/domain/<numeric>/edit/", 3 );
		pathMap.add( "/domain/<numeric>/account/list/", 4 );
		pathMap.add( "/domain/<numeric>/account/add/", 5 );
		pathMap.add( "/domain/<numeric>/account/<numeric>/edit/", 6 );

		Assert.assertEquals( 2, domainPathMap.pathMap.size() );
		Assert.assertNotNull( domainPathMap.numeric );
		Assert.assertNull( domainPathMap.action );

		domainNumericPathMap = domainPathMap.numeric;
		Assert.assertNotNull( domainNumericPathMap );

		Assert.assertEquals( 2, domainNumericPathMap.pathMap.size() );
		Assert.assertNull( domainNumericPathMap.numeric );
		Assert.assertNull( domainNumericPathMap.action );

		accountPathMap = domainNumericPathMap.pathMap.get( "account" );
		Assert.assertNotNull( accountPathMap );

		Assert.assertEquals( 2, accountPathMap.pathMap.size() );
		Assert.assertNotNull( accountPathMap.numeric );
		Assert.assertNull( accountPathMap.action );

		accountNumericPathMap = accountPathMap.numeric;
		Assert.assertNotNull( accountNumericPathMap );

		Assert.assertEquals( 1, accountNumericPathMap.pathMap.size() );
		Assert.assertNull( accountNumericPathMap.numeric );
		Assert.assertNull( accountNumericPathMap.action );

		Assert.assertEquals( 1, pathMap.pathMap.size() );
		Assert.assertNull( pathMap.numeric );
		Assert.assertEquals( new Integer( 0 ), pathMap.action );

		/*
		 * get().
		 */

		Integer action;
		List<Integer> numerics = new ArrayList<Integer>();

		action = pathMap.get( "/", numerics );
		System.out.println( action );
		printarray( numerics );
		Assert.assertEquals( new Integer( 0 ), action );

		action = pathMap.get( "/domain/list/", numerics );
		System.out.println( action );
		printarray( numerics );
		Assert.assertEquals( new Integer( 1 ), action );

		action = pathMap.get( "/domain/add/", numerics );
		System.out.println( action );
		printarray( numerics );
		Assert.assertEquals( new Integer( 2 ), action );

		action = pathMap.get( "/domain/42/edit/", numerics );
		System.out.println( action );
		printarray( numerics );
		Assert.assertEquals( new Integer( 3 ), action );

		action = pathMap.get( "/domain/43/account/list/", numerics );
		System.out.println( action );
		printarray( numerics );
		Assert.assertEquals( new Integer( 4 ), action );

		action = pathMap.get( "/domain/44/account/add/", numerics );
		System.out.println( action );
		printarray( numerics );
		Assert.assertEquals( new Integer( 5 ), action );

		action = pathMap.get( "/domain/45/account/46/edit/", numerics );
		System.out.println( action );
		printarray( numerics );
		Assert.assertEquals( new Integer( 6 ), action );
	}

	public static void printarray(List<Integer> numerics) {
		if ( numerics != null ) {
			for ( int i=0; i<numerics.size(); ++i ) {
				if ( i > 0 ) {
					System.out.print( ", " );
				}
				System.out.print( numerics.get( i ) );
			}
			System.out.println();
		}
	}

}
