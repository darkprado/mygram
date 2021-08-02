package com.example.mygram.repository;

import com.example.mygram.entity.Post;
import com.example.mygram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserOrderByDateCreatedDesc(User user);

//    List<Post> findAllByOrderByDateCreatedDesc(User user);

    Optional<Post> findPostByIdAndUser(Long id, User user);

}
