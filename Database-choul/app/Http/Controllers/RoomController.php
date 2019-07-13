<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Room;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use App\Estate;
use App\EstateImg;
use App\Users;


use Illuminate\Database\QueryException;

class RoomController extends Controller
{
    public function saveRoom(Request $request)
    {
        // Log::info($request->services);
        $city = DB::table('cities')->where('name', $request->city)->first()->id;


        $services = array(
            "free_wifi" => "0",
            "parking_space_avalible" => "0",
            "AC" => "0"
        );

        foreach($request->services as $value){
            if($value == "Free Wifi")$services["free_wifi"] = "1";
            if($value == "Available Parking Space")$services["parking_space_avalible"] = "1";
            if($value == "AC")$services["AC"] = "1";
        }

        $curTime = new \DateTime();
        $estate = Estate::create(
            [
                'title'=>$request->title,
                'price'=>$request->price,
                'description'=>$request->description,
                'phone'=>$request->phone,
                'address'=>$request->address,
                'date'=>$curTime->format("Y-m-d"),
                'accepted'=>1, 
                'lat'=>$request->lat,
                'lng'=>$request->lng,
                'currency'=>$request->currency,
                'duration'=>$request->duration,
                'phone_option'=>$request->phone_option,
                'publish'=>1,
                'city_id'=>$city,
                'user_id'=>$request->user_id
            ]
        );
        $Room = Room::create(
            [
                'size'=>$request->size,
                'free_wifi'=>$services["free_wifi"],
                'parking_space_avalible'=>$services["parking_space_avalible"],
                'AC' => $services["AC"],
                'estate_id'=>$estate->id
                
            ]
        );

        // // saving the images
        if($request->hasFile('imgs'))
        {
            Log::info("My File 1".sizeof($request->file('imgs')));
            foreach($request->file('imgs') as $file)
            {
                $fileNameWithExt = $file->getClientOriginalName();
                $fileName = pathinfo($fileNameWithExt, PATHINFO_FILENAME);
                $extension = $file->getClientOriginalExtension();
                $fileNameToStore = $fileName.'_'.time().'.'.$extension;
                $path = $file->storeAs('public/estate_imgs', $fileNameToStore);
                
                // save image path 
                $es_img = new EstateImg;
                $es_img->img_loc = $fileNameToStore;
                $es_img->estate_id = $estate->id;
                $es_img->save();
            }   
        }else
        {
            $fileNameToStore = 'no_esatate_img.jpg';
            $es_img = new \App\EstateImg;
            $es_img->img_loc = $fileNameToStore;
            $es_img->estate_id = $estate->$id;
            $es_img->save();
        }
       
        
        return "successful";
    }

    // public function index()
    // {
    //     $users = DB::table('cities')->get();

    //     return response()->json($users);
    // }



    //get data from db
    public function getData(){
        $storeRoom = Room::all();
    
        $response = array();
        $save_post = DB::table('saved_post')->where('user_id')->get();
        foreach($storeRoom as $value){
            $estate = Estate::find($value->estate_id);
            $save_status = "Save";
            foreach($save_post as $save){
                if($estate->id == $save_post->estate_id){
                    $save_status="Unsave";
                    break;
                }
            }
            
            $room_id = $value->id;
            $estate_id = $estate->id;
            $title = $estate->title;
            $location = $estate->city()->first()->name;
            $address = $estate->address;
            $price = $estate->price;
           
            Log::info($estate_id);
            // img things
            $img_loc = EstateImg::where('estate_id',$estate->id)->first()->img_loc;
            $img_loc = '/storage/estate_imgs/'.$img_loc;
            
            $room = array(
                "save"=> $save_status,
                "estate_id"=>$estate_id,
                "room_id"=>$room_id,
                "title"=> $title,
                "location"=> $location, 
                "address"=> $address,
                "price"=>$price,
                "img"=>$img_loc,
                
            );


            $response[] = $room;
        }

        return response()->json($response);
    }
    //room detail get data
    public function roomDetail(Request $request){
        $estate = Estate::find($request->estate_id);
        $user = Users::find($estate->user_id);
        $storeDetail = Room::where("estate_id",$estate->id)->first();

            $email = $user->email;
            $estate_id = $estate->id;
            $title = $estate->title;
            $address = $estate->address;
            $price = $estate->price;
            $location = $estate->city()->first()->name;
            $description = $estate->description;
            $phone = $estate->phone;
            $lat = $estate->lat;
            $lng = $estate->lng;
            $currency = $estate->currency;
            $phone_option = $estate->phone_option;
            $size = $storeDetail->size;
            $service1 = $storeDetail->free_wifi;
            $service2 = $storeDetail->parking_space_avalible;
            $service3 = $storeDetail->AC;
            $city_id = $estate->city_id;
            


            // img things
            $imgs = EstateImg::where('estate_id',$estate->id)->get();
            $imgs_loc = array();
            
            foreach($imgs as $img){
                $imgs_loc[] = '/storage/estate_imgs/'.$img->img_loc;
            }
            
            $room = array(
                "estate_id"=>$estate_id,
                "title"=> $title,
                "address"=> $address,
                "price"=>$price,
                "img"=>$imgs_loc,
                'description'=>$description,
                'phone'=>$phone,
                'lat'=>$lat,
                'lng'=>$lng,
                'currency'=>$currency,
                "location"=> $location, 
                'phone_option'=>$phone_option,
                'size'=>$size,
                'free_wifi'=>$service1,
                'parking_space_avalible'=>$service2,
                'AC' => $service3,
                'email'=>$email,
                'city_id'=>$city_id
            );


        return response()->json($room);
    }
    //get data from db

}
