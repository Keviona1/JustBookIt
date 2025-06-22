package com.justbookit.justbookit.dto;

public class HotelSearchDTO {
    private String location;
    private String checkInDate;
    private String checkOutDate;
    private int guests;
    private Integer minStars;
    private Integer maxStars;

    // Constructors, getters, setters
    public HotelSearchDTO() {}

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }

    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }

    public int getGuests() { return guests; }
    public void setGuests(int guests) { this.guests = guests; }

    public Integer getMinStars() { return minStars; }
    public void setMinStars(Integer minStars) { this.minStars = minStars; }

    public Integer getMaxStars() { return maxStars; }
    public void setMaxStars(Integer maxStars) { this.maxStars = maxStars; }
}
