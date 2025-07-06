package com.justbookit.justbookit.dto;

import java.util.List;


public class HotelDTO {
    private String name;
    private String description;
    private String location;
    private String address;
    private int stars;
    private String amenities;
    private List<RoomDTO> rooms;


    public List<RoomDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }
    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }
}
