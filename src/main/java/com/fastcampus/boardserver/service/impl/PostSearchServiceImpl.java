package com.fastcampus.boardserver.service.impl;

import com.fastcampus.boardserver.dto.PostDTO;
import com.fastcampus.boardserver.dto.request.PostSearchRequest;
import com.fastcampus.boardserver.exception.BoardServerException;
import com.fastcampus.boardserver.mapper.PostSearchMapper;
import com.fastcampus.boardserver.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostSearchServiceImpl implements PostSearchService {

    private final PostSearchMapper postSearchMapper;


    @Cacheable(unless = "#result == null", value = "getProducts", key = "'getProducts' + #postSearchRequest.getName() + #postSearchRequest.getCategoryId()")
    @Async // 성능향상을 위해 비동기로 호출
    @Override
    public List<PostDTO> getProducts(PostSearchRequest postSearchRequest) {
        List<PostDTO> postDTOList = null;
        try {
            postDTOList = postSearchMapper.selectPosts(postSearchRequest);
        } catch (RuntimeException e) {
            log.error("selectPosts 실패");
            throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return postDTOList;
    }

    public List<PostDTO> getPostByTag(String tagName) {
        List<PostDTO> postDTOList = null;
        try {
            postDTOList = postSearchMapper.getPostByTag(tagName);
        } catch (RuntimeException e) {
            log.error("getPostByTag 실패", e.getMessage());
        }
        return postDTOList;
    }
}
