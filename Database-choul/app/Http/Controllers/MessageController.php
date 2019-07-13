<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use \Datetime;
use App\MessageModel;
use App\User;
use App\Estate;
use Illuminate\Support\Facades\Log;

class MessageController extends Controller
{
    

    public function index(Request $request){
        $user_messages = MessageModel::where('from_self_user_id',$request->user_id)->orWhere('to_user_user_id',$request->user_id)->orderBy('date','DESC')->get();
        // filter to get only this user message
        

        $response = array();
        foreach($user_messages as $key => $value){
            $tmp_date = new DateTime($value->date);
            $to_user_id = $value->to_user_user_id != $request->user_id ? $value->to_user_user_id : $value->from_self_user_id;
            $to_user = User::find($to_user_id);
            $username = $to_user->fname . " " . $to_user->lname;
            $estate = Estate::find($value->estate_id);
            $title = $estate->title;
            $estate_id = $estate->id;
            $time = $tmp_date->format('H:m');
            $msg = $value->content;
            $tmp_arrary = array(
                "estate_id"=> $estate->id,
                "name"=>$username,
                "title"=>$title,
                "time"=>$time,
                "message"=>$msg
            );
            if(!$this->findTheSame($estate_id, $response))
                $response[] = $tmp_arrary; // put the message to its correspond estate 
        }

        return \response()->json($response);
    }

    public function detail(Request $request){
        $user_id = $request->user_id;
        $messages = MessageModel::where('estate_id',$request->estate_id)
        ->where(function($query) use ($user_id){
            $query->where('from_self_user_id','=',$user_id)
            ->orWhere('to_user_user_id','=',$user_id);
        })->get();

        return \response()->json($messages);
    }

    public function create(Request $request){

        $curTime = new \DateTime();
        // Log::info($request->all());
        MessageModel::create([
            'content' => $request->content,
            'from_self_user_id' =>$request->from_self_user_id,
            'to_user_user_id' =>$request->to_user_user_id,
            'estate_id' => $request->estate_id,
            'seen' => 0,
            'date' =>$curTime->format("Y-m-d H:i:s")
        ]);
        return \response()->json(["status"=>"Success"]);
    }

    // my functions
    private function findTheSame($id, $array){
        foreach($array as $value){
            if($value['estate_id'] == $id)return true;
        }
        return false;
    }
}
