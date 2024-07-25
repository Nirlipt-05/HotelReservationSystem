# Hotel Reservation System
This will be a CLI based project that will help in maintaining the reservation of a hotel.

## Target Audience:

This system is targeting the personnel like **receptionist**. They will be having this system to carry out the below 5 functionality i.e.:-

1. New reservation
2. Check Reservation
3. Get Room No.
4. Update Reservation
5. Delete Reservation

Means with this we can communiate with the data.

Now the user will have a main menu where in he/she will be having the 6 options to select:

1. New Reservation
2. Check Reservation
3. Get Room Number
4. Update Reservation
5. Delete Reservation
6. Exit

**Exit** option will be provided to exit from the main menu.

## The Database Schema:

* ***Database Name:*** hotel_db
* ***Table Name:*** reservations
* ***Schema:***
    1. *reservation_id*:   int        auto-increment.
    2. *guest_name*:       varchar    not null.
    3. *room_number*:      int        not null.
    4. *contact_number*:   varchar    not null.
    5. *reservation_date*: timestamp  default.

The *reservation_id* will be the **Primary Key** and it should be `auto incremented` so that the reservation id will automatically be unique and assigned to the guest.

The other columns like *guest_name*, *room_number* and *contact_number*
should be `not null`, so as these would be the mandatory fields that needs to be filled and can't be skipped.

*reservation_date* will be filled automatically by itself hence it is `default`.