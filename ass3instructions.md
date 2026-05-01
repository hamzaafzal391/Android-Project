Assignment 3 – CineFAST (Authentication, Database & Navigation)
Use the existing Assignment 2 (CineFAST) project. Do NOT create a new project.
Learning Outcomes
By completing this assignment, students will gain hands-on experience with:
• Drawer Navigation (NavigationView + DrawerLayout)
• SQLite Database
• Firebase Authentication
• Firebase Realtime Database
• JSON Parsing
• Session Management (SharedPreferences)
UI Design Requirement (Mandatory)
Figma Link: https://www.figma.com/design/VKjN1tM9ysccVxsjklvIhM/Assignment-1_6A
Important:
• New screens (Login, Signup, My Bookings) are already added in Figma
• Follow layout structure closely. Do NOT redesign flow
Updated App Flow
Splash Screen -> Onboarding Screen -> Login / Signup -> MainActivity (Drawer + Fragments)
National University of Computer and Emerging Sciences, Lahore Campus
Course: Software for Mobile Devices Course Code: CS 4039
Section: A Marks: 50
Submission
deadline:

28April 2026 Weight
Assignment: 3 Page(s):

Instruction/Notes: 1. “Submit your app/src folder compressed as a .zip file” named as yourroll

number., i.e., 23L-1111.zip.
2. Copying solutions from other students isstrictly prohibited. Plagiarism
will be checked, and any violation will result in a zero for the
assignment/quiz.
3. Late submission of yoursolution is not allowed. After the deadline, no
submission will be accepted.

Screen Requirements
1. Login Screen (Activity)
• Email + Password
• Implement Firebase Authentication for Login
• ‘Don’t have account? Register Now’ link -> navigates to Signup activity
• On successful login -> navigate to MainActivity
Add a toaststatement in MainActivity with tag "CineFAST" when app launches.
• Implement Session Management using SharedPreferences (Remember Me)
Use Shared Preferences file name "cinefast_session_pref_v3"
o If user is already logged in → skip Login screen
o On logout → clear session
2. Signup Screen (Activity)
Fields: Name, Email and Password
• Implement Firebase Authentication for user registration
• Validate: Empty fields and Password ≥ 8 characters
• On Success: Store user data in Firebase Realtime Database. Navigate to Home
3. MainActivity (Drawer Navigation)
• Implement DrawerLayout with NavigationView
• Menu Items: Home, My Bookings and Logout
• Navigation must work across all fragments. Use NavigationView
• Handle item clicks properly
• Logout → clear session + go to Login
4. Home Screen (Fragment)
Same as Assignment 2 with the following update:
• Movies must be loaded using JSON Parsing (NOT hardcoded)
• Replace hardcoded movie lists with JSON
Option 1: Store JSON file in assets/
Option 2: Fetch from API
• Parse JSON
• Populate RecyclerView using Model class
5. Snacks Screen (Fragment)

Change from Assignment 2: Snacks must be loaded using SQLite Database
• Create SQLite DB. Table: snacks. Columns: id, name, price and image
• Load snacks from database
• Insert initial snack data when app runs first time
6. Ticket Summary Screen (Fragment)
• Same as Assignment 2
• Currently booking is temporary → now make it persistent.
• Store in Firebase: User ID, Movie Name, Seats, Total Price and Date and Time
• Store data under node: "bookings/{userId}/{bookingId}"
7. My Bookings Screen (Fragment)
• Fetch bookings from Firebase. Display using RecyclerView
UI Design: UI must closely match the Figma “My Bookings” design (image on left and action icon on
right). Each booking item must include:
• Movie Poster (left)
• Movie Name
• Date & Time
• Number of Tickets
• Cancel Button ( on right side). The cancel button must be placed on the right side
• Each booking item must have a Cancel button
• On click → show AlertDialog: Are you sure you want to cancel this booking?
Cancellation Rule (IMPORTANT)
• Booking can ONLY be canceled if: Booking date/time is in the future. Use timestamp or
SimpleDateFormat comparison
• If booking is in the past: Cannot cancel past bookings
On Successful Cancellation:
• Remove booking from Firebase
• Update RecyclerView
• Show Toast: Booking Cancelled Successfully

Important Instructions
• Plagiarism will be checked.
• Copied assignments will receive heavy penalties.
• Late submissions will NOT be accepted.
• App must not crash.
• Use proper IDs for all views.
• All logic must be written in Java.
• No hardcoded movie data (must use JSON)
• No hardcoded snacks (must use SQLite)
• Do not remove previous features
Submission and GitHub Instructions
1. Continue using the same GitHub repository used in previous assignments.
2. The repository must include: Assignment 1, Assignment 2 and Assignment 3
3. Make multiple commits throughout development:
o Do NOT submit a single final commit
o Use meaningful commit messages
4. Ensure that your GitHub repository is public and accessible until grading is completed.
5. Submit on GCR:
o A .zip file of your app/src folder named using your roll number
(Example: 23L-1234.zip)
o Your GitHub repository link