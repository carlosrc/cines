package es.udc.pa.pa006.cines.model.buyservice;

import java.util.List;

import es.udc.pa.pa006.cines.model.buy.Buy;

public class BuyBlock {

    private List<Buy> buys;
    private boolean existMoreBuys;

    public BuyBlock(List<Buy> buys, boolean existMoreBuys) {
        
        this.buys = buys;
        this.existMoreBuys = existMoreBuys;

    }
    
    public List<Buy> getBuys() {
        return buys;
    }
    
    public boolean getExistMoreBuys() {
        return existMoreBuys;
    }
	
}
