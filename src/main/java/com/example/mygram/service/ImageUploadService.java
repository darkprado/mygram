package com.example.mygram.service;

import com.example.mygram.entity.ImageModel;
import com.example.mygram.entity.Post;
import com.example.mygram.entity.User;
import com.example.mygram.exceptions.ImageNotFoundException;
import com.example.mygram.repository.ImageModelRepository;
import com.example.mygram.repository.PostRepository;
import com.example.mygram.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadService.class);

    private final ImageModelRepository imageModelRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ImageUploadService(ImageModelRepository imageModelRepository, UserRepository userRepository, PostRepository postRepository) {
        this.imageModelRepository = imageModelRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        LOGGER.info("Uploading image profile to user {}", user.getUsername());
        ImageModel userProfileImage = imageModelRepository.findByUserId(user.getId())
                .orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageModelRepository.delete(userProfileImage);
        }
        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageModelRepository.save(imageModel);
    }

    public ImageModel uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts().stream().filter(p -> p.getId() == (postId)).collect(toSingleCollector());
        ImageModel imageModel = new ImageModel();
        imageModel.setPostId(post.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        LOGGER.info("Uploading image to Post {}", post.getId());
        return imageModelRepository.save(imageModel);
    }

    public ImageModel getImageToUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        ImageModel imageModel = imageModelRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    public ImageModel getImageToPost(Long postId) {
        ImageModel imageModel = imageModelRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post " + postId));
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    private <T> Collector<T, ?, T> toSingleCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username: " + username + ", not found")
                );
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            os.write(buffer, 0, count);
        }
        try {
            os.close();
        } catch (IOException exception) {
            LOGGER.error("Cannot compress bytes");
        }
        byte[] bytes = os.toByteArray();
        System.out.println("Compressed image byte size - " + bytes.length);
        return bytes;
    }

    private byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                os.write(buffer, 0, count);
            }
            os.close();
        } catch (IOException | DataFormatException exception) {
            LOGGER.error("Cannot decompress bytes");
        }
        return os.toByteArray();
    }

}
