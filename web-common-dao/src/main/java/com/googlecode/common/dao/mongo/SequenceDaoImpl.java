
package com.googlecode.common.dao.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.googlecode.common.dao.SequenceRepo;
import com.googlecode.common.dao.domain.SequenceEntity;


public class SequenceDaoImpl implements SequenceRepo {

    @Autowired
    private MongoTemplate   mongoTemplate;

    
    public SequenceDaoImpl() {
    }
    
    @Override
    public long next(String name) {
        SequenceEntity seq = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(name)), 
                new Update().inc("seq", 1L), 
                SequenceEntity.class);
        
        return (seq != null ? seq.getSeq() : 0L);
    }
    
    @Override
    public int nextInt(String name) {
        return (int)next(name);
    }

}
