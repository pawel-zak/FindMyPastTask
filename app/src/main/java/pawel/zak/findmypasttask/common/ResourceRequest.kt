package pawel.zak.findmypasttask.common

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ResourceRequest {
    suspend fun <R> safeRequest(
        withLoading: Boolean = true,
        request: suspend () -> R
    ) =
        flow {
            try {
                if (withLoading)
                    emit(Resource.Loading())
                val result = request()
                emit(Resource.Success(result))
            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        UiText.DynamicString(e.localizedMessage ?: "An unexpected error occurred")
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error(message = UiText.StringResource(pawel.zak.findmypasttask.R.string.internet_connection_error)))
            } catch (ex: UnknownHostException) {
                emit(Resource.Error(UiText.StringResource(pawel.zak.findmypasttask.R.string.network_connection_error)))
            } catch (ex: SocketTimeoutException) {
                emit(Resource.Error(UiText.StringResource(pawel.zak.findmypasttask.R.string.socket_timeout_error)))
            } catch (ex: SQLiteException) {
                emit(Resource.Error(UiText.StringResource(pawel.zak.findmypasttask.R.string.database_error)))
            } catch (ex: java.lang.NullPointerException) {
                emit(Resource.Error(UiText.StringResource(pawel.zak.findmypasttask.R.string.null_error)))
            }
        }
}