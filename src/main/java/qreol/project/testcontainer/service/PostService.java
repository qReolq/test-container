package qreol.project.testcontainer.service;

import qreol.project.testcontainer.model.Post;

import java.util.List;

public interface PostService {

    Post getById(long id);

    List<Post> getAll();

    Post create(Post post);

    void delete(long id);

}
