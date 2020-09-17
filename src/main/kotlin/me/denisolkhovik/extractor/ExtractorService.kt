package me.denisolkhovik.extractor

import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

@Component
class ExtractorService {

    companion object {
        val objectMapper = ObjectMapper()
    }

    fun extract(shortPageUrl: String): ByteArray {
        val mainPage = getPage(shortPageUrl)
        val dataBlock = mainPage.getElementById("__NEXT_DATA__").data()
        val dataNode = objectMapper.readTree(dataBlock)

        val host = dataNode.at("/props/initialProps/\$host").textValue()
        val pageUrl = dataNode.at("/props/initialProps/\$pageUrl").textValue()
        val videoUrl = dataNode.at("/props/pageProps/videoData/itemInfos/video/urls/0").textValue()

        return getVideo(videoUrl, "https://$host${pageUrl.substringBefore("?")}")
    }

    fun getPage(pageUrl: String): Document {
        val con = URL(pageUrl).openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        con.setRequestProperty("Connection", "keep-alive")
        con.setRequestProperty("Pragma", "no-cache")
        con.setRequestProperty("Cache-Control", "no-cache")
        con.setRequestProperty("Upgrade-Insecure-Requests", "1")
        con.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36"
        )
        con.setRequestProperty(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
        )
        con.setRequestProperty("Sec-Fetch-Site", "none")
        con.setRequestProperty("Sec-Fetch-Mode", "navigate")
        con.setRequestProperty("Sec-Fetch-User", "?1")
        con.setRequestProperty("Sec-Fetch-Dest", "document")
        con.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
        con.setRequestProperty(
            "Cookie",
            "tt_webid=6873242376355481094; tt_webid_v2=6873242376355481094; _ga=GA1.2.977390374.1600301460; _gid=GA1.2.63885771.1600301460"
        )

        con.connect()
        val responseContent = con.inputStream
        val page = Jsoup.parse(String(responseContent.readBytes()))
        con.disconnect()

        return page
    }

    fun getVideo(videoUrl: String, fullPageUrl: String): ByteArray {
        val con = URL(videoUrl).openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        con.setRequestProperty("Connection", "keep-alive")
        con.setRequestProperty("Pragma", "no-cache")
        con.setRequestProperty("Cache-Control", "no-cache")
        con.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36"
        )
        con.setRequestProperty("Accept", "*/*")
        con.setRequestProperty("Sec-Fetch-Site", "cross-site")
        con.setRequestProperty("Sec-Fetch-Mode", "no-cors")
        con.setRequestProperty("Sec-Fetch-Dest", "video")
        con.setRequestProperty("Referer", fullPageUrl)
        con.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
        con.setRequestProperty("Range", "bytes=0-")

        con.connect()
        val video = BufferedInputStream(con.inputStream).readBytes()
        con.disconnect()

        return video
    }
}