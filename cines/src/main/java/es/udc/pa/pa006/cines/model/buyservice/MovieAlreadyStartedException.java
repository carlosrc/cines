package es.udc.pa.pa006.cines.model.buyservice;

import java.util.Calendar;

@SuppressWarnings("serial")
public class MovieAlreadyStartedException extends Exception {

    private Calendar dateSession;
    private Calendar actualDate;

    public MovieAlreadyStartedException(Calendar dateSession, Calendar actualDate) {
	super("Movie Already Started exception => dateSession = " + dateSession
		+ ". Actual date = " + actualDate);
	this.dateSession = dateSession;
	this.actualDate = actualDate;
    }

    public Calendar getDateSession() {
	return dateSession;
    }

    public Calendar getActualDate() {
	return actualDate;
    }

}
