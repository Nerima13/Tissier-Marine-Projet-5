package com.openclassrooms.SafetyNetAlerts.service;

import java.util.List;

public interface CrudService<T> {
	
	public void add(T element);
	
	public void delete(T element);
	
	public void update(T element);
	
	public T get(T element);
	
	public List<T> findAll();
	}

