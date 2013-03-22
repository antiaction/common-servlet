/*
 * Created on 03/08/2011
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.mayhem.mailmanager;

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

}
