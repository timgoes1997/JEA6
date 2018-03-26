package dao.interfaces;

import entity.tag.Tag;

public interface TagDAO {

    void create(Tag tag);

    void remove(Tag tag);

    Tag find(long id);

    Tag findTagByName(String tagName);

}
