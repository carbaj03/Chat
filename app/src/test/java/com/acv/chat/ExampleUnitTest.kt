package com.acv.chat

import arrow.atomic.AtomicBoolean
import arrow.core.raise.Raise
import arrow.optics.Optional
import com.acv.chat.arrow.optics.get
import com.acv.chat.arrow.optics.getOrNull
import com.acv.chat.arrow.optics.invoke
import com.acv.chat.arrow.optics.last
import com.acv.chat.components.audioButton
import com.acv.chat.components.galleryButton
import com.acv.chat.components.input.text
import com.acv.chat.components.onClick
import com.acv.chat.components.photoButton
import com.acv.chat.components.send
import com.acv.chat.components.text
import com.acv.chat.components.value
import com.acv.chat.domain.App
import com.acv.chat.domain.AppOptics
import com.acv.chat.domain.DependenciesMock
import com.acv.chat.domain.DomainError
import com.acv.chat.domain.Media
import com.acv.chat.domain.NavigatorImpl
import com.acv.chat.domain.PhotoServiceMock
import com.acv.chat.domain.Store
import com.acv.chat.domain.screen
import com.acv.chat.domain.screen.Screen
import com.acv.chat.domain.screen.bottomBar
import com.acv.chat.domain.screen.destroy
import com.acv.chat.domain.screen.error
import com.acv.chat.domain.screen.files
import com.acv.chat.domain.screen.home
import com.acv.chat.domain.screen.input
import com.acv.chat.domain.screen.messages
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.experimental.ExperimentalTypeInference

class ExampleUnitTest {

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `should stop and update correctly`(): TestResult = test {
    val di = DependenciesMock(photoService = PhotoServiceMock())
    val navigator = with(di) { NavigatorImpl() }

    di.store {
      navigator.toHome()

      screen.home test {
        advanceUntilIdle()
        messages.last().text.value assert "Hola My Friend"
        input.text.value set "abc"
        advanceUntilIdle()
        bottomBar.galleryButton.onClick()
        advanceUntilIdle()
        files assertOn { size == 1 }
        bottomBar.photoButton.onClick()
        advanceUntilIdle()
        files assertOn { size == 2 && last() is Media.Image }
        bottomBar.audioButton.onClick()
        advanceUntilIdle()
        input.text.value assert "abc"
        bottomBar.audioButton.onClick()
        advanceUntilIdle()
        input.text.value assert "abc asfdsadf"
        bottomBar.send.onClick()
        advanceUntilIdle()
        messages.last().text.value assert "Prompt abc asfdsadf"
        destroy()
      }
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `should show error`(): TestResult = test {
    val di = DependenciesMock(photoService = PhotoServiceMock(null))
    val navigator = with(di) { NavigatorImpl() }

    di.store {
      navigator.toHome()

      screen.home test {
        bottomBar.photoButton.onClick()
        advanceTimeBy(1000)
        error assertOn { isNotEmpty() }
        advanceTimeBy(5)
        error.assertNull()
      }
    }
  }
}

@OptIn(ExperimentalTypeInference::class)
inline fun test(
  @BuilderInference crossinline block: context(TestScope, Raise<DomainError>) () -> Unit
): TestResult =
  runTest {
    block(this@runTest, TestRaise(false))
  }

inline operator fun Store<App>.invoke(block: context(AppOptics, Store<App>) () -> Unit) {
  block(AppOptics, this)
}

context(Store<A>, Raise<DomainError>)
inline infix fun <A, reified B : Screen> Optional<A, B>.test(f: context(Optional<A, B>) () -> Unit) {
  assertScreen()
  f(this)
}

context(Store<A>, Raise<DomainError>)
@JvmName("testOptional")
inline fun <A, reified B : Screen> Optional<A, B>.assertScreen() {
  getOrNull() ?: raise(DomainError.UnknownDomainError("Screen not found : ${B::class.java.simpleName}"))
}

context(Store<A>, Raise<DomainError>)
inline fun <A, B> Optional<A, B>.assertNull() {
  assert(getOrNull() == null)
}

context(Store<A>, Raise<DomainError>)
inline infix fun <A, B> Optional<A, B>.assertOn(block: B.() -> Boolean) {
  assert(block(get()))
}

context(Store<A>, Raise<DomainError>)
inline infix fun <A, B> Optional<A, B>.assert(b: B) {
  Assert.assertEquals(b, get())
}

class TestRaise(@PublishedApi internal val isTraced: Boolean) : Raise<Any?> {
  private val isActive = AtomicBoolean(true)

  @PublishedApi
  internal fun complete(): Boolean = isActive.getAndSet(false)
  override fun raise(r: Any?): Nothing = throw Exception(r.toString())
}