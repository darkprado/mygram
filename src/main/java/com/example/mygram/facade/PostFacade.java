package com.example.mygram.facade;

import org.springframework.stereotype.Component;

import com.example.mygram.dto.PostDTO;
import com.example.mygram.entity.Post;

/**
 * @author s.melekhin
 * @since 04 май 2022 г.
 */
@Component
public class PostFacade {

    public PostDTO postToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setId(post.getId());
        postDTO.setCaption(post.getCaption());
        postDTO.setLikes(post.getLikes());
        postDTO.setUserLikes(post.getLikeUsers());
        postDTO.setLocation(post.getLocation());
        postDTO.setTitle(post.getTitle());
        return postDTO;
    }

}
