package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    UserEntity findByUsername(String username);
    
    void updateUserToken(UserEntity user);

    void insertUser(UserEntity user);
    
    List<UserEntity> findAllWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    
    long countTotal();

    List<UserEntity> findAll();
}