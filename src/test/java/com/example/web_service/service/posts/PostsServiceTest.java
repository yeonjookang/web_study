package com.example.web_service.service.posts;

import com.example.web_service.web.domain.posts.Posts;
import com.example.web_service.web.domain.posts.PostsRepository;
import com.example.web_service.web.dto.PostsResponseDto;
import com.example.web_service.web.dto.PostsResultDto;
import com.example.web_service.web.dto.PostsSaveRequestDto;
import com.example.web_service.web.dto.PostsUpdateRequestDto;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostsServiceTest {
    @Autowired
    PostsService postsService;

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void setUp(){
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
    }

    @Test
    public void 게시글_하나조회() throws Exception{
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        Posts savePost = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        PostsResponseDto findPostsDto = postsService.findById(savePost.getId());

        assertThat(findPostsDto.getTitle()).isEqualTo("title");
    }
    @Test
    public void 게시글_전체조회() throws Exception{
        // 게시글 전체 조회
        PostsResultDto<List<PostsResponseDto>> resultDto = postsService.findAll();

        // 결과가 null이 아닌지 확인
        assertNotNull(resultDto);

        // 포함된 데이터가 null이 아닌지 확인
        List<PostsResponseDto> posts = resultDto.getData();
        assertNotNull(posts);

        // 만약 조회된 게시글의 개수를 검증하려면 다음과 같이 검증할 수 있습니다.
        // assertEquals(예상 개수, posts.size());

        // 각 게시글의 필드를 검증
        for (PostsResponseDto post : posts) {
            assertNotNull(post.getId());
            assertNotNull(post.getTitle());
            assertNotNull(post.getContent());
            assertNotNull(post.getAuthor());
        }
    }
    @Test
    public void 게시글_수정() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        Posts savePost = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        PostsUpdateRequestDto postsUpdateRequestDto = new PostsUpdateRequestDto("title_update", "content_update");

        //when
        Long update = postsService.update(savePost.getId(), postsUpdateRequestDto);

        //then
        assertThat(postsService.findById(savePost.getId()).getTitle()).isEqualTo("title_update");
        assertThat(postsService.findById(savePost.getId()).getContent()).isEqualTo("content_update");
    }

    @Test
    public void 게시글_삭제() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        Posts savePost = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        //when
        Long deletedId = postsService.delete(savePost.getId());

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            postsService.findById(deletedId);
        });

    }
}