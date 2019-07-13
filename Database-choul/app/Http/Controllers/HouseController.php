<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\House;
use App\EstateImg;

use App\Estate;
use App\Users;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
class HouseController extends Controller
{
   
    public function saveHouse(Request $request)
    {
        $curTime = new \DateTime();
        $city_id = DB::table('cities')->where('name', $request->city_id)->first()->id;
        $house_type_id = DB::table('house_types')->where('name', $request->type)->first()->id;

        $estate = Estate::create(
            [
                'title'=>$request->title,
                'price'=>$request->price,
                'description'=>$request->description,
                'phone'=>$request->phone,
                'address'=>$request->address,
                'accepted'=>1,
                'date'=>$curTime->format("Y-m-d"),
                'lat'=>$request->lat,
                'lng'=>$request->lng,
                'currency'=>$request->currency,
                'duration'=>$request->duration,
                'phone_option'=>$request->phone_option,
                'publish'=>1,
                'city_id'=>$city_id,
                'user_id'=>$request->user_id
            ]
        );


        $house = House::create(
            [
                'bedroom'=>$request->bedroom,
                'bathroom'=>$request->bathroom,
                'floor'=>$request->floor,
                'house_size'=>$request->house_size,
                'yard_size'=>$request->yard_size,
                'for_sale_status'=> $request->for_sale_status,
                'estate_id'=>$estate->id,
                'house_type_id'=>$house_type_id
            ]
        );
        // // saving the images
        if($request->hasFile('imgs'))
        {
            foreach($request->file('imgs') as $file)
            {
                $fileNameWithExt = $file->getClientOriginalName();
                $fileName = pathinfo($fileNameWithExt, PATHINFO_FILENAME);
                $extension = $file->getClientOriginalExtension();
                $fileNameToStore = $fileName.'_'.time().'.'.$extension;
                $path = $file->storeAs('public/estate_imgs', $fileNameToStore);
                
                // save image path 
                $es_img = new \App\EstateImg;
                $es_img->img_loc = $fileNameToStore;
                $es_img->estate_id = $estate->id;
                $es_img->save();
            }   
        }else
        {
            $fileNameToStore = 'no_esatate_img.jpg';
            $es_img = new \App\EstateImg;
            $es_img->img_loc = $fileNameToStore;
            $es_img->estate_id = $estate->id;
            $es_img->save();
        }
        return "successful";
        
    }

    //get data from db

    public function getData(){
        $storeHouse = House::all();
        // address , type  , location ,title, price, for sale or rent, id ;
        $response = array();
        foreach($storeHouse as $value){
            $estate = Estate::find($value->estate_id);

            $id = $estate->id;
            $house_type = $value->house_type()->first()->name;
            $title = $estate->title;
            $location = $estate->city()->first()->name;
            $address = $estate->address;
            $price = $estate->price;
            $for_sale_status = $value->for_sale_status;

            // img things
            $img_loc = EstateImg::where('estate_id',$estate->id)->first()->img_loc;
            $img_loc = '/storage/estate_imgs/'.$img_loc;

            $tmp = array(
            
                "estate_id"=>$id,
                "house_type"=> $house_type,
                "title"=> $title,
                "location"=> $location, 
                "address"=> $address,
                "price"=>$price,
                "for_sale_status"=> $for_sale_status,
                "img" => $img_loc
            );
            $response[] = $tmp;
        }

        return response()->json($response);
    }
    //house detail get data
    public function houseDetail(Request $request){
        $estate = Estate::find($request->estate_id);
        $user = Users::find($estate->user_id);
        $house = House::where("estate_id",$estate->id)->first();

        $estate_user_id = $estate->user_id;
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
        $bedroom = $house ->bedroom;
        $bathroom = $house->bathroom;
        $floor = $house->floor;
        $house_size = $house->house_size;
        $yard_size = $house->yard_size;
        $for_sale_status = $house->for_sale_status;
        $estate_user_id = $estate->user_id;
        $city_id = $estate->city_id;
        
        


        // img things
        $imgs = EstateImg::where('estate_id',$estate->id)->get();
        $imgs_loc = array();
        
        foreach($imgs as $img){
            $imgs_loc[] = '/storage/estate_imgs/'.$img->img_loc;
        }
        
        $room = array(
            "estate_user_id"=> $estate_user_id,
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
            'bedroom'=>$bedroom,
            'bathroom'=>$bathroom,
            'floor'=>$floor,
            'house_size'=>$house_size,
            'yard_size'=>$yard_size,
            'for_sale_status'=> $for_sale_status,
            'email'=>$email,
            'estate_user_id'=>$estate_user_id,
            'city_id'=>$city_id
        );


        return response()->json($room);
    }
    //get data from db

}



