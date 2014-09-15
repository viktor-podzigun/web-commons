
package com.googlecode.common.dao;


public interface SequenceRepo {

    /**
     * Generates and returns next sequence number for the given sequence name.
     * 
     * @param name  unique sequence name
     * @return      next sequence number
     * 
     * @see SequenceRepo#nextInt(String)
     */
    public long next(String name);
    
    /**
     * Generates and returns next sequence as integer number 
     * for the given sequence name.
     * 
     * @param name  unique sequence name
     * @return      next sequence as integer number
     * 
     * @see SequenceRepo#next(String)
     */
    public int nextInt(String name);
    
}
