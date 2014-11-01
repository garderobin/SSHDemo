package app.dao;

import java.util.List;

import app.entries.EntryPage;


/**
 * @summary:
 * 		业务实例的DAO层的操作API定义接口
 * 
 *    注意此处的方法都通过entry标识也操作属于DAO层
 *
 */
public interface SupportDao<T> {
	
	//保存实体
	public void saveEntry(T t);
	
	//更新实体
	public void updateEntry(T t);
	
	//保存或更新
	public void saveOrUpdateEntry(T t);
	
	//删除实体
	public void deleteEntryById(T t);
	
	//加载实体
	public T loadEntry(Integer id);
	
	//加载实体
	public T getEntry(Integer id);
	
	//通过HQL查询单个实体
	public T queryEntry(String hql);
	
	//以HQL方式批量操作
	public int batchEntryByHQL(String hql,Object ...objects);
	
	//通过HQL获得实体列表
	public List<T> getEntryListByHQL(String hql,Object ...objects);
	
	//通过SQL获得提示列表
	public List<T> getEntryListBySQL(String sql,Object ...objects);

	//通过HQL获取实例列表
	public List<T> findEntityByHQL(String hql, Object[] objects);
	
	//以分页的方式获得实体
	public EntryPage query(final String hql, int page, final int size);
	
}
