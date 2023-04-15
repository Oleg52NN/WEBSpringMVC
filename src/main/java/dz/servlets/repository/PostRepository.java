package dz.servlets.repository;

import dz.servlets.exception.NotFoundException;
import dz.servlets.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final AtomicLong idCounter;
    private final ConcurrentHashMap<Long, String> postMap;

    public PostRepository() {
        idCounter = new AtomicLong(1);
        postMap = new ConcurrentHashMap<Long, String>();
    }

    public List all() {
        List<String> list = new ArrayList<>();
        for (Long index : postMap.keySet()) {
            if (index > 0) {
                list.add(postMap.get(index));
            }
        }
        return list;
    }

    public Optional<Post> getById(long id) {
        if (postMap.containsKey(id) && id > 0) {
            return Optional.of(new Post(id, postMap.get(id)));
        } else {
            return Optional.empty();
        }
    }

    public Post save(Post post) {

        long valId = post.getId();
        if (valId != 0 && postMap.containsKey(-valId)) {
            throw new NotFoundException();
        }
        if (valId > 0) {
            if (valId >= idCounter.get()) {
                postMap.put(idCounter.get(), post.getContent());
                idCounter.incrementAndGet();
                return post;
            } else {
                postMap.put(valId, post.getContent());
                return post;
            }
        } else if (valId == 0) {
            postMap.put(idCounter.get(), post.getContent());
            idCounter.incrementAndGet();
            return post;
        } else {
            return null;
        }
    }

    public void removeById(long id) {
        if (!postMap.containsKey(id) || postMap.containsKey(-id) || id < 0) {
            throw new NotFoundException();
        } else {
            postMap.put(-id, postMap.get(id));
        }
        postMap.remove(id);
    }
}
