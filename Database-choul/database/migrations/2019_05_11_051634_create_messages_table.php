<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreatemessagesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create("messages", function(Blueprint $table){
            $table->increments('id');
            $table->string('content',1000);
            $table->date('date');
            $table->boolean('seen');
            $table->unsignedInteger('from_self_user_id');
            $table->unsignedInteger('to_user_user_id');
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
        Schema::dropIfExists("messages");
    }
}
