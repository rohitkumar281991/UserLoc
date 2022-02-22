# UserLoc
This uses a API call using Retrofit to obtain list of users details and show on UI in recycler view and google map.


Login:

<img src="https://user-images.githubusercontent.com/13178473/153553337-f8fe3cf2-36c7-453b-b8d7-dba20e58f4ed.jpg" width="250" height="500"> 		User List: <img src="https://user-images.githubusercontent.com/13178473/153554141-479c1ba8-9267-4b15-be15-2c61a1f66dee.jpg" width="250" height="500">  		User Map: <img src="https://user-images.githubusercontent.com/13178473/153554150-946e5fdb-6125-4b72-9f57-3ab8f0608969.jpg" width="250" height="500">

3. Implemented features:
	1. Login : 


			1. Input fields validations.
			3. Server call to fetch user list.
			4. Login button.
	2. User list :


			1. User list using recycler view.
			2. Logout in action bar menu.
			3. Once logout, user can not come to user list page directly.
	3. User Details


			1. Map with selected user's lat-lng location.
			2. Current location button to search device's current location and show pointer on map.
			3. Logout and back button in action bar menu.
			
4. App uses:
		
		1. MVVM
		2. Coroutines
		3. Hilt
		4. Dagger
		5. Retrofit2
		6. Recycler view and Card View
		7. Data binding
		8. Activity-Fragment communication
		9. Live-MutableLive data
		10.Room Db
		11.Google Maps

5. How to use this app
		
		1. User can enter any username and password. Only input validations are present.
		2. It supports dark and light mode themes
		3. Default user location is taken as Singapore.
		4. If logout is pressed while using app, user will be navigated to login page.
		5. Press on "Current Location" button in map view page to locate current location. App will ask for requried location permissions.
   
