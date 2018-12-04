package com.creedfreak.common.utility;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * This class contains the Utilities for a UUID or Unique Identifier
 *
 *  All credit for these methods go to.
 *  https://gist.github.com/jeffjohnson9046/c663dd22bbe6bb0b3f5e
 */
public class UuidUtil
{

	/**
	 * Converts a UUID to Binary[16]
	 * @param uuid The Unique ID to convert to binary 16
	 * @return The uuid in byte array form
	 */
    public static byte[] toBytes (UUID uuid)
    {
        ByteBuffer byteBuffer = ByteBuffer.wrap (new byte [16]);
        byteBuffer.putLong (uuid.getMostSignificantBits ());
        byteBuffer.putLong (uuid.getLeastSignificantBits ());
        return byteBuffer.array ();
    }

	/**
	 * Converts a byte array back into a Unique ID
	 * @param bytes The byte array to convert
	 * @return The UUID constructed from the byte array
	 */
	public static UUID fromBytes (byte[] bytes)
    {
        ByteBuffer byteBuffer = ByteBuffer.wrap (bytes);
        long mostSigLong = byteBuffer.getLong ();
        long leastSigLong = byteBuffer.getLong ();
        return new UUID (mostSigLong, leastSigLong);
    }
}
