Use the existing Assignment 1 (CineFAST) project. Do NOT create a new project.
Learning Outcomes
By completing this assignment, students will gain hands-on experience with:
• Fragments and Single-Activity Architecture
• TabLayout with ViewPager2
• Custom RecyclerView and RecyclerView Adapter
• Custom ListView using Custom Adapter
• SharedPreferences for storing simple data
• Model Classes for data representation
• Passing data between Fragments

National University of Computer and Emerging Sciences, Lahore Campus
Course: Software for Mobile Devices Course Code: CS 4039
Section: A Marks: 50
Submission
deadline:

19-03-2026, 11:59pm Weight
Assignment: 2 Page(s):

Instruction/Notes: 1. “Submit your app/src folder compressed as a .zip file” named as yourroll

number., i.e., 23L-1111.zip.
2. Copying solutions from other students isstrictly prohibited. Plagiarism
will be checked, and any violation will result in a zero for the
assignment/quiz.
3. Late submission of yoursolution is not allowed. After the deadline, no
submission will be accepted.

Updated Architecture Requirement
In this assignment, the application must follow a Single Activity + Multiple Fragments architecture.
Application Structure:
SplashActivity launches first and navigates to OnboardingActivity, which then leads to MainActivity.
MainActivity acts as the host for all fragments: HomeFragment (which internally contains
NowShowingFragment and ComingSoonFragment as two tabs), SeatSelectionFragment,
SnacksFragment, and TicketSummaryFragment.
The first two screens remain Activities, while the remaining screens must be implemented as
Fragments inside MainActivity. All fragment navigation must be handled inside MainActivity.
Data Passing Between Fragments
Since this assignment uses a Single Activity + Multiple Fragments architecture, students must pass
data between fragments when navigating through the app. Data may be passed using either of the
following approaches:
• Bundle arguments
• Methods inside MainActivity acting as a mediator
UI Design Requirement (Mandatory)
Students must continue using the same Figma design from Assignment 1. Layouts should remain
visually similar to the original design.
Figma Link: https://www.figma.com/design/VKjN1tM9ysccVxsjklvIhM/Assignment-1_6A
Important:
• All six screens must still exist.
• UI structure should remain similar to Assignment 1.
• Only implementation techniques will change.
• Do NOT redesign the application flow.
Screen Requirements
1. Splash Screen: Same as Assignment 1.
2. Onboarding Screen: Same as Assignment 1.

3. Home Screen (Fragments + Tabs + RecyclerView)
In Assignment 1, movies were added manually using XML layouts.
In this assignment, the Home Screen must be implemented using: Fragments, TabLayout, ViewPager2
and RecyclerView.
The Home Screen must be implemented as HomeFragment inside MainActivity.
Tabs Requirement:
HomeFragment must contain two tabs:
1. Now Showing
2. Coming Soon
Since the original Figma design does not include tabs, you may add the TabLayout below the radio
buttons or position it wherever it works best, ensuring the layout remains visually consistent with the
design. Each tab must load a separate fragment: NowShowingFragment and ComingSoonFragment
Movie List Requirement (Custom RecyclerView)
Each tab fragment must display movies using a RecyclerView with a Custom Adapter.
Each movie item must contain:
• Movie Poster (actual image)
• Movie Name
• Genre or Duration
• Trailer Button/Icon
• Book Seats Button
Minimum Requirement: At least 3 movies per tab
Functionality
Same as Assignment 1, with the following additions:
• Movie lists must be populated using RecyclerView.
• Data must be stored inside Model Classes (Movie class) and ArrayLists.
When a movie is selected: The selected movie information must be passed to SeatSelectionFragment
4. Seat Selection Screen (Fragment)
Basic functionality remains same as Assignment 1.

New Behavior Based on Movie Type
The behavior of this screen will depend on whether the selected movie is Now Showing or Coming
Soon.
Case 1: Now Showing Movie
Same functionality as Assignment 1:
Buttons:
• Proceed to Snacks
• Book Seats (Skip Snacks)
New requirement: When booking is confirmed, display a Toast message: Booking Confirmed!
Case 2: Coming Soon Movie
If the selected movie belongs to the Coming Soon tab, the Seat Selection screen must behave
differently.
Requirements:
• Seats must not be clickable.
• Seat selection must be disabled.
Instead of the original buttons, display:
1. Coming Soon (disabled button)
2. Watch Trailer
Functionality:
• Coming Soon button should not be clickable.
• Watch Trailer button should open the movie trailer using Implicit Intent (YouTube).
Users should only be able to navigate back to the Home Screen. Cannot move to booking or Snack
selection
5. Snacks & Drinks Fragment (Custom ListView)
In Assignment 1, snack items were added manually using XML layouts.
In this assignment, snacks must be displayed using a Custom ListView with a Custom Adapter.

Snack Item Requirements
Each list item must include:
• Food Image (real food image)
• Item Name
• Price
• Quantity TextView
• Plus (+) Button
• Minus (–) Button
Minimum Requirement: At least 4 snacks
Functionality
Same as Assignment 1.
Snack items must now be populated using:
• Model Class (Snack class)
• ArrayList
6. Ticket Summary Screen (Fragment)
Same basic functionality as Assignment 1, with the following addition.
SharedPreferences Requirement
When a booking is confirmed (after reaching the Ticket Summary Screen), store the following data
using SharedPreferences:
• Movie Name
• Number of Seats
• Total Ticket Price
This information represents the last successful booking made by the user.
Viewing Last Booking Information
To allow users to view this stored information, add a three-dots menu icon (⋮) on the Home Screen.
Requirements:
• The three-dots icon must be visible on the Home Screen (inside HomeFragment).
• It should be accessible regardless of whether the user is on the Now Showing or Coming Soon tab.

When the user clicks the three-dots icon, display an option: View Last Booking
Functionality
When View Last Booking is selected:
• Retrieve the stored values from SharedPreferences.
• Display the information in a simple format.
Students may display the information using an AlertDialog box. Example display format:
Last Booking
Movie: Interstellar
Seats: 3
Total Price: $30
If no booking exists yet (i.e., SharedPreferences has no stored data), display a message such as: No
previous booking found.
Model Classes Requirement
Students must create Model Classes to represent application data. Examples: Movie class, Snack class.
These classes must be used to populate:
• RecyclerView (movies)
• Custom ListView (snacks)
Data may still be hardcoded inside ArrayLists.
Important Instructions
• Plagiarism will be checked.
• Copied assignments will receive heavy penalties.
• Late submissions will NOT be accepted.
Students must ensure:
• App must not crash.
• Use proper IDs for all views.
• All logic must be written in Java.

Submission Instructions
1. Upload your Assignment 1 project to GitHub before starting Assignment 2.
2. Assignment 2 must be developed in the same GitHub repository, continuing from Assignment 1.
3. Students must make multiple commits while working on the assignment. Do not upload the entire
assignment in a single commit at the end.
4. Ensure that the repository is public.
5. At the time of submission, submit the following on GCR:
• A .zip file of your app/src folder using your roll number. Example: 23L-1234.zip
• Your GitHub repository link
GitHub Requirements
• The repository must contain the complete project from Assignment 1 and Assignment 2.
• Students must show regular commits during development.
• Commit messages should be meaningful.
Important: Repositories must remain accessible until grading is completed. Submissions with only one
final commit may receive penalties.
Restrictions
• No database allowed. All data must still be hardcoded using ArrayLists.
• RecyclerView must be used for movies. Custom ListView must be used for snacks.
• Fragments must be used for all screens inside MainActivity