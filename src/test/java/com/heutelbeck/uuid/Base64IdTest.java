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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class Base64IdTest {

	private static final String TOO_SHORT_BASE64 = "x";
	private static final String TOO_LONG_BASE64 = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	private static final String NON_BASE64 = "ßßß";
	private static final String VALID_BASE64_1 = "wwwwwwwwwwwwwwwwwwwwww";
	private static final String INVALID_RIGHT_LENGTH = "y@@@yyyyyyyyyyyyyyyyyy";

	@Test
	public void factoryReturnsValidBase64Id() {
		var uuid = UUID.randomUUID();
		var encoded = Base64Id.ofUUID(uuid);
		var decoded = Base64Id.toUUID(encoded);
		assertEquals(uuid, decoded, "decoded not equal to encoded id");
	}

	@Test
	public void encodedDecodesBack() {
		assertTrue(Base64Id.isValid(Base64Id.randomID()), "factory returned invalid id");
	}

	@Test
	public void nullIsInvalid() {
		assertFalse(Base64Id.isValid(null), "null was valid");
	}

	@Test
	public void invalidRightLengthIsInvalid() {
		assertFalse(Base64Id.isValid(INVALID_RIGHT_LENGTH), "invalid was valid");
	}

	@Test
	public void nonBase64StringIsInvalid() {
		assertFalse(Base64Id.isValid(NON_BASE64), "non Base64 String was valid");
	}

	@Test
	public void shortLengthIsInvalid() {
		assertFalse(Base64Id.isValid(TOO_SHORT_BASE64), "non Base64 String was valid");
	}

	@Test
	public void longLengthIsInvalid() {
		assertFalse(Base64Id.isValid(TOO_LONG_BASE64), "non Base64 String was valid");
	}

	@Test
	public void nullThrowsInvalid() {
		assertThrows(IllegalArgumentException.class, () -> Base64Id.validate(null), "null must not be valid");
	}

	@Test
	public void nonBase64StringThrowsInvalid() {
		assertThrows(IllegalArgumentException.class, () -> Base64Id.validate(NON_BASE64),
				"String with non Base64 characters must not be valid");
	}

	@Test
	public void shortLengthThrowsInvalid() {
		assertThrows(IllegalArgumentException.class, () -> Base64Id.validate(TOO_SHORT_BASE64),
				"String with too little characters must not be valid");
	}

	@Test
	public void longLengthThrowsInvalid() {
		assertThrows(IllegalArgumentException.class, () -> Base64Id.validate(TOO_LONG_BASE64),
				"String with too many characters must not be valid");
	}

	@Test
	public void validValidates() {
		assertNotNull(Base64Id.validate(VALID_BASE64_1), "valid String must validate sucessfully");
	}

}
