/**
 * Copyright © 2020 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heutelbeck.uuid;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import lombok.experimental.UtilityClass;

/**
 * 
 * This utility class provides convenience methods to convert UUIDs to a more
 * compact URL safe String representation.
 * 
 * @author dominic
 *
 */
@UtilityClass
public class Base64Id {
	private static final String THE_DECODED_BASE64_MUST_HAVE_D_BYTES_BUT_IT_WAS_D = "The identifier must have %d bytes, but it was %d.";
	private static final String THE_IDENTIFIER_IS_NOT_IN_BASE64 = "The Identifier is not in Base64.";
	private static final String THE_IDENTIFIER_MUST_NOT_BE_NULL = "The Identifier must not be null.";
	private static final int UUID_BYTES = Long.BYTES * 2;

	/**
	 * @return a base 64 encoded UUID
	 */
	public String randomID() {
		return ofUUID(UUID.randomUUID());
	}

	/**
	 * @param uuid a UUID
	 * @return a base 64 encoded UUID
	 */
	public String ofUUID(UUID uuid) {
		var bytes = new byte[UUID_BYTES];
		longToBytes(uuid.getLeastSignificantBits(), bytes, 0);
		longToBytes(uuid.getMostSignificantBits(), bytes, 8);
		return Base64.encodeBase64URLSafeString(bytes);
	}

	/**
	 * @param id a String
	 * @return true, if the String is a Base64 encoded UUID
	 */
	public static boolean isValid(String id) {
		if (id == null) {
			return false;
		} else if (!Base64.isBase64(id)) {
			return false;
		}

		try {
			if (Base64.decodeBase64(id).length != UUID_BYTES) {
				return false;
			}
		} catch (IllegalArgumentException e) {
			// Base64 syntax problem here
			return false;
		}
		return true;
	}

	/**
	 * @param id a String
	 * @throws IllegalArgumentException when the String is not a valid UUID in base 64
	 *                           encoding
	 * @return the decoded bytes of the Base64 Sting.
	 */
	public static byte[] validate(String id) {

		if (id == null) {
			throw new IllegalArgumentException(THE_IDENTIFIER_MUST_NOT_BE_NULL);
		} else if (!Base64.isBase64(id)) {
			throw new IllegalArgumentException(THE_IDENTIFIER_IS_NOT_IN_BASE64);
		}

		var bytes = Base64.decodeBase64(id);

		if (bytes.length != UUID_BYTES) {
			throw new IllegalArgumentException(String.format(THE_DECODED_BASE64_MUST_HAVE_D_BYTES_BUT_IT_WAS_D, UUID_BYTES,
					Base64.decodeBase64(id).length));
		}
		return bytes;
	}

	/**
	 * @param id a Base64 encoded UUID
	 * @return a UUID
	 */
	public UUID toUUID(String id) {
		var bytes = validate(id);
		var leastSigBits = bytesToLong(bytes, 0);
		var mostSigBits = bytesToLong(bytes, 8);
		return new UUID(mostSigBits, leastSigBits);
	}

	private void longToBytes(long l, byte[] bytes, final int offset) {
		for (int i = 7 + offset; i >= offset; i--) {
			bytes[i] = (byte) (l & 0xFF);
			l >>= 8;
		}
	}

	private long bytesToLong(final byte[] bytes, final int offset) {
		long result = 0;
		for (int i = offset; i < Long.BYTES + offset; i++) {
			result <<= Long.BYTES;
			result |= (bytes[i] & 0xFF);
		}
		return result;
	}
}
