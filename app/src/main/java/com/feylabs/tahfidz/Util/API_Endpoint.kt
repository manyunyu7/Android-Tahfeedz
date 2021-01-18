package com.feylabs.tahfidz.Util

object API_Endpoint {

    const val BASE_URL = "http://tahfidz.sditwahdahbtg.com/"
    const val LARAVEL_URL = "http://web-tahfidz.sditwahdahbtg.com/"
    const val MENTOR_DASHBOARD = LARAVEL_URL + "login/mentor"

//    Auth Level
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
    const val MENTOR_UPDATE_AUTH = BASE_URL + "mentor/update_pass_user.php"


    const val GROUP_ANNOUNCEMENT = BASE_URL + "group/get_announcement.php"
    const val UPDATE_ANNOUNCEMENT = BASE_URL + "group/update_announcement.php"

    const val GET_ADMIN = BASE_URL+"admin/get_admin.php"

    const val GET_QUIZ = BASE_URL + "quiz/get_quiz.php"
    const val DELETE_QUIZ = BASE_URL + "quiz/delete_quiz.php"
    const val CREATE_QUIZ = BASE_URL + "quiz/create_quiz.php"

    const val GROUP_DATA = BASE_URL + "group/group_info.php"
    const val GROUPING_MENTOR = BASE_URL + "group/grouping_mentor_info.php"

    const val SUBMISSION = BASE_URL+"submission/api_get_submission_master.php"
    const val UPLOAD_SUBMISSION = BASE_URL+"submission/upload_submission.php"
    const val DELETE_SUBMISSION = BASE_URL+"submission/delete_submission.php"
    const val MP3 = BASE_URL+"submission/"
    const val MP3_MOBILE = BASE_URL+"submission/detail_mobile.php?url="
    const val MOT = BASE_URL+"motivation/get_motivation.php"

    const val UPDATE_CORRECTION = BASE_URL+"submission/api_correct_submission.php"

    const val SHOW_CORRECTION = BASE_URL+"submission/show_correction_mobile.php?id="
    const val INPUT_CORRECTION = BASE_URL+"submission/correction_mobile.php?submission_id="




}