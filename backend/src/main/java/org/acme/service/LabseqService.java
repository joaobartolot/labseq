package org.acme.service;

import java.math.BigInteger;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LabseqService {
	public final static String KEY_PREFIX = "labseq:";

	@Inject
	RedisService redisService;

	public BigInteger calculate(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Index must be non-negative");
		}

		if (index == 0 || index == 2) {
			return BigInteger.ZERO;
		} else if (index == 1 || index == 3) {
			return BigInteger.ONE;
		}

		String key = KEY_PREFIX + index;

		Optional<String> cachedValue = redisService.getValue(key);
		if (cachedValue.isPresent()) {
			return new BigInteger(cachedValue.get());
		}

		BigInteger newValue = calculate(index - 4).add(calculate(index - 3));
		redisService.setValue(key, newValue.toString());

		return newValue;
	}
}
