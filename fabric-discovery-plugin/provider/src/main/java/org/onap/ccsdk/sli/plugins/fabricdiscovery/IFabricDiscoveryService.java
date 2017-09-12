package org.onap.ccsdk.sli.plugins.fabricdiscovery;

/**
 * Created by arun on 9/10/17.
 */
public interface IFabricDiscoveryService {

  /* (non-Javadoc)
	 * @see void#processDcNotificationStream(java.lang.String, java.lang.Integer, java.lang.String, java.lang.Boolean)
	 */
  public void processDcNotificationStream (String stream, boolean enable);

}
