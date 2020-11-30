package com.feylabs.tahfidz.Util

object URL {

    const val BASE_URL = "http://192.168.1.89/API-Tahfidz/"

    const val LOGIN_STUDENT = BASE_URL + "student/login.php"
    const val LOGIN_MENTOR = BASE_URL + "mentor/login.php"
    const val STUDENT_DATA = BASE_URL + "student/student_info.php"
    const val STUDENT_PHOTO = BASE_URL + "student/"
    const val STUDENT_UPDATE_IMG = BASE_URL + "student/update_img.php"
    const val STUDENT_UPDATE_BASIC = BASE_URL + "student/update_data_user.php"
    const val STUDENT_UPDATE_AUTH = BASE_URL + "student/update_pass_user.php"


    const val MENTOR_DATA = BASE_URL + "mentor/mentor_info.php"
    const val MENTOR_UPDATE = BASE_URL + "mentor/update_data_user.php"
    const val MENTOR_PHOTO = BASE_URL + "mentor/"
    const val MENTOR_UPDATE_IMG = BASE_URL + "mentor/update_img.php"

    const val GROUP_DATA = BASE_URL + "group/group_info.php"
//    http://202.157.177.52/API-Tahfidz/group/grouping_mentor_info.php
    const val GROUPING_MENTOR = BASE_URL + "group/grouping_mentor_info.php"
//    param : mentor_id or group_id via post or get

//    const val SUBMISSION_HISTORY = BASE_URL+"submission/get_submission_student.php"
    const val SUBMISSION = BASE_URL+"submission/api_get_submission.php"
    const val UPLOAD_SUBMISSION = BASE_URL+"submission/upload_submission.php"
    const val DELETE_SUBMISSION = BASE_URL+"submission/delete_submission.php"
    const val MP3 = BASE_URL+"submission/"
    const val MP3_MOBILE = BASE_URL+"submission/detail_mobile.php?url="
    const val MOT = BASE_URL+"motivation/get_motivation.php"

    const val UPDATE_CORRECTION = BASE_URL+"submission/api_correct_submission.php"

    const val SHOW_CORRECTION = BASE_URL+"submission/show_correction_mobile.php?id="
    const val INPUT_CORRECTION = BASE_URL+"submission/correction_mobile.php?submission_id="




}