
package com.googlecode.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Thread safe implementation for MD5, SHA-1 digest.
 */
public final class SafeDigest {
    
    
    private SafeDigest() {
    }
	
	private static ThreadLocal<MessageDigest> sha1Digest = 
	        new ThreadLocal<MessageDigest>() {
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("SHA-1");
			
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
	};
	
    private static ThreadLocal<MessageDigest> md5Digest = 
            new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };
    
	/**
	 * Calculates SHA-1 digest from specified message with salt.
	 * Thread safe.
	 * 
	 * @param message  message to find digest for
	 * @param salt     at least 64 bit salt is recommended
	 * @return         message digest
	 */
	public static String digest(String message, byte[] salt) {
		try {
			byte[] bytesOfMessage = message.getBytes("UTF-8");
			final MessageDigest digest = sha1Digest.get();
			digest.reset();
			digest.update(salt);
			return StringHelpers.byteArray2Hex(digest.digest(bytesOfMessage));
		
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}	
	
    /**
     * Calculates MD5 digest from specified message.
     * Thread safe.
     * 
     * @param message message to calculate digest for
     * @return 
     */
    public static String digestMD5(String message) {
        try {
            byte[] bytesOfMessage = message.getBytes("UTF-8");
            return StringHelpers.byteArray2Hex(md5Digest.get().digest(
                    bytesOfMessage));
        
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
	/**
	 * Calculates SHA-1 digest from specified message.
	 * Thread safe.
	 * 
	 * @param message message to calculate digest for
	 * @return 
	 */
	public static String digest(String message) {
		try {
			byte[] bytesOfMessage = message.getBytes("UTF-8");
			return StringHelpers.byteArray2Hex(sha1Digest.get().digest(
			        bytesOfMessage));
		
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
	    System.out.println("SHA-1: " + digest("test"));
	}

}
