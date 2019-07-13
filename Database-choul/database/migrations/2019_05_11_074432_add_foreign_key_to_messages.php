<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AddForeignKeyToMessages extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('messages', function (Blueprint $table) {
            $table->foreign('from_self_user_id')->references('id')->on("users");
            $table->foreign('to_user_user_id')->references('id')->on("users");
            $table->foreign('estate_id')->references('id')->on("estates");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('messages', function (Blueprint $table) {
            $table->dropForeign(['from_self_user_id']);
            $table->dropForeign(['to_user_user_id']);
            $table->dropForeign(['estate_id']);
        });
    }
}
