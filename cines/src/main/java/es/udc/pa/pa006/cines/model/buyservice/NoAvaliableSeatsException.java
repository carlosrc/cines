package es.udc.pa.pa006.cines.model.buyservice;

@SuppressWarnings("serial")
public class NoAvaliableSeatsException extends Exception {

    private int seats;
    private int avaliableSeats;

    public NoAvaliableSeatsException(int seats, int avaliableSeats) {
	super("No Avaliable Seats exception => seats = " + seats
		+ ". Avaliable Seats => avaliable seats = " + avaliableSeats);
	this.seats = seats;
	this.avaliableSeats = avaliableSeats;
    }

    public int getSeats() {
	return seats;
    }

    public int getAvaliableSeats() {
	return avaliableSeats;
    }

}
