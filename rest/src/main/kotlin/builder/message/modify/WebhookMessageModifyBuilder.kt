package dev.kord.rest.builder.message.modify

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.common.entity.optional.map
import dev.kord.common.entity.optional.mapList
import dev.kord.rest.builder.RequestBuilder
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.json.request.MultipartWebhookEditMessageRequest
import dev.kord.rest.json.request.WebhookEditMessageRequest
import java.io.InputStream

class WebhookMessageModifyBuilder
    : PersistentMessageModifyBuilder,
    RequestBuilder<MultipartWebhookEditMessageRequest> {

    private var state = MessageModifyStateHolder()

    override var files: MutableList<Pair<String, InputStream>>? by state::files.delegate()

    override var content: String? by state::content.delegate()

    override var embeds: MutableList<EmbedBuilder>? by state::embeds.delegate()

    override var allowedMentions: AllowedMentionsBuilder? by state::allowedMentions.delegate()


    override var components: MutableList<MessageComponentBuilder>? by state::components.delegate()

    @OptIn(KordPreview::class)
    override fun toRequest(): MultipartWebhookEditMessageRequest {
        return MultipartWebhookEditMessageRequest(
            WebhookEditMessageRequest(
                content = state.content,
                embeds = state.embeds.mapList { it.toRequest() },
                allowedMentions = state.allowedMentions.map { it.build() },
                components = state.components.mapList { it.build() }
            ),
            files = state.files
        )
    }

}
