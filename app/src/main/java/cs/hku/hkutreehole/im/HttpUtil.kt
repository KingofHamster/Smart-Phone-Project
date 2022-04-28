package cs.hku.hkutreehole.im

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

object HttpUtil {
    private const val TIME_OUT = 30000

    fun getAllUser(onResult: (List<User>?) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val data = requestAllUser()
            if (data.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    onResult.invoke(null)
                }
                return@launch
            }
            try {
                val list = mutableListOf<User>()
                val jsonArray = JSONObject(data)
                jsonArray.keys().forEach {
                    val obj = jsonArray.getJSONObject(it)
                    val bean = User(
                        email = obj.optString("email"),
                        name = obj.optString("name"),
                        faculty = obj.optString("faculty")
                    )
                    list.add(bean)
                }
                withContext(Dispatchers.Main) {
                    onResult.invoke(list)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onResult.invoke(null)
                }
            }
        }
    }

    @WorkerThread
    private fun requestAllUser(): String? {
        return try {
            val url = URL("http://175.178.42.68:8001/api/users")
            val connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = TIME_OUT
            connection.connectTimeout = TIME_OUT
            connection.doInput = true //允许输出流
            connection.useCaches = false //不允许使用缓存
            connection.requestMethod = "GET"
            connection.setRequestProperty("Charset", "utf-8") //设置编码
            connection.connect()
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val result = is2String(inputStream) //将流转换为字符串。
                result
            } else {
                null
            }
        } catch (ex: Exception) {
            null
        }
    }

    fun is2String(`is`: InputStream?): String {
        val bufferedReader =
            BufferedReader(InputStreamReader(`is`, "utf-8"))
        var line: String? = ""
        val stringBuilder = StringBuilder()
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        return stringBuilder.toString().trim { it <= ' ' }
    }
}

