package com.github.timgoes1997.java.services.beans.interfaces;

import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.tag.Tag;

import java.util.List;

public interface TagService {
    List<Message> getTagMessages(String tag);
    Tag createTag(String tag);
}
