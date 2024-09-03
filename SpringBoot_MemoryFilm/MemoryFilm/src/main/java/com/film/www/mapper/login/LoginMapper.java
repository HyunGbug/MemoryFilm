package com.film.www.mapper.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {

	int countById(@Param("id") String id);
	
	int countByUserName(@Param("username")String username);
	
	int countByEmail(@Param("email")String email);
	
	int insertUser(User user);
}
