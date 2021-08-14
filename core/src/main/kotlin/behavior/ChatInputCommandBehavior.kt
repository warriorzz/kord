package dev.kord.core.behavior

import dev.kord.common.annotation.KordPreview
import dev.kord.core.cache.data.ApplicationCommandData
import dev.kord.core.entity.application.ChatInputCommandCommand
import dev.kord.core.entity.application.GlobalChatInputCommand
import dev.kord.core.entity.application.GuildChatInputCommand
import dev.kord.rest.builder.interaction.ChatInputModifyBuilder


interface ChatInputCommandBehavior : ApplicationCommandBehavior {

     suspend fun edit(builder: suspend ChatInputModifyBuilder.() -> Unit): ChatInputCommandCommand

}


interface GuildInputCommandBehavior : ChatInputCommandBehavior, GuildApplicationCommandBehavior {
     override suspend fun edit(builder: suspend ChatInputModifyBuilder.() -> Unit): GuildChatInputCommand {
          val request = ChatInputModifyBuilder().apply { builder() }.toRequest()
          val response = service.modifyGuildApplicationCommand(applicationId, guildId, id, request)
          val data = ApplicationCommandData.from(response)
          return GuildChatInputCommand(data, service)
     }
}



interface GlobalInputCommandBehavior : ChatInputCommandBehavior,GlobalApplicationCommandBehavior {
     override suspend fun edit(builder: suspend ChatInputModifyBuilder.() -> Unit): GlobalChatInputCommand {
          val request = ChatInputModifyBuilder().apply { builder() }.toRequest()
          val response = service.modifyGlobalApplicationCommand(applicationId,id, request)
          val data = ApplicationCommandData.from(response)
          return GlobalChatInputCommand(data, service)
     }
}