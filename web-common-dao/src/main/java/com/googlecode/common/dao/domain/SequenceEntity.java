
package com.googlecode.common.dao.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Represents sequence entity.
 */
@Document(collection="sequences")
public class SequenceEntity {

    @Id
    private String name;
    
    private long seq;
    
    
    public SequenceEntity() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getSeq() {
        return seq;
    }
    
    public void setSeq(long seq) {
        this.seq = seq;
    }

}
