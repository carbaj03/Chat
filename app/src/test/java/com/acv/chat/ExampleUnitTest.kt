package com.acv.chat

import arrow.atomic.AtomicBoolean
import arrow.core.raise.Raise
import arrow.optics.Optional
import com.acv.chat.arrow.optics.get
import com.acv.chat.domain.App
import com.acv.chat.domain.AppOptics
import com.acv.chat.domain.Dependencies
import com.acv.chat.domain.DependenciesMock
import com.acv.chat.domain.Store
import com.acv.chat.domain.StoreImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import com.acv.chat.domain.DomainError
import org.junit.Test
import kotlin.experimental.ExperimentalTypeInference

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `should stop and update correctly`() = AppOptics test {
    val store: Store<App> = StoreImpl(App())
    val di = DependenciesMock()

    store(di) {

//      screen set createHome()
//
//      screen.home.init()
//
//      advanceUntilIdle()
//
//      screen.home.stop()


    }
  }
}

@OptIn(ExperimentalTypeInference::class)
inline infix fun AppOptics.test(
  @BuilderInference crossinline block: context(AppOptics, TestScope, Raise<DomainError>) () -> Unit
): TestResult =
  runTest() {
    block(App, this@runTest, TestRaise(false))
  }

inline operator fun Store<App>.invoke(deps: Dependencies, block: context(AppOptics, Store<App>) () -> Unit) {
  block(AppOptics, this)
}

context(Store<A>)
inline infix fun <A, B> Optional<A, B>.asserEquals(b: B) {
  Assert.assertEquals(b, get())
}

context(Store<A>)
inline fun <A, B> Optional<A, B>.assert() {
  get()
}

class TestRaise(@PublishedApi internal val isTraced: Boolean) : Raise<Any?> {
  private val isActive = AtomicBoolean(true)

  @PublishedApi
  internal fun complete(): Boolean = isActive.getAndSet(false)
  override fun raise(r: Any?): Nothing = throw Exception(r.toString())
}