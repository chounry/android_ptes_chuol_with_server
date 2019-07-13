<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AddForeignKeyToHouses extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('houses', function (Blueprint $table) {
            $table->foreign('estate_id')->references('id')->on("estates");
            $table->foreign('house_type_id')->references('id')->on("house_types");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('houses', function (Blueprint $table) {
            $table->dropForeign(['house_type_id']);
            $table->dropForeign(['estate_id']);
        });
    }
}
