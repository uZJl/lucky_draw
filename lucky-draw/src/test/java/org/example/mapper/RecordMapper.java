package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.base.BaseMapper;
import org.example.model.Record;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}