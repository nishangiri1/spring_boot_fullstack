package com.example.Nishan;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TestcontainersTest extends AbstractTestcontainers {
	@Test
	void canStartPostgresDB() {
		assertThat(postgreSQLContainer.isRunning()).isTrue();
		assertThat(postgreSQLContainer.isCreated()).isTrue();
	}

}
