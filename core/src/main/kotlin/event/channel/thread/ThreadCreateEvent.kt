package dev.kord.core.event.channel.thread

import dev.kord.core.entity.channel.thread.NewsChannelThread
import dev.kord.core.entity.channel.thread.TextChannelThread
import dev.kord.core.entity.channel.thread.ThreadChannel
import dev.kord.core.event.channel.ChannelCreateEvent

sealed interface ThreadChannelCreateEvent : ChannelCreateEvent {
    override val channel: ThreadChannel
}


class TextChannelThreadCreateEvent(override val channel: TextChannelThread, override val shard: Int) : ThreadChannelCreateEvent {
    override fun toString(): String {
        return "TextThreadChannelCreateEvent(channel=$channel, shard=$shard)"
    }
}


class NewsChannelThreadCreateEvent(override val channel: NewsChannelThread, override val shard: Int) : ThreadChannelCreateEvent {
    override fun toString(): String {
        return "NewsThreadChannelCreateEvent(channel=$channel, shard=$shard)"
    }
}

class UnknownChannelThreadCreateEvent(override val channel: ThreadChannel, override val shard: Int) : ThreadChannelCreateEvent {
    override fun toString(): String {
        return "UnknownChannelThreadCreateEvent(channel=$channel, shard=$shard)"
    }
}