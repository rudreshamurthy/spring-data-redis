/*
 * Copyright 2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.keyvalue.redis.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.data.keyvalue.redis.core.BoundHashOperations;
import org.springframework.data.keyvalue.redis.core.RedisOperations;

/**
 * Default implementation for {@link RedisMap}.
 *  
 * @author Costin Leau
 */
public class DefaultRedisMap<K, V> implements RedisMap<K, V> {

	private final BoundHashOperations<String, K, V> hashOps;

	/**
	 * Constructs a new <code>DefaultRedisMap</code> instance.
	 *
	 * @param key
	 * @param operations
	 */
	public DefaultRedisMap(String key, RedisOperations<String, ?> operations) {
		this.hashOps = operations.forHash(key);
	}

	/**
	 * Constructs a new <code>DefaultRedisMap</code> instance.
	 *
	 * @param boundOps
	 */
	public DefaultRedisMap(BoundHashOperations<String, K, V> boundOps) {
		this.hashOps = boundOps;
	}

	@Override
	public Integer increment(K key, int delta) {
		return hashOps.increment(key, delta);
	}

	@Override
	public boolean putIfAbsent(K key, V value) {
		if (!hashOps.hasKey(key)) {
			put(key, value);
			return true;
		}
		return false;
	}

	@Override
	public String getKey() {
		return hashOps.getKey();
	}

	@Override
	public RedisOperations<String, ?> getOperations() {
		return hashOps.getOperations();
	}

	@Override
	public void clear() {
		getOperations().delete(getKey());
	}

	@Override
	public boolean containsKey(Object key) {
		return hashOps.hasKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		return hashOps.get(key);
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Set<K> keySet() {
		return hashOps.keys();
	}

	@Override
	public V put(K key, V value) {
		V oldV = get(key);
		hashOps.set(key, value);
		return oldV;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		hashOps.multiSet(m);
	}

	@Override
	public V remove(Object key) {
		V v = get(key);
		hashOps.delete(key);
		return v;
	}

	@Override
	public int size() {
		return hashOps.length();
	}

	@Override
	public Collection<V> values() {
		return hashOps.values();
	}
}