package com.chesire.gattai

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties = ["mal.client-id=test-value"])
class GattaiApplicationTests {

    @Test
    fun contextLoads() {
        assert(true)
    }
}
