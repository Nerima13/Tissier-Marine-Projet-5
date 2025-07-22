package com.openclassrooms.SafetyNetAlerts.repository;

import java.util.List;

public interface CrudRepository<T> {
	
	public void add(T element);

	public void delete(T element);

	public void update(T element);
	
	public T get(T element);

	public List<T> findAll();

}
