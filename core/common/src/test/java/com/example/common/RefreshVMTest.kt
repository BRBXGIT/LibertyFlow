package com.example.common

import app.cash.turbine.test
import com.example.common.refresh.RefreshEffect
import com.example.common.refresh.RefreshVM
import com.example.unit.base.base.BaseUnitTest
import com.example.unit.base.flow.testState
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RefreshVMTest: BaseUnitTest() {

    private lateinit var vm: RefreshVM

    @Before
    fun setUp() {
        vm = RefreshVM()
    }

    @Test
    fun `sendEffect sends effect`() = runTest {
        vm.refreshEffects.testState(skipInitial = false) {
            vm.sendEffect(RefreshEffect.RefreshFavorites)
            assertTrue(awaitItem() is RefreshEffect.RefreshFavorites)
        }
    }
}