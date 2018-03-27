package com.github.timgoes1997.java.dao.interfaces;

import com.github.timgoes1997.java.entity.tag.Tag;

public interface TagDAO {

    void create(Tag tag);

    void remove(Tag tag);

    Tag find(long id);

    Tag findTagByName(String tagName);

}
