package com.kakaobean.core.unit;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.junit.jupiter.MockitoExtension;

@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(MockitoExtension.class)
public class UnitTest {
}
