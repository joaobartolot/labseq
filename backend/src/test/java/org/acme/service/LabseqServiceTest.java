package org.acme.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LabseqServiceTest {

	private RedisService redisService;
	private LabseqService labseqService;

	@BeforeEach
	void setUp() {
		redisService = Mockito.mock(RedisService.class);
		labseqService = new LabseqService(redisService);
	}

	@Test
	void testCalculateWithCachedValue() {
		when(redisService.getValue("labseq:5")).thenReturn(Optional.of("2"));

		BigInteger result = labseqService.calculate(5);

		assertEquals(BigInteger.valueOf(2), result);
		verify(redisService, times(1)).getValue("labseq:5");
	}

	@Test
	void testCalculateWithoutCachedValue() {
		when(redisService.getValue("labseq:6")).thenReturn(Optional.empty());

		BigInteger result = labseqService.calculate(6); // labseq(6) = labseq(2) + labseq(3) = 0 + 1 = 1

		assertEquals(BigInteger.ONE, result);
		verify(redisService, times(1)).getValue("labseq:6");
	}

	@Test
	void testCalculateNegativeIndexThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> labseqService.calculate(-1));
	}

	@Test
	void testBaseCases() {
		when(redisService.getValue(anyString())).thenReturn(Optional.empty());

		assertEquals(BigInteger.ZERO, labseqService.calculate(0));
		assertEquals(BigInteger.ONE, labseqService.calculate(1));
		assertEquals(BigInteger.ZERO, labseqService.calculate(2));
		assertEquals(BigInteger.ONE, labseqService.calculate(3));
	}
}
