package app.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

import app.dao.SupportDao;
import app.entries.EntryPage;

/******************************************************************
 * @summary: 用于完成DAO操作的抽象基类，主要用于继承
 ******************************************************************/
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class SupportDaoImpl<T> implements SupportDao<T> {

	@Resource(name = "hibernateTemplate")
	@Getter @Setter private HibernateTemplate hibernateTemplate;


	// 构造时初始（获取泛型）
	private Class<T> clazz;

	public SupportDaoImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	public void saveEntry(T t) {
		hibernateTemplate.save(t);
	}

	public void updateEntry(T t) {
		hibernateTemplate.update(t);
	}

	public void saveOrUpdateEntry(T t) {
		hibernateTemplate.saveOrUpdate(t);
	}

	public int batchByHQL(String hql, Object... objects) {
		List<T> list = (List<T>) hibernateTemplate.find(hql, objects);
		return list.size();
	}

	public void deleteEntryById(T t) {
		hibernateTemplate.delete(t);
	}

	public T loadEntry(Integer id) {
		return hibernateTemplate.load(clazz, id);
	}

	public T getEntry(Integer id) {
		return hibernateTemplate.get(clazz, id);
	}

	public List<T> getEntryListByHQL(String hql, Object... objects) {
		List<T> list = (List<T>) hibernateTemplate.find(hql, objects);
		return list;
	}

	public List<T> getEntryListBySQL(String sql, Object... objects) {
		// 如果没有开启事务管理，在线程中不允许获取session
		SQLQuery q = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		// 添加实体类
		if (clazz != null) {
			q.addEntity(clazz);
		}
		for (int i = 0; i < objects.length; i++) {
			q.setParameter(i, objects[i]);
		}
		return q.list();
	}

	@Override
	public List<T> findEntityByHQL(String hql, Object[] objects) {
		return (List<T>) hibernateTemplate.find(hql, objects);
	}

	@Override
	public int batchEntryByHQL(String hql, Object... objects) {
		List<T> list = (List<T>) hibernateTemplate.find(hql, objects);
		return list.size();
	}

	/**
	 * page默认值为1. 从第一页开始 size默认值为100,每页100条记录
	 */
	public EntryPage query(final String hql, int page, final int size) {
		final EntryPage pageInfo = new EntryPage();
		pageInfo.setPageSize(size);
		pageInfo.setCurrentPage(page);
		pageInfo.setAllRow(hibernateTemplate.find(hql).size());
		pageInfo.init();
		List<Object> list = (List<Object>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setFirstResult(pageInfo.getOffset());
				query.setMaxResults(size);
				List list = query.list();
				return list;
			}
		});
		pageInfo.setList(list);
		pageInfo.init();
		return pageInfo;
	}

	@Override
	public T queryEntry(final String hql) {
		List<Object> list = (List<Object>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setFirstResult(0);
				query.setMaxResults(1);
				List list = query.list();
				return list;
			}
		});
		return (T) list.get(0);
	}
	
	
}
