<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class CitiesController extends Controller
{
    public function saveCity(Request $request)
    {
        $city = Cities::create(
            [
                'name'=>$request->name,
            ]
        );
        return "successful";   
    } 
}
