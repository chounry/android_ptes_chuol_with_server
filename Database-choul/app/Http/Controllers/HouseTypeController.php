<?php

namespace App\Http\Controllers;

use App\HouseType;
use Illuminate\Http\Request;

class HouseTypeController extends Controller
{
    public function index(Request $request)
    {
        $house_types = HouseType::all()->pluck('name');
        return response()->json($house_types);
    } 
}
