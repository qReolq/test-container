package qreol.project.testcontainer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qreol.project.testcontainer.model.Post;
import qreol.project.testcontainer.model.excpetion.PostNotFoundException;
import qreol.project.testcontainer.repository.PostRepository;
import qreol.project.testcontainer.service.PostService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post getById(long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.addView(id);

        return post;
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll(
                Sort.by("id").descending()
        );
    }

    @Override
    @Transactional
    public Post create(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public void delete(long id) {
        postRepository.deleteById(id);
    }

}
