package org.acme.service;

import java.util.Optional;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RedisService {

	private final ValueCommands<String, String> commands;

	public RedisService(RedisDataSource redisDataSource) {
		this.commands = redisDataSource.value(String.class);
	}

	public void setValue(String key, String value) {
		commands.set(key, value);
	}

	public Optional<String> getValue(String key) {
		return Optional.ofNullable(commands.get(key));
	}

	public void deleteValue(String key) {
		commands.getdel(key);
	}
}
