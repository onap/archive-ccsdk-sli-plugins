package org.onap.ccsdk.sli.plugins.fabricdiscovery;

import java.util.Map;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;

/**
 * Created by arun on 9/10/17.
 */
public interface IFabricDiscoveryService {

  /* (non-Javadoc)
   * @see void#processDcNotificationStream(java.lang.String, java.lang.Integer, java.lang.String, java.lang.Boolean)
   */
  void processDcNotificationStream (Map<String, String> paramMap, SvcLogicContext ctx) throws SvcLogicException;

}
