<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class Estate_img_controller extends Controller
{
    public function saveEstateImg(Request $request)
    {
        $estate = EstateModel::create(
            [
                'title'=>$request->title,
                'price'=>$request->price,
                'description'=>$request->description,
                'phone'=>$request->phone,
                'address'=>$request->address,
                'date'=>$request->date,
                'publish'=>$request->publish,
                'city_id'=>$request->city_id,
                'user_id'=>$request->user_id
            ]
        );
        $Estate_img = Estate_imgs_Model::create(
            [
                'name'=>$estate->name,
            ]
        );
        return "successful";   
    } 
}
