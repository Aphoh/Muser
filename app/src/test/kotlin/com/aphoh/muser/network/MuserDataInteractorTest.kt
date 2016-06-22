package com.aphoh.muser.network

import com.aphoh.muser.data.network.model.reddit.Media
import com.aphoh.muser.data.network.model.reddit.Oembed_
import com.aphoh.muser.data.network.model.reddit.PostData
import com.aphoh.muser.data.network.model.reddit.PostItem
import com.aphoh.muser.network.interactors.MuserDataInteractor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Created by Will on 1/5/16.
 */

public class MuserDataInteractorTest {

    @Test
    fun testIsPlaylist() {
        val playlist = Oembed_()
        playlist.html =
                """
                 &lt;
                iframe class=\"embedly-embed\" src=\"https://cdn.embedly.com/widgets/media.html?
                src=https%3A%2F%2Fw.soundcloud.com%2Fplayer%2F%3Fvisual%3Dtrue%26url%3D
                http%253A%252F%252Fapi.soundcloud.com%252Fplaylists%252F181866313%26show_artwork%3Dtrue&amp;
                url=https%3A%2F%2Fsoundcloud.com%2Fhearddit%2Fsets%2Fr-futurebeats-week-of-2016-01&amp;
                image=http%3A%2F%2Fa1.sndcdn.com%2Fimages%2Ffb_placeholder.png%3F1451995570&amp;
                key=2aa3c4d5f3de4f5b9120b660ad850dc9&amp;
                type=text%2Fhtml&amp;
                schema=soundcloud\" width=\"500\" height=\"500\" scrolling=\"no\" frameborder=\"0\" allowfullscreen&gt;
                &lt;
                /iframe&gt;
                """
        assertThat(MuserDataInteractor.isTrack(playlist)).isEqualTo(false)
        val track = Oembed_()
        track.html =
                """
                &lt;
                iframe class=\"embedly-embed\" src=\"//cdn.embedly.com/widgets/media.html?
                src=https%3A%2F%2Fw.soundcloud.com%2Fplayer%2F%3Fvisual%3Dtrue%26url%3D
                http%253A%252F%252Fapi.soundcloud.com%252Ftracks%252F240374338%26show_artwork%3Dtrue&amp;
                url=https%3A%2F%2Fsoundcloud.com%2Flostgemsofholyrain%2Ftry-me&amp;
                image=http%3A%2F%2Fa1.sndcdn.com%2Fimages%2Ffb_placeholder.png%3F1451995570&amp;
                key=2aa3c4d5f3de4f5b9120b660ad850dc9&amp;
                type=text%2Fhtml&amp;
                schema=soundcloud\" width=\"500\" height=\"500\" scrolling=\"no\" frameborder=\"0\" allowfullscreen&gt;
                &lt;
                /iframe&gt;
                """
        assertThat(MuserDataInteractor.isTrack(track)).isEqualTo(true)
    }
}