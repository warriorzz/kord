package com.gitlab.kordlib.core.gateway.handler

import com.gitlab.kordlib.cache.api.DataCache
import com.gitlab.kordlib.cache.api.find
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.cache.data.UserData
import com.gitlab.kordlib.core.entity.User
import com.gitlab.kordlib.core.event.UserUpdateEvent
import com.gitlab.kordlib.gateway.Event
import com.gitlab.kordlib.gateway.Gateway
import com.gitlab.kordlib.gateway.UserUpdate
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import com.gitlab.kordlib.core.event.Event as CoreEvent
import kotlinx.coroutines.channels.Channel as CoroutineChannel

@Suppress("EXPERIMENTAL_API_USAGE")
internal class UserEventHandler(
        kord: Kord,
        gateway: Gateway,
        cache: DataCache,
        coreEventChannel: SendChannel<CoreEvent>
) : BaseGatewayEventHandler(kord, gateway, cache, coreEventChannel) {

    override suspend fun handle(event: Event) = when (event) {
        is UserUpdate -> handle(event)
        else -> Unit
    }

    private suspend fun handle(event: UserUpdate) {
        val data = UserData.from(event.user)

        val old = cache.find<UserData> { UserData::id eq data.id }
                .asFlow().map { User(it, kord) }.singleOrNull()

        cache.put(data)
        val new = User(data, kord)

        coreEventChannel.send(UserUpdateEvent(old, new))
    }

}