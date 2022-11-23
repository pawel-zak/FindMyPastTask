package pawel.zak.findmypasttask.networking

import java.io.File


fun Any.getJson(path: String): String {
    val uri = this.javaClass.classLoader!!.getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
}
