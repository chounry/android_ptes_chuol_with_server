
# ផ្ទះជួល​

An android app that show and post the information of the **HOUSE** or **ROOM** for **SELL** or **RENT**.

### Installing
**I . Server**
 1. create a new **.env** file from the existing **.env.example**
 2. Create a **database** on the your WAMP or whatever you use 
 3. config the 
	1. DB_DATABASE
	2. DB_USERNAME
	3. DB_PASSWORD
 4. Run these commands in the **root** project folder
	```
	php artisan key:generate
	php artisan migrate
	php artisan passport:install  // for passport AUTH Key
	```
 5. Import **data/chuol.sql** to your database
 6. Now your can run the server
	```
	php artisan serve --host=your_computer_ip_address
	```


**II . Android**
 1. Open the android project
 2. Change the ip address in **app/src/main/java/com/group6/choul/shares/MyConfig.java** to the ip address that used to run command **php artisan serve**


## Built With
**I . Server**
* [Passport](https://laravel.com/docs/5.8/passport) - Authentication 


**II . Android**
* [Retrofit](https://github.com/square/retrofit) - API request
* [Volley](https://developer.android.com/training/volley) - API request
* [ BSImagePicker](https://github.com/mikaoj/BSImagePicker) - Select images from phone
*  [ BubbleView](https://github.com/lguipeng/BubbleView) - Each message layout
* [ Picasso](https://github.com/lguipeng/BubbleView) - URL image loader
* [ scaledrone](https://www.scaledrone.com/) - Realtime message



## Developers

* **Hay Chhaykoung** (https://www.facebook.com/hay.chhaykoung.3)
* **Kim Sokhey** (https://www.facebook.com/sokey.kim)
* **Aun Chounry** (https://www.facebook.com/chounry.oun)


