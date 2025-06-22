package com.justbookit.justbookit.dto;

public class ReservationDTO {
    private Long roomId;
    private String checkInDate;
    private String checkOutDate;
    private int guests;

    // Constructors, getters, setters
    public ReservationDTO() {}

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }

    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }

    public int getGuests() { return guests; }
    public void setGuests(int guests) { this.guests = guests; }
}
