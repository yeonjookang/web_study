package com.example.web_service.web.domain.posts;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@Transactional
public class PostsRepositoryTest    {
    @Autowired
    PostsRepository postsRepository;

    @Test
    public void 게시글저장_불러오기() throws Exception{
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("playduck@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록(){
        //given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        assertThat(posts.getCreatedDate()).isAfter(LocalDateTime.from(now));
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

    @Test
    public void 게시글_수정(){
        //given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        Posts savePost = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        //when
        //flush 할 필요 없음!
        savePost.update("title_update","content_update");

        //then
        Optional<Posts> optionalPosts = postsRepository.findById(savePost.getId());
        Posts updatePost = optionalPosts.get();

        assertThat(updatePost.getTitle()).isEqualTo("title_update");
        assertThat(updatePost.getContent()).isEqualTo("content_update");

    }

    @Test
    public void 게시글_전체조회(){
        //given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        Posts savePost = postsRepository.save(Posts.builder()
                .title("title1")
                .content("content1")
                .author("author1")
                .build());

        LocalDateTime now2 = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        Posts savePost2 = postsRepository.save(Posts.builder()
                .title("title2")
                .content("content2")
                .author("author2")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        assertThat(postsList.get(0).getTitle()).isEqualTo("title1");
        assertThat(postsList.get(1).getTitle()).isEqualTo("title2");
    }

    @Test
    public void 게시글_삭제(){
        //given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        Posts savePost = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        //when
        postsRepository.delete(savePost);

        //then
        postsRepository.findById(savePost.getId()).ifPresent(post -> {
            fail("게시글이 삭제되지 않았습니다.");
        });
    }
}
