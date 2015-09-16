package edu.fudan.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public class MultiValueMap<K, V> implements Map<K, V> {



	TreeMap<K, TreeSet<V>> map;

	public MultiValueMap() {
		map = new TreeMap<K, TreeSet<V>>();
	}

	@Override
	public int size() {
		
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		System.err.println("Not Implimented Yet!");
		return false;
	}

	@Override
	public V get(Object key) {
		System.err.println("Not Implimented Yet!");
		return null;
	}
	
	
	public TreeSet<V> getSet(Object key) {
		return map.get(key);
	}

	@Override
	public V put(K key, V value) {
		TreeSet<V> set = map.get(key);
		if(value==null){
			map.put(key, null);
			return null;
		}

		if(set == null) {
			set = new TreeSet<V>();
			map.put(key, set);
		} 
		set.add(value);
		return null;
	}

	@Override
	public V remove(Object key) {
		System.err.println("Not Implimented Yet!");
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		System.err.println("Not Implimented Yet!");

	}

	@Override
	public void clear() {
		System.err.println("Not Implimented Yet!");
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		System.err.println("Not Implimented Yet!");
		return null;
	}
	public  Collection<TreeSet<V>> valueSets() {
		return map.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		System.err.println("Not Implimented Yet!");
		return null;
	}
	@Override
	 public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<K, TreeSet<V>>> it1 = map.entrySet().iterator();
		while(it1.hasNext()){
			Entry<K, TreeSet<V>> entry = it1.next();
			
			sb.append(entry.getKey());
			sb.append("\t");
			
			 TreeSet<V> val = entry.getValue();
			if(val==null){
				if(it1.hasNext())
					sb.append("\n");
				continue;
			}
			Iterator<V> it = val.iterator();
			while (it.hasNext()) {
				V en = it.next();
				sb.append(en);
				if(it.hasNext())
					sb.append("\t");
			}
			if(it1.hasNext())
				sb.append("\n");
		}
		return sb.toString();
		 
	 }

	

}
