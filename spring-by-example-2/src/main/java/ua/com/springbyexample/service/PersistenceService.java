package ua.com.springbyexample.service;

import java.io.Serializable;
import java.util.List;

public interface PersistenceService<E, PK extends Serializable> {
	void update(E entity);

	void save(E entity);

	void delete(E entity);

	E find(PK id);

	List<E> find();

}
