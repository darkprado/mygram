package com.example.mygram.facade;

import org.springframework.stereotype.Component;

import com.example.mygram.dto.CommentDTO;
import com.example.mygram.entity.Comment;

/**
 * @author s.melekhin
 * @since 04 май 2022 г.
 */
@Component
public class CommentFacade {

    public CommentDTO commentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUsername(comment.getUsername());
        commentDTO.setMessage(comment.getMessage());
        return commentDTO;
    }

}
