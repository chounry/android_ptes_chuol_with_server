<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\EstateImg;
use App\Estate;
use App\Users;
use App\House;
use App\Room;
use Illuminate\Support\Facades\Log;

class EstateController extends Controller{


    public function add_to_saved(Request $request){
        $estate_id = $request->estate_id;
        $user_id = $request->user_id;
        $attach_status = true;

        $saved_posts = Users::find($user_id)->saved_posts()->get();
        if(sizeof($saved_posts) > 0){
            foreach($saved_posts as $post){
                if($estate_id == $post->id){
                    $attach_status = false;
                    break;
                }
            }
        }
        
        if($attach_status){
            Users::find($user_id)->saved_posts()->attach($estate_id); 
            return \response()->json(["status"=>"Unsave"]);
        }
        Users::find($user_id)->saved_posts()->detach($estate_id); 
        return \response()->json(["status"=>"Save"]);
    }

    // public function getData(){
    //     $storeEstate = EstateModel::all();
    //     return response()->json($storeEstate);
    // }
    public function get_saved_posts(Request $request)
    {

        $users = Users::find($request->user_id);
        $estates = $users->saved_posts()->get();
        $response = array();
        foreach($estates as $value){
            $house_or_room = $value->room()->first();
            $house_type = null;
            if($house_or_room == null){
                $house_or_room = $value->house()->first();
                $house_type = $house_or_room = $house_or_room->house_type()->first()->name;
            }
            
            $estate_id = $value->id;
            $title = $value->title;
            $location = $value->city()->first()->name;
            $address = $value->address;
            $price = $value->price;
            $for_sale_status = $value->for_sale_status;

            // img things
            $img_loc = EstateImg::where('estate_id',$value->id)->first()->img_loc;
            $img_loc = '/storage/estate_imgs/'.$img_loc;

            $tmp = array(
                "estate_id"=>$estate_id,
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
    public function get_house_related(Request $request){
        $storeRelated = Estate::where("city_id",$request->city_id)->get();
        // address , type  , location ,title, price, for sale or rent, id ;
        $response = array();
        foreach($storeRelated as $value){
            if(House::where('estate_id',$value->id)->first() == null || $value->id == $request->estate_id){
                continue;
            }

            $id = $value->id;
            $title = $value->title;
            $price = $value->price;
            $currency = $value->currency;


            if ($currency=="Dollar"){
                $i = "$";
            }
            else{
                $i = "R";
            }

            // img things
            $img_loc = EstateImg::where('estate_id',$value->id)->first()->img_loc;
            $img_loc = '/storage/estate_imgs/'.$img_loc;

            $tmp = array(
                "estate_id"=>$id,
                "title"=> $title,
                "price"=>$price,
                "currency"=>$i,
                "img" => $img_loc
            );
            $response[] = $tmp;
        }

        return response()->json($response);
    }
    public function get_room_related(Request $request){
        $storeRelated = Estate::where("city_id",$request->city_id)->get();
        // address , type  , location ,title, price, for sale or rent, id ;
        $response = array();
        foreach($storeRelated as $value){
            if(Room::where('estate_id',$value->id)->first() == null  || $value->id == $request->estate_id){
                continue;
            }

            $id = $value->id;
            $title = $value->title;
            $price = $value->price;
            $currency = $value->currency;


            if ($currency=="Dollar"){
                $i = "$";
            }
            else{
                $i = "R";
            }


            // img things
            $img_loc = EstateImg::where('estate_id',$value->id)->first()->img_loc;
            $img_loc = '/storage/estate_imgs/'.$img_loc;

            $tmp = array(
                "estate_id"=>$id,
                "title"=> $title,
                "price"=>$price,
                "currency"=>$i,
                "img" => $img_loc
            );
            $response[] = $tmp;
        }

        return response()->json($response);
    }
}

