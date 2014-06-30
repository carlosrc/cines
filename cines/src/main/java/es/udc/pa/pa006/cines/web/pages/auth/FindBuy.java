/**
 * 
 */
package es.udc.pa.pa006.cines.web.pages.auth;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;

import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;

@AuthenticationPolicy(AuthenticationPolicyType.SALESMAN)
public class FindBuy {

	@Property
	private Long id;

	@InjectPage
	private BuyDetails buyDetails;

	Object onSuccess() {
		buyDetails.setBuyId(id);
		return buyDetails;
	}

}