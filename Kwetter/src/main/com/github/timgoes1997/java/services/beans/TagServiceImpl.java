package com.github.timgoes1997.java.services.beans;

import com.github.timgoes1997.java.dao.interfaces.MessageDAO;
import com.github.timgoes1997.java.dao.interfaces.TagDAO;
import com.github.timgoes1997.java.entity.message.Message;
import com.github.timgoes1997.java.entity.tag.Tag;
import com.github.timgoes1997.java.services.beans.interfaces.TagService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Stateless
public class TagServiceImpl implements TagService {

    @EJB
    private TagDAO tagDAO;

    @EJB
    private MessageDAO messageDAO;

    @Override
    public List<Message> getTagMessages(String tag) {
        try {
            Tag found = tagDAO.findTagByName(tag.toLowerCase());
            return messageDAO.findMessagesByTag(found);
        }catch (Exception e){
            throw new NotFoundException("Couldn't find any messages for this tag, has it been created yet, " +
                    "try making a message using this tag if that doesn't work please contact a administrator.");
        }
    }

    @Override
    public Tag createTag(String tag) {
        try {
            tagDAO.create(new Tag(tag));
            return tagDAO.findTagByName(tag);
        }catch (Exception e){
            throw new NotFoundException("Couldn't create new tag or find newly created tag, " +
                    "please contact a administrator if this occurs multiple times");
        }
    }
}
