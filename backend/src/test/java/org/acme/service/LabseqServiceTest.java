package org.acme.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class LabseqServiceTest {

	@Inject
	LabseqService labseqService;

	@InjectMock
	RedisService redisService;

	@BeforeEach
	void setup() {
		reset(redisService);
	}

	@Test
	void testBaseCases() {
		assertEquals(BigInteger.ZERO, labseqService.calculate(0));
		assertEquals(BigInteger.ONE, labseqService.calculate(1));
		assertEquals(BigInteger.ZERO, labseqService.calculate(2));
		assertEquals(BigInteger.ONE, labseqService.calculate(3));

		verify(redisService, never()).getValue(any());
		verify(redisService, never()).setValue(any(), any());
	}

	@Test
	void testRecursiveCalculationWithoutCache() {
		// Simulate no cached value at all
		when(redisService.getValue(any())).thenReturn(Optional.empty());

		BigInteger result = labseqService.calculate(6); // should trigger recursive logic

		assertEquals(BigInteger.valueOf(4), result); // labseq(6) = labseq(2)+labseq(3) + labseq(1)+labseq(2) = 0+1+1+0
														// = 2, then labseq(4)=1, labseq(5)=3, so 4

		verify(redisService).setValue("labseq:4", "1");
		verify(redisService).setValue("labseq:5", "3");
		verify(redisService).setValue("labseq:6", "4");
	}

	@Test
	void testNegativeInputThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> labseqService.calculate(-1));
	}
}
