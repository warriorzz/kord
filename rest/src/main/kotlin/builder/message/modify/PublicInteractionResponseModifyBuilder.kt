package dev.kord.rest.builder.message.modify

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.coerceToMissing
import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.common.entity.optional.map
import dev.kord.common.entity.optional.mapList
import dev.kord.rest.builder.RequestBuilder
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.json.request.InteractionResponseModifyRequest
import dev.kord.rest.json.request.MultipartInteractionResponseModifyRequest
import java.io.InputStream


class PublicInteractionResponseModifyBuilder
    : PersistentMessageModifyBuilder,
    RequestBuilder<MultipartInteractionResponseModifyRequest> {

    private var state = MessageModifyStateHolder()

    override var files: MutableList<Pair<String, InputStream>>? by state::files.delegate()

    override var content: String? by state::content.delegate()

    override var embeds: MutableList<EmbedBuilder>? by state::embeds.delegate()

    override var allowedMentions: AllowedMentionsBuilder? by state::allowedMentions.delegate()


    override var components: MutableList<MessageComponentBuilder>? by state::components.delegate()

    override fun toRequest(): MultipartInteractionResponseModifyRequest {
        return MultipartInteractionResponseModifyRequest(
            InteractionResponseModifyRequest(
                content = state.content,
                embeds = state.embeds.mapList { it.toRequest() },
                allowedMentions = state.allowedMentions.map { it.build() },
                components = Optional(components).coerceToMissing().mapList { it.build() },
            ),
            state.files
        )
    }

}
