package com.github.timgoes1997.java.dao;

import com.github.timgoes1997.java.dao.interfaces.TagDAO;
import com.github.timgoes1997.java.entity.tag.Tag;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class TagDAOImpl implements TagDAO{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Tag tag) {
        em.persist(tag);
    }

    @Override
    public void remove(Tag tag) {
        em.remove(tag);
    }

    @Override
    public Tag find(long id) {
        return null;
    }

    @Override
    public Tag findTagByName(String tagName) {
        return null;
    }

    @Override
    public List<Tag> getAllTags() {
        TypedQuery<Tag> query =
                em.createNamedQuery("Tag.findAll", Tag.class);
        return query.getResultList();
    }

    /**
     * For testing purposes
     * @param em entitymanager for unittesting this bean/dao
     */
    public void setEntityManager(EntityManager em){
        this.em = em;
    }
}
