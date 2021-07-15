package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.base.BaseMapper;
import org.example.model.Setting;

@Mapper
public interface SettingMapper extends BaseMapper<Setting> {
}