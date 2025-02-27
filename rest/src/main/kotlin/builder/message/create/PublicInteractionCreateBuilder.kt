package dev.kord.rest.builder.message.create

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.optional.*
import dev.kord.rest.builder.RequestBuilder
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import dev.kord.rest.json.request.MultipartInteractionResponseCreateRequest
import java.io.InputStream

/**
 * Message builder for publicly responding to an interaction.
 */

class PublicInteractionResponseCreateBuilder
    : PersistentMessageCreateBuilder,
    RequestBuilder<MultipartInteractionResponseCreateRequest> {

    override var content: String? = null

    override var tts: Boolean? = null

    override val embeds: MutableList<EmbedBuilder> = mutableListOf()

    override var allowedMentions: AllowedMentionsBuilder? = null


    override val components: MutableList<MessageComponentBuilder> = mutableListOf()

    override val files: MutableList<Pair<String, InputStream>> = mutableListOf()

    override fun toRequest(): MultipartInteractionResponseCreateRequest {
        return MultipartInteractionResponseCreateRequest(
            InteractionResponseCreateRequest(
                type = InteractionResponseType.ChannelMessageWithSource,
                data = Optional(
                    InteractionApplicationCommandCallbackData(
                        content = Optional(content).coerceToMissing(),
                        tts = Optional(tts).coerceToMissing().toPrimitive(),
                        embeds = Optional(embeds).mapList { it.toRequest() },
                        allowedMentions = Optional(allowedMentions).coerceToMissing().map { it.build() },
                        components = Optional(components).coerceToMissing().mapList { it.build() },
                    )
                )
            ),
            Optional(files)
        )
    }

}
