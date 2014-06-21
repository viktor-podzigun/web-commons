
package com.googlecode.common.io;

import java.util.zip.ZipEntry;


/**
 * Filter for ZIP entries.
 */
public interface ZipEntryFilter {

    /**
     * Tests whether or not the specified abstract pathname should be
     * included in a pathname list.
     *
     * @param  pathname  The abstract pathname to be tested
     * @return  <code>true</code> if and only if <code>pathname</code>
     *          should be included
     */
    public boolean accept(ZipEntry pathname);

}
