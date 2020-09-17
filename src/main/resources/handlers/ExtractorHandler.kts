import me.denisolkhovik.extractor.ExtractorService
import me.ruslanys.telegraff.core.dsl.HandlerState
import me.ruslanys.telegraff.core.dsl.handler
import me.ruslanys.telegraff.core.dto.request.video.VideoMessage

handler("command") {

    process { state, _ ->
        val extractorService = getBean<ExtractorService>()
        val video = extractorService.extract(
            state.attributes[HandlerState.Attribute.SOURCE_MESSAGE.name] as String
        )

        VideoMessage(video)
    }

}