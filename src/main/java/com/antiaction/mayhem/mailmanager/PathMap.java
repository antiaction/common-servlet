/*
 * Created on 03/08/2011
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.mayhem.mailmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.antiaction.common.strings.Strings;

public class PathMap<T> {

	protected Map<String, PathMap<T>> pathMap;

	protected PathMap<T> numeric = null;

	protected T action;

	public PathMap() {
		pathMap = new HashMap<String, PathMap<T>>();
	}

	public void add(String pathStr, T action) {
		if ( pathStr == null ) {
			return;
		}
		if ( pathStr.startsWith( "/" ) ) {
			pathStr = pathStr.substring( 1 );
		}

		PathMap<T> parent = this;
		PathMap<T> current;

		List<String> pathList = Strings.splitString( pathStr, "/" );
		String path;

		boolean b = true;
		int idx = 0;
		while ( b ) {
			if ( idx < pathList.size() ) {
				path = pathList.get( idx++ );
				if ( path.length() > 0 ) {
					if ( "<numeric>".compareTo( path ) == 0 ) {
						if ( parent.numeric == null ) {
							parent.numeric = new PathMap<T>();
						}
						parent = parent.numeric;
					}
					else {
						current = parent.pathMap.get( path );
						if ( current == null ) {
							current = new PathMap<T>();
							parent.pathMap.put( path, current );
						}
						parent = current;
					}
				}
				else {
					if ( idx == pathList.size() ) {
						parent.action = action;
					}
					else {
						// debug
						System.out.println( "Invalid path: " + pathStr );
					}
				}
			}
			else {
				b = false;
			}
		}
	}

	public T get(String pathStr, List<Integer> numerics) {
		if ( pathStr == null ) {
			return null;
		}
		if ( pathStr.startsWith( "/" ) ) {
			pathStr = pathStr.substring( 1 );
		}

		T action = null;

		PathMap<T> parent = this;
		PathMap<T> current;

		List<String> pathList = Strings.splitString( pathStr, "/" );
		String path;

		numerics.clear();

		boolean b = true;
		int idx = 0;
		while ( b ) {
			if ( idx < pathList.size() ) {
				path = pathList.get( idx++ );
				if ( path.length() > 0 ) {
					current = parent.pathMap.get( path );
					if ( current != null ) {
						parent = current;
					}
					else {
						if ( parent.numeric != null ) {
							try {
								int numeric = Integer.parseInt( path );
								numerics.add( numeric );
								parent = parent.numeric;
							}
							catch (NumberFormatException e) {
								b = false;
							}
						}
						else {
							b = false;
						}
					}
				}
				else {
					if ( idx == pathList.size() ) {
						action = parent.action;
					}
					else {
						// debug
						System.out.println( "Invalid path: " + pathStr );
					}
				}
			}
			else {
				b = false;
			}
		}
		return action;
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

	public static void main(String[] args) {
		PathMap<Integer> pathMap = new PathMap<Integer>();

		pathMap.add( "/", 0 );
		pathMap.add( "/domain/list/", 1 );
		pathMap.add( "/domain/add/", 2 );
		pathMap.add( "/domain/<numeric>/edit/", 3 );
		pathMap.add( "/domain/<numeric>/account/list/", 4 );
		pathMap.add( "/domain/<numeric>/account/add/", 5 );
		pathMap.add( "/domain/<numeric>/account/<numeric>/edit/", 6 );

		Integer action;
		List<Integer> numerics = new ArrayList<Integer>();

		action = pathMap.get( "/", numerics );
		System.out.println( action );
		printarray( numerics );

		action = pathMap.get( "/domain/list/", numerics );
		System.out.println( action );
		printarray( numerics );

		action = pathMap.get( "/domain/add/", numerics );
		System.out.println( action );
		printarray( numerics );

		action = pathMap.get( "/domain/42/edit/", numerics );
		System.out.println( action );
		printarray( numerics );

		action = pathMap.get( "/domain/43/account/list/", numerics );
		System.out.println( action );
		printarray( numerics );

		action = pathMap.get( "/domain/44/account/add/", numerics );
		System.out.println( action );
		printarray( numerics );

		action = pathMap.get( "/domain/45/account/46/edit/", numerics );
		System.out.println( action );
		printarray( numerics );
	}

}
