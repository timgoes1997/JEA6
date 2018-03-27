package dao;

import dao.interfaces.TagDAO;
import entity.tag.Tag;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
