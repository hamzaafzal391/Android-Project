# CineFAST - Movie Seat Booking App
**Roll Number: 22L-6713**  
**Course: Software for Mobile Devices**

## Submission
Compress the `app/src` folder and name the zip file: **22L-6713.zip**

## Project Structure
- **SplashActivity** - Launcher with logo animation, auto-navigates after 5 seconds
- **OnboardingActivity** - Get Started button, explicit intent to Home
- **HomeActivity** - 4 movies (hardcoded in XML), Book Seats & Trailer (YouTube) intents
- **SeatSelectionActivity** - Seat grid, Available/Selected/Booked states, Proceed to Snacks / Book Seats
- **SnacksActivity** - 4 snack items with +/- quantity, dynamic total
- **TicketSummaryActivity** - Booking summary, Send Ticket (ACTION_SEND share)

## Building
Open in Android Studio and sync Gradle. Build and run on emulator or device.

## Adding Movie Poster Images
To use actual movie poster images, add PNG/JPG files to `res/drawable` and reference them in `activity_home.xml` (e.g., `android:src="@drawable/poster_dark_knight"` for ImageView moviePoster1).
