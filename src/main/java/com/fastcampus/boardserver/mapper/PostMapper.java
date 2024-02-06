package com.fastcampus.boardserver.mapper;

import com.fastcampus.boardserver.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    int register(PostDTO postDTO);

    List<PostDTO> selectMyProducts(int accountId);

    void updateProducts(PostDTO postDTO);

    void deleteProduct(int productId);

}
