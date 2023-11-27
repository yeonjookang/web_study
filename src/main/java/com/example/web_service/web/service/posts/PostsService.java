package com.example.web_service.web.service.posts;

import com.example.web_service.web.domain.posts.Posts;
import com.example.web_service.web.domain.posts.PostsRepository;
import com.example.web_service.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        posts.update(requestDto.getTitle(),requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id =" + id));

        return new PostsResponseDto(entity);
    }

    public PostsResultDto findAll(){
        List<Posts> posts = postsRepository.findAll();

        List<PostsResponseDto> collect = posts.stream()
                .map(p -> new PostsResponseDto(p))
                .collect(Collectors.toList());

        return new PostsResultDto(collect);
    }

    public Long delete(Long id){
        Optional<Posts> findPosts = postsRepository.findById(id);
        Posts posts = findPosts.get();

        postsRepository.delete(posts);

        return id;
    }

    @Transactional
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
}
