package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.base.BaseMapper;
import org.example.model.Member;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}