package org.onap.ccsdk.sli.plugins.restapicall;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestRetryException {

	@Test
	public void RetryExceptionTest() {
		assertNotNull(new RetryException("JUnit Test"));
	}
	

}
