<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\UserType;

class UserTypeController extends Controller
{
    public function saveUserType(Request $request)
    {
        $UseType = UserType::create(
            [
                'name'=>$request->name,
            ]
        );
        return "successful";   
    } 

}
