package app.service.impl;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import app.dao.SupportDao;
import app.entries.EntryPage;
import app.service.SupportService;

public abstract class SupportServiceImpl<T> implements SupportService<T> {
	
		public  Logger log = Logger.getLogger(this.getClass());
	
		@Autowired
		@Getter @Setter private SupportDao<T> supportDao;
		
		
		@Override
		public void save(T t) {
			supportDao.saveEntry(t);
		}

		@Override
		public void update(T t) {
			supportDao.updateEntry(t);
		}

		@Override
		public void saveOrUpdate(T t) {
			supportDao.saveOrUpdateEntry(t);
		}

		@Override
		public int batchByHQL(String hql, Object... objects) {
			return supportDao.batchEntryByHQL(hql, objects);
		}

		@Override
		public void delete(T t) {
			supportDao.deleteEntryById(t);
		}

		@Override
		public T load(Integer id) {
			return (T) supportDao.loadEntry(id);
		}

		@Override
		public T get(Integer id) {
			return (T) supportDao.getEntry(id);
		}

		@Override
		public List<T> getListByHQL(String hql, Object... objects) {
			return supportDao.getEntryListByHQL(hql, objects);
		}

		@Override
		public List<T> getListBySQL(String sql, Object... objects) {
			return supportDao.getEntryListBySQL(sql, objects);
		}
		
		@Override
		public List<T> findByHQL(String hql, Object... objects) {
			return supportDao.findEntityByHQL(hql, objects);
		}

		@Override
		public EntryPage query(final String hql, int page, final int size){
			return supportDao.query(hql, page, size);
		}
		
}
