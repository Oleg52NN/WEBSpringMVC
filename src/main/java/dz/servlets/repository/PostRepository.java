package dz.servlets.repository;

import dz.servlets.model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        List<String> list = new ArrayList<>(postMap.values());
        return list;
    }

    public Optional<Post> getById(long id) {
        return Optional.of(new Post(id, postMap.get(id)));
    }

    public Post save(Post post) {

        long valId = post.getId();

        if (valId != 0) {
            if (valId > idCounter.get() + 1) {
                valId = idCounter.get();
                idCounter.incrementAndGet();
            }
            postMap.put(valId, post.getContent());
            return post;
        } else {
            postMap.put(idCounter.get(), post.getContent());
            idCounter.incrementAndGet();
        }
        return post;
    }

    public void removeById(long id) {
        if (!postMap.containsKey(id)) {
            return;
        }
        postMap.remove(id);
    }
}
