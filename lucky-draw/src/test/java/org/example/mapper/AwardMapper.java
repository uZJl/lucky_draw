package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.base.BaseMapper;
import org.example.model.Award;

@Mapper
public interface AwardMapper extends BaseMapper<Award> {
}