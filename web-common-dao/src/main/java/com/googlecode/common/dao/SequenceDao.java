
package com.googlecode.common.dao;

import org.springframework.data.repository.Repository;
import com.googlecode.common.dao.domain.SequenceEntity;


/**
 * {@link SequenceEntity} dao interface.
 */
public interface SequenceDao extends Repository<SequenceEntity, String>, 
        SequenceRepo {

}
