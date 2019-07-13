<?php

namespace App\Http\Controllers\MyAuth;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Laravel\Passport\Client;
use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Log;
use App\User;

class RegisterController extends Controller
{
    // use IssueTokenTrait;
    private $client;
    
    public function __construct(){
        $this->client = Client::find(2);
    }

    public function register(Request $request){
        $this->validate($request,[
            'username'=>'nullable',
            'fname'=>'required',
            'lname' => 'required',
            'email'=>'required|email|unique:users,email|unique:users',
            'password'=>'required|min:6|confirmed',
            'phone' => 'required|min:9'
            
        ]);
        
        $user = User::create([
            'username'=>$request->username,
            'fname' => $request->fname,
            'lname' => $request->lname,
            'email' => $request->email,
            'phone' => $request->phone,
            'password' => \bcrypt($request->password)
        ]);
        
        $params = [
            'grant_type' => 'password',
            'client_id' => $this->client->id,
            'client_secret' => $this->client->secret,
            'username' => $request->email,
            'password' => $request->password,
            'scope' => '*'
        ];

        $request->request->add($params);

        $proxy = Request::create('oauth/token','POST');

        return Route::dispatch($proxy);
    }

    public function edit(Request $request){
        $user_id = $request->id;
        $user_instance = User::find($user_id);
        $img_location = "/storage/profile_imgs/";

        if($request->hasFile('img'))
        {
            $file = $request->file('img');
            if($user_instance->img_loc != "default_user.png"){
                $img_loc = 'public/profile_imgs/' . $user_instance->img_loc;
                if(Storage::disk('local')->exists($img_loc))
                    Storage::disk('local')->delete($img_loc);
            }
            $fileNameWithExt = $file->getClientOriginalName();
            $fileName = pathinfo($fileNameWithExt, PATHINFO_FILENAME);
            $extension = $file->getClientOriginalExtension();
            $fileNameToStore = $fileName.'_'.time().'.'.$extension;
            $path = $file->storeAs('public/profile_imgs', $fileNameToStore);
            
            // save image path 
            $user_instance->img_loc = $fileNameToStore;
        }

        if($user_instance != null){
            $user_instance->fname = $request->fname;
            $user_instance->lname = $request->lname;
            $user_instance->email = $request->email;
            $user_instance->phone = $request->phone;
            $user_instance->save();
            return response()->json(["status"=>"success"]);
        }
        return \response()->json(["status"=>"falid"]);
    }
}