<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Users;
class UserController extends Controller
{
    public function saveUser(Request $request)
    {
        $user = Users::create(
            [
                'fname'=>$request->fname,
                'lname'=>$request->lname,
                'phone'=>$request->phone,
                'email'=>$request->email,
                'img_loc'=>$request->img_loc,
                'user_type_id'=>$request->user_type_id
            ]
        );
        return "successful";         
    }

    public function getData(){
        $storeUsers= Users::all();
        return response()->json($storeUsers);
    }
}
