package it.xm.android.smartWeather.repository

import it.xm.android.smartWeather.CoroutinesTestRule
import it.xm.android.smartWeather.TestUtil
import it.xm.android.smartWeather.model.data.City
import it.xm.android.smartWeather.model.data.WeatherData
import it.xm.android.smartWeather.network.OWMRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WeatherDataRepositoryTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()
    @Mock
    private val remoteRepository = mock(OWMRemoteDataSource::class.java)

    @Test
    fun fetchWeatherDataByCityName_null() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val  methodUnderTest = WeatherDataRepository(remoteRepository)
        `when`(remoteRepository.fetchOWeatherMapData(anyString())).thenReturn(null)

        val results = methodUnderTest.fetchWeatherDataByCityName("Lisbona")

        assertEquals(null, results)
    }

    @Test
    fun fetchWeatherDataByCityName_full() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val  methodUnderTest = WeatherDataRepository(remoteRepository)
        val now = System.currentTimeMillis()
        val weatherData = WeatherData(listOf(TestUtil().mockWeatherData(now)), City("Lisbona"))
        `when`(remoteRepository.fetchOWeatherMapData(anyString())).thenReturn(weatherData)

        val results = methodUnderTest.fetchWeatherDataByCityName("Lisbona")
        verify(remoteRepository).fetchOWeatherMapData("Lisbona")

        assertNotEquals(results, null)
        assertThat(results!![0].timeInSecs, `is`(now))
        assertThat(results[0].weather[0].icon, `is`("icon"))
        assertThat(results[0].main.temp, `is`(23.4f))
    }
}