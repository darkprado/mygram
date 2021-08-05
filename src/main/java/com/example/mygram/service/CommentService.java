package com.example.mygram.service;

import com.example.mygram.dto.CommentDTO;
import com.example.mygram.entity.Comment;
import com.example.mygram.entity.Post;
import com.example.mygram.entity.User;
import com.example.mygram.exceptions.PostNotFoundException;
import com.example.mygram.repository.CommentRepository;
import com.example.mygram.repository.PostRepository;
import com.example.mygram.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                                "Post not found for username: " + user.getUsername()
                        )
                );
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUsername(user.getUsername());
        comment.setUserId(user.getId());
        comment.setMessage(commentDTO.getMessage());
        LOGGER.info("Saving comment for Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                                "Username: " + username + ", not found"
                        )
                );
    }

}
