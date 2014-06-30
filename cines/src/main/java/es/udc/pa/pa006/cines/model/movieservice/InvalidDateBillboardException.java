package es.udc.pa.pa006.cines.model.movieservice;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidDateBillboardException extends Exception {

    private Calendar date;

    public InvalidDateBillboardException(Calendar date) {
	super("Invalid date. Can't find billboard in this date => date = " + date.toString());
	this.date = date;
    }

    public Calendar getDate() {
	return date;
    }
	
}
