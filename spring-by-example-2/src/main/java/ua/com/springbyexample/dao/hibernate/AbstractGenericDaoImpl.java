package ua.com.springbyexample.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;

import ua.com.springbyexample.dao.GenericDao;

public abstract class AbstractGenericDaoImpl<E, PK extends Serializable> implements GenericDao<E, PK> {
	private Class<E> entityClass;

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return entityClass;
	}

	@Override
	public void update(E entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}

	@SuppressWarnings("unchecked")
	public E merge(E entity) {
		return (E) sessionFactory.getCurrentSession().merge(entity);
	}

	@Override
	public void save(E entity) {
		sessionFactory.getCurrentSession().save(entity);
	}

	@Override
	public void delete(E entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E find(PK id) {
		return (E) sessionFactory.getCurrentSession().get(getEntityClass(), id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> find() {
		return sessionFactory.getCurrentSession().createQuery("from " + getEntityClass().getName()).list();
	}

	public void saveOrUpdate(E entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
