package org.example.base;

public interface BaseMapper<T> {

    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Integer id);

    //根据主键修改其他非主键的字段
    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}