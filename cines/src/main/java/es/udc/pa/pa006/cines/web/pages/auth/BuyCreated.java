/**
 * 
 */
package es.udc.pa.pa006.cines.web.pages.auth;

import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class BuyCreated {
    private Long buyId;

    public Long getBuyId() {
	return buyId;
    }

    public void setBuyId(Long buyId) {
	this.buyId = buyId;
    }

    Long onPassivate() {
	return buyId;
    }

    void onActivate(Long buyId) {
	this.buyId = buyId;
    }
}