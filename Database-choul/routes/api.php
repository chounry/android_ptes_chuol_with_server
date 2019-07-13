<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::post('/auth/register', 'MyAuth\RegisterController@register');

Route::post('/auth/login','MyAuth\LoginController@login');

Route::post('/auth/edit','MyAuth\RegisterController@edit');

Route::post('/auth/refresh','MyAuth\LoginController@refresh');

Route::middleware('auth:api')->group(function () {
    Route::post('/auth/get-user','MyAuth\LoginController@get_user');
    Route::post('/auth/logout','MyAuth\LoginController@logout');
});


Route::get('/rooms/getall','RoomController@index');

//user
Route::post('/users/create','UserController@saveUser');
Route::get('/users/get','UserController@getData');


//Room
Route::post('/rooms/create  ','RoomController@saveRoom');
Route::get('/rooms/get','RoomController@getData');
Route::post('/rooms/get_detail','RoomController@roomDetail');


//House
Route::post('/houses/create','HouseController@saveHouse');
Route::get('/houses/get','HouseController@getData');
Route::post('/houses/get_detail','HouseController@houseDetail');

//Estate
Route::post('/estates/create','EstateController@saveEstate');
Route::post('/estates/add_to_saved','EstateController@add_to_saved');
Route::post('/estates/get_saved_post','EstateController@get_saved_posts');
Route::post('/estates/getRelated','EstateController@get_house_related');
Route::post('/estates/get_room_related','EstateController@get_room_related');

// Messages
Route::post('/messages/get',"MessageController@index");
Route::post('/messages/detail',"MessageController@detail");
Route::post('/messages/create',"MessageController@create");

// houose type
Route::get('/house_type/index',"HouseTypeController@index");

