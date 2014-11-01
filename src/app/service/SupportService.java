package app.service;

import java.util.List;

import app.annotation.ServiceMethod;
import app.annotation.ServiceType;
import app.entries.EntryPage;

/**
 * 
 * @Summary:
 * 		实体的业务层对应的操作方法
 * 	注意：
 * 		此处使用非表明该操作属于业务层
 * 
 *
 */
public interface SupportService<T> {
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类保存实体方法")
	public void save(T t);
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类更新实体方法")
	public void update(T t);
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类更新活保存实体方法")
	public void saveOrUpdate(T t);
	
	@ServiceMethod(type=ServiceType.SYSTEM,name="超类批量执行HQL")
	public int batchByHQL(String hql,Object ...objects);
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类删除实体方法")
	public void delete(T t);
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类加载实体方法")
	public T load(Integer id);
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类加载实体方法")
	public T get(Integer id);
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类加载实体集合方法HQL")
	public List<T> getListByHQL(String hql,Object ...objects);
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类加载实体集合方法SQL")
	public List<T> getListBySQL(String sql,Object ...objects);
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类加载实体集合方法HQL")
	public List<T> findByHQL(String hql, Object... objects);
	
	@ServiceMethod(type=ServiceType.BUSINESS,name="超类加载实体分页集合方法")
	public EntryPage query(final String hql, int page, final int size);

}
