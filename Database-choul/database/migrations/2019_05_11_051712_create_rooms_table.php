<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateRoomsTable extends Migration
{ 
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create("rooms", function(Blueprint $table){
            $table->increments('id');
            $table->string('size');
            $table->boolean('free_wifi');
            $table->boolean('parking_space_avalible');
            $table->boolean('AC');
            $table->unsignedInteger('estate_id');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("rooms");
    }
}
