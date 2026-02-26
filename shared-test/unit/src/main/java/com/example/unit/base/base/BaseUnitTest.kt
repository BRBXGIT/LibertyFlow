package com.example.unit.base.base

import io.mockk.unmockkAll
import org.junit.After
import org.junit.Rule

abstract class BaseUnitTest {

   @get:Rule
   val mainDispatcherRule = MainDispatcherRule()

    @After
    open fun tearDown() {
        unmockkAll()
    }
}