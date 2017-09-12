package org.onap.ccsdk.sli.plugins.fabricdiscovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Created by arun on 9/9/17.
 */
public class ClientMessageCallback implements IClientMessageCallback {
  private static final Logger LOG = LoggerFactory.getLogger(ClientMessageCallback.class);

  @Override
  public void onMessageReceived(final Object message) {
    if (message instanceof TextWebSocketFrame) {
      LOG.info("received message {}" + ((TextWebSocketFrame) message).text());
    }
  }

}
