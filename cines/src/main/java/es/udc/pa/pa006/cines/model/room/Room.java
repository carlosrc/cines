package es.udc.pa.pa006.cines.model.room;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Immutable;

import es.udc.pa.pa006.cines.model.cinema.Cinema;
import es.udc.pa.pa006.cines.web.util.Constants;

@Entity
@Immutable
@BatchSize(size=Constants.BATCHSIZE)
public class Room {

	private Long roomId;
	private String name;
	private int capacity;
	private Cinema cinema;

	public Room() {
	}

	public Room(String name, int capacity, Cinema cinema) {
		this.name = name;
		this.capacity = capacity;
		this.cinema = cinema;
	}

	@SequenceGenerator( // It only takes effect for
	name = "RoomIdGenerator", // databases providing identifier
	sequenceName = "RoomSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RoomIdGenerator")
	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "cinemaId")
	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

}
