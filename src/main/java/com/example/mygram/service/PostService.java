package com.example.mygram.service;

import com.example.mygram.dto.PostDTO;
import com.example.mygram.entity.ImageModel;
import com.example.mygram.entity.Post;
import com.example.mygram.entity.User;
import com.example.mygram.exceptions.PostNotFoundException;
import com.example.mygram.repository.ImageModelRepository;
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
public class PostService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageModelRepository imageModelRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, ImageModelRepository imageModelRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageModelRepository = imageModelRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setTitle(postDTO.getTitle());
        post.setCaption(postDTO.getCaption());
        post.setLikes(0);
        post.setLocation(postDTO.getLocation());
        LOGGER.info("Saving post for user: {}", user.getUsername());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByDateCreatedDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post by user: " + user.getUsername() + " not found")
                );
    }

    public List<Post> getAllPostsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByDateCreatedDesc(user);
    }

    public Post likePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Post not found")
                );
        Optional<String> userLiked = post.getLikeUsers()
                .stream().filter(u -> u.equals(username)).findAny();
        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikeUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikeUsers().add(username);
        }
        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageModelRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageModelRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username: " + username + ", not found")
                );
    }

}
