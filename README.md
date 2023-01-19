# PhoenixFitness


## Description:
Video Demo: https://drive.google.com/file/d/1OFXEUsRuey_jLDhbexWlMy76mJ-6PAId/view

This is an simple Android fitness app created in Android Studio that keeps track of a user's daily calorie intake, daily walking steps and calories burned from walking. There is also a leaderboard where the top 10 users with the most daily steps are displayed. 

**NOTE:** This Android app will only run on Android devices with a step sensor. The app will not run on an Android emulator, non-Android device and Android devices without a step sensor.

## Instructions:
0. If you don't have Android Studio, go to https://developer.android.com/studio to install
1. To clone this repo, open Android Studio, click on Get from Version Control, select Git for version control option, copy and paste https://github.com/andyLiSyr/phoenixFitness in the URL section and then click on clone button
2. To run app on Android device, follow the instructions here https://developer.android.com/studio/run/device
3. Create an account in the app to start using PhoenixFitness 


## Structure:
- User account creation and account authentication uses Firebase Authentication
- Data on users like calorie intake and calories burned are stored in a user database using Firebase Cloud FireStore
- User profile pictures are stored in Firebase Cloud Storage
- Steps are tracked using the Android Sensors API


## Contributors: 
- Andy Li
- Eric Rodriguez
- Petr Sopidi



