package org.acme.service;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LabseqService {
	public final static String KEY_PREFIX = "labseq:";

	private final RedisService redisService;

	@Inject
	public LabseqService(RedisService redisService) {
		this.redisService = redisService;
	}

	public BigInteger calculate(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Index must be non-negative");
		}

		if (index == 0 || index == 2)
			return BigInteger.ZERO;
		if (index == 1 || index == 3)
			return BigInteger.ONE;

		String key = KEY_PREFIX + index;

		Optional<String> cachedValue = redisService.getValue(key);
		if (cachedValue.isPresent()) {
			return new BigInteger(cachedValue.get());
		}

		Deque<BigInteger> window = new ArrayDeque<>(4);
		window.addLast(BigInteger.ZERO); // labseq(0)
		window.addLast(BigInteger.ONE); // labseq(1)
		window.addLast(BigInteger.ZERO); // labseq(2)
		window.addLast(BigInteger.ONE); // labseq(3)

		for (int i = 4; i <= index; i++) {
			BigInteger next = window.peekFirst().add(getNth(window, 1)); // labseq(i) = labseq(i-4) + labseq(i-3)
			window.pollFirst();
			window.addLast(next);
		}

		return window.peekLast();
	}

	private BigInteger getNth(Deque<BigInteger> deque, int n) {
		return deque.stream().skip(n).findFirst().orElse(BigInteger.ZERO);
	}

}
