/*
 * Copyright (c) 2012 Sohu. All Rights Reserved
 */
package com.sohu.ccm.heart.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Description: 数据库操作接口
 * </p>
 * @author solidwang
 * @version 1.0
 * @Date 2012-10-12
 */
public interface Dao {

    /**
     * <p>
     * Description: 加载指定ID的持久化对象
     * </p>
     * @param clazz
     * @param id
     * @return
     * @author solidwang
     * @date 2012-11-13
     */
    public Object getById(Class<?> clazz, Long id);

    /**
      * <p>
      *     Description: 加载满足条件的持久化对象
      * </p>
      * @param hql
      * @return
      * @author solidwang
      * @date 2012-11-13
     */
    public Object loadObject(String hql);

    /**
      * <p>
      *     Description: 装载指定类的所有持久化对象
      * </p>
      * @param clazz
      * @return
      * @author solidwang
      * @date 2012-11-13
     */
    public List<?> listAll(String clazz);

    /**
      * <p>
      *     Description: 查询指定类的满足条件的持久化对象
      * </p>
      * @param hql
      * @return
      * @author solidwang
      * @date 2012-11-13
     */
    public List<?> query(String hql);

    public Object execute(String hql);

    /**
      * <p>
      *     Description: 分页查询指定类的满足条件的持久化对象
      * </p>
      * @param hql 查询sql
      * @param pageNo 页号
      * @param pageSize 每页条数
      * @return
      * @author solidwang
      * @date 2012-11-13
     */
    public List<?> query(String hql, int pageNo, int pageSize);

    /**
     * <p>
     * Description: 指定参数的分页查询满足条件的持久化对象
     * </p>
     * @param hql 查询sql
     * @param pageNo 页号
     * @param pageSize 每页条数
     * @param map 参数map
     * @return
     * @author solidwang
     * @date 2012-11-13
     */
    public List<?> query(String hql, int pageNo, int pageSize, Map<?, ?> map);

    /**
      * <p>
      * Description:更新指定对象
      * </p>
      * @param obj
      * @author MR.Jin
      * @date 2012-12-11
     */
    public boolean update(Object obj);
    /**
      * <p>
      *     Description: 保存或更新指定的持久化对象
      * </p>
      * @param obj
      * @author solidwang
      * @date 2012-11-13
     */
    public boolean saveOrUpdate(Object obj);
    /**
      * <p>
      * Description:批量保存或修改
      * </p>
      * @param collection
      * @author MR.Jin
      * @date 2012-12-10
     */
    public boolean saveOrUpdateAll(Collection<Object> collection);
    /**
     * <p>
     * Description: 删除指定ID的持久化对象
     * </p>
     * @param clazz
     * @param id
     * @author solidwang
     * @date 2012-11-13
     */
    public boolean delById(Class<?> clazz, Long id);
    /**
      * <p>
      * Description:保存一个对象
      * </p>
      * @param obj
      * @return
      * @author MR.Jin
      * @date 2012-12-27
     */
    public Serializable save(Object obj);
    /**
      * <p>
      * Description:删除一个实体
      * </p>
      * @param obj
      * @return
      * @author MR.Jin
      * @date 2012-12-27
     */
    public boolean delete(Object obj);

    public List querySql(String sql);

    public boolean executeSql(String sql);

}
